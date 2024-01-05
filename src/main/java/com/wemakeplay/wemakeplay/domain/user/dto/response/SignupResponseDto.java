package com.wemakeplay.wemakeplay.domain.user.dto.response;

import com.wemakeplay.wemakeplay.domain.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupResponseDto {

    String username;
    String nickname;
    UserRoleEnum role;

    @Builder
    public SignupResponseDto(String username, String nickname, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.role = role;
    }
}
