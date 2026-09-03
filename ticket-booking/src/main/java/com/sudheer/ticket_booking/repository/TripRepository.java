package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDateTime;


@Repository
public interface TripRepository extends  JpaRepository<Trip, Long>{

    List<Trip> findByActiveTrue();
    
    @Modifying
    @Transactional
    @Query("UPDATE Trip t SET t.availableSeats = t.availableSeats- :seatsNeeded  where t.id = :tripId and t.availableSeats >= :seatsNeeded")
    int decrementAvailableSeats(@Param("tripId") Long tripId, @Param("seatsNeeded") int seatsNeeded);

    @Modifying
    @Transactional
    @Query("UPDATE Trip t SET t.availableSeats = t.availableSeats + :seatsCancelled where t.id = :tripId")
    int incrementAvailableSeats(@Param("tripId") Long tripId, @Param("seatsCancelled") int seatsCancelled);

    @Query("SELECT t from Trip t "+ "WHERE LOWER(t.route.source) = LOWER(:source) " +
        "AND LOWER(t.route.destination) = LOWER(:destination) "+
        "AND t.departureTime >= :startOfDay " +
        "AND t.departureTime < :endOfDay "+
        "AND t.availableSeats > 0")
    
    List<Trip> searchTrips(
        @Param("source") String source,
        @Param("destination") String destination,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
    );

}
