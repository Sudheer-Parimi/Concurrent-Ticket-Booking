package com.sudheer.ticket_booking.controller;

import com.sudheer.ticket_booking.entity.User;
import com.sudheer.ticket_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository= userRepository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        //TODO: process POST request

        if(userRepository.existsByName(user.getName())){
            throw new RuntimeException("Username: " + user.getName()+ " is already taken");
        }

        return userRepository.save(user);

    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    
    
}
