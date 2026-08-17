package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sudheer.ticket_booking.constant.TicketStatus;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTripIdAndSeatNumberAndStatus(Long tripId, int seatNumber, TicketStatus status);
}
