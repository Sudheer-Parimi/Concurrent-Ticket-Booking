package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.entity.Route;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/routes")

public class RouteController {
    
    @Autowired private RouteRepository routeRepository;


    @PostMapping
    public Route postMethodName(@RequestBody Route route) {
        //TODO: process POST request
        
        return routeRepository.save(route);

    }

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
       
}
