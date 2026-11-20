package com.sudheer.ticket_booking.repository;


import com.sudheer.ticket_booking.entity.Bus;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BusRepository extends JpaRepository<Bus, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT b 
            FROM Bus b
            WHERE b.id = :busId
            """)
    Optional<Bus> findBusForTripSchedule(
        @Param("busId") Long busId
    );

}
