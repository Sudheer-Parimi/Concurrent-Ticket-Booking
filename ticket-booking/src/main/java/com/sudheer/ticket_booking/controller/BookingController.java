package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.dto.BookingRequestDTO;
import com.sudheer.ticket_booking.dto.BookingResponseDTO;
import com.sudheer.ticket_booking.dto.CancellationResponseDTO;
import com.sudheer.ticket_booking.entity.Booking;
import com.sudheer.ticket_booking.repository.BookingRepository;
import com.sudheer.ticket_booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository){
        this.bookingService=bookingService; this.bookingRepository=bookingRepository;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO request, Authentication authentication){
        return ResponseEntity.ok(bookingService.createBooking(request, authentication.getName()));
    }

    @PostMapping("/hold")
    public ResponseEntity<Booking> holdSeats(@RequestBody BookingRequestDTO request, Authentication authentication){
        return ResponseEntity.ok(bookingService.holdSeats(request, authentication.getName()));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id, Authentication authentication){
        return ResponseEntity.ok(bookingService.confirmBooking(id, authentication.getName()));
    }

    @PostMapping("/{id}/cancel")
    public CancellationResponseDTO cancelBooking(@PathVariable Long id, Authentication authentication){
        return bookingService.cancelBooking(id, authentication.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Booking> getBookings(){ return bookingRepository.findAll(); }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookingHistory(@PathVariable Long userId, Authentication authentication){
        return ResponseEntity.ok(bookingService.getUserBookingHistory(userId, authentication.getName()));
    }

    @GetMapping("/bookedSeats/{tripId}")
    public ResponseEntity<List<Integer>> getBookedSeatsOfTrip(@PathVariable Long tripId){
        return ResponseEntity.ok(bookingService.getBookedSeats(tripId));
    }
    
    
}
