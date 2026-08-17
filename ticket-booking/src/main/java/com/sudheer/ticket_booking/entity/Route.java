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

    public Route(){}

    public Route(String source, String destination){
        this.source = source;
        this.destination = destination;
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
}
