package com.sudheer.ticket_booking.controller;


import com.sudheer.ticket_booking.dto.TripRequestDTO;
import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.repository.RouteRepository;
import com.sudheer.ticket_booking.repository.TripRepository;

import com.sudheer.ticket_booking.service.TripService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/trips")

public class TripController {
    
    @Autowired private TripRepository tripRepository;
    @Autowired private BusRepository busRepository;
    @Autowired private RouteRepository routeRepository;

    private TripService tripService;

    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequestDTO request) {
        //TODO: process POST request
        Trip trip = tripService.createNewTrip(request);
        return ResponseEntity.ok(trip);
       
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id){
        if (!tripRepository.existsById(id)) {
            throw new EntityNotFoundException("Trip with ID " + id + " does not exist.");
        }
        tripRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
