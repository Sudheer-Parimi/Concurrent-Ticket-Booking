package com.sudheer.ticket_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// import jakarts.persistence.*

import java.time.LocalDateTime;

import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;

@Entity
@Table(name="trips")

public class Trip {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime departureTime;
    private double ticketPrice;
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name="bus_id", nullable=false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name="route_id", nullable= false)
    private Route route;

    public Trip(){}

    public Long getId(){
        return id;
    }
    public LocalDateTime getDepartureTime(){
        return departureTime;
    }
    public int getAvailableSeats(){
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats){
        this.availableSeats = availableSeats;
    }
    public double getTicketPrice(){
        return ticketPrice;
    }
    public void setDepartureTime(LocalDateTime departureTime){
        this.departureTime = departureTime;
    }
    public void setTicketPrice(double ticketPrice){
        this.ticketPrice = ticketPrice;
    }
    public Bus getBus(){
        return bus;
    }
    public void setBus(Bus bus){
        this.bus = bus;
    }

    @PrePersist
    // This runs automatically EXACTLY ONCE, right before the row is inserted into the database for the first time
    public void initializeAvialableSeats(){
        if(this.bus!=null && this.availableSeats==0){
            this.availableSeats = this.bus.getCapacity();
        }
    }

    public Route getRoute(){
        return route;
    }
    public void setRoute(Route route){
        this.route = route;
    }

}
