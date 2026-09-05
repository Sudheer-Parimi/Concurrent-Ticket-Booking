import React, {useState} from 'react';
// import { AdminDashboard } from './AdminDashboard';
import { useAuth } from '../context/AuthContext';
import "../styles/Header.css"

export const Header = ({onOpenAdmin}) =>{

    const {user, logout, openAuthModal, isAdmin} = useAuth();

    return(
        <header className='navbar'>
            <h2 className='logo'> WHEELOBUS </h2>

            <nav>
                {isAdmin && (
                    <button className='admin-btn' onClick={onOpenAdmin}>
                        ⚙️ Admin Portal
                    </button>

                )}

                {user ? (
                    <div className='user-profile'>
                        <span>Hi, {user.name}</span>
                        <button className='logout-btn' onClick={logout}>Logout</button>
                    </div>
                    )
                    :
                    (
                        <button className ='login-btn' onClick={openAuthModal}>Login / Sign Up</button>
                    )

                }
            </nav>
            

        </header>
    )
    
}