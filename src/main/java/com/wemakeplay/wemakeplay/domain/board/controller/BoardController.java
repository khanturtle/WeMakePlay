package com.wemakeplay.wemakeplay.domain.board.controller;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createBoard(
            @RequestBody BoardRequestDto boardRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
                .code("201")
                .message("보드 생성 성공")
                .data(boardResponseDto)
                .build());
    }

    @GetMapping
    public List<BoardViewResponseDto> getBoards(
    ) {
        return boardService.getBoards();
    }

    @GetMapping("/{boardId}")
    public BoardResponseDto getBoard(
            @PathVariable Long boardId
    ) {
        return boardService.getBoard(boardId);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto boardRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardId, boardRequestDto, userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("201")
                .message("보드 수정 성공")
                .data(boardResponseDto)
                .build());
    }
    @GetMapping("/checkOwner/{boardId}")
    public void checkOwner(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        boardService.checkBoardOwner(boardId,userDetails.getUser());
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message("보드 삭제 성공")
                .build());
    }

    @PostMapping("/attend/{boardId}")
    public ResponseEntity<?> attendBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.attendBoard(boardId, userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message("보드 가입 신청 성공")
                .build());
    }

    @GetMapping("/attend/{boardId}")
    public List<AttendBoard> checkBoardAttender(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return boardService.checkBoardAttender(boardId, userDetails.getUser());
    }
    @DeleteMapping("/quit/{boardId}")
    public ResponseEntity<?> quitBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.quitBoard(boardId,userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message("보드에서 탈퇴하였습니다.")
                .build());
    }

    @PatchMapping("/allowAttend/{boardId}/{userId}")
    public ResponseEntity<?> allowBoardAttend(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.allowBoardAttend(boardId, userId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message("보드 가입 신청을 수락하였습니다.")
                .build());
    }

    @PatchMapping("/rejectAttend/{boardId}/{userId}")
    public ResponseEntity<?> rejectBoardAttend(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.rejectBoardAttend(boardId, userId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message("보드 가입 신청을 거절하였습니다.")
                .build());
    }

    @PostMapping("/{boardId}/kick/{userId}")
    public ResponseEntity<?> kickUserFromBoard(
        @PathVariable Long boardId,
        @PathVariable Long userId) {
        boardService.kickUserFromBoard(boardId, userId);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message(userId + "유저가 강퇴되었습니다.")
            .build());
    }
}
