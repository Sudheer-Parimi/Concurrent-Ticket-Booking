package com.sudheer.ticket_booking.service;

import com.sudheer.ticket_booking.entity.Bus;
import com.sudheer.ticket_booking.entity.Route;
import com.sudheer.ticket_booking.entity.Trip;
import com.sudheer.ticket_booking.entity.TripSchedule;
import com.sudheer.ticket_booking.repository.BusRepository;
import com.sudheer.ticket_booking.repository.TripRepository;
import com.sudheer.ticket_booking.repository.TripScheduleRepository;
import com.sudheer.ticket_booking.service.TripScheduleProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TripScheduleProcessorTest {

    @Mock 
    private TripRepository tripRepository;

    @Mock 
    private TripScheduleRepository scheduleRepository;

    @Mock 
    private BusRepository busRepository;

    @InjectMocks
    private TripScheduleProcessor processor;

    @Test 
    void shouldReturnTrueWhenOverlappingTimesSameBusOneTripCreated(){

        Bus bus = new Bus();
        bus.setId(1L);
        
        Route route = new Route();

        LocalDate date = LocalDate.of(2026,11,19);

        route.setEstimatedDurationMinutes(600);

        TripSchedule schedule1 = new TripSchedule();

        schedule1.setId(101L);
        schedule1.setActive(true);
        schedule1.setStartDate(date);
        schedule1.setEndDate(LocalDate.of(2026,11,30));
        schedule1.setBus(bus);
        schedule1.setOperatingDays(Set.of(date.getDayOfWeek()));
        schedule1.setDepartureTime(LocalTime.of(12,30));
        schedule1.setRoute(route);

        TripSchedule schedule2 = new TripSchedule();
        schedule2.setId(102L);
        schedule2.setActive(true);
        schedule2.setStartDate(date);
        schedule2.setEndDate(LocalDate.of(2026,11,30));
        schedule2.setBus(bus);
        schedule2.setOperatingDays(Set.of(date.getDayOfWeek()));
        schedule2.setDepartureTime(LocalTime.of(16,30));
        schedule2.setRoute(route);

        when(scheduleRepository.findActiveSchedulesWithDetails(101L)).thenReturn(Optional.of(schedule1));

        when(scheduleRepository.findActiveSchedulesWithDetails(102L)).thenReturn(Optional.of(schedule2));

        when(busRepository.findBusForTripSchedule(1L)).thenReturn(Optional.of(bus));

        when(tripRepository.existsBySchedule_IdAndDepartureTime(eq(101L), any(LocalDateTime.class)))
            .thenReturn(false);

        when(tripRepository.existsBySchedule_IdAndDepartureTime(eq(102L), any(LocalDateTime.class)))
            .thenReturn(false);

        when(tripRepository.existOverlappingTrip(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(false, true);


        processor.generateTripForSchedule(101L, date);
        processor.generateTripForSchedule(102L, date);

        verify(tripRepository, times(1)).save(any(Trip.class));

    }

}
