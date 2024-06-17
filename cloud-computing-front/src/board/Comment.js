import React, { useState, useEffect, useRef } from 'react';
import { useUser } from '../context/UserContext';
import { useNavigate } from 'react-router-dom';  // 추가된 부분
import './Comment.css';
import { CM_BASE_URL } from '../fetch.js'; // 수정된 부분

const Comment = ({ boardId, isGuest }) => {
  const { user } = useUser();
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [error, setError] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [editCommentId, setEditCommentId] = useState(null);
  const anonymousMap = useRef(new Map());  // 사용자별 익명 번호를 저장할 맵
  const anonymousCounter = useRef(1);  // 익명 번호를 증가시킬 카운터
  const navigate = useNavigate();  // 추가된 부분

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = () => {
    fetch(`${CM_BASE_URL}/list/${boardId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setComments(data.content);
        data.content.forEach(comment => {
          if (comment.nickname.startsWith('익명')) {
            const number = parseInt(comment.nickname.replace('익명', ''), 10);
            if (!anonymousMap.current.has(comment.userId)) {
              anonymousMap.current.set(comment.userId, number);
              if (number >= anonymousCounter.current) {
                anonymousCounter.current = number + 1;
              }
            }
          }
        });
      })
      .catch(error => {
        console.error('Error fetching comments:', error);
        setError('');
      });
  };

  const handleCreateOrUpdateComment = () => {
    if (isGuest || !user) {
      alert('댓글을 작성할 수 없습니다. 로그인 해주세요.');
      return;
    }

    let nickname = user.nickname;
    if (isAnonymous) {
      if (!anonymousMap.current.has(user.userId)) {
        anonymousMap.current.set(user.userId, anonymousCounter.current++);
      }
      nickname = `익명${anonymousMap.current.get(user.userId)}`;
    }

    const newComment = { content, nickname, boardId, userId: user.userId };

    const url = editMode ? `${CM_BASE_URL}/${editCommentId}` : `${CM_BASE_URL}/${boardId}`;
    const method = editMode ? 'PUT' : 'POST';

    fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newComment)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.text();
    })
    .then(() => {
      setContent('');
      setIsAnonymous(false);
      setEditMode(false);
      setEditCommentId(null);
      fetchComments();
    })
    .catch(error => console.error('Error creating comment:', error));
  };

  const handleEditComment = (comment) => {
    setContent(comment.content);
    setEditCommentId(comment.id);
    setEditMode(true);
  };

  const handleDeleteComment = (id) => {
    const confirmDelete = window.confirm('댓글을 삭제하시겠습니까?');
    if (confirmDelete) {
      fetch(`${CM_BASE_URL}/${id}`, {
        method: 'DELETE',
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
      .catch(error => console.error('Error deleting comment:', error));
    }
  };

  return (
    <div className="comment-section">
      <h2>댓글</h2>
      {error && <p className="error-message">{error}</p>}
      <div className="comment-list">
        {comments.map((comment, index) => (
          <div key={comment.id} className="comment">
            <p className="comment-nickname">{comment.nickname}</p>
            <p className="comment-content">{comment.content}</p>
            {comment.nickname === user.nickname && (
              <>
                <span className="edit-link" onClick={() => handleEditComment(comment)}>수정</span>
                <span className="delete-link" onClick={() => handleDeleteComment(comment.id)}>삭제</span>
              </>
            )}
          </div>
        ))}
      </div>
      <div className="comment-form">
        <div className="form-group">
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
          ></textarea>
        </div>
        <div className="form-group-inline">
          <label className="anonymous-checkbox">
            <input type="checkbox" checked={isAnonymous} onChange={() => setIsAnonymous(!isAnonymous)} />
            익명
          </label>
          <button onClick={handleCreateOrUpdateComment}>{editMode ? '수정' : '작성'}</button>
        </div>
      </div>
    </div>
  );
};

export default Comment;
