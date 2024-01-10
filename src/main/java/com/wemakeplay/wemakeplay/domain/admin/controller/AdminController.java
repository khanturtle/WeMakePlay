package com.wemakeplay.wemakeplay.domain.admin.controller;

import com.wemakeplay.wemakeplay.domain.admin.service.AdminService;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardResponseDto;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import com.wemakeplay.wemakeplay.domain.user.dto.response.ProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.UserProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.service.UserService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final BoardService boardService;
    private final UserService userService;

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> getUser(
//        @PathVariable(name = "userId") Long userId) {
//
//        ProfileResponseDto responseDto = adminService.getUserProfile(userId);
//
//        return ResponseEntity.ok(RootResponseDto.builder()
//            .code("200")
//            .message(userId + "번 유저 조회 성공")
//            .data(responseDto)
//            .build());
//    }

    @GetMapping("/users")
    public List<UserProfileResponseDto> getUsers(
    ) {
        return adminService.getUsers();
    }

    @GetMapping("/boards")
    public List<BoardViewResponseDto> getBoards(
    ) {
        return boardService.getBoards();
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(
        @PathVariable Long boardId
    ) {

        adminService.deleteBoard(boardId);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("보드 삭제 성공")
            .build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
        @PathVariable(name = "userId") Long userId) {

        adminService.deleteUser(userId);

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message(userId + "번 유저를 탈퇴시켰습니다.")
            .build());
    }

}
