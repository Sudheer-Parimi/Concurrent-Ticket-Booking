package com.sudheer.ticket_booking.repository;


import com.sudheer.ticket_booking.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BusRepository extends JpaRepository<Bus, Long>{


}
