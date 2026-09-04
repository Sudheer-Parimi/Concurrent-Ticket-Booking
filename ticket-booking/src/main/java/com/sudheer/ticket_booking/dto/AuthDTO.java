package com.sudheer.ticket_booking.dto;

public class AuthDTO {
    
    public static class RegisterRequest{
        public String name;
        public String username;
        public String password;
        public String role;
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class AuthResponse {
        public Long id;
        public String username;
        public String message;
        public String name;
        public String role;

        public AuthResponse(Long id, String name, String username, String role, String message){
            this.id = id;
            this.name = name;
            this.username= username;
            this.message = message;
            this.role = role;
        }
    }
}
