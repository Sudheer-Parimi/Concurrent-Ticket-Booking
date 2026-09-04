import React, {useState, useEffect} from 'react';
import { searchTrips } from '../services/apis';
import "./SearchTrips.css";

const SearchTrip = ({onSelectTrip}) =>{

    const [searchParams, setSearchParams] = useState({
        source: '',
        destination:'',
        date: ''
    });

    const[trips, setTrips] = useState([]);
    const[loading, setLoading] = useState(false);
    const[searched, setSearched] = useState(false);
    const[error, setError] = useState(null);

    const handleChange = (e) => {
        setSearchParams({
            ...searchParams,
            [e.target.name] : e.target.value
        });
    }

    const handleSearch = async(e) =>{
        e.preventDefault()

        if (searchParams.date < today) {
            setError("Please select today or a future date.");
            return;
        }

        setLoading(true);
        setError(null);
        setSearched(true);

        // console.log("kdshfhdsb")

        try{
            const resp = await searchTrips(searchParams);

            console.log("khdfksd", resp);

            setTrips(resp);
        }
        catch(error){
            console.error("Failed to search trips", error);
            setError("Search failed! ");
            
        }
        finally{
            setLoading(false);
            
        }
    }

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

    const today = new Date().toISOString().split('T')[0];

    return (

        <div className="search-container">
            <div className="search-header">
                <h2 className = "search-title">Find Bus Trips</h2>
                <p className="search-subtitle">Search active routes and select your preferred seats</p>
            </div>
            
            
            {/* Search Form */}

            <form onSubmit={handleSearch} className="search-form-card">
                <div className="input-group">
                    <label className="input-label">From</label>
                    <input
                        className="search-input"
                        type="text"
                        name="source"
                        placeholder="Ex: Visakhapatnam"
                        value={searchParams.source}
                        onChange={handleChange}
                        required
                    />
                </div>
                
                <div className="input-group">
                    <label className="input-label">To</label>
                    <input
                        className="search-input"
                        type="text"
                        name="destination"
                        placeholder="Ex: Hyderabad"
                        value={searchParams.destination}
                        onChange={handleChange}
                        required
                    />
                </div>
                
                <div className="input-group">
                    <label className="input-label">Travel Date</label>
                    <input
                        className="search-input"
                        type="date"
                        name="date"
                        min={today}
                        value={searchParams.date}
                        onChange={handleChange}
                        required
                    />
                </div>
                
                <button 
                    className="search-button"
                    type="submit" 
                    disabled={loading} 
                >
                    {loading ? 'Searching...' : 'Search Trips'}
                </button>
            </form>

            {/* Error Message */}

            {error && 
                <div className="error-box">
                    {error}
                </div>
            }

            {/* Results Section */}

            <div className="results-container">
                {searched && trips.length === 0 && !loading && (
                    <div className='empty-state'>
                        <p className= "empty-text">No trips found for the selected route and date.</p>
                    </div>
                    
                )}

                {trips.map((trip) => (
                    <div 
                        key={trip.tripId} 
                        className ="trip-card"
                    >
                        <div className='trip-details'>
                            <div className="bus-badge-group">
                                <h3 className='bus-name'>
                                    {trip.bus?.busName || 'Standard Bus'}
                                </h3>
                                <span className='bus-type-tag'>
                                    ({trip.bus?.busType || 'AC Semi-Sleeper'})
                                </span> 
                            </div>

                            <div className='route-row'>
                                <span className='city-name'>{trip.route?.source}</span> 
                                <span className='arrow'> ➔ </span>
                                <span className='city-name'>{trip.route?.destination}</span>
                            </div>

                            <div className="meta-row">
                                <p className='meta-text'>
                                    <strong>Departure: </strong>{formatDeparture(trip.trip?.departureTime || trip.departureDate)}
                                </p>
                                <span className='dot-separator'>.</span>

                                <p className='meta-text'>
                                    <strong>Available Seats:</strong>{" "}
                                    <span className='available-seats-highlight'>
                                        {trip.trip?.availableSeats ?? trip.bus?.capacity}
                                    </span>
                                </p>
                                
                            </div>

                        </div>

                        <div className='action-column'>
                            <div className='price-container'>
                                <span className='price-label'>Fare</span>
                                <h3 className='price-value'>₹{trip.trip?.ticketPrice || trip.price}</h3>
                            </div>

                            <button 
                                className='select-seat-button'
                                onClick={() => onSelectTrip(trip.trip)}
                            >
                                Select Seats
                            </button>
                        </div>

                    </div>
                ))}
            </div>
        </div>
    );

}

export default SearchTrip;