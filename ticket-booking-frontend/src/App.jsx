import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'


import { SeatMap } from './components/SeatMap';

function App() {

  return(
    <div className ="App">
      <SeatMap tripId={7}/>
    </div>
  )
}

export default App;
