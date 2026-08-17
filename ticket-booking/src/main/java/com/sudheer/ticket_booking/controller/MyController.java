package com.sudheer.ticket_booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//This is for Rest Endpoints, we can use @Controller for other web applications.
@RequestMapping("/api/test")

public class MyController{

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello World! Starting my new journey";
    }

}
