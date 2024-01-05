package com.wemakeplay.wemakeplay.domain.user.controller;

import com.wemakeplay.wemakeplay.domain.user.dto.request.SignupRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.SignupResponseDto;
import com.wemakeplay.wemakeplay.domain.user.service.UserService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // @Valid : SignupRequestDto 에 대한 유효성 검사
    // BindingResult : 유효성 검사 결과를 저장
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {

        // 유효성 검사 결과 fieldErrors 가 비어있지 않으면 SIGNUP_FAIL 던짐
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            throw new ServiceException(ErrorCode.SIGNUP_FAIL);
        }

        SignupResponseDto responseDto = userService.signUp(requestDto);

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("회원가입 성공")
            .data(responseDto)
            .build()
        );
    }

}
