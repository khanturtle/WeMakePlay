package com.wemakeplay.wemakeplay.domain.like.controller;

import com.wemakeplay.wemakeplay.domain.like.dto.LikeResponseDto;
import com.wemakeplay.wemakeplay.domain.like.service.LikeService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> pressLike(
        @PathVariable(name = "userId") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        LikeResponseDto responseDto = likeService.pressLike(userId, userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("201")
                .message(userId + "번 유저 좋아요")
                .data(responseDto)
            .build()
        );
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> unPressLike(
        @PathVariable(name = "userId") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        likeService.unPressLike(userId, userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message(userId + "번 유저 좋아요 취소")
            .build());
    }
}
