package com.sudheer.ticket_booking.exception;

public class SeatUnavailableException extends RuntimeException{
    public SeatUnavailableException(String message){
        super(message);
    }
}
