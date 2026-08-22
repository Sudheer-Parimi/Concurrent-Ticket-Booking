import react, {useState, useEffect} from 'react';
import {fetchBookedSeats} from  '../services/apis';
import './SeatMap.css';

export function SeatMap({tripId = 1}){

    const[bookedSeats, setBookedSeats] = useState([]);
    const[selectedSeats, setSelectedSeats] = useState([]);
    const[loading, setLoading] = useState(true);
    const[error, setError] = useState(null);

    const totalSeats = 60;

    useEffect(() =>{
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
    }, 
    [tripId]);

    const toggleSeatSelection = async(seatNum) =>{
        if(bookedSeats.includes(seatNum)) {
            return;
        }

        if (selectedSeats.includes(seatNum)){
            setSelectedSeats(selectedSeats.filter(s => s != seatNum));
        }
        else{
            setSelectedSeats([...selectedSeats, seatNum]);
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

            <div className= "seat-grid">
                {Array.from({length: totalSeats}, (_, i) => i+1).map((seatNumber) =>{
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
      </div>
    )


}