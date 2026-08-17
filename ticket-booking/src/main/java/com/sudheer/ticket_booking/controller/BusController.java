package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/buses")

public class BusController {
    private BusRepository  busRepository;

    @Autowired
    public BusController(BusRepository busRepository){
        this.busRepository= busRepository;
    }

    @PostMapping
    public Bus createBus(@RequestBody Bus bus){
        //TODO: process POST request
        
        return busRepository.save(bus);
    }

    @GetMapping
    public List<Bus> getBuses() {
        return busRepository.findAll();
    }
    
    
}
