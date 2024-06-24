import React, { useState } from 'react';
import { useUser } from '../context/UserContext';
import { useParams, useNavigate } from 'react-router-dom';
import './ItemDetail.css';
import { SB_BASE_URL } from '../fetch.js'; // 수정된 부분

const ItemDetail = ({ item, onClose }) => {
  const { user } = useUser();
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [productName, setProductName] = useState(item.productName);
  const [price, setPrice] = useState(item.price);
  const [url, setUrl] = useState(item.url);

  const handleUpdateItem = () => {
    const updatedItem = { ...item, productName, price, url };
    if (window.confirm('게시물을 수정하시겠습니까?')) {
    fetch(`${SB_BASE_URL}/${item.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(updatedItem)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.text();
      })
      .then(data => {
        alert('수정 완료되었습니다');
        navigate('/bulletin');
      })
      .catch(error => console.error('', error));
    }
  };

  const handleDeleteItem = () => {
    if (window.confirm('게시물을 삭제하시겠습니까?')) {
      fetch(`${SB_BASE_URL}/${item.id}`, {
        method: 'DELETE'
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.text();
        })
        .then(() => {
          alert('게시글이 삭제되었습니다');
          navigate('/bulletin');
        })
        .catch(error => console.error('', error));
    }
  };

  const handleLinkClick = (e) => {
    e.preventDefault();
    if (url) {
      window.open(url.startsWith('http') ? url : `http://${url}`, '_blank');
    }
  };

  return (
    <div className="item-detail-container">
      <button className="detail-back-button" onClick={onClose}>←</button>
      {isEditing ? (
        <>
          <div className="form-group">
            <label>상품명</label>
            <input type="text" value={productName} onChange={(e) => setProductName(e.target.value)} />
          </div>
          <div className="form-group">
            <label>가격</label>
            <input
              type="number"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              step="1000"
            />
          </div>
          <div className="form-group">
            <label>링크</label>
            <input type="text" value={url} onChange={(e) => setUrl(e.target.value)} />
          </div>
          <button className="modal-update-button" onClick={handleUpdateItem}>수정 완료</button>
        </>
      ) : (
        <>
          <h2 className="item-title">{item.productName}</h2>
          <p>{item.price}원</p>
          <p>
            <a href={url && (url.startsWith('http') ? url : `http://${url}`)} onClick={handleLinkClick} style={{ color: '#4787e7' }}>
              {url}
            </a>
          </p>
          <div className="item-footer">
            <span className="item-nickname">{item.nickname}</span>
            <span className="item-date">{item.date}</span>
          </div>
          {user && item.nickname === user.nickname && (
            <div className="edit-delete-buttons">
              <button className="edit-button" onClick={() => setIsEditing(true)}>수정</button>
              <button className="delete-button" onClick={handleDeleteItem}>삭제</button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default ItemDetail;
