package com.wemakeplay.wemakeplay.domain.board.controller;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import com.wemakeplay.wemakeplay.domain.comment.dto.response.CommentResponseDto;
import com.wemakeplay.wemakeplay.domain.comment.entity.Comment;
import com.wemakeplay.wemakeplay.domain.comment.repository.CommentRepository;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.wemakeplay.wemakeplay.domain.comment.dto.request.CommentRequestDto;
import com.wemakeplay.wemakeplay.domain.comment.service.CommentService;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardViewController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    //보드 생성 페이지
    @GetMapping("/boardCreate")
    public String showCreateForm(
            Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto());
        return "PlaySpace/boardForm";
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
        return "PlaySpace/board";
    }

    //보드 전체 조회
    @GetMapping("/playSpace") //(http://localhost:8080/playSpace)
    public String getBoards(Model model) {
        List<BoardViewResponseDto> boardsList = boardService.getBoards();
        model.addAttribute("boardList", boardsList);
        return "PlaySpace/playSpace"; //templates 폴더 내에 html파일 (playSpace.html)
    }

    //보드 수정
    @GetMapping("/board/{boardId}/edit")
    public String editBoardForm(@PathVariable Long boardId, Model model) {
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        model.addAttribute("board", boardResponseDto);
        return "PlaySpace/editBoardForm";
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
    @GetMapping("/boardAttend/{boardId}")
    public String attendBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.attendBoard(boardId,userDetails.getUser());
        return "redirect:/board/{boardId}";
    }
    //신청자 목록 조회
    @GetMapping("/boardAttender/{boardId}")
    public String checkBoardAttender(
            @PathVariable Long boardId,
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<AttendBoard> attenderList=boardService.checkBoardAttender(boardId,userDetails.getUser());
        model.addAttribute("attenderList", attenderList);
        return "PlaySpace/BoardAttend/boardAttender";
    }
    //신청 승인
    @GetMapping("/allowBoard/{boardId}/{userId}")
    public String allowBoardAttend(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.allowBoardAttend(boardId, userId, userDetails.getUser());
        return "redirect:/board/{boardId}";
    }
    //신청 거절
    @GetMapping("/rejectBoard/{boardId}/{userId}")
    public String rejectBoardAttend(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.rejectBoardAttend(boardId, userId, userDetails.getUser());
        return "redirect:/board/{boardId}";
    }
    //참여자 확인
    @GetMapping("/allowed/attender/{boardId}")
    public String allowedAttender(
            @PathVariable Long boardId,
            Model model){
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        model.addAttribute("boardResponseDto", boardResponseDto);
        return "PlaySpace/BoardAttend/allowedAttender";
    }
    //댓글 달기
    @PostMapping("/board/{boardId}")
    public String addComment(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(userDetails.getUser(),boardId,commentRequestDto);

        return "redirect:/board/"+boardId;
    }
    //댓글 수정
    @GetMapping("/board/{boardId}/comment/{commentId}/edit")
    public String editCommentPage(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @ModelAttribute CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ServiceException(ErrorCode.NOT_EXIST_COMMENT)
        );
        BoardResponseDto board = boardService.getBoard(boardId);
        model.addAttribute("board",board);
        model.addAttribute("comment", comment);
        return "PlaySpace/Comment/edit-comment";
    }
    @PostMapping("/board/{boardId}/comment/{commentId}/edit")
    public String editComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @ModelAttribute CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(userDetails.getUser(), commentId, commentRequestDto);
        return "redirect:/board/" + boardId;
    }

    @GetMapping("/board/{boardId}/comment/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(userDetails.getUser(), commentId);
        return "redirect:/board/" + boardId;
    }
}
