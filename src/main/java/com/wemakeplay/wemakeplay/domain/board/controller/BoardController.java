package com.wemakeplay.wemakeplay.domain.board.controller;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public List<BoardResponseDto> getBoards(
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

    @PatchMapping("/attend/{boardId}")
    public List<AttendBoard> allowBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return boardService.allowBoard(boardId, userDetails.getUser());
    }

}
