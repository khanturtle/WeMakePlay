package com.wemakeplay.wemakeplay.domain.user.controller;

import com.wemakeplay.wemakeplay.domain.user.dto.request.ModifyProfileRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.request.SignupRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.ProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.SignupResponseDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.UserProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.service.UserService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequestDto requestDto) {

        SignupResponseDto responseDto = userService.signUp(requestDto);

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("회원가입 성공")
            .data(responseDto)
            .build()
        );
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> modifyProfile(@Valid @RequestBody ModifyProfileRequestDto requestDto,
        BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if (!fieldErrors.isEmpty()) {
            throw new ServiceException(ErrorCode.MODIFY_PROFILE_FAILED);
        }

        ProfileResponseDto responseDto = userService.modifyProfile(userDetails.getUser(), requestDto);

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("유저 프로필 변경 성공")
            .data(responseDto)
            .build()
        );
    }

        @GetMapping("/profile")
        public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
            UserProfileResponseDto responseDto = userService.getMyProfile(userDetails.getUser());

            return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message("프로필 조회 성공")
                .data(responseDto)
                .build()
            );
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawUser(userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("회원 탈퇴 성공")
            .build()
        );
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(
        @PathVariable(name = "userId") Long userId
    ) {

        UserProfileResponseDto responseDto = userService.getUserProfile(userId);

        return ResponseEntity.ok(RootResponseDto.builder()
                .code("200")
                .message(userId + "번 유저 프로필 조회 성공")
                .data(responseDto)
            .build()
        );

    }


}
