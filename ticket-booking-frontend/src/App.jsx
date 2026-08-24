import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'


import { SeatMap } from './components/SeatMap';
import { TripList } from './components/TripList';
import { BookingConfirmation } from './components/BookingConfirmation'

function App() {

  const[selectedTrip, setSelectedTrip]= useState(null);
  const[capacity, setCapacity] = useState(0);
  const[bookingDetails, setBookingDetails] = useState(null);

  const handleReset = () =>{
    setBookingDetails(null);
    setSelectedTrip(null);
  }

  return(
    <div className ="App">
    
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
