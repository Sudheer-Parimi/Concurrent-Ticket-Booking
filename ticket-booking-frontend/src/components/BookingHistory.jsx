import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getUserBookingHistory } from '../services/apis';
import "../styles/BookingHistory.css";

export const BookingHistory = () => {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchHistory = async () => {
      if (!user || !user.id) {
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const data = await getUserBookingHistory(user.id);
        setBookings(data);
      } catch (err) {
        console.error("Error fetching history:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();
  }, [user]);

    const formatDeparture = (dateString) => {
        if (!dateString) return 'N/A';
        
        const date = new Date(dateString);
        
        if (isNaN(date.getTime())) return dateString;

        return date.toLocaleString('en-IN', {
            day: '2-digit',
            month: 'short',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        });
    };

  if (!user) {
    return <div className="history-container"><p>Please log in to view your bookings.</p></div>;
  }

  if (loading) {
    return <div className="history-container"><p>Loading your booking history...</p></div>;
  }

  if (error) {
    return <div className="history-container"><p className="error">{error}</p></div>;
  }

  return (
    <div className="history-container">
      <h2>Your Booking History</h2>

      {bookings.length === 0 ? (
        <p>No bookings found yet.</p>
      ) : (
        <div className="bookings-list">
          {bookings.map((booking) => (
            <div key={booking.id || booking.bookingId} className="booking-card">
              <div className="booking-header">
                <span className="booking-id">Booking #{booking.id || booking.bookingId}</span>
                <span className="booking-date">
                  {booking.bookingTime ? formatDeparture(booking.bookingTime) : 'N/A'}
                </span>
              </div>

              <div className="booking-details">
                <p><strong>Bus ID / Name:</strong> {booking.busId || booking.busName}</p>
                <p><strong>Seats:</strong> {Array.isArray(booking.seatNumbers) ? booking.seatNumbers.join(', ') : booking.seatNumbers}</p>
                <p><strong>Total Paid:</strong> ₹{booking.totalPrice}</p>
                <p><strong>Status:</strong> <span className="status-badge">{booking.status || 'CONFIRMED'}</span></p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default BookingHistory;