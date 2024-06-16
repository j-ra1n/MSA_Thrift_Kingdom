import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import { SB_BASE_URL } from '../fetch.js'; // 수정된 부분
import './SharingBoard.css';
import ItemDetail from './ItemDetail'; // ItemDetail 컴포넌트 가져오기

const SharingBoard = () => {
  const navigate = useNavigate();
  const { user } = useUser();
  const [items, setItems] = useState([]);
  const [productName, setProductName] = useState('');
  const [price, setPrice] = useState('');
  const [url, setUrl] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [selectedItem, setSelectedItem] = useState(null); // 선택된 아이템 상태 추가
  const itemsPerPage = 6;

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = () => {
    fetch(SB_BASE_URL + '/list')
      .then(response => response.json())
      .then(data => setItems(data.content))
      .catch(error => console.error('아이템을 불러오는 중 에러 발생:', error));
  };

  const handleBackClick = () => {
    navigate('/doors');
  };

  const handleCreateItem = () => {
    const newItem = { productName, price, nickname: user.nickname, url };

    fetch(SB_BASE_URL + '/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newItem)
    })
      .then(response => response.json())
      .then(() => {
        fetchItems();
        setShowModal(false);
        setProductName('');
        setPrice('');
        setUrl('');
      })
      .catch(error => console.error('아이템을 생성하는 중 에러 발생:', error));
  };

  const handleItemClick = (item) => {
    setSelectedItem(item);
    setShowModal(true); // 모달을 열고 아이템을 설정
  };

  const handlePreviousPage = () => {
    setCurrentPage(prevPage => Math.max(prevPage - 1, 0));
  };

  const handleNextPage = () => {
    setCurrentPage(prevPage => Math.min(prevPage + 1, Math.ceil(items.length / itemsPerPage) - 1));
  };

  const currentItems = items.slice(currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage);

  return (
    <div className="board-container">
      <button className="back-button" onClick={handleBackClick}>←</button>
      <h1 className="board-title">공유의 방</h1>
      {user && user.nickname !== 'Guest' && (
        <button className="create-button" onClick={() => setShowModal(true)}>글 작성</button>
      )}
      <div className="items-container">
        {currentItems.map((item, index) => (
          <div key={item.id} className="item-box" onClick={() => handleItemClick(item)}>
            <div className="item-header">
              <h2>{item.productName}</h2>
            </div>
            <p>{item.price}원</p>
            <p>{item.url}</p>
            <p>{item.nickname}</p>
          </div>
        ))}
      </div>
      <div className="pagination-buttons">
        <button onClick={handlePreviousPage} disabled={currentPage === 0}>◁</button>
        <button onClick={handleNextPage} disabled={currentPage === Math.ceil(items.length / itemsPerPage) - 1}>▷</button>
      </div>
      <div className="door-navigation">
        <div className="door" onClick={() => navigate('/bulletin')}>
          <div className="door-image bulletin"></div>
          <div className="door-name">자유</div>
        </div>
        <div className="door" onClick={() => navigate('/sharing')}>
          <div className="door-image sharing"></div>
          <div className="door-name">공유</div>
        </div>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <button className="modal-back-button" onClick={() => setShowModal(false)}>←</button>
            <h2 className="modal-title">최저가 상품 공유하기</h2>
            <div className="form-group">
              <label>상품명</label>
              <input type="text" value={productName} onChange={(e) => setProductName(e.target.value)} />
            </div>
            <div className="form-group">
              <label>가격</label>
              <input
                type="number"
                value={price}
                onChange={(e) => setPrice(Math.floor(e.target.value / 1000) * 1000)}
                step="1000"
              />
            </div>
            <div className="form-group">
              <label>위치 또는 링크</label>
              <input type="text" value={url} onChange={(e) => setUrl(e.target.value)} />
            </div>
            <button className="modal-create-button" onClick={handleCreateItem}>작성</button>
          </div>
        </div>
      )}

      {selectedItem && (
        <div className="modal-overlay">
          <ItemDetail item={selectedItem} onClose={() => setShowModal(false)} />
        </div>
      )}
    </div>
  );
};

export default SharingBoard;
