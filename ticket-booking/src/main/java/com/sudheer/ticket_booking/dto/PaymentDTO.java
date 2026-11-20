package com.sudheer.ticket_booking.dto;

public class PaymentDTO {
    public static class PaymentRequest{
        private Long bookingId;
        private Double amount;

        public PaymentRequest(){

        }

        public PaymentRequest(Long bookingId, Double amount){
            this.bookingId = bookingId;
            this.amount = amount;
        }

        public Long getBookingId(){return this.bookingId;}
        public void setBookingId(Long bookingId){this.bookingId = bookingId;}
        public Double getAmount(){return amount;}
        public void setAmount(Double amount){this.amount = amount;}
    }

    public static class PaymentResponse{
        private Long bookingId;
        private String razorpayOrderId;
        private String razorpayKeyId;
        private Double amount;

        public PaymentResponse(String razorpayOrderId, String razorpayKeyId, Double amount, Long bookingId){
            this.razorpayKeyId= razorpayKeyId;
            this.razorpayOrderId= razorpayOrderId;
            this.bookingId= bookingId;
            this.amount= amount;
        }

        public Double getAmount(){return amount;}
        public void setAmount(Double amount){this.amount= amount;}
        public Long getBookingId(){return bookingId;}
        public void setBookingId(Long bookingId){this.bookingId= bookingId;}
        public String getRazorpayKeyId(){ return razorpayKeyId;}
        public void setRazorpayKeyId(String razorpayKeyId){this.razorpayKeyId= razorpayKeyId;}
        public String getRazorpayOrderId(){return razorpayOrderId;}
        public void setRazorpayOrderId(String razorpayOrderId){this.razorpayOrderId= razorpayOrderId;}
    }
}
