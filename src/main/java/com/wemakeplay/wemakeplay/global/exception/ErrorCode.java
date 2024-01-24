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
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "1009", "유저를 찾을 수 없습니다."),
    // board (2000)
    NOT_EXIST_BOARD(HttpStatus.NOT_FOUND, "2001", "게시글이 존재하지 않습니다."),
    NOT_BOARD_OWNER(HttpStatus.NOT_ACCEPTABLE, "2002", "게시글 생성자가 아닙니다."),
    BOARD_OWNER(HttpStatus.NOT_ACCEPTABLE, "2003", "게시글 생성자는 이미 가입 되어 있습니다."),
    ALREADY_ATTENDED_BOARD(HttpStatus.NOT_ACCEPTABLE,"2004","가입 신청 대기중 입니다."),
    ALREADY_APPLIED_BOARD(HttpStatus.NOT_ACCEPTABLE,"2005","이미 가입된 게시글 입니다."),
    BOARD_FULL_PERSONNEL(HttpStatus.NOT_ACCEPTABLE,"2006","정원이 다 찼습니다."),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND,"2007","게시글을 찾을 수 없습니다."),
    // team (3000)
    NOT_TEAM_OWNER(HttpStatus.NOT_ACCEPTABLE, "3000", "팀 생성자가 아닙니다."),
    NOT_EXIST_TEAM(HttpStatus.NOT_FOUND, "3001", "팀이 존재하지 않습니다."),
    ALREADY_ATTENDING_TEAM(HttpStatus.NOT_ACCEPTABLE, "3002", "이미 참여 중인 팀 입니다."),
    NOT_TEAM_MEMBER(HttpStatus.NOT_ACCEPTABLE, "3003", "팀 멤버가 아닙니다."),
    TEAM_OWNER_CANNOT_WITHDRAW(HttpStatus.NOT_ACCEPTABLE, "3004", "팀 소유자는 탈퇴할 수 없습니다."),
    TEAM_OWNER(HttpStatus.NOT_ACCEPTABLE, "3005", "이미 가입 되어 있습니다."),
    TEAM_FULL_PERSONNEL(HttpStatus.NOT_ACCEPTABLE, "3006", "정원이 다 찼습니다."),
    CANNOT_KICK_TEAM_OWNER(HttpStatus.NOT_ACCEPTABLE, "3007", "팀 소유자를 쫓아낼 수 없습니다."),



    NOT_FOUND_TEAM(HttpStatus.BAD_REQUEST, "3003", "팀을 찾을 수 없습니다."),
    // comment (4000)
    NOT_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, "4001", "댓글 내용을 입력해주세요."),
    NOT_EXIST_COMMENT(HttpStatus.BAD_REQUEST, "4002", "댓글이 존재하지 않습니다."),
    NOT_COMMENT_OWNER(HttpStatus.BAD_REQUEST, "4003", "댓글 생성자가 아닙니다."),
    // follow (5000),
    NOT_FOLLOWING_YOURSELF(HttpStatus.BAD_REQUEST, "5001", "유저 본인을 팔로잉할 수 없습니다."),
    ALREADY_FOLLOWING_USER(HttpStatus.BAD_REQUEST, "5002", "이미 팔로잉을 했습니다."),
    NOT_UNFOLLOWING_YOURSELF(HttpStatus.BAD_REQUEST, "5003", "유저 본인을 팔로잉 취소를 할 수 없습니다."),
    NOT_FOLLOWING_USER(HttpStatus.BAD_REQUEST, "5004", "팔로잉을 한 유저를 찾을 수 없습니다."),

    // like (6000)
    NOT_LIKE_YOURSELF(HttpStatus.BAD_REQUEST, "6001", "자신에게 좋아요를 누를 수 없습니다."),
    ALREADY_PRESS_LIKE(HttpStatus.BAD_REQUEST, "6002", "이미 좋아요를 눌렀습니다."),
    NOT_UNLIKE_YOURSELF(HttpStatus.BAD_REQUEST, "6003", "자신에게 좋아요 취소를 누를 수 없습니다."),
    NOT_PRESS_LIKE(HttpStatus.BAD_REQUEST, "6004", "좋아요를 누르지 않았습니다.");

    // bestPlayer(7000)

    private final HttpStatus status;
    private final String code;
    private final String message;
}
