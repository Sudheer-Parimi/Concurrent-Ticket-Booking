const BASE_URL = 'http://localhost:8080/api';

export const fetchBookedSeats = async(tripId) =>{
    
    const response = await fetch(`${BASE_URL}/bookings/bookedSeats/${tripId}`);

    if(!response.ok){
        throw new Error('Failed to fetch booked seats');

    }
    return response.json();
}

export const getTripDetails = async(tripId) => {
    const response = await fetch(`${BASE_URL}/trips/getTripDetails/${tripId}`);

    if(!response.ok){
        throw new Error('Failed to get Trip Details');
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
    console.log("sdkhflsh");
    const response = await fetch(`${BASE_URL}/trips/${tripId}`, {
        method: 'DELETE'
        
    })
    console.log("dhfkshdfksd", response);

    if(!response.ok){
        console.error("failed! ", response);
        throw new Error("Failed to delete Trip");
    }

    if (response.status === 204) {
        return true; // Deletion successful, return boolean
    }

    return response.json();
}

export const searchTrips = async(tripDetails) => {

    const {source, destination, date} = tripDetails;

    const params = new URLSearchParams({
        source,
        destination,
        date
    })

    const response = await fetch(`${BASE_URL}/trips/search?${params.toString()}`)

    if(!response.ok){
        throw new Error("Failed to search Trips");
    }

    return response.json();
}

