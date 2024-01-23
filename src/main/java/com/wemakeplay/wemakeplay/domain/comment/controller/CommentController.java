package com.wemakeplay.wemakeplay.domain.comment.controller;

import com.wemakeplay.wemakeplay.domain.comment.dto.request.CommentRequestDto;
import com.wemakeplay.wemakeplay.domain.comment.dto.response.CommentResponseDto;
import com.wemakeplay.wemakeplay.domain.comment.service.CommentService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<?> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "boardId") Long boardId,
        @RequestBody CommentRequestDto requestDto
    ) {

        CommentResponseDto responseDto =
            commentService.createComment(userDetails.getUser(), boardId, requestDto);

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("201")
                .message(boardId + "번 게시글에 댓글이 생성되었습니다.")
                .data(responseDto)
            .build());
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "commentId") Long commentId,
        @RequestBody CommentRequestDto requestDto
    ) {

        CommentResponseDto responseDto =
            commentService.updateComment(userDetails.getUser(), commentId, requestDto);

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message(commentId + "번 댓글이 수정되었습니다.")
                .data(responseDto)
            .build()
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "commentId") Long commentId
    ){

        commentService.deleteComment(userDetails.getUser(), commentId);

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message(commentId + "번 댓글이 삭제되었습니다.")
            .build()
        );
    }

    @GetMapping("/checkOwner/{commentId}")
    public void checkOwner(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.checkCommentOwner(commentId,userDetails.getUser());
    }
}
