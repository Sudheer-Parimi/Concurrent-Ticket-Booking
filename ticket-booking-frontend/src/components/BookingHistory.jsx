import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getUserBookingHistory, cancelBooking} from '../services/apis';
import "../styles/BookingHistory.css";

export const BookingHistory = () => {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [cancellingId, setCancellingId] = useState(null);

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

  useEffect(() => {
    fetchHistory();
  }, [user]);

  const handleCancel = async(bookingId) =>{

    if (!window.confirm("Are you sure you want to cancel this booking?")) return;

    try{
        setCancellingId(bookingId);
        await cancelBooking(bookingId);

        setBookings((prev) => 
            prev.map((item) => 
             (item.id == bookingId || item.bookingId === bookingId) ? 
             {...item, status:'CANCELLED'} :
             item
        ));

    }
    catch(error){
        alert(`Cancellation Failed: ${error.message}`);
    }
    finally{
        setCancellingId(null);
    }
  }

  const isCancellable = (travelDateString) => {
    if (!travelDateString) return false;

    const travelDate = new Date(travelDateString).getTime();
    const now = new Date().getTime();
    
    // 24 hours in milliseconds
    const ONE_DAY_MS = 24 * 60 * 60 * 1000; 

    return (travelDate - now) >= ONE_DAY_MS;
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return dateString;

    return date.toLocaleString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    });
  };

  if (!user) {
    return <div className="history-container"><p className="empty-msg">Please log in to view your bookings.</p></div>;
  }

  if (loading) {
    return <div className="history-container"><p className="empty-msg">Loading your booking history...</p></div>;
  }

  if (error) {
    return <div className="history-container"><p className="error-msg">{error}</p></div>;
  }

  return (
    <div className="history-container">
      <h2 className="history-title">Your Booking History</h2>

      {bookings.length === 0 ? (
        <p className="empty-msg">No bookings found yet.</p>
      ) : (
        <div className="bookings-list">
          {bookings.map((booking) => {
            const bId = booking.id || booking.bookingId;
            const travelDateVal = booking.travelDate || booking.departureTime;
            const isConfirmed = (booking.status || 'CONFIRMED').toUpperCase() === 'CONFIRMED';
            const canCancel = isConfirmed && isCancellable(travelDateVal);

            return(

            <div key={booking.id || booking.bookingId} className="booking-card">
              
              {/* Header: Booking ID & Booking Date */}
              <div className="booking-header">
                <span className="booking-id">Booking #{booking.id || booking.bookingId}</span>
                <span className="booking-date">
                  Booked: {formatDate(booking.bookingTime || booking.createdDate)}
                </span>
              </div>

              {/* Main Info Body */}
              <div className="booking-details">
                {/* Route + Travel Date Row */}
                <div className="route-container">
                  <div className="route-path">
                    <span className="city">{booking.source || 'Source'}</span>
                    <span className="arrow">➔</span>
                    <span className="city">{booking.destination || 'Destination'}</span>
                  </div>
                  <div className="travel-date">
                    <span>📅 Travel: </span>
                    <strong>{formatDate(booking.travelDate || booking.departureTime)}</strong>
                  </div>
                </div>

                <div className="detail-row">
                  <span>Seats:</span>
                  <strong>{Array.isArray(booking.seatNumbers) ? booking.seatNumbers.join(', ') : booking.seatNumbers}</strong>
                </div>

                <div className="detail-row">
                  <span>Total Paid:</span>
                  <strong className="amount">₹{booking.totalPrice ?? booking.totalAmount ?? 0}</strong>
                </div>

                <div className="card-footer">
                  <span className={`status-badge ${(booking.status || 'CONFIRMED').toLowerCase()}`}>
                    {booking.status || 'CONFIRMED'}
                  </span>

                  {canCancel && (
                    <button 
                        className='cancel-btn'
                        onClick = {() => handleCancel(bId)}
                        disabled = {cancellingId === bId}
                    >
                        {cancellingId === bId ? 'Cancelling...' : 'Cancel Booking'}
                    </button>
                  )}
                </div>

              </div>

            </div>
            );

            })}
        </div>
      )}
    </div>
  );
};

export default BookingHistory;