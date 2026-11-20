package com.sudheer.ticket_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.math.BigDecimal;

// import jakarts.persistence.*

import java.time.LocalDateTime;

import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;

@Entity
@Table(name="trips",
    uniqueConstraints = {
        @UniqueConstraint(
            name= "uk_trip_schedule_departure",
            columnNames = {"schedule_id", "departure_time"}
        )
    }
)

public class Trip {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(precision = 10, scale = 2, nullable = false)

    private BigDecimal ticketPrice;
    private int availableSeats;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(optional = false)
    @JoinColumn(name="bus_id", nullable=false)
    private Bus bus;

    @ManyToOne(optional = false)
    @JoinColumn(name="route_id", nullable= false)
    private Route route;

    @ManyToOne
    @JoinColumn(name="schedule_id")
    private TripSchedule schedule;

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
    public BigDecimal getTicketPrice(){
        return ticketPrice;
    }
    public void setDepartureTime(LocalDateTime departureTime){
        this.departureTime = departureTime;
    }
    public void setTicketPrice(BigDecimal ticketPrice){
        this.ticketPrice = ticketPrice;
    }
    public Bus getBus(){
        return bus;
    }
    public void setBus(Bus bus){
        this.bus = bus;
    }

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
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

    public LocalDateTime getArrivalTime() {
    return arrivalTime;
}

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public TripSchedule getSchedule(){
        return schedule;
    }

    public void setSchedule(TripSchedule schedule){
        this.schedule = schedule;
    }

}
