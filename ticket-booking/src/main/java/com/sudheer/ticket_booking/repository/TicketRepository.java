package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sudheer.ticket_booking.constant.TicketStatus;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTripIdAndSeatNumberAndStatus(Long tripId, int seatNumber, TicketStatus status);

    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.trip.id = :tripId and t.status = :status")
    List<Integer> findAllBookedSeatNumbers(@Param("tripId") Long tripId, @Param("status") TicketStatus status);
}
