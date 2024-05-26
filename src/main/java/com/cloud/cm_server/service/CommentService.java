package com.cloud.cm_server.service;


import com.cloud.cm_server.dto.CommentRequestDto;
import com.cloud.cm_server.entiry.Comment;
import com.cloud.cm_server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    //Comment 작성
    public void write(CommentRequestDto board) {
        commentRepository.save(
                Comment.builder()
                        .boardId(board.getBoardId())
                        .content(board.getContent())
                        .nickname(board.getNickname())
                        .userId(board.getUserId())
                        .build()
        );
    }

    // Comment 리스트 처리
    public Page<Comment> commentList(Pageable pageable, Long boardId) {
        return commentRepository.findByBoardId(pageable, boardId);
    }



    // comment 불러오기
    public Comment commentView(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    // comment 삭제
    public void commentDelete(Integer id) {
        commentRepository.deleteById(id);
    }

    //comment 업데이트
    public void commentUpdate(Comment board) {
        Comment existingComment = commentRepository.findById(board.getId()).orElse(null);
        if (existingComment != null) {
            existingComment.setContent(board.getContent());
            commentRepository.save(existingComment);
        }
    }
}
