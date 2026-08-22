const BASE_URL = 'http://127.0.0.1:8080/api';

export const fetchBookedSeats = async(tripId) =>{
    const response = await fetch(`${BASE_URL}/bookings/bookedSeats/${tripId}`);

    if(!response.ok){
        throw new Error('Failed to fetch booked seats');

    }
    return response.json();
}

export const getUserBookingHistory = async(userId) => {
    const response = await fetch(`${BASE_URL}/bookings/user/${userId}`);

    if(!response.ok){
        throw new Error('Failed to get User History');
        
    }
    return response.json();
}

