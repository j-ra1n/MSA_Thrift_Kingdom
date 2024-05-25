import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './PostDetail.css';

const PostDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);

  useEffect(() => {
    fetchPost();
  }, []);

  const fetchPost = () => {
    fetch(`http://localhost:8082/board/${id}`)
      .then(response => response.json())
      .then(data => setPost(data))
      .catch(error => console.error('Error fetching post:', error));
  };

  return (
    <div className="post-detail-container">
      <button className="back-button" onClick={() => navigate(-1)}>←</button>
      {post ? (
        <>
          <h1 className="post-title">{post.title}</h1>
          <p className="post-writer">작성자: {post.nickname}</p>
          <p className="post-date">{new Date(post.createdTime).toLocaleDateString()}</p>
          <div className="post-content">{post.content}</div>
        </>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default PostDetail;
