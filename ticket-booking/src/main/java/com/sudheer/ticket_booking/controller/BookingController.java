package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.dto.BookingRequestDTO;
import com.sudheer.ticket_booking.entity.Booking;
import com.sudheer.ticket_booking.entity.Ticket;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.exception.ResourceNotFoundException;
import com.sudheer.ticket_booking.exception.SeatUnavailableException;
import com.sudheer.ticket_booking.repository.BookingRepository;
import com.sudheer.ticket_booking.repository.TicketRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import com.sudheer.ticket_booking.repository.UserRepository;
import com.sudheer.ticket_booking.constant.BookingStatus;
import com.sudheer.ticket_booking.constant.TicketStatus;
import com.sudheer.ticket_booking.dto.CancellationResponseDTO;
import com.sudheer.ticket_booking.dto.BookingResponseDTO;
import com.sudheer.ticket_booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/api/bookings")

public class BookingController {
    @Autowired private BookingRepository bookingRepository;
    @Autowired private TripRepository tripRepository;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }
    
    @PostMapping
    @Transactional
    public Booking createBooking(@RequestBody BookingRequestDTO request) {
        //TODO: process POST request
        User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Trip trip = tripRepository.findById(request.getTripId())
                    .orElseThrow(() ->new ResourceNotFoundException("Trip with Id " + request.getTripId()+ " was not found"));

        int maxCapacity = trip.getBus().getCapacity();

        int seatsNeeded = request.getSeatNumbers().size();

        System.out.println("Seats Needed: "+ seatsNeeded);

        int rowsModified = tripRepository.decrementAvailableSeats(trip.getId(), seatsNeeded);

        System.out.println("rows: " + rowsModified);

        if(rowsModified==0){
            throw new RuntimeException("Sorry! There are not enough seats available for this trip to book.");
        }

        for(int seatNum: request.getSeatNumbers()){
            if(seatNum < 1 || seatNum > maxCapacity){
                throw new RuntimeException("Invalid Seat Numbers Selected " + seatNum + " This bus only has "+maxCapacity+ " seats.");
            }
            if(ticketRepository.existsByTripIdAndSeatNumberAndStatus(trip.getId(), seatNum, TicketStatus.ACTIVE)){
                throw new SeatUnavailableException("Seat " + seatNum + " is already booked for this trip !");
            }
        }

        Booking booking = new Booking();
        booking.setTrip(trip);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTimestamp(LocalDateTime.now());

        double totalAmount = trip.getTicketPrice()*request.getSeatNumbers().size();

        booking.setTotalAmount(totalAmount);

        List<Ticket> ticketList = new ArrayList<>();
        for(int seatNum: request.getSeatNumbers()){
            Ticket ticket = new Ticket();
            ticket.setSeatNumber(seatNum);
            ticket.setTrip(trip);
            ticket.setStatus(TicketStatus.ACTIVE);
            ticket.setBooking(booking);
            
            ticketList.add(ticket);
        }

        booking.setTickets(ticketList);

        return bookingRepository.save(booking);
        
    }

    @PostMapping("/{id}/cancel")
    @Transactional
    public CancellationResponseDTO cancelBooking(@PathVariable Long id) {
        //TODO: process POST request
        
        Booking booking = bookingRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("No Booking Found"));

        if(booking.getTickets().isEmpty() || booking.getStatus() == BookingStatus.CANCELLED){
            throw new RuntimeException("The Booking has been cancelled already!");
        }

        int seatsCancelled = booking.getTickets().size();

        Trip trip = booking.getTrip();

        double original_amount = booking.getTotalAmount();
        double cancel_fee = original_amount*0.05;
        double refund_amount = original_amount - cancel_fee;

        List<Ticket> tickets = booking.getTickets();

        for(Ticket ticket: tickets){
            ticket.setStatus(TicketStatus.CANCELLED);
        }

        tripRepository.incrementAvailableSeats(trip.getId(), seatsCancelled);
        trip.setAvailableSeats(trip.getAvailableSeats()+ seatsCancelled);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return new CancellationResponseDTO(
            id,
            "CANCELLED",
            refund_amount,
            "Successfully Cancelled "+ seatsCancelled +" seats. A refund of Rs. "+refund_amount+" has been initiated after a 5% cancellation fee."
        );     
    }
    
    @GetMapping()
    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookingHistory(@PathVariable Long userId){
        List<BookingResponseDTO> history = bookingService.getUserBookingHistory(userId);
        return ResponseEntity.ok(history);
    }
    
    
}
