package com.sudheer.ticket_booking.dto;

public class AuthDTO {
    
    public static class RegisterRequest{
        public String name;
        public String username;
        public String password;
        public String role;

        public RegisterRequest(){

        }
    }

    public static class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(){
            
        }
    }

    public static class AuthResponse {
        public Long id;
        public String name;
        public String username;
        public String role;
        public String message;

        public AuthResponse(){}

        public AuthResponse(Long id, String name, String username, String role, String message){
            this.id = id;
            this.name = name;
            this.username= username;
            this.role = role;
            this.message = message;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getMessage() { return message; }
    }
}
