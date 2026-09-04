package com.sudheer.ticket_booking.config;

import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin@busapp.com").isEmpty()) {
                User admin = new User(
                    "Admin User", 
                    "admin@busapp.com", 
                    passwordEncoder.encode("password123"), 
                    "ROLE_ADMIN"
                );
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("john@example.com").isEmpty()) {
                User user = new User(
                    "John Doe", 
                    "john@example.com", 
                    passwordEncoder.encode("password123"), 
                    "ROLE_USER"
                );
                userRepository.save(user);
            }
        };
    }
    
}
