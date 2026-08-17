package com.sudheer.ticket_booking.dto;

import java.util.List;

public class BookingRequestDTO {
    private Long tripId;
    private Long userId;
    private List<Integer> seatNumbers;

    public BookingRequestDTO(){}

    public Long getUserId(){
        return userId;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }
    public Long getTripId(){
        return tripId;
    }
    public void setTripId(Long tripId){this.tripId= tripId;}
    public List<Integer> getSeatNumbers(){
        return seatNumbers;
    }
    public void setSeatNumbers(List<Integer> seatNumbers){
        this.seatNumbers = seatNumbers;
    }
}
