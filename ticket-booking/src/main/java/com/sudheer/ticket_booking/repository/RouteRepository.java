package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}


