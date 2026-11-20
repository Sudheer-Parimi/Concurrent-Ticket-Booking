package com.sudheer.ticket_booking.service;

import com.sudheer.ticket_booking.entity.Booking;
import com.sudheer.ticket_booking.entity.Ticket;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.dto.BookingRequestDTO;
import com.sudheer.ticket_booking.dto.BookingResponseDTO;
import com.sudheer.ticket_booking.dto.CancellationResponseDTO;
import com.sudheer.ticket_booking.repository.BookingRepository;
import com.sudheer.ticket_booking.repository.TicketRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import com.sudheer.ticket_booking.repository.UserRepository;
import com.sudheer.ticket_booking.exception.ResourceNotFoundException;
import com.sudheer.ticket_booking.exception.SeatUnavailableException;
import com.sudheer.ticket_booking.constant.BookingStatus;
import com.sudheer.ticket_booking.constant.TicketStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private static final int HOLD_MINUTES = 5;
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, TicketRepository ticketRepository, TripRepository tripRepository, UserRepository userRepository){
        this.bookingRepository=bookingRepository; this.ticketRepository=ticketRepository; this.tripRepository=tripRepository; this.userRepository=userRepository;
    }

    @Transactional
    public Booking createBooking(BookingRequestDTO request, String authenticatedUsername){
        User user = getUser(request.getUserId(), authenticatedUsername);
        Trip trip = lockTrip(request.getTripId());
        List<Integer> seats = validateSeats(request.getSeatNumbers(), trip);
        releaseExpiredHolds(trip, LocalDateTime.now());
        ensureSeatsAvailable(trip, seats);

        trip.setAvailableSeats(trip.getAvailableSeats() - seats.size());
        Booking booking = buildBooking(user, trip, seats, BookingStatus.CONFIRMED, null);
        tripRepository.save(trip);
        Booking saved=bookingRepository.save(booking);
        log.info("booking_created bookingId={} tripId={} seats={} user={}", saved.getId(), trip.getId(), seats, user.getUsername());
        return saved;
    }

    @Transactional
    public Booking holdSeats(BookingRequestDTO request, String authenticatedUsername){
        User user = getUser(request.getUserId(), authenticatedUsername);
        Trip trip = lockTrip(request.getTripId());
        LocalDateTime now=LocalDateTime.now();
        releaseExpiredHolds(trip, now);
        List<Integer> seats=validateSeats(request.getSeatNumbers(), trip);
        ensureSeatsAvailable(trip, seats);
        LocalDateTime expiry=now.plusMinutes(HOLD_MINUTES);
        trip.setAvailableSeats(trip.getAvailableSeats()-seats.size());
        Booking booking=buildBooking(user, trip, seats, BookingStatus.PENDING, expiry);
        tripRepository.save(trip);
        Booking saved=bookingRepository.save(booking);
        log.info("seat_hold_created bookingId={} tripId={} seats={} expiresAt={}", saved.getId(), trip.getId(), seats, expiry);
        return saved;
    }

    @Transactional
    public Booking confirmBooking(Long bookingId, String username){
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        requireOwnerOrAdmin(booking, username);
        Trip trip=lockTrip(booking.getTrip().getId());
        releaseExpiredHolds(trip, LocalDateTime.now());
        if(booking.getStatus()!=BookingStatus.PENDING) throw new IllegalStateException("Only pending bookings can be confirmed");
        LocalDateTime now=LocalDateTime.now();
        for(Ticket ticket: booking.getTickets()){
            if(ticket.getStatus()!=TicketStatus.HELD || ticket.getExpiresAt()==null || !ticket.getExpiresAt().isAfter(now)) throw new IllegalStateException("The seat hold has expired");
            ticket.setStatus(TicketStatus.ACTIVE); ticket.setExpiresAt(null);
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking saved=bookingRepository.save(booking);
        log.info("booking_confirmed bookingId={} user={}", bookingId, username);
        return saved;
    }

    @Transactional
    public CancellationResponseDTO cancelBooking(Long id, String username){
        Booking booking=bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Booking Found"));
        requireOwnerOrAdmin(booking, username);
        if(booking.getStatus()==BookingStatus.CANCELLED) throw new IllegalStateException("The booking has already been cancelled");
        Trip trip=lockTrip(booking.getTrip().getId());
        int seatsCancelled=0;
        for(Ticket ticket: booking.getTickets()) {
            if(ticket.getStatus()==TicketStatus.ACTIVE || ticket.getStatus()==TicketStatus.HELD){ ticket.setStatus(TicketStatus.CANCELLED); ticket.setExpiresAt(null); seatsCancelled++; }
        }
        trip.setAvailableSeats(Math.min(trip.getBus().getCapacity(), trip.getAvailableSeats()+seatsCancelled));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        log.info("booking_cancelled bookingId={} seats={} user={}", id, seatsCancelled, username);
        return new CancellationResponseDTO(id,"CANCELLED",booking.getTotalAmount().multiply(BigDecimal.valueOf(0.95)),"Successfully Cancelled "+seatsCancelled+" seats. A refund of Rs. "+ (booking.getTotalAmount().multiply(BigDecimal.valueOf(0.95)))+" has been initiated after a 5% cancellation fee.");
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void expireHolds(){
        LocalDateTime now=LocalDateTime.now();
        for(Long tripId: ticketRepository.findTripIdsWithExpiredTickets(TicketStatus.HELD, now)){
            Trip trip=lockTrip(tripId);
            releaseExpiredHolds(trip, now);
        }
    }

    private void releaseExpiredHolds(Trip trip, LocalDateTime now){
        List<Ticket> expired=ticketRepository.findExpiredByTrip(trip.getId(), TicketStatus.HELD, now);
        if(expired.isEmpty()) return;
        Set<Long> bookingIds=new HashSet<>();
        for(Ticket ticket: expired){ ticket.setStatus(TicketStatus.CANCELLED); ticket.setExpiresAt(null); bookingIds.add(ticket.getBooking().getId()); }
        trip.setAvailableSeats(Math.min(trip.getBus().getCapacity(), trip.getAvailableSeats()+expired.size()));
        for(Long bookingId: bookingIds){ bookingRepository.findById(bookingId).ifPresent(b -> { if(b.getStatus()==BookingStatus.PENDING) b.setStatus(BookingStatus.CANCELLED); }); }
        log.info("expired_holds_released tripId={} seats={}", trip.getId(), expired.size());
    }

    private Trip lockTrip(Long tripId){ return tripRepository.findByIdForUpdate(tripId).orElseThrow(() -> new ResourceNotFoundException("Trip with Id "+tripId+" was not found")); }

    private User getUser(Long userId, String username){
        User authUser=userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        if(userId==null) return authUser;
        if(!Objects.equals(authUser.getId(), userId) && !"ROLE_ADMIN".equals(authUser.getRole())) throw new IllegalArgumentException("You cannot create a booking for another user");
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void requireOwnerOrAdmin(Booking booking,String username){
        User auth=userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        if(!Objects.equals(auth.getId(),booking.getUser().getId()) && !"ROLE_ADMIN".equals(auth.getRole())) throw new IllegalArgumentException("You do not have access to this booking");
    }

    private List<Integer> validateSeats(List<Integer> requested, Trip trip){
        if(requested==null || requested.isEmpty()) throw new IllegalArgumentException("At least one seat must be selected");
        if(requested.size()>6) throw new IllegalArgumentException("A booking can contain at most 6 seats");
        List<Integer> seats=new ArrayList<>(requested);
        if(new HashSet<>(seats).size()!=seats.size()) throw new IllegalArgumentException("Duplicate seat numbers are not allowed");
        for(int seat: seats) if(seat<1 || seat>trip.getBus().getCapacity()) throw new IllegalArgumentException("Invalid seat number: "+seat);
        return seats;
    }

    private void ensureSeatsAvailable(Trip trip,List<Integer> seats){
        if(trip.getAvailableSeats()<seats.size()) throw new SeatUnavailableException("Not enough seats are available");
        for(int seat: seats){
            if(ticketRepository.existsByTripIdAndSeatNumberAndStatus(trip.getId(),seat,TicketStatus.ACTIVE) || ticketRepository.existsByTripIdAndSeatNumberAndStatus(trip.getId(),seat,TicketStatus.HELD)) throw new SeatUnavailableException("Seat "+seat+" is already occupied for this trip");
        }
    }

    private Booking buildBooking(User user,Trip trip,List<Integer> seats,BookingStatus status,LocalDateTime expiry){
        Booking booking=new Booking(); booking.setTrip(trip); booking.setUser(user); booking.setStatus(status); booking.setTimestamp(LocalDateTime.now()); 
        booking.setTotalAmount(trip.getTicketPrice().multiply(BigDecimal.valueOf(seats.size())));
        List<Ticket> tickets=new ArrayList<>();
        for(int seat:seats){ Ticket ticket=new Ticket(); ticket.setSeatNumber(seat); ticket.setTrip(trip); ticket.setStatus(status==BookingStatus.PENDING?TicketStatus.HELD:TicketStatus.ACTIVE); ticket.setExpiresAt(expiry); ticket.setBooking(booking); tickets.add(ticket); }
        booking.setTickets(tickets); return booking;
    }

    @Transactional(readOnly=true)
    public List<BookingResponseDTO> getUserBookingHistory(Long userId, String username){
        // System.out.println("========== BOOKING HISTORY METHOD HIT ==========");


        User auth=userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        // System.out.println("Authenticated username: " + username);
        // System.out.println("Authenticated user ID: " + auth.getId());
        // System.out.println("Requested user ID: " + userId);
        // System.out.println("Authenticated role: " + auth.getRole());

        if(!Objects.equals(auth.getId(), userId) && !"ROLE_ADMIN".equals(auth.getRole())) throw new IllegalArgumentException("You do not have access to this booking history");
        List<Booking> bookings=bookingRepository.findByUser_IdOrderByTimestampDesc(userId);
        return bookings.stream().map(b -> new BookingResponseDTO(b.getId(),b.getTrip().getId(),b.getTrip().getRoute().getSource(),b.getTrip().getRoute().getDestination(),b.getTrip().getDepartureTime(),b.getTickets().stream().map(Ticket::getSeatNumber).collect(Collectors.toList()),b.getTotalAmount(),b.getStatus(),b.getTimestamp())).collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    public List<Integer> getBookedSeats(Long tripId){ return ticketRepository.findAllOccupiedSeatNumbers(tripId); }
}
