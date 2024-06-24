package com.cloud.bbserver.service;


import com.cloud.bbserver.dto.BoardRequestDto;
import com.cloud.bbserver.entity.Board;
import com.cloud.bbserver.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //Bulletin Board 글 작성
    public void write(BoardRequestDto board) {
        boardRepository.save(
                Board.builder()
                        .title(board.getTitle())
                        .content(board.getContent())
                        .nickname(board.getNickname())
                        .user_id(board.getUser_id())
                        .build()
        );
    }

    // Bulletin Board 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // Bulletin Board 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 특정 Bulletin Board 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    // Bulletin Board 게시글 업데이트
    public void boardUpdate(Board board) {
        Board existingBoard = boardRepository.findById(board.getId()).orElse(null);
        if (existingBoard != null) {
            existingBoard.setTitle(board.getTitle());
            existingBoard.setContent(board.getContent());
            boardRepository.save(existingBoard);
        }
    }
}