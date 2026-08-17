package com.sudheer.ticket_booking.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.sudheer.ticket_booking.constant.BookingStatus;

public class BookingResponseDTO {
    private Long bookingId;
    private Long tripId;
    private LocalDateTime departureTime;
    private String source;
    private String destination;
    private double totalPrice;
    private BookingStatus status;
    private List<Integer> seatNumbers;
    private LocalDateTime bookingTime;

    public BookingResponseDTO(){

    }

    public BookingResponseDTO(Long bookingId, Long tripId,  String source, String destination, LocalDateTime departureTime,
        List<Integer> seatNumbers, double totalPrice, BookingStatus status,  LocalDateTime bookingTime
    ){
        this.bookingId = bookingId;
        this.tripId = tripId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.seatNumbers = seatNumbers;
        this.totalPrice = totalPrice;
        this.status = status;
        this.bookingTime = bookingTime;

    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public List<Integer> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<Integer> seatNumbers) { this.seatNumbers = seatNumbers; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }


}
