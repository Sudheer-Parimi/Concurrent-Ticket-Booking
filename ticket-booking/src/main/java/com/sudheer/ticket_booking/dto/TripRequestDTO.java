package com.sudheer.ticket_booking.dto;

import java.time.LocalDateTime;

public class TripRequestDTO {
    
    private Long busId;
    private Long routeId;
    //Incase if trip is assigned newly with not existing buses and routes

    private String newBusNumber;
    private Integer newCapacity;
    private String newBusType;

    private String newSource;
    private String newDestination;

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

    public String getNewBusNumber() {return newBusNumber;}
    public void setNewBusNumber(String newBusNumber) {this.newBusNumber = newBusNumber;}
    public String getNewBusType() {return newBusType;}
    public void setNewBusType(String newBusType) {this.newBusType = newBusType;}
    public Integer getNewCapacity() {return newCapacity;}
    public void setNewCapacity(Integer newCapacity) {this.newCapacity = newCapacity;}
    public String getNewSource() {return newSource;}
    public void setNewSource(String newSource) {this.newSource = newSource;}
    public String getNewDestination() {return newDestination;}
    public void setNewDestination(String newDestination) {this.newDestination = newDestination;}
        
}
