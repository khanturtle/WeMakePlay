package com.wemakeplay.wemakeplay.domain.board.controller;

import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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

    //보드 생성 페이지
    @GetMapping("/boardCreate")
    public String showCreateForm(Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto());
        return "boardForm";
    }
    //보드 생성 요청
    @PostMapping("/boardCreate")
    public String createBoard(@ModelAttribute BoardRequestDto boardRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        boardService.createBoard(boardRequestDto, userDetails.getUser());
        return "redirect:/mainPage-j";
    }
    //보드 단건 조회
    @GetMapping("/board/{boardId}")
    public String getBoard(@PathVariable Long boardId, Model model) {
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        model.addAttribute("boardResponseDto", boardResponseDto);
        return "board";
    }
    //보드 전체 조회
    @GetMapping("/boardMain") //(http://localhost:8080/boardMain)
    public String getBoards(Model model){
        List<BoardViewResponseDto> boardsList = boardService.getBoards();
        model.addAttribute("boardList",boardsList);
        return "boardMain"; //templates 폴더 내에 html파일 (boardMain.html)
    }

    @GetMapping("/board/{boardId}/edit")
    public String editBoardForm(@PathVariable Long boardId, Model model) {
        // 게시물 수정 페이지로 이동할 때 필요한 로직을 추가하세요.
        // 예를 들면, 게시물 정보를 가져와서 수정 폼에 미리 채워주는 등의 작업을 수행할 수 있습니다.
        // model.addAttribute("board", boardService.getBoard(boardId));

        return "boardForm"; // 수정 페이지의 Thymeleaf 템플릿 이름으로 변경하세요.
    }
    @PatchMapping("/board/{boardId}/edit")
    public String editBoard(
            @ModelAttribute BoardRequestDto boardRequestDto
            ,@PathVariable Long boardId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        boardService.updateBoard(boardId,boardRequestDto, userDetails.getUser());

        return "board";
    }

    @GetMapping("/board/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId) {
        // 게시물 삭제 로직을 추가하세요.
        // boardService.deleteBoard(boardId);
        return "redirect:/"; // 삭제 후 메인 페이지 또는 다른 페이지로 이동하도록 변경하세요.
    }
}
