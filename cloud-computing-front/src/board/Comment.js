import React, { useState, useEffect } from 'react';
import './Comment.css';

const Comment = ({ boardId }) => {
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState('');
  const [nickname, setNickname] = useState('');
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = () => {
    fetch(`http://localhost:8083/comment/list/${boardId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => setComments(data.content))
      .catch(error => {
        console.error('Error fetching comments:', error);
        setError('댓글을 불러올 수 없습니다. 나중에 다시 시도해주세요.');
      });
  };

  const handleCreateComment = () => {
    const newComment = { content, nickname, boardId };

    fetch(`http://localhost:8083/comment/${boardId}`, {
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
      setNickname('');
      fetchComments();
    })
    .catch(error => {
      console.error('Error creating comment:', error);
      setError('댓글을 작성할 수 없습니다. 나중에 다시 시도해주세요.');
    });
  };

  return (
    <div className="comment-section">
      <h2>comment</h2>
      {error && <p className="error-message">{error}</p>}
      <div className="comment-list">
        {comments.map((comment, index) => (
          <div key={comment.id} className="comment">
            <p className="comment-nickname">{comment.nickname}</p>
            <br></br><br></br>
            <p className="comment-content">{comment.content}</p>
          </div>
        ))}
      </div>
      <div className="comment-form">
        <div className="form-group">
          <br></br>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
          ></textarea>
        </div>
        <button onClick={handleCreateComment}>작성</button>
      </div>
    </div>
  );
};

export default Comment;
