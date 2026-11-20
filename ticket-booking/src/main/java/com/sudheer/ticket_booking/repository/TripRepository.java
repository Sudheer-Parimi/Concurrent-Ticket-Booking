package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.Trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TripRepository extends JpaRepository<Trip, Long>{
    List<Trip> findByActiveTrue();

    @Lock (LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trip t WHERE t.id = :tripId")
    Optional<Trip> findByIdForUpdate(@Param("tripId") Long tripId);

    @Query("SELECT t from Trip t WHERE LOWER(t.route.source) = LOWER(:source) " +
        "AND LOWER(t.route.destination) = LOWER(:destination) " +
        "AND t.departureTime >= :startOfDay " +
        "AND t.departureTime < :endOfDay " +
        "AND t.availableSeats > 0")
    
    List<Trip> searchTrips(
        @Param("source") String source,
        @Param("destination") String destination,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
    );

    boolean existsBySchedule_IdAndDepartureTime(
        Long scheduleId,
        LocalDateTime departureTime
    );

    @Query("""
            SELECT COUNT(t) > 0
            FROM Trip t
            WHERE t.bus.id = :busId
            AND t.active = true
            AND t.departureTime < :newArrival
            AND t.arrivalTime > :newDeparture
            """)

    boolean existOverlappingTrip(
        @Param("busId") Long busId,
        @Param("newDeparture") LocalDateTime newDeparture,
        @Param("newArrival") LocalDateTime newArrival   
    );

}
