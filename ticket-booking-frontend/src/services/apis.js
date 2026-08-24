const BASE_URL = '/api';

export const fetchBookedSeats = async(tripId) =>{
    console.log("jksdfhs");
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

export const getAllTrips = async() => {
    const response = await fetch(`${BASE_URL}/trips`);

    if(!response.ok){
        throw new Error('Failed to get any trips');
    }
    return response.json();
}

export const bookSelectedSeats = async(bookingData) => {
    const response = await fetch(`${BASE_URL}/bookings`, {
        method:'POST',
        headers:{
            'Content-Type':'application/json'
        },
        body: JSON.stringify(bookingData)
    });

    if(!response.ok){
        throw new Error('Failed to create the booking');
    }

    return response.json();
}

