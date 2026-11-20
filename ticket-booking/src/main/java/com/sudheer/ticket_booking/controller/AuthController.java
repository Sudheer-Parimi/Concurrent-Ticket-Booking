package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.repository.UserRepository;
import com.sudheer.ticket_booking.dto.AuthDTO.*;
import com.sudheer.ticket_booking.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        if(request.username==null || request.password==null || request.name==null || request.username.isBlank() || request.password.isBlank()) return ResponseEntity.badRequest().body("Name, username and password are required");
        if(userRepository.findByUsername(request.username).isPresent()) return ResponseEntity.badRequest().body("Error: User is already registered.");
        User user=new User(request.name,request.username,passwordEncoder.encode(request.password),"ROLE_USER");
        User saved=userRepository.save(user);
        return ResponseEntity.ok(new AuthResponse(saved.getId(),saved.getName(),saved.getUsername(),saved.getRole(),jwtService.generateToken(saved),"User registered successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        User user=userRepository.findByUsername(request.username).orElse(null);
        if(user==null || !passwordEncoder.matches(request.password,user.getPassword())) return ResponseEntity.status(401).body("Invalid Username or Password");
        return ResponseEntity.ok(new AuthResponse(user.getId(),user.getName(),user.getUsername(),user.getRole(),jwtService.generateToken(user),"User Login Successful."));
    }
    
    
}
