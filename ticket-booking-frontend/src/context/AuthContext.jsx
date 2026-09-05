import React, {useState, useContext, createContext} from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({children}) =>{

    const[user, setUser] = useState(() =>{
        try {
            const savedUser = localStorage.getItem('user');
            return savedUser ? JSON.parse(savedUser) : null;
        } catch (error) {
            console.error("Failed to parse user from localStorage", error);
            return null;
        }
    });

    const[isAuthModalOpen, setIsAuthModalOpen] = useState(false);

    const openAuthModal =() =>setIsAuthModalOpen(true);
    const closeAuthModal = () => setIsAuthModalOpen(false);

    const login = (userData) => {
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData);
        closeAuthModal();
    }

    const logout = () =>{
        localStorage.removeItem('user');
        setUser(null);
    }

    return (
        <AuthContext.Provider value ={{

            user,
            login,
            logout,
            isAuthModalOpen,
            openAuthModal,
            closeAuthModal,
            isAdmin : user?.role === "ROLE_ADMIN"
        }}>
            {children}

        </AuthContext.Provider>
    );
}

export const useAuth= () => useContext(AuthContext);