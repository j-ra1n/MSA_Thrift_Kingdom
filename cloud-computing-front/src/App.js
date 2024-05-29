// App.js
import React, { useState, useEffect } from 'react';
import { Route, Routes, Navigate, useLocation } from 'react-router-dom';
import './App.css';
import Login from './login/Login';
import Door from './component/Door';
import BulletinBoard from './component/BulletinBoard';
import SharingBoard from './component/SharingBoard';
import PostDetail from './board/PostDetail';
import { useUser } from './context/UserContext';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function App() {
  const { user, setUser } = useUser();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isGuest, setIsGuest] = useState(false);
  const query = useQuery();

  useEffect(() => {
    const loggedIn = query.get('loggedIn') === 'true';
    const nickname = query.get('nickname');
    if (loggedIn && nickname) {
      setIsLoggedIn(true);
      setUser({ nickname });
      if (query.get('guest') === 'true') {
        setIsGuest(true);
      }
    }
  }, [query, setUser]);

  const handleLogin = (guest = false) => {
    setIsLoggedIn(true);
    setIsGuest(guest);
  };

  return (
    <div className="App">
      <Routes>
        <Route path="/" element={!isLoggedIn ? <Login onLogin={handleLogin} /> : <Navigate to="/doors" />} />
        <Route path="/doors" element={isLoggedIn ? <Door /> : <Navigate to="/" />} />
        <Route path="/bulletin" element={isLoggedIn ? <BulletinBoard isGuest={isGuest} /> : <Navigate to="/" />} />
        <Route path="/bulletin/:id" element={isLoggedIn ? <PostDetail isGuest={isGuest} /> : <Navigate to="/" />} />
        <Route path="/sharing" element={isLoggedIn ? <SharingBoard /> : <Navigate to="/" />} />
      </Routes>
    </div>
  );
}

export default App;
