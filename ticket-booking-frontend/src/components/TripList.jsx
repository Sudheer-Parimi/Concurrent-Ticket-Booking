import React, {useState, useEffect} from "react";
import { getAllTrips } from "../services/apis";
import '../styles/TripList.css';


export function TripList({onSelectTrip}){

    const [trips, setTrips] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    const formatDateTime = (dateTimeString) => {
        if (!dateTimeString) return { date: 'N/A', time: '10:00 AM' };
        
        const dateObj = new Date(dateTimeString);
        
        const date = dateObj.toLocaleDateString('en-IN', {
            day: 'numeric',
            month: 'short',
            year: 'numeric'
        }); // e.g., "25 Aug 2026"
        
        const time = dateObj.toLocaleTimeString('en-IN', {
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        }); // e.g., "10:30 AM"

        return { date, time };

    };

    const loadTrips = async() =>{
        getAllTrips().
        then(response =>{
            setTrips(response);
            setLoading(false);
        }).
        catch((err) => {
            console.error('Error loading trips', err);
            setError('Could not load Available trips');
            setLoading(false);
        })
    }

    useEffect(() => {
        loadTrips();
    },[]);

    if(loading) return <div> Loading Available trips...</div>;
    if(error) return <div style ={{color:'red'}}> {error}</div>;

    return(
        <div className="trip-container">
            <h2>Scheduled Buses</h2>
            <div className= "trip-grid">
                {trips.map((trip) =>{
                    const{date, time} = formatDateTime(trip.departureTime);

                    return(
                    <div key={trip.id} className="trip-card">
                        <div className="trip-header">
                            {trip.route.source} ➔ {trip.route.destination}
                        </div>
                        <div className = "trip-details">
                            <p><strong>Trip Id:</strong> {trip.id}</p>
                            <p><strong>Date: </strong> {date}</p>
                            <p><strong>Departure: </strong> {time || '10:00 AM'}</p>
                            <p><strong>Bus Type: </strong> {trip.busType || 'Express'}</p>
                            <p><strong>Price: </strong> {trip.ticketPrice || 700}</p>
                        </div>
                        <button
                            className ="select-trip-btn"
                            onClick={() => onSelectTrip(trip.id, trip.bus.capacity, trip.ticketPrice, trip.route.source, trip.route.destination, trip.departureTime)}
                        >
                            Select Seats
                        </button>
                    </div>
                    );
                })}
            </div>
        </div>
    );
    
}

