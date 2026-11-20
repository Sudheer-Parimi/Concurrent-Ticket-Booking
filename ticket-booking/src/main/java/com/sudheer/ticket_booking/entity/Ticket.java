package com.sudheer.ticket_booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sudheer.ticket_booking.constant.TicketStatus;

@Entity
@Table(name="tickets", indexes = {
    @Index(name = "idx_ticket_trip_status_seat", columnList = "trip_id,status,seat_number"),
    @Index(name = "idx_ticket_status_expiry", columnList = "status,expires_at")
})
public class Ticket {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name="trip_id", nullable=false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name="booking_id", nullable=false)
    @JsonBackReference
    private Booking booking;

    public Ticket(){}
    public Long getId(){ return id; }
    public int getSeatNumber(){ return seatNumber; }
    public void setSeatNumber(int seatNumber){ this.seatNumber=seatNumber; }
    public Trip getTrip(){ return trip; }
    public void setTrip(Trip trip){ this.trip=trip; }
    public Booking getBooking(){ return booking; }
    public void setBooking(Booking booking){ this.booking=booking; }
    public TicketStatus getStatus(){ return status; }
    public void setStatus(TicketStatus status){ this.status=status; }
    public LocalDateTime getExpiresAt(){ return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt){ this.expiresAt=expiresAt; }
}
