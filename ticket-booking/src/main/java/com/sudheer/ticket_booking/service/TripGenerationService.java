package com.sudheer.ticket_booking.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.sudheer.ticket_booking.repository.TripScheduleRepository;

import java.util.List;

@Service 
public class TripGenerationService {
    
    private final TripScheduleRepository scheduleRepository;
    private final TripScheduleProcessor processor;

    public TripGenerationService( TripScheduleRepository scheduleRepository, TripScheduleProcessor processor){
        
        this.scheduleRepository = scheduleRepository;
        this.processor = processor;
    }

    public void generateTripsForDate(LocalDate date){
        List<Long> scheduleIds = scheduleRepository.findActiveScheduleIds();

        for(Long scheduleId: scheduleIds){
            processor.generateTripForSchedule(scheduleId, date);
        }
    }

}
