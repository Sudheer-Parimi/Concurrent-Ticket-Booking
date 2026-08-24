import React from 'react';
import './BookingConfirmation.css';

export function BookingConfirmation({bookingData, onConfirmation}){
    const {bookingId, source, destination, seats, totalPrice, departureTime} = bookingData;

    return (
        <div className="ticket-card">
            <div className="ticket-header">
                <span className="success-icon">✓</span>
                <h2>Booking Confirmed!</h2>
                <p className="booking-id">Booking ID: {bookingId || 'BKG-' + Math.floor(100000 + Math.random() * 900000)}</p>
            </div>
            <div className="ticket-body">
                <div className="ticket-row">
                    <span>Route:</span>
                    <strong>{source || 'Source'} ➔ {destination || 'Destination'}</strong>
                </div>
                <div className="ticket-row">
                    <span>Departure:</span>
                    <strong>{departureTime || '10:00 AM'}</strong>
                </div>
                <div className="ticket-row">
                    <span>Seats Reserved:</span>
                    <strong className="seat-badges">{seats.join(', ')}</strong>
                </div>
                <div className="ticket-row total">
                    <span>Total Fare:</span>
                    <strong>₹{totalPrice}</strong>
                </div>

                <button className ="done-btn" onClick = {onConfirmation}>
                    Back To Trips
                </button>
            </div>
        </div>
    )
}