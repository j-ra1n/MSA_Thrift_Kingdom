import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './PostDetail.css';
import Comment from './Comment';
import { useUser } from '../context/UserContext';
import { BB_BASE_URL } from '../fetch.js'; // 수정된 부분

const PostDetail = ({ isGuest }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const { user } = useUser();

  useEffect(() => {
    fetchPost();
  }, []);

  const fetchPost = () => {
    fetch(`${BB_BASE_URL}/board/${id}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setPost(data);
        setTitle(data.title);
        setContent(data.content);
      })
      .catch(error => {
        console.error('Error fetching post:', error);
        setError('게시글을 불러올 수 없습니다. 나중에 다시 시도해주세요.');
      });
  };

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setTitle(post.title);
    setContent(post.content);
  };

  const handleSaveEdit = () => {
    const updatedPost = { title, content };
    fetch(`${BB_BASE_URL}/board/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedPost),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        setIsEditing(false);
        alert('수정 완료되었습니다');
        navigate('/bulletin');
      })
      .catch(error => {
        console.error('Error updating post:', error);
        setError('게시글을 수정할 수 없습니다. 나중에 다시 시도해주세요.');
      });
  };

  const handleDelete = () => {
    if (window.confirm('게시글을 삭제하시겠습니까?')) {
      fetch(`${BB_BASE_URL}/board/${id}`, {
        method: 'DELETE'
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          alert('게시글이 삭제되었습니다');
          navigate('/bulletin');
        })
        .catch(error => {
          console.error('Error deleting post:', error);
          setError('게시글을 삭제할 수 없습니다. 나중에 다시 시도해주세요.');
        });
    }
  };

  return (
    <div className="post-detail-container">
      <div className="post-detail-content">
        <button className="back-button" onClick={() => navigate(-1)}>←</button>
        {error ? (
          <p>{error}</p>
        ) : (
          post ? (
            <>
              {isEditing ? (
                <>
                  <div className="form-group">
                    <br></br><br></br>
                    <label>제목</label>
                    <input
                      type="text"
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label>내용</label>
                    <textarea
                      value={content}
                      onChange={(e) => setContent(e.target.value)}
                    />
                  </div>
                  <div className="button-container">
                    <button className="save-button" onClick={handleSaveEdit}>저장</button>
                    <button className="cancel-button" onClick={handleCancelEdit}>취소</button>
                  </div>
                </>
              ) : (
                <>
                  <h1 className="post-title">{post.title}</h1>
                  <div className="post-meta">
                    <p className="post-writer">{post.nickname}</p>
                    <p className="post-date">{new Date(post.createdTime).toLocaleDateString()}</p>
                  </div>
                  <hr className="post-divider" />
                  <div className="post-content">{post.content}</div>
                  {post.nickname === user.nickname && (
                    <>
                      <button className="edit-button" onClick={handleEditClick}>수정</button>
                      <button className="delete-button" onClick={handleDelete}>삭제</button>
                    </>
                  )}
                </>
              )}
            </>
          ) : (
            <p>Loading...</p>
          )
        )}
      </div>
      <Comment boardId={id} isGuest={isGuest} />
    </div>
  );
};

export default PostDetail;
