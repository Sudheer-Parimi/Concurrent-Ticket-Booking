package com.sudheer.ticket_booking.dto;

public class CancellationResponseDTO {
    private Long bookingId;
    private String status;
    private double refundAmount;
    private String message;

    public CancellationResponseDTO(Long bookingId, String status, double refundAmount, String message) {
        this.bookingId = bookingId;
        this.status = status;
        this.refundAmount = refundAmount;
        this.message = message;
    }

    // Getters and Setters
    public Long getBookingId() { return bookingId; }
    public String getStatus() { return status; }
    public double getRefundAmount() { return refundAmount; }
    public String getMessage() { return message; }
}
