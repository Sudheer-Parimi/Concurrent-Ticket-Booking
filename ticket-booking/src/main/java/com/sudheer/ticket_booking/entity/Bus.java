package com.sudheer.ticket_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name="bus")

public class Bus{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="busNumber", nullable=false, unique=true)
    private String busNumber;

    @Column(name="capacity")
    private int capacity;

    public Bus(){}

    public Long getId(){
        return this.id;
    }

    public void setId(Long Id){
        this.id = Id;
    }

    public String getBusNumber(){
        return this.busNumber;
    }

    public void setBusNumber(String busNo){
        this.busNumber = busNo;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public void setCapacity(int cap){
        this.capacity = cap;
    }


}