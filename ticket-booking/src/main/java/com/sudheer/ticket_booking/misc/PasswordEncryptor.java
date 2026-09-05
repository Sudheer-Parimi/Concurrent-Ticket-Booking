package com.sudheer.ticket_booking.misc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Sudheer123"; // Replace with your desired password
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Encoded Password: " + encodedPassword);
    }
}
