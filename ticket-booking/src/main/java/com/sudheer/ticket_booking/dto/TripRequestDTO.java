package com.sudheer.ticket_booking.dto;

import java.time.LocalDateTime;

public class TripRequestDTO {
    
    private Long busId;
    private Long routeId;
    private LocalDateTime departureTime;
    private double ticketPrice;

    public TripRequestDTO(Long busId, Long routeId, LocalDateTime departureTime, double ticketPrice){
        this.busId = busId;
        this.routeId = routeId;
        this.departureTime= departureTime;
        this.ticketPrice= ticketPrice;
    }

    public Long getBusId(){return this.busId;}
    public void setBusId(Long busId) { this.busId = busId; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
}
