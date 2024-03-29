package com.wemakeplay.wemakeplay.domain.user.dto.response;

import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProfileResponseDto {

    String nickname;
    String area;
    String intro;
    String age;
    UserRoleEnum role;

    @Builder
    public ProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.area = user.getArea();
        this.intro = user.getIntro();
        this.age = user.getAge();
        this.role = user.getRole();
    }
}
