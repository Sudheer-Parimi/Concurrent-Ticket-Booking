import React, {useState} from 'react';
import {loginUser, registerUser} from '../services/apis';
import {useAuth} from '../context/AuthContext';
import '../styles/AuthModal.css';


export function AuthModal({isOpen, onClose}){
    const[name, setName] = useState('');
    const[username, setUsername] = useState('');
    const[password, setPassword] = useState('');
    const[isRegister, setIsRegister] = useState(false);
    const[loading, setLoading] = useState(false);
    const[error, setError] = useState('');

    const {login} = useAuth();

    if(!isOpen) return null;

    const handleSubmit = async(e) =>{
        e.preventDefault();

        setError('');
        setLoading(true);

        try{
            if(isRegister){

                const data = await registerUser({name, username, password, role: 'ROLE_USER'});
                login(data);
            }
            else{
                const data = await loginUser({username, password});
                login(data);
            }

            setName('');
            setUsername('');
            setPassword('');
            
        }
        catch(error){
            setError(error.message)
        }
        finally{
            setLoading(false);
           
        }
    }

    return (
        <div className= 'auth-overlay'>
            <div className = 'auth-card'>
                <button className= 'auth-close' onClick={onClose}>×</button>
                <h2>{isRegister ? 'Create Account' : 'Welcome Back'}</h2>

                {error && <div className='auth-error'>{error}</div>}

                <form onSubmit={handleSubmit}>
                    {isRegister && (
                        <div className = 'form-group'>
                            <label>Full Name</label>
                            <input 
                                type='text'
                                required
                                value ={name}
                                onChange = {(e) => setName(e.target.value)}
                                placeholder = "Enter your name..."
                            />
                        </div>
                    )}

                    <div className='form-group'>
                        <label>Username(Email)</label>
                        <input 
                            type='email'
                            required
                            value ={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder = "random@example.com"
                        />
                    </div>

                    <div className='form-group'>
                        <label>Password</label>
                        <input 
                            type='password'
                            required
                            value ={password}
                            onChange = {(e) => setPassword(e.target.value)}
                            placeholder="......"
                        />
                    </div>

                    <button className="auth-submit-btn" type='submit' >
                        {loading ? "Processing" : isRegister ? "Sign Up" : "Login"}
                    </button>
                </form>

                <p className= "auth-toggle">
                    {isRegister ? "Already have an account" : "Don't have an account?"}{" "}
                    <span onClick = {() => {setIsRegister(!isRegister); setError('');}}>
                        {isRegister ? "Login" : "Sign Up"}
                    </span>
                </p>
            </div>
        </div>

    );
}