package com.sudheer.ticket_booking.service;


import com.sudheer.ticket_booking.entity.Booking;
import com.sudheer.ticket_booking.entity.Ticket;
import com.sudheer.ticket_booking.dto.BookingResponseDTO;
import com.sudheer.ticket_booking.repository.BookingRepository;
import com.sudheer.ticket_booking.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
   private final BookingRepository bookingRepository; 

    public BookingService(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUserBookingHistory(Long userId){
        List<Booking> bookings = bookingRepository.findByUser_IdOrderByTimestampDesc(userId);

        if(bookings.isEmpty()){
            throw new ResourceNotFoundException("No Booking History found for this user");
        }

        return bookings.stream().map(b -> {
            List<Integer> seats = b.getTickets().stream().map(Ticket::getSeatNumber).collect(Collectors.toList());

            return new BookingResponseDTO(
                b.getId(),
                b.getTrip().getId(),
                b.getTrip().getRoute().getSource(),
                b.getTrip().getRoute().getDestination(),
                b.getTrip().getDepartureTime(),
                seats,
                b.getTotalAmount(),
                b.getStatus(),
                b.getTimestamp()
            );
        }).collect(Collectors.toList());
    }

}
