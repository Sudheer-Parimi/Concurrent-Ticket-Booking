import React, {useState, useEffect} from 'react';
import { searchTrips } from '../services/apis';

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
        setLoading(true);
        setError(null);
        setSearched(true);

        console.log("kdshfhdsb")

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

    return (

        <div style={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
            <h2>Find Bus Trips</h2>
            
            {/* Search Form */}
            <form onSubmit={handleSearch} style={{ display: 'flex', gap: '10px', marginBottom: '30px' }}>
                <input
                    type="text"
                    name="source"
                    placeholder="From (Source)"
                    value={searchParams.source}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="destination"
                    placeholder="To (Destination)"
                    value={searchParams.destination}
                    onChange={handleChange}
                    required
                />
                <input
                    type="date"
                    name="date"
                    value={searchParams.date}
                    onChange={handleChange}
                    required
                />
                <button type="submit" disabled={loading} >
                    {loading ? 'Searching...' : 'Search Trips'}
                </button>
            </form>

            {/* Error Message */}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {/* Results Section */}
            <div>
                {searched && trips.length === 0 && !loading && (
                    <p>No trips found for the selected route and date.</p>
                )}

                {trips.map((trip) => (
                    <div 
                        key={trip.tripId} 
                        style={{
                            border: '1px solid #ccc',
                            borderRadius: '8px',
                            padding: '16px',
                            marginBottom: '15px',
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center'
                        }}
                    >
                        <div>
                            <h3>{trip.bus?.busName || 'Standard Bus'} ({trip.bus?.busType || 'AC Semi-Sleeper'})</h3>
                            <p><strong>Route:</strong> {trip.route?.source} → {trip.route?.destination}</p>
                            <p><strong>Departure:</strong> {trip.departureTime || trip.departureDate}</p>
                            <p><strong>Available Seats:</strong> {trip.availableSeats ?? trip.bus?.totalSeats}</p>
                        </div>
                        <div style={{ textAlign: 'right' }}>
                            <h4 style={{ color: '#2e7d32', margin: '0 0 10px 0' }}>₹{trip.fare || trip.price}</h4>
                            <button 
                                onClick={() => onSelectTrip(trip)}
                                style={{ backgroundColor: '#1976d2', color: '#fff', border: 'none', padding: '10px 16px', borderRadius: '4px', cursor: 'pointer' }}
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