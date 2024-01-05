package com.wemakeplay.wemakeplay.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // user (1000)
    SIGNUP_FAIL(HttpStatus.BAD_REQUEST, "1000", "회원가입에 실패했습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "1001", "이미 사용중인 닉네임입니다."),
    WRONG_ADMIN_CODE(HttpStatus.BAD_REQUEST, "1002", "관리자 암호가 틀려 등록이 불가능합니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "1003", "로그인에 실패했습니다."),
    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "1004", "토큰이 틀립니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "1005", "유저가 존재하지 않습니다."),
    MODIFY_PROFILE_FAILED(HttpStatus.BAD_REQUEST, "1006", "유저 정보 수정에 실패했습니다."),
    NOT_ADMIN(HttpStatus.BAD_REQUEST, "1007", "관리자가 아닙니다."),
    NOT_AUTHORIZATION(HttpStatus.BAD_REQUEST, "1008", "인증되지 않은 사용자입니다."),
    // board (2000)
    NOT_EXIST_BOARD(HttpStatus.BAD_REQUEST, "2001", "보드가 존재하지 않습니다."),
    NOT_BOARD_OWNER(HttpStatus.BAD_REQUEST, "2002", "보드 생성자가 아닙니다.");
    // team (3000)

    // comment (4000)

    // follow (5000)

    // like (6000)

    // bestPlayer(7000)

    private final HttpStatus status;
    private final String code;
    private final String message;
}
