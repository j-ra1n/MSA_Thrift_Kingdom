import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import './SharingBoard.css';

const SharingBoard = () => {
  const navigate = useNavigate();
  const { user } = useUser(); // 현재 로그인된 사용자 정보 가져오기
  const [items, setItems] = useState([]);
  const [productName, setProductName] = useState('');
  const [price, setPrice] = useState('');
  const [url, setUrl] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [selectedItem, setSelectedItem] = useState(null); // 선택된 아이템 상태 변수 추가
  const [isEditing, setIsEditing] = useState(false); // 수정 모드 상태 변수 추가
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
    const newItem = { productName, price, nickname: user.nickname, url };

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
      setUrl('');
    })
    .catch(error => console.error('아이템을 생성하는 중 에러 발생:', error));
  };

  const handleUpdateItem = () => {
    const updatedItem = { ...selectedItem, productName, price, url };

    fetch(`http://172.25.235.177:8081/item/${selectedItem.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(updatedItem)
    })
    .then(response => response.json())
    .then(() => {
      fetchItems();
      setSelectedItem(null);
      setProductName('');
      setPrice('');
      setUrl('');
      setIsEditing(false);
    })
    .catch(error => console.error('아이템을 수정하는 중 에러 발생:', error));
  };

  const handleDeleteItem = (itemId) => {
    fetch(`http://172.25.235.177:8081/item/${itemId}`, {
      method: 'DELETE'
    })
    .then(response => response.text())
    .then(data => {
      alert(data);
      fetchItems();
      setSelectedItem(null);
    })
    .catch(error => console.error('아이템을 삭제하는 중 에러 발생:', error));
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
    setProductName(item.productName);
    setPrice(item.price);
    setUrl(item.url);
    setIsEditing(false); // 클릭 시 수정 모드 해제
  };

  const handleCloseDetails = () => {
    setSelectedItem(null);
    setIsEditing(false);
  };

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
          <div className="modal">
            <button className="modal-back-button" onClick={handleCloseDetails}>←</button>
            <h2 className="modal-title">{selectedItem.productName}</h2>
            <p> {selectedItem.price}원</p>
            <p> {selectedItem.url}</p>
            <p>작성자: {selectedItem.nickname}</p>
            {user && selectedItem.nickname === user.nickname && (
              <>
                {!isEditing && (
                  <>
                    <button className="edit-button" onClick={() => setIsEditing(true)}>수정</button>
                    <button className="delete-button" onClick={() => handleDeleteItem(selectedItem.id)}>삭제</button>
                  </>
                )}
                {isEditing && (
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
                        onChange={(e) => setPrice(Math.floor(e.target.value / 1000) * 1000)}
                        step="1000"
                      />
                    </div>
                    <div className="form-group">
                      <label>링크</label>
                      <input type="text" value={url} onChange={(e) => setUrl(e.target.value)} />
                    </div>
                    <button className="modal-update-button" onClick={handleUpdateItem}>수정 완료</button>
                  </>
                )}
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default SharingBoard;
