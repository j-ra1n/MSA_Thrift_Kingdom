import React from 'react';
import beggarImage from '../images/begger.png';
import kakaoImage from '../images/kakao.png';
import googleImage from '../images/google.png';
import './Login.css';

const Login = ({ onLogin }) => {
  const handleLogin = async (platform) => {
    try {
      const response = await fetch(`http://localhost:8081/auth/loginPage?platform=${platform}`);
      const data = await response.json();
      if (data.resCode === 200) {
        const loginUrl = data.resObj;
        window.location.href = loginUrl;
      } else {
        console.error(`Failed to get login URL for platform ${platform}: ${data.resMsg}`);
      }
    } catch (error) {
      console.error('Error fetching login URL:', error);
    }
  };

  const handleKakaoLogin = () => handleLogin('KAKAO');
  const handleGoogleLogin = () => handleLogin('GOOGLE');
  const handleGuestLogin = () => onLogin(true);

  return (
    <div className="login-container">
      <div className="login-box">
        <h1>절약의 방</h1>
        <img src={beggarImage} alt="beggar" className="login-image" />
        <h2>로그인</h2>
        <button className="login-button kakao" onClick={handleKakaoLogin}>
          <img src={kakaoImage} alt="Kakao" className="button-image" />
          Kakao로 시작하기
        </button>
        <button className="login-button google" onClick={handleGoogleLogin}>
          <img src={googleImage} alt="Google" className="button-image" />
          Google로 시작하기
        </button>
        <button className="login-button guest" onClick={handleGuestLogin}>구경만 할게요</button>
      </div>
    </div>
  );
};

export default Login;
