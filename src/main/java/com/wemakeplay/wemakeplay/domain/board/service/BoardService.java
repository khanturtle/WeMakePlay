package com.wemakeplay.wemakeplay.domain.board.service;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoardRepository;
import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.board.repository.BoardRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final AttendBoardRepository attendBoardRepository;

    private final UserRepository userRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = new Board(boardRequestDto, user);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    public List<BoardViewResponseDto> getBoards() {
        List<Board> boardList = boardRepository.findAll();
        Collections.sort(boardList, Comparator.comparing(Board::getModifiedAt).reversed());
        List<BoardViewResponseDto> boardViewResponseDtos = new ArrayList<>();
        for (Board board : boardList) {
            boardViewResponseDtos.add(new BoardViewResponseDto(board));
        }
        return boardViewResponseDtos;
    }

    public List<BoardViewResponseDto> get3Boards() {
        List<Board> boardList = boardRepository.findAll();
        // createdAt을 기준으로 내림차순 정렬
        Collections.sort(boardList, Comparator.comparing(Board::getCreatedAt).reversed());
        List<BoardViewResponseDto> boardViewResponseDtos = new ArrayList<>();
        // 상위 3개의 보드를 선택
        for (int i = 0; i < Math.min(boardList.size(), 3); i++) {
            Board board = boardList.get(i);
            boardViewResponseDtos.add(new BoardViewResponseDto(board));
        }
        return boardViewResponseDtos;
    }

    public Page<BoardViewResponseDto> getBoards(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("modifiedAt"))));
        return boardPage.map(BoardViewResponseDto::new);
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = findBoard(boardId);
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto, User user) {
        Board board = findBoard(boardId);
        //수정하려는 사람이 보드 생성자인지 확인
        if (user.getNickname().equals(board.getBoardOwner().getNickname())) {
            board.updateBoard(boardRequestDto);
            return new BoardResponseDto(board);
        } else {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = findBoard(boardId);
        //삭제하려는 사람이 보드 생성자인지 확인
        if (user.getNickname().equals(board.getBoardOwner().getNickname())) {
            attendBoardRepository.deleteAll(attendBoardRepository.findByBoardId(boardId));
            boardRepository.delete(board);
        } else {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }

    //현재 사용자가 보드에 신청
    @Transactional
    public void attendBoard(Long boardId, User user) {
        Board board = findBoard(boardId);
        if (board.getBoardAttendPersonnel() >= board.getBoardPersonnel()) {
            throw new ServiceException(ErrorCode.BOARD_FULL_PERSONNEL);
        }
        //게시글 주인 예외 처리
        if (user.getNickname().equals(board.getBoardOwner().getNickname())) {
            throw new ServiceException(ErrorCode.BOARD_OWNER);
        }
        List<AttendBoard> attendBoardList = user.getAttendBoards();
        for (AttendBoard myAttendBoard : attendBoardList) {
            if (myAttendBoard.getBoard().getId() == boardId) {
                if (myAttendBoard.getParticipation() == Participation.wait) {   //신청 대기자 예외 처리
                    throw new ServiceException(ErrorCode.ALREADY_ATTENDED_BOARD);
                } else if (myAttendBoard.getParticipation() == Participation.attend) {  //가입자 예외 처리
                    throw new ServiceException(ErrorCode.ALREADY_APPLIED_BOARD);
                }
            }
        }
        AttendBoard attendBoard = AttendBoard.builder()
                .user(user)
                .board(board)
                .participation(Participation.wait)
                .build();
        attendBoardRepository.save(attendBoard);
    }

    //보드 신청자 목록 확인
    public List<AttendBoard> checkBoardAttender(Long boardId, User user) {
        Board board = findBoard(boardId);
        if (user.getNickname().equals(board.getBoardOwner().getNickname())) {
            List<AttendBoard> attendBoardList = attendBoardRepository.findByBoardId(boardId);
            List<AttendBoard> attendBoardWaitingList = new ArrayList<>();
            for (AttendBoard attendBoard : attendBoardList) {
                if (attendBoard.getParticipation().equals(Participation.wait)) {
                    attendBoardWaitingList.add(attendBoard);
                }
            }
            return attendBoardWaitingList;
        } else {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }

    //각 유저 수락
    @Transactional
    public void allowBoardAttend(Long boardId, Long userId, User user) {
        Board board = findBoard(boardId);
        if (user.getNickname().equals(board.getBoardOwner().getNickname())) {
            if (board.getBoardAttendPersonnel() >= board.getBoardPersonnel()) {
                throw new ServiceException(ErrorCode.BOARD_FULL_PERSONNEL);
            }
            List<AttendBoard> attendBoardList = attendBoardRepository.findByBoardId(boardId);
            for (AttendBoard attendBoard : attendBoardList) {
                if (attendBoard.getUser().getId() == userId) {
                    attendBoard.allowAttend();
                }
            }
            board.attendUser();
        } else {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }

    //각 유저 거절
    @Transactional
    public void rejectBoardAttend(Long boardId, Long userId, User user) {
        Board board = findBoard(boardId);
        if (user.getNickname().equals(board.getBoardOwner().getNickname())) {
            List<AttendBoard> attendBoardList = attendBoardRepository.findByBoardId(boardId);
            for (AttendBoard attendBoard : attendBoardList) {
                if (attendBoard.getUser().getId() == userId) {
                    attendBoard.rejectAttend();
                    attendBoardRepository.delete(attendBoard);
                }
            }
        } else {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }

    @Transactional
    public void deleteExpiredBoards() {
        Date currentDate = Date.from(Instant.now());
        List<Board> boardsToDelete = boardRepository.findByPlayDateBefore(currentDate);
        boardRepository.deleteAll(boardsToDelete);
    }

    //보드 탈퇴
    @Transactional
    public void quitBoard(Long boardId, User user) {
        Board board = findBoard(boardId);
        List<AttendBoard> attendBoardList = attendBoardRepository.findByBoardId(boardId);
        boolean flag = true;
        for (AttendBoard attendBoard : attendBoardList) {
            if (user.getId().equals(board.getBoardOwner().getId())) {
                throw new ServiceException(ErrorCode.BOARD_OWNER_CANNOT_QUIT);
            } else if (attendBoard.getUser().getId().equals(user.getId()) && attendBoard.getParticipation().equals(Participation.attend)) {
                attendBoardRepository.delete(attendBoard);
                break;
            } else {
                flag = false;
            }
        }
        if (!flag) {
            throw new ServiceException(ErrorCode.NOT_BOARD_ATTENDER);
        }
    }

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new ServiceException(ErrorCode.NOT_EXIST_BOARD)
        );
    }


    @Transactional
    public void kickUserFromBoard(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_BOARD));

        User userToKick = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_USER));

        List<AttendBoard> attendBoardList = attendBoardRepository.findByBoardId(boardId);

        // 현재 로그인한 사용자가 보드의 소유자인지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if (!board.getBoardOwner().getUsername().equals(loggedInUsername)) {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }

        for (AttendBoard attendBoard : attendBoardList) {
            if (attendBoard.getParticipation().equals(Participation.attend)) {
                attendBoardRepository.delete(attendBoard);

                board.kickUser();
            }
        }
    }

    public void checkBoardOwner(Long boardId, User user) {
        Board board = findBoard(boardId);
        if (board.getBoardOwner().getNickname().equals(user.getNickname())) {
            return;
        } else {
            throw new ServiceException(ErrorCode.NOT_BOARD_OWNER);
        }
    }
}
