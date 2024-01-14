package com.wemakeplay.wemakeplay.domain.board.controller;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardViewController {
    private final BoardService boardService;
    private final UserRepository userRepository;


    //보드 생성 페이지
    @GetMapping("/boardCreate")
    public String showCreateForm(
            Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto());
        return "boardForm";
    }

    //보드 생성 요청
    @PostMapping("/boardCreate")
    public String createBoard(
            @ModelAttribute BoardRequestDto boardRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.createBoard(boardRequestDto, userDetails.getUser());
        return "redirect:/playSpace";
    }

    //보드 단건 조회
    @GetMapping("/board/{boardId}")
    public String getBoard(@PathVariable Long boardId, Model model) {
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        model.addAttribute("boardResponseDto", boardResponseDto);
        return "board";
    }

    //보드 전체 조회
    @GetMapping("/playSpace") //(http://localhost:8080/playSpace)
    public String getBoards(Model model) {
        List<BoardViewResponseDto> boardsList = boardService.getBoards();
        model.addAttribute("boardList", boardsList);
        return "playSpace"; //templates 폴더 내에 html파일 (playSpace.html)
    }

    //보드 수정
    @GetMapping("/board/{boardId}/edit")
    public String editBoardForm(@PathVariable Long boardId, Model model) {
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        model.addAttribute("board", boardResponseDto);
        return "editBoardForm";
    }

    @PostMapping("/board/{boardId}/edit")
    public String editBoard(
            @ModelAttribute BoardRequestDto boardRequestDto,
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.updateBoard(boardId, boardRequestDto, userDetails.getUser());
        return "redirect:/playSpace";
    }

    //보드 삭제
    @GetMapping("/board/{boardId}/delete")
    public String deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return "redirect:/playSpace";
    }
    //보드 참여
    @GetMapping("/attend/{boardId}")
    public String attendBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.attendBoard(boardId,userDetails.getUser());
        return "redirect:/board/{boardId}";
    }
    //신청자 목록 조회
    @GetMapping("/attender/{boardId}")
    public String checkBoardAttender(
            @PathVariable Long boardId,
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<AttendBoard> attenderList=boardService.checkBoardAttender(boardId,userDetails.getUser());
        model.addAttribute("attenderList", attenderList);
        return "boardAttender";
    }
    @GetMapping("/allow/{boardId}/{userId}")
    public String allowBoardAttend(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.allowBoardAttend(boardId, userId, userDetails.getUser());
        return "redirect:/board/{boardId}";
    }
    @GetMapping("/reject/{boardId}/{userId}")
    public String rejectBoardAttend(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.rejectBoardAttend(boardId, userId, userDetails.getUser());
        return "redirect:/board/{boardId}";
    }
}
