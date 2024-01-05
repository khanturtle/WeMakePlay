package com.wemakeplay.wemakeplay.domain.comment.controller;

import com.wemakeplay.wemakeplay.domain.comment.dto.request.CommentRequestDto;
import com.wemakeplay.wemakeplay.domain.comment.dto.response.CommentResponseDto;
import com.wemakeplay.wemakeplay.domain.comment.service.CommentService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<?> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "boardId") Long boardId,
        @RequestBody CommentRequestDto requestDto) {

        CommentResponseDto responseDto =
            commentService.createComment(userDetails.getUser(), boardId, requestDto);

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("201")
                .message(boardId + "번 게시글에 댓글이 생성되었습니다.")
                .data(responseDto)
            .build());

    }

}
