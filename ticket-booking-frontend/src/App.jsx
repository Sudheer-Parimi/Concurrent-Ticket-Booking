import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'

import { AuthModal } from './components/AuthModal';
import { useAuth } from './context/AuthContext';
import { Header } from './components/Header';
import { SeatMap } from './components/SeatMap';
import { TripList } from './components/TripList';
import  SearchTrip from './components/SearchTrips'
import { BookingConfirmation } from './components/BookingConfirmation';
import { AdminDashboard } from './components/AdminDashboard';

function App() {

  const[selectedTrip, setSelectedTrip]= useState();
  const[capacity, setCapacity] = useState(0);
  const[bookingDetails, setBookingDetails] = useState(null);
  const[isAdminView , setIsAdminView] = useState(false);
  const[step, setStep]  = useState('search');  // search | trip select | seatmap

  const {isAuthModalOpen, closeAuthModal} = useAuth();

  const handleReset = () =>{
    setBookingDetails(null);
    setSelectedTrip(null);
    setStep('search');
  }

  return(

    <div className = 'app-container'>
      <Header onOpenAdmin={() => setIsAdminView(true)}/>

      <main>
        <div className ="App">

          {/* Admin Toggle Header */}
          {/* <header style={{ display: 'flex', justifyContent: 'flex-end', padding: '10px 20px', background: '#f8fafc' }}>
            <button 
              style={{ padding: '6px 12px', cursor: 'pointer', background: '#0f172a', color: 'white', border: 'none', borderRadius: '4px' }}
              onClick={() => setIsAdminView(true)}
            >
              ⚙️ Admin Portal
            </button>
          </header> */}
      
          {/* 1. Booking Confirmation Page */}

          {isAdminView ? (
              <AdminDashboard onBack={() => setIsAdminView(false)} />
            ) :

              step === 'search' ? (
                <SearchTrip onSelectTrip= {(trip) => {
                console.log("jksdfhlsd", trip);
                setSelectedTrip(trip);
                setStep('select_seats');
                }
            }
            />

            ) :
          
            step == 'select_trip' ?

            (<TripList onSelectTrip={(trip) => setSelectedTrip(trip)} /> ) :

            step == 'select_seats' ?

            ( <div>
                <button
                  style={{ margin: '20px', padding: '8px 16px', cursor: 'pointer' }}
                  onClick = {() => {
                    setStep("search");
                    setSelectedTrip(null);
                  }}

                >
                  ← Get Back To Trips

                </button>

                <SeatMap tripId = {selectedTrip.id} onBookingSuccess={(bookingDetails) => {
                      setBookingDetails(bookingDetails);
                      setStep('booking-confirm');
                    }
                  }
                  maxSeats = {6}
                
                />
              

              </div>
                  
            ) :

            step == 'booking-confirm' ?

            (<BookingConfirmation bookingData = {bookingDetails} onConfirmation={handleReset}/>) :

            <TripList/>


          }

          
        
        </div>
      </main>

      <AuthModal isOpen = {isAuthModalOpen} onClose = {closeAuthModal} />
    </div>

  )
}

export default App;
