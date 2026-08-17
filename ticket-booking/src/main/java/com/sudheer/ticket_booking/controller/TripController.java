package com.sudheer.ticket_booking.controller;


import com.sudheer.ticket_booking.dto.TripRequestDTO;
import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.repository.RouteRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/trips")

public class TripController {
    
    @Autowired private TripRepository tripRepository;
    @Autowired private BusRepository busRepository;
    @Autowired private RouteRepository routeRepository;

    @PostMapping
    public Trip createTrip(@RequestBody TripRequestDTO request) {
        //TODO: process POST request
        
        Bus bus = busRepository.findById(request.getBusId())
                  .orElseThrow(() -> new RuntimeException("Bus not found"));
        Route route = routeRepository.findById(request.getRouteId())
                        .orElseThrow(() -> new RuntimeException("Route not found"));

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setRoute(route);
        trip.setDepartureTime(request.getDepartureTime());
        trip.setTicketPrice(request.getTicketPrice());

        return tripRepository.save(trip);
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @GetMapping("/search")
    public List<TripRequestDTO> searchTrips(
        @RequestParam String source,
        @RequestParam String destination,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Trip> trips = tripRepository.searchTrips(source, destination, startOfDay, endOfDay);

        return trips.stream()
                    .map(t -> new TripRequestDTO(
                        t.getBus().getId(),
                        t.getRoute().getId(),
                        t.getDepartureTime(),
                        t.getTicketPrice()
                    ))
                    .collect(Collectors.toList());

    }
    


}
