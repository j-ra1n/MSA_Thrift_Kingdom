package com.cloud.sbserver.controller;

import com.cloud.sbserver.dto.BoardRequestDto;
import com.cloud.sbserver.entity.Item;
import com.cloud.sbserver.service.BoardService;
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

    // Bulletin Board 게시글 작성
    @PostMapping("/")
    public String boardWrite(@RequestBody BoardRequestDto board) {
        boardService.write(board);
        return "Writing is complete";
    }

    // Bulletin Board 게시글 리스트 목록
    @GetMapping("/list")
    public Page<Item> boardList(@PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable,
                                @RequestParam(name = "searchKeyword", required = false) String searchKeyword) {
        if (searchKeyword == null) {
            return boardService.boardList(pageable);
        } else {
            return boardService.boardSearchList(searchKeyword, pageable);
        }
    }

    // Bulletin Board 게시글 조회
    @GetMapping("/{id}")
    public Item boardView(@PathVariable("id") Integer id) {
        return boardService.boardView(id);
    }

    // Bulletin Board 게시글 삭제
    @DeleteMapping("/{id}")
    public String boardDelete(@PathVariable("id") Integer id) {
        boardService.boardDelete(id);
        return "Deleted complete";
    }

    // Bulletin Board 게시글 수정
    @PutMapping("/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, @RequestBody Item board) {
        Item boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        boardService.boardUpdate(boardTemp);
        return "post has been updated";
    }
}
