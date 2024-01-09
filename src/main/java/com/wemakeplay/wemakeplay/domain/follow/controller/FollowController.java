package com.wemakeplay.wemakeplay.domain.follow.controller;

import com.wemakeplay.wemakeplay.domain.follow.dto.FollowerResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowingResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.service.FollowService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> followUser(
        @PathVariable(name = "userId") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        FollowingResponseDto responseDto = followService.followUser(userId, userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("201")
            .message(userId + "번 유저 팔로우 신청 완료")
            .data(responseDto)
            .build()
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> unFollowUser(
        @PathVariable(name = "userId") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        FollowingResponseDto responseDto = followService.unFollowUser(userId, userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message(userId + "번 유저 팔로우 취소")
            .data(responseDto)
            .build()
        );
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        List<FollowingResponseDto> responseDto = followService.getFollowing(userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팔로잉 조회 성공")
            .data(responseDto)
            .build()
        );
    }

    @GetMapping("/follower")
    public ResponseEntity<?> getFollower(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        List<FollowerResponseDto> responseDto = followService.getFollower(userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팔로워 조회 성공")
            .data(responseDto)
            .build()
        );
    }

}
