package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.repository.UserRepository;
import com.sudheer.ticket_booking.dto.AuthDTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.AuthenticationFailureProxyUntrustedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")

public class AuthController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
       
        if(userRepository.findByUsername(request.username).isPresent()){
            return ResponseEntity.badRequest().body("Error: User is already registered.");
        }

        String userRole = request.role!=  null && !request.role.isEmpty() ? request.role : "ROLE_USER";

        User user = new User(request.name, request.username, passwordEncoder.encode(request.password), userRole);

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(new AuthResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getUsername(),
            savedUser.getRole(),
            "User registered successfully."
        ));

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.username);

        if(userOptional.isEmpty()){
            return ResponseEntity.status(401).body("Invalid Username or password");
        }

        User user = userOptional.get();

        return ResponseEntity.ok(new AuthResponse(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getRole(),
            "User Login Successful."
        ));
        
    }
    
    
}
