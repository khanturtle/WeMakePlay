package com.wemakeplay.wemakeplay.domain.admin.controller;

import com.wemakeplay.wemakeplay.domain.admin.service.AdminService;
import com.wemakeplay.wemakeplay.domain.user.dto.response.ProfileResponseDto;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(
        @PathVariable(name = "userId") Long userId) {

        ProfileResponseDto responseDto = adminService.getUserProfile(userId);

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message(userId + "번 유저 조회 성공")
            .data(responseDto)
            .build());
    }

}