package com.cloud.cm_server.repository;


import com.cloud.cm_server.entiry.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByBoardId(Pageable pageable, Long postId);
}