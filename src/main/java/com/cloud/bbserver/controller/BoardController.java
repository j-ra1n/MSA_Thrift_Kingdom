package com.cloud.bbserver.controller;

import com.cloud.bbserver.dto.BoardRequestDto;
import com.cloud.bbserver.entity.Board;
import com.cloud.bbserver.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 게시글 작성
    @PostMapping("/write")
    public String boardWrite(@RequestBody BoardRequestDto board) {
        boardService.write(board);
        return "글 작성이 완료되었습니다!";
    }

    //게시글 리스트 목록
    @GetMapping("/list")
    public Page<Board> boardList(@PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(required = false) String searchKeyword) {
        if (searchKeyword == null) {
            return boardService.boardList(pageable);
        } else {
            return boardService.boardSearchList(searchKeyword, pageable);
        }
    }

    //게시글 조회
    @GetMapping("/view/{id}")
    public Board boardView(@PathVariable("id") Integer id) {
        return boardService.boardView(id);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    public String boardDelete(@PathVariable("id") Integer id) {
        boardService.boardDelete(id);
        return "삭제완료!";
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, @RequestBody Board board) {
        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        boardService.boardUpdate(boardTemp);
        return "게시글이 업데이트되었습니다!";
    }
}
