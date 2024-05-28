import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './PostDetail.css';
import Comment from './Comment';

const PostDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchPost();
  }, []);

  const fetchPost = () => {
    fetch(`http://localhost:8082/board/${id}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => setPost(data))
      .catch(error => {
        console.error('Error fetching post:', error);
        setError('게시글을 불러올 수 없습니다. 나중에 다시 시도해주세요.');
      });
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
              <h1 className="post-title">{post.title}</h1>
              <div className="post-meta">
                <p className="post-writer">{post.nickname}</p>
                <p className="post-date">{new Date(post.createdTime).toLocaleDateString()}</p>
              </div>
              <hr className="post-divider" />
              <div className="post-content">{post.content}</div>
            </>
          ) : (
            <p>Loading...</p>
          )
        )}
      </div>
      <Comment boardId={id} />
    </div>
  );
};

export default PostDetail;
