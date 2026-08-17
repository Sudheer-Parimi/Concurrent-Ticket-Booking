package com.sudheer.ticket_booking.repository;

import com.sudheer.ticket_booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByName(String name);
}
