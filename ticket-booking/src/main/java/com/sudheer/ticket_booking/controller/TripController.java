package com.sudheer.ticket_booking.controller;


import com.sudheer.ticket_booking.constant.TicketStatus;
import com.sudheer.ticket_booking.dto.TripDetailsDTO;
import com.sudheer.ticket_booking.dto.TripRequestDTO;
import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.exception.ResourceNotFoundException;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.repository.RouteRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import com.sudheer.ticket_booking.repository.TicketRepository;

import com.sudheer.ticket_booking.service.TripService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/trips")

public class TripController {
    
    @Autowired private TripRepository tripRepository;
    @Autowired private BusRepository busRepository;
    @Autowired private RouteRepository routeRepository;

    private TripService tripService;
    private TicketRepository ticketRepository;

    public TripController(TripService tripService, TicketRepository ticketRepository){
        this.tripService = tripService;
        this.ticketRepository= ticketRepository;
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequestDTO request) {
        //TODO: process POST request
        Trip trip = tripService.createNewTrip(request);
        return ResponseEntity.ok(trip);
       
    }

    @GetMapping
    public List<Trip> getAllActiveTrips() {
        return tripRepository.findByActiveTrue();
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> softDeleteTrip(@PathVariable Long id){
        
        Trip trip = tripRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Trip with id #" +id + " not found"));
        
        trip.setActive(false);
        tripRepository.save(trip);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<TripDetailsDTO> searchTrips(
        @RequestParam String source,
        @RequestParam String destination,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Trip> trips = tripRepository.searchTrips(source, destination, startOfDay, endOfDay);

        return trips.stream()
                    .map((t) -> {
                        List<Integer> seats = ticketRepository.findAllBookedSeatNumbers(t.getId(), TicketStatus.ACTIVE);

                        TripDetailsDTO tripData = new TripDetailsDTO();
                        tripData.setBus(t.getBus());
                        tripData.setRoute(t.getRoute());
                        tripData.setTrip(t);
                        tripData.setBookedSeats(seats);
                        
                        return tripData;
                    }
                    )
                    .collect(Collectors.toList());

    }

    @GetMapping("/getTripDetails/{tripId}")
    public ResponseEntity<TripDetailsDTO> getTripDetils(@PathVariable Long tripId) {
        TripDetailsDTO data =  tripService.getTripDetails(tripId);
        return ResponseEntity.ok(data);
    }
    

}
