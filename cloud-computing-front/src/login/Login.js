import React, { useEffect } from 'react';
import { useUser } from '../context/UserContext';
import beggarImage from '../images/begger.png';
import kakaoImage from '../images/kakao.png';
import googleImage from '../images/google.png';
import './Login.css';
import { Login_BASE_URL } from '../fetch.js'; // 수정된 부분

const Login = ({ onLogin }) => {
  const { setUser } = useUser();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const nickname = params.get('nickname');
    if (nickname) {
      setUser({ nickname });
      onLogin(false);  // 로그인 상태를 업데이트합니다.
    }
  }, [setUser, onLogin]);

  const handleLogin = async (platform) => {
    try {
      //const response = await fetch(`${Login_BASE_URL}/loginPage?platform=${platform}`);
      const response = await fetch(`${Login_BASE_URL}/doors?loggedIn=true&nickname=정우`);
      const text = await response.text(); // 응답을 텍스트로 읽기
      console.log("Response text:", text); // 응답 텍스트 출력
      const data = JSON.parse(text); // 텍스트를 JSON으로 파싱
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
  const handleGuestLogin = () => {
    onLogin(true);
    setUser({ nickname: 'Guest' });
  };

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
