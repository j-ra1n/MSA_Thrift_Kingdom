import React, { useState, useEffect, useRef } from 'react';
import { useUser } from '../context/UserContext';
import './Comment.css';

const Comment = ({ boardId, isGuest }) => {
  const { user } = useUser();
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [error, setError] = useState(null);
  const anonymousMap = useRef(new Map());  // 사용자별 익명 번호를 저장할 맵
  const anonymousCounter = useRef(1);  // 익명 번호를 증가시킬 카운터

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = () => {
    fetch(`http://172.25.235.177:8082/comment/list/${boardId}`)
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
        setError('댓글을 불러올 수 없습니다. 나중에 다시 시도해주세요.');
      });
  };

  const handleCreateComment = () => {
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

    fetch(`http://172.25.235.177:8082/comment/${boardId}`, {
      method: 'POST',
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
      fetchComments();
    })
    .catch(error => console.error('Error creating comment:', error));
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
          </div>
        ))}
      </div>
      <div className="comment-form">
        <br></br>
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
          <button onClick={handleCreateComment}>작성</button>
        </div>
      </div>
    </div>
  );
};

export default Comment;
