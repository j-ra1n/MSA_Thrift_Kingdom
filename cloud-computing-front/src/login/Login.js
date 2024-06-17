import React, { useEffect } from 'react';
import { useUser } from '../context/UserContext';
import beggarImage from '../images/begger.png';
import kakaoImage from '../images/kakao.png';
import googleImage from '../images/google.png';
import './Login.css';
import { Login_BASE_URL } from '../fetch.js';

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

  const handleFakeLogin = () => {
    // 하드코딩된 유저로 로그인
    setUser({ nickname: 'jw' });
    onLogin(false); // 로그인 상태를 업데이트합니다.
  };

  const handleLoginRedirect = async (url) => {
    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nickname: 'jw' }),
      });

      if (response.ok) {
        const result = await response.json();
        setUser({ nickname: result.data });
        setTimeout(() => {
          // 여기에 회원가입 후 리디렉션할 URL을 설정하세요.
          window.location.href = `http://172.25.235.160:31685/doors?loggedIn=true&nickname=jw`;
        }, 2000);
      } else {
        console.error('Registration failed');
      }
    } catch (error) {
      console.error('Error during registration', error);
    }
  };

  const handleKakaoLogin = () => {
    handleFakeLogin();
    handleLoginRedirect(`${Login_BASE_URL}/loginPage?platform=KAKAO`);
  };

  const handleGoogleLogin = () => {
    handleFakeLogin();
    handleLoginRedirect(`${Login_BASE_URL}/regis`);
  };

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
