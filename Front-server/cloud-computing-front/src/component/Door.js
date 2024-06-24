import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Door.css';
import bulletinImage from '../images/doorb.png'; // bulletin board 이미지
import sharingImage from '../images/doorb.png'; // sharing board 이미지

const Door = () => {
  const navigate = useNavigate();

  const handleBulletinClick = () => {
    navigate('/bulletin');
  };

  const handleSharingClick = () => {
    navigate('/sharing');
  };

  return (
    <div className="door-container">
      <div className="door" onClick={handleBulletinClick}>
        <img src={bulletinImage} alt="Bulletin Board" className="door-image" />
        <div className="door-name">자유의 방</div>
      </div>
      <div className="door" onClick={handleSharingClick}>
        <img src={sharingImage} alt="Sharing Board" className="door-image" />
        <div className="door-name">공유의 방</div>
      </div>
    </div>
  );
};

export default Door;
