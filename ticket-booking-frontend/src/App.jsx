import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'


import { SeatMap } from './components/SeatMap';
import { TripList } from './components/TripList';

function App() {

  const[selectedTripId, setSelectedTripId]= useState(null);
  const[capacity, setCapacity] = useState(0);

  return(
    <div className ="App">
      {selectedTripId === null ? (
        <TripList onSelectTrip = {(tripId, capacity) => {setSelectedTripId(tripId);
          setCapacity(capacity);
        }}
        />
        ) :
      
        (
          <div>
            <button
              style={{ margin: '20px', padding: '8px 16px', cursor: 'pointer' }}
              onClick = {() => {
                setSelectedTripId(null);
                setCapacity(0);
              }}

            >
              ← Get Back To Trips

            </button>
            <SeatMap tripId={selectedTripId} capacity = {capacity}/>
          </div>
        
          
        )

      }
      
    </div>
  )
}

export default App;
