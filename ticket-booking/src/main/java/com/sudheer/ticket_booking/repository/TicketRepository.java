package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.Ticket;
import com.sudheer.ticket_booking.constant.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTripIdAndSeatNumberAndStatus(Long tripId, int seatNumber, TicketStatus status);

    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.trip.id = :tripId and t.status IN (com.sudheer.ticket_booking.constant.TicketStatus.ACTIVE, com.sudheer.ticket_booking.constant.TicketStatus.HELD)")
    List<Integer> findAllOccupiedSeatNumbers(@Param("tripId") Long tripId);

    @Query("SELECT t FROM Ticket t WHERE t.trip.id = :tripId AND t.status = :status AND t.expiresAt <= :now")
    List<Ticket> findExpiredByTrip(@Param("tripId") Long tripId, @Param("status") TicketStatus status, @Param("now") LocalDateTime now);

    @Query("SELECT DISTINCT t.trip.id FROM Ticket t WHERE t.status = :status AND t.expiresAt <= :now")
    List<Long> findTripIdsWithExpiredTickets(@Param("status") TicketStatus status, @Param("now") LocalDateTime now);
}
