package com.sudheer.ticket_booking.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sudheer.ticket_booking.entity.Ticket;
import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.constant.BookingStatus;

@Entity
@Table(name="bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name="trip_id", nullable= false)
    private Trip trip;

    // mappedBy tells Spring that the "booking" field inside the Ticket class 
    // is the one handling the foreign key relationship
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Booking(){}

    public Long getId(){
        return id;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
    public Trip getTrip(){
        return trip;
    }
    public void setTrip(Trip trip){
        this.trip = trip;
    }
    public List<Ticket> getTickets(){
        return tickets;
    }
    public void setTickets(List<Ticket> tickets){
        this.tickets = tickets;
    }
    public double getTotalAmount(){
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount){
        this.totalAmount=totalAmount;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    public BookingStatus getStatus(){
        return status;
    }
    public void setStatus(BookingStatus status){
        this.status = status;
    }

}
