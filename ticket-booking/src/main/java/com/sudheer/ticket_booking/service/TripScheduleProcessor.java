package com.sudheer.ticket_booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.entity.TripSchedule;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import com.sudheer.ticket_booking.repository.TripScheduleRepository;

@Service 
public class TripScheduleProcessor {
    
    private final TripRepository tripRepository;
    private final TripScheduleRepository scheduleRepository;
    private final BusRepository busRepository;

    public TripScheduleProcessor(TripRepository tripRepository, TripScheduleRepository scheduleRepository, BusRepository busRepository){
        this.tripRepository = tripRepository;
        this.scheduleRepository = scheduleRepository;
        this.busRepository = busRepository;
    }

    @Transactional 
    public void generateTripForSchedule(long scheduleId, LocalDate date){

        TripSchedule schedule = scheduleRepository.findActiveSchedulesWithDetails(scheduleId).orElse(null);
         
        boolean validConditions = isScheduleEligible(schedule, date);

        if(!validConditions){
            return;
        }

        Bus bus = busRepository.findBusForTripSchedule(schedule.getBus().getId()).orElse(null);

        if(bus == null){
            return;
        }

        LocalDateTime departure = date.atTime(schedule.getDepartureTime());
        LocalDateTime arrival = departure.plusMinutes(schedule.getRoute().getEstimatedDurationMinutes());

        boolean existsAlready = tripRepository.existsBySchedule_IdAndDepartureTime(schedule.getId(), departure);

     
        if(existsAlready){
            return;
        }

        boolean isBusAlreadyAssigned = tripRepository.existOverlappingTrip(
                                        bus.getId(),
                                        departure,
                                        arrival );
            
        if(isBusAlreadyAssigned){
            return;
        }

        Trip trip = new Trip();

        trip.setDepartureTime(departure);
        trip.setBus(bus);
        trip.setRoute(schedule.getRoute());
        trip.setTicketPrice(schedule.getTicketPrice());
        trip.setActive(schedule.isActive());
        trip.setArrivalTime(arrival);
        trip.setSchedule(schedule);

        tripRepository.save(trip);
     
    }

    boolean isScheduleEligible(TripSchedule schedule, LocalDate date) {

        if(schedule == null || date == null ||
            schedule.getStartDate() == null ||
            schedule.getOperatingDays() == null ||
            schedule.getOperatingDays().isEmpty()
        ) return false;

        return schedule.isActive()
            && !schedule.getStartDate().isAfter(date)
            && (schedule.getEndDate() == null
                || !schedule.getEndDate().isBefore(date))
            && schedule.getOperatingDays().contains(date.getDayOfWeek());
    }

}
