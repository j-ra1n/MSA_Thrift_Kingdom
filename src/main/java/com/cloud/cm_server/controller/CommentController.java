package com.cloud.cm_server.controller;


import com.cloud.cm_server.dto.CommentRequestDto;
import com.cloud.cm_server.entiry.Comment;
import com.cloud.cm_server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 특정 게시글에 Comment 작성
    @PostMapping("/{boardId}")
    public String commentWrite(@PathVariable("boardId") Long boardId, @RequestBody CommentRequestDto comment) {
        comment.setBoardId(boardId);
        commentService.write(comment);
        return "[Comment] Writing is complete";
    }


    // 특정 게시글의 Comment 리스트 목록
    @GetMapping("/list/{boardId}")
    public Page<Comment> commentList(@PathVariable("boardId") Long boardId,
                                     @PageableDefault(page = 0, size = 10, sort = "id",
                                             direction = Sort.Direction.DESC) Pageable pageable) {

        return commentService.commentList(pageable, boardId);

    }

    // comment 조회
    @GetMapping("/{id}")
    public Comment commentView(@PathVariable("id") Integer id) {
        return commentService.commentView(id);
    }

    // comment 삭제
    @DeleteMapping("/{id}")
    public String commentDelete(@PathVariable("id") Integer id) {
        commentService.commentDelete(id);
        return "[comment] Deleted complete";
    }

    // comment 게시글 수정
    @PutMapping("/{id}")
    public String commentUpdate(@PathVariable("id") Integer id, @RequestBody Comment comment) {
        Comment commentTemp = commentService.commentView(id);
        commentTemp.setContent(comment.getContent());
        commentService.commentUpdate(commentTemp);
        return "comment has been updated";
    }
}
