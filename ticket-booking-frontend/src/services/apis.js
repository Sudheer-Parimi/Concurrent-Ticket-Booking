const BASE_URL = 'http://localhost:8080/api';

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

export const getAllBuses = async () => {
    const response = await fetch(`${BASE_URL}/buses`);
    if (!response.ok) throw new Error('Failed to fetch buses');
    return response.json();
};

export const getAllRoutes = async () => {
    const response = await fetch(`${BASE_URL}/routes`);
    if (!response.ok) throw new Error('Failed to fetch routes');
    return response.json();
};

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

export const createNewTrip = async(tripData) => {

    const response = await fetch(`${BASE_URL}/trips`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify(tripData),
  
    });

    if(!response.ok){
        throw new Error("Failed to Create Trip");
    }

    return response.json();
}

export const deleteTrip = async(tripId) => {
    const response = await fetch(`${BASE_URL}/trips/${tripId}`, {
        method: 'DELETE'
        
    })

    if(!response.ok){
        console.error("failed! ", response);
        throw new Error("Failed to delete Trip");
    }

    return response.json();
}

