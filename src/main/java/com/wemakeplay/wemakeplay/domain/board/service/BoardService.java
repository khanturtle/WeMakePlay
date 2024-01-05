package com.wemakeplay.wemakeplay.domain.board.service;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoardRepository;
import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.board.repository.BoardRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final AttendBoardRepository attendBoardRepository;
    @Transactional
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
        Board board = findBoard(boardId);
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) {
        Board board = findBoard(boardId);
        //수정하려는 사람이 보드 생성자인지 확인
        if(userDetails.getUser().getNickname().equals(board.getBoardOwner().getNickname())){
            board.updateBoard(boardRequestDto);
            return new BoardResponseDto(board);
        }else{
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }
    @Transactional
    public void deleteBoard(Long boardId, UserDetailsImpl userDetails) {
        Board board = findBoard(boardId);
        //삭제하려는 사람이 보드 생성자인지 확인
        if(userDetails.getUser().getNickname().equals(board.getBoardOwner().getNickname())){
            boardRepository.delete(board);
        }else{
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }
    //현재 사용자가 보드에 신청
    //보드 작성자가 요청할 경우 예외처리
    @Transactional
    public void attendBoard(Long boardId, UserDetailsImpl userDetails) {
        Board board = findBoard(boardId);
        User user = userDetails.getUser();
        user.attendBoard(board);
    }
    @Transactional
    public List<AttendBoard> allowBoard(Long boardId, UserDetailsImpl userDetails) {
        Board board = findBoard(boardId);

        if(userDetails.getUser().getNickname().equals(board.getBoardOwner().getNickname())){
            List<AttendBoard> attendBoardList = attendBoardRepository.findByBoardId(boardId);
            List<AttendBoard> attendBoardWaitingList = new ArrayList<>();
            for(AttendBoard attendBoard:attendBoardList){
                if(attendBoard.getParticipation().equals(Participation.wait)){
                    attendBoardWaitingList.add(attendBoard);
                }
            }
            return attendBoardWaitingList;
        }else{
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }
    public Board findBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(
                ()-> new ServiceException(ErrorCode.NOT_EXIST_BOARD)
        );
    }
}
