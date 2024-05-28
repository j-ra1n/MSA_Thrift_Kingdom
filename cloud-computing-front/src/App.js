import React, { useState, useEffect } from 'react';
import { Route, Routes, Navigate, useLocation } from 'react-router-dom';
import './App.css';
import Login from './login/Login';
import Door from './component/Door';
import BulletinBoard from './component/BulletinBoard';
import SharingBoard from './component/SharingBoard';
import PostDetail from './board/PostDetail';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isGuest, setIsGuest] = useState(false);
  const query = useQuery();

  useEffect(() => {
    if (query.get('loggedIn') === 'true') {
      setIsLoggedIn(true);
      if (query.get('guest') === 'true') {
        setIsGuest(true);
      }
    }
  }, [query]);

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
