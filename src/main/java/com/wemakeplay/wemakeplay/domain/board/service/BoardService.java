package com.wemakeplay.wemakeplay.domain.board.service;

import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.board.repository.BoardRepository;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) {
        Board board = new Board(boardRequestDto, userDetails);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> getBoards() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }
        return boardResponseDtoList;
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new ServiceException(ErrorCode.NOT_EXIST_BOARD)
        );
        return new BoardResponseDto(board);
    }

    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new ServiceException(ErrorCode.NOT_EXIST_BOARD)
        );
        //수정하려는 사람이 보드 생성자인지 확인
        if(userDetails.getUser().getNickname().equals(board.getBoardOwner().getNickname())){
            board.updateBoard(boardRequestDto);
            return new BoardResponseDto(board);
        }else{
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }
    public void deleteBoard(Long boardId, UserDetailsImpl userDetails) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new ServiceException(ErrorCode.NOT_EXIST_BOARD)
        );
        //수정하려는 사람이 보드 생성자인지 확인
        if(userDetails.getUser().getNickname().equals(board.getBoardOwner().getNickname())){
            boardRepository.delete(board);
        }else{
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }

    public void attendBoard(Long boardId, UserDetailsImpl userDetails) {
    }
}
