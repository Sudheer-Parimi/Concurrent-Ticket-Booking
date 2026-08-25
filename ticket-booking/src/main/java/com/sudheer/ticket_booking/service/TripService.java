package com.sudheer.ticket_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.sudheer.ticket_booking.repository.RouteRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.dto.TripRequestDTO;
import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.exception.ResourceNotFoundException;
import com.sudheer.ticket_booking.dto.TripRequestDTO;


@Service
public class TripService {
    
    private TripRepository tripRepository;
    private BusRepository busRepository;
    private RouteRepository routeRepository;

    public TripService(BusRepository busRepository, RouteRepository routeRepository, TripRepository tripRepository){
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
    }

    public Trip createNewTrip(@RequestBody TripRequestDTO req){

        Bus bus;
        if(req.getBusId() != null){
            bus = busRepository.findById(req.getBusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bus Not found with Id " + req.getBusId()));
        }
        else{
            bus = new Bus();
            bus.setBusNumber(req.getNewBusNumber());
            bus.setBusType(req.getNewBusType());
            bus.setCapacity(req.getNewCapacity());
            bus = busRepository.save(bus);
        }

        Route route;

        if(req.getRouteId() != null){
            route = routeRepository.findById(req.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Route not Found with Id "+ req.getRouteId()));
        }
        else{
            route = new Route();
            route.setDestination(req.getNewDestination());
            route.setSource(req.getNewSource());
            route = routeRepository.save(route);
        }

        Trip trip = new Trip();

        trip.setBus(bus);
        trip.setRoute(route);
        trip.setDepartureTime(req.getDepartureTime());
        trip.setTicketPrice(req.getTicketPrice());

        return tripRepository.save(trip);
    }
}
