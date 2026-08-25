import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'


import { SeatMap } from './components/SeatMap';
import { TripList } from './components/TripList';
import { BookingConfirmation } from './components/BookingConfirmation';
import { AdminDashboard } from './components/AdminDashboard';

function App() {

  const[selectedTrip, setSelectedTrip]= useState(null);
  const[capacity, setCapacity] = useState(0);
  const[bookingDetails, setBookingDetails] = useState(null);
  const[isAdminView , setIsAdminView] = useState(false);

  const handleReset = () =>{
    setBookingDetails(null);
    setSelectedTrip(null);
  }

  if(isAdminView){
    console.log("khgkhkj");
    return <AdminDashboard onBack = {() => setIsAdminView(false)} />
  }

  return(
    <div className ="App">

        {/* Admin Toggle Header */}
        <header style={{ display: 'flex', justifyContent: 'flex-end', padding: '10px 20px', background: '#f8fafc' }}>
          <button 
            style={{ padding: '6px 12px', cursor: 'pointer', background: '#0f172a', color: 'white', border: 'none', borderRadius: '4px' }}
            onClick={() => setIsAdminView(true)}
          >
            ⚙️ Admin Portal
          </button>
        </header>
    
        {/* 1. Booking Confirmation Page */}

        { bookingDetails ? (
            <BookingConfirmation 
              bookingData = {bookingDetails}
              onConfirmation = {handleReset}
            />

          ) :

          selectedTrip ? (
              /* 2. Seat Slection Page */
          <div>
            
            <button
              style={{ margin: '20px', padding: '8px 16px', cursor: 'pointer' }}
              onClick = {() => {
                setSelectedTrip(null);
              }}

            >
              ← Get Back To Trips

            </button>
            <SeatMap 
              tripId = {selectedTrip.id}
              capacity = {selectedTrip.capacity}
              ticketPrice={selectedTrip.ticketPrice}
              source = {selectedTrip.source}
              destination = {selectedTrip.destination}
              departureTime = {selectedTrip.departureTime}
              onBookingSuccess={(details) => setBookingDetails(details)}
            />
          </div>
            

          ) :
      
          (
          /* 3. Available Trips Page */
            <TripList onSelectTrip = {(tripId, capacity, ticketPrice, source, destination, departureTime) => {setSelectedTrip({id: tripId, capacity, ticketPrice, source, destination, departureTime});
              }}
            />
        
          )

        }
      
    </div>
  )
}

export default App;
