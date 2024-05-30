import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './SharingBoard.css';

const SharingBoard = () => {
  const navigate = useNavigate();
  const [items, setItems] = useState([]);
  const [productName, setProductName] = useState('');
  const [price, setPrice] = useState('');
  const [nickname, setNickname] = useState('');
  const [location, setLocation] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [selectedItem, setSelectedItem] = useState(null); // 선택된 아이템 상태 변수 추가
  const itemsPerPage = 6; // 페이지당 아이템 수를 6으로 설정

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = () => {
    fetch('http://172.25.235.177:8081/item/list')
      .then(response => response.json())
      .then(data => setItems(data.content))
      .catch(error => console.error('아이템을 불러오는 중 에러 발생:', error));
  };

  const handleBackClick = () => {
    navigate('/doors');
  };

  const handleCreateItem = () => {
    const newItem = { productName, price, nickname, location };

    fetch('http://172.25.235.177:8081/item/', {
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
      setNickname('');
      setLocation('');
    })
    .catch(error => console.error('아이템을 생성하는 중 에러 발생:', error));
  };

  const handlePreviousPage = () => {
    setCurrentPage(prevPage => Math.max(prevPage - 1, 0));
  };

  const handleNextPage = () => {
    setCurrentPage(prevPage => Math.min(prevPage + 1, Math.ceil(items.length / itemsPerPage) - 1));
  };

  const currentItems = items.slice(currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage);

  const handleItemClick = (item) => {
    setSelectedItem(item);
  };

  const handleCloseDetails = () => {
    setSelectedItem(null);
  };

  return (
    <div className="board-container">
      <button className="back-button" onClick={handleBackClick}>←</button>
      <h1 className="board-title">공유의 방</h1>
      <button className="create-button" onClick={() => setShowModal(true)}>글 작성</button>
      <div className="items-container">
        {currentItems.map((item, index) => (
          <div key={item.id} className="item-box" onClick={() => handleItemClick(item)}>
            <h2>{item.productName}</h2>
            <p>{item.price}원</p>
            <p>{item.location}</p>
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
              <input type="number" value={price} onChange={(e) => setPrice(e.target.value)} />
            </div>
            <div className="form-group">
              <label>작성자</label>
              <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} />
            </div>
            <div className="form-group">
              <label>위치 또는 링크</label>
              <input type="text" value={location} onChange={(e) => setLocation(e.target.value)} />
            </div>
            <button className="modal-create-button" onClick={handleCreateItem}>작성</button>
          </div>
        </div>
      )}

      {selectedItem && (
        <div className="modal-overlay" onClick={handleCloseDetails}>
          <div className="modal">
            <button className="modal-back-button" onClick={handleCloseDetails}>←</button>
            <h2 className="modal-title">{selectedItem.productName}</h2>
            <p>가격: {selectedItem.price}원</p>
            <p>위치: {selectedItem.location}</p>
            <p>작성자: {selectedItem.nickname}</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default SharingBoard;
