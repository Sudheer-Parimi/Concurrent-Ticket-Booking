import React, {useState, useEffect} from 'react';
import {fetchBookedSeats, bookSelectedSeats, getTripDetails} from  '../services/apis';
import { useAuth } from '../context/AuthContext';
import '../styles/SeatMap.css';

export function SeatMap({tripId =1, onBookingSuccess, maxSeats = 6}){

    const[trip, setTrip] = useState(null);
    const[bookedSeats, setBookedSeats] = useState([]);
    const[selectedSeats, setSelectedSeats] = useState([]);
    const[loading, setLoading] = useState(true);
    const[isSubmitting, setIsSubmitting] = useState(false);
    const[error, setError] = useState(null);

    const {user} = useAuth();
    

    useEffect(() =>{
      
       loadTripDetails(tripId);
    }, 
    [tripId]);

    const loadTripDetails = async(tripId) =>{

        try{
            const response = await getTripDetails(tripId);
            //console.log("dhfgksdh", response);
            setTrip(response.trip);
            setBookedSeats(response.bookedSeats);

        }
        catch(error){
            console.error('Error loading the trip Detaails ', error);
            setError('Could not load trip details fro backend');
        }
        finally{
            setLoading(false);
        }
    
    }

    const loadSeats =() =>{
        fetchBookedSeats(tripId).
        then((data) =>{
            
            setBookedSeats(data);
            setLoading(false);
        })
        .catch((err) =>{
            console.error('Error fetching the seats: ', err);
            setError('Could not connect to springboot backend');
            setLoading(false);
        })
    }

    const toggleSeatSelection = async(seatNum) =>{
        if(bookedSeats.includes(seatNum)) {
            return;
        }

        if (selectedSeats.includes(seatNum)){
            setSelectedSeats(selectedSeats.filter(s => s != seatNum));
        }
        else{
            if(selectedSeats.length >= maxSeats){
                alert(`You can only select up to ${maxSeats} seats per booking.`);
                return;
            }
            setSelectedSeats([...selectedSeats, seatNum]);
        }
    }

    const handleBooking = async() =>{

        if(!user || !user.id){
            alert("Please Login to continue booking!")
            return;
        }

        setIsSubmitting(true);

        const bookingData = {
            userId: user.id,
            tripId: tripId, 
            seatNumbers: selectedSeats
        }

        try{
            const response = await bookSelectedSeats(bookingData);

            onBookingSuccess({
                bookingId: response?.id,
                seats: selectedSeats,
                totalPrice: selectedSeats.length * trip.ticketPrice,
                source:trip.route.source,
                destination: trip.route.destination,
                departureTime: trip.departureTime

            });

            setSelectedSeats([]);
            loadSeats();

        }
        catch(err){
            console.error('Booking Failed! ', err);
            alert('Failed to place booking. Please try again.')
        }
        finally{
            setIsSubmitting(false);
        }
    }

    if(loading) return <div>Loading seat layout from backend....</div>
    if(error) return <div style ={{color: 'red'}}>{error}</div>

    return(
        
        <div className ="seat-container">
            <h2>Bus Seat Selection (Trip #{tripId})</h2>

            <div className="legend">
                <span className="badge available">Available</span>
                <span className="badge occupied">Occupied</span>
                <span className="badge selected">Selected</span>
            </div>

            {/* <div className="bus-front-indicator">
                <span> Driver </span>
            </div> */}

            <div className= "seat-grid">
                {Array.from({length: trip.bus.capacity}, (_, i) => i+1).map((seatNumber) =>{
                    const isBooked = bookedSeats.includes(seatNumber);
                    const isSelected = selectedSeats.includes(seatNumber);

                    let classname = 'seat';

                    if(isBooked) classname+= ' booked';
                    else if(isSelected) classname += ' selected';
                    else classname+= ' available';

                    return(
                         <button
                            key = {seatNumber}
                            className= {classname}
                            disabled= {isBooked}
                            onClick= {() =>toggleSeatSelection(seatNumber)}   
                        >
                        {seatNumber}
                    
                        </button>
                    );
                   
                })}
            </div>

            <div
                className = "booking-summary"
            >
                <p>
                    Selected Seats: {selectedSeats.length  > 0 ?  selectedSeats.join(', ') : 'None'}
                    <small style={{ display: 'block', color: '#64748b', marginTop: '4px' }}>
                        (Max {maxSeats} seats allowed per booking)
                    </small>
                </p>
                <button 
                    disabled= {selectedSeats.length === 0 || isSubmitting}
                    onClick= {handleBooking}
                    
                >
                   {isSubmitting ? 'Booking...' : `Confirm Booking (${selectedSeats.length}) Seats`} 

                </button>

            </div>
      </div>
    )


}