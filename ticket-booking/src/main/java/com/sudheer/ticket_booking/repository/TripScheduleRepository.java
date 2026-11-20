package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.TripSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {

    @Query("""
            SELECT s.id
            FROM TripSchedule s
            WHERE s.active = true  
            """)
    List<Long> findActiveScheduleIds();
    
    @Query("""
            SELECT DISTINCT s 
            FROM TripSchedule s 
            LEFT JOIN FETCH s.operatingDays
            JOIN FETCH s.bus
            JOIN FETCH s.route
            WHERE s.id = :scheduleId
            AND s.active = true
            """)
    Optional<TripSchedule> findActiveSchedulesWithDetails(
        @Param ("scheduleId") Long scheduleId
    );
}   
