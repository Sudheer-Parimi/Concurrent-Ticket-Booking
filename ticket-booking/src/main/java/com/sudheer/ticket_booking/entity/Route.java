package com.sudheer.ticket_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name="routes")

public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private String destination;

    @Column(nullable = false)
    private int estimatedDurationMinutes;

    public Route(){}

    public Route(String source, String destination, int estimatedDurationMinutes){
        this.source = source;
        this.destination = destination;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public Long getId(){
        return id;
    }
    public String getSource(){
        return source;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getDestination(){
        return destination;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }

    public int getEstimatedDurationMinutes(){
        return estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(int minutes){
        if(minutes <= 0){
            throw new IllegalArgumentException("Journey duration should be positive");
        }
        this.estimatedDurationMinutes= minutes;
    }
}
