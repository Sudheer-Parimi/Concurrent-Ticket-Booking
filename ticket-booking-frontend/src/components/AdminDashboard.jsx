import React, {useState, useEffect} from 'react';
import { getAllTrips, createNewTrip, deleteTrip, getAllBuses, getAllRoutes} from "../services/apis";

import './AdminDashboard.css';

export function AdminDashboard({onBack}){

    const [trips, setTrips] = useState([]);
    const [buses, setBuses] = useState([]);
    const [routes, setRoutes] = useState([]);

    const [busMode, setBusMode] = useState('EXISTING');
    const [routeMode, setRouteMode] = useState('EXISTING');

    const [busId, setBusId] = useState('');
    const [routeId, setRouteId] = useState('');

    const [newSource, setNewSource] = useState('');
    const [newDestination, setNewDestination] = useState('');
    const [newBusNumber, setNewBusNumber] = useState('');
    const [newBusType, setNewBusType] = useState('AC Sleeper');
    const [newCapacity, setNewCapacity] = useState(40);

    const [ticketPrice, setTicketPrice] = useState(700);
    const [departureTime, setDepartureTime] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [loading, setLoading] = useState(true);

    const loadData = async() =>{
        setLoading(true);
        
        try{
            const[busRes, routeRes, tripRes] = await Promise.all([
                getAllBuses ? getAllBuses() : Promise.resolve([]),
                getAllRoutes ? getAllRoutes() : Promise.resolve([]),
                getAllTrips()
            ]);

            setBuses(busRes || []);
            setRoutes(routeRes || []);
            setTrips(tripRes);
        }
        catch(error){
            console.error("Failed to load dashboard data", error);
            alert("Failed to Load Dashboard data");

        }
        finally{
            setLoading(false);
        }
       
    }

    useEffect(() =>{
        loadData();
    }, []);

    const handleCreateTrip = async(e) =>{

        e.preventDefault();

        //validation check for mandatory selection
        if (routeMode === 'EXISTING' && !routeId) {
            alert("Please select an existing route.");
            return;
        }
        if (busMode === 'EXISTING' && !busId) {
            alert("Please select an existing bus.");
            return;
        }

        setIsSubmitting(true);

        console.log("bus type ", newBusType);

        const tripData = {
           busId: busMode === 'EXISTING' ? Number(busId) : null,
            routeId: routeMode === 'EXISTING' ? Number(routeId) : null,
            newSource: routeMode === 'NEW' ? newSource : null,
            newDestination: routeMode === 'NEW' ? newDestination : null,
            newBusType: busMode === 'NEW' ? newBusType : null,
            newBusNumber: busMode === 'NEW' ? newBusNumber : null,
            newCapacity: busMode === 'NEW' ? Number(newCapacity) : null,
            ticketPrice: Number(ticketPrice),
            departureTime

        };

        try{
            
            await createNewTrip(tripData);
            alert("Trip created Successfully");

            setNewBusNumber('');
            setBusId('');
            setRouteId('');
            setNewSource('');
            setNewDestination('');
            setNewBusType('');
            setNewCapacity(null);
            setDepartureTime('');
            setTicketPrice(1000);

            loadData();

        }
        catch(error){
            console.error("Trip creation failed", error);
            alert("Failed to create trip");
        }
        finally{
            setIsSubmitting(false);
        }
    }

    const handleDeleteTrip = async(id) =>{

        if(!window.confirm(`Are you sure to delete trip #${id}`)){
            return;
        }

        try{
            await deleteTrip(id);
            loadData();
        }
        catch(error){
            alert("Failed to delete trip");
        }
    }


    return(
        <div className="admin-container">
            <div className="admin-header">
                <h2>Admin Management Dashboard</h2>
                <button className="back-btn" onClick={onBack}>← Back to App</button>
            </div>

            <div className="admin-grid">
                {/* Create Trip Form */}
                <div className="admin-card">
                <h3>Add New Bus Trip</h3>

                <form onSubmit={handleCreateTrip}>
                    
                    {/* --- ROUTE SECTION --- */}
                    <div className="form-group">
                    <label>Route Selection</label>
                    <select value={routeMode} onChange={(e) => setRouteMode(e.target.value)}>
                        <option value="EXISTING">Select Existing Route</option>
                        <option value="NEW">+ Create New Route</option>
                    </select>
                    </div>

                    {routeMode === 'EXISTING' ? (
                    <div className="form-group">
                        <label>Select Route</label>
                        <select required value={routeId} onChange={(e) => {console.log("route id", routeId); setRouteId(e.target.value);}}>
                        <option value="">-- Choose Route --</option>
                        {routes.map((r) => (
                            <option key={r.id} value={r.id}>
                            {r.source} ➔ {r.destination}
                            </option>
                        ))}
                        </select>
                    </div>
                    ) : (
                    <div className="nested-fields">
                        <div className="form-group">
                        <label>Source City</label>
                        <input
                            type="text"
                            required
                            value={newSource}
                            onChange={(e) => setNewSource(e.target.value)}
                            placeholder="e.g. Mumbai"
                        />
                        </div>
                        <div className="form-group">
                        <label>Destination City</label>
                        <input
                            type="text"
                            required
                            value={newDestination}
                            onChange={(e) => setNewDestination(e.target.value)}
                            placeholder="e.g. Goa"
                        />
                        </div>
                    </div>
                    )}

                    {/* --- BUS SECTION --- */}
                    <div className="form-group">
                    <label>Bus Selection</label>
                    <select value={busMode} onChange={(e) => setBusMode(e.target.value)}>
                        <option value="EXISTING">Select Existing Bus</option>
                        <option value="NEW">+ Add New Bus</option>
                    </select>
                    </div>

                    {busMode === 'EXISTING' ? (
                    <div className="form-group">
                        <label>Select Bus</label>
                        <select required value={busId} onChange={(e) => setBusId(e.target.value)}>
                        <option value="">-- Choose Bus --</option>
                        {buses.map((b) => (
                            <option key={b.id} value={b.id}>
                            {b.busNumber} ({b.busType} - {b.capacity} seats)
                            </option>
                        ))}
                        </select>
                    </div>
                    ) : (
                    <div className="nested-fields">
                        <div className="form-group">
                        <label>Bus Plate Number</label>
                        <input
                            type="text"
                            required
                            value={newBusNumber}
                            onChange={(e) => setNewBusNumber(e.target.value)}
                            placeholder="e.g. MH-12-AB-1234"
                        />
                        </div>

                        <div className="form-group">
                        <label>Bus Type</label>
                        <select value={newBusType} onChange={(e) => setNewBusType(e.target.value)}>
                            <option value="AC Sleeper">AC Sleeper</option>
                            <option value="AC Seater">AC Seater</option>
                            <option value="Express Non-AC">Express Non-AC</option>
                            <option value="Volvo Multi-Axle">Volvo Multi-Axle</option>
                        </select>
                        </div>

                        <div className="form-group">
                        <label>Seat Capacity</label>
                        <input
                            type="number"
                            required
                            value={newCapacity}
                            onChange={(e) => setNewCapacity(e.target.value)}
                        />
                        </div>
                    </div>
                    )}

                    {/* --- SCHEDULE & FARE --- */}
                    <div className="form-group">
                    <label>Departure Date & Time</label>
                    <input
                        type="datetime-local"
                        required
                        value={departureTime}
                        onChange={(e) => setDepartureTime(e.target.value)}
                    />
                    </div>

                    <div className="form-group">
                    <label>Ticket Price (₹)</label>
                    <input
                        type="number"
                        required
                        value={ticketPrice}
                        onChange={(e) => setTicketPrice(e.target.value)}
                    />
                    </div>

                    <button type="submit" className="submit-btn" disabled={isSubmitting}>
                    {isSubmitting ? 'Publishing...' : 'Publish Trip'}
                    </button>
                </form>
                </div>

                {/* Active Trips Table */}
                <div className="admin-card">
                <h3>Active Scheduled Trips ({trips.length})</h3>
                {loading ? (
                    <p>Loading trips...</p>
                ) : trips.length === 0 ? (
                    <p>No active trips created yet.</p>
                ) : (
                    <table className="admin-table">
                    <thead>
                        <tr>
                        <th>ID</th>
                        <th>Route</th>
                        <th>Bus</th>
                        <th>Departure</th>
                        <th>Price</th>
                        <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {trips.map((trip) => (
                        <tr key={trip.id}>
                            <td>#{trip.id}</td>
                            <td>{trip.route?.source || trip.source} ➔ {trip.route?.destination || trip.destination}</td>
                            <td>{trip.bus?.busNumber || trip.busNumber || 'N/A'}</td>
                            <td>{trip.departureTime || '10:00 AM'}</td>
                            <td>₹{trip.ticketPrice || 700}</td>
                            <td>
                            <button className="delete-btn" onClick={() => handleDeleteTrip(trip.id)}>
                                Delete
                            </button>
                            </td>
                        </tr>
                        ))}
                    </tbody>
                    </table>
                )}
                </div>
            </div>
        </div>
    );
}