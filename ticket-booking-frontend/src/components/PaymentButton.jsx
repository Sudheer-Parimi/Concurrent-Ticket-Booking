import React, {useState} from 'react';
import axios from 'axios';

export const PaymentButton = async({bookingId, totalAmount, onSuccess}) =>{
    const[loading, setLoading] = useState(false);

    const handlePayment = () =>{
        
    }

    return(
        <button
            onClick = {handlePayment}
            disabled = {loading}
            className='pay-btn'
        >
            {loading ? "Processing...": `Pay ${totalAmount}`}
        </button>
    );
}