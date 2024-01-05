package com.wemakeplay.wemakeplay.user.dto.response;

import com.wemakeplay.wemakeplay.user.entity.User;
import com.wemakeplay.wemakeplay.user.entity.UserRoleEnum;
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
    String imageName;
    String imagePath;
    UserRoleEnum role;

    @Builder
    public ProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.area = user.getArea();
        this.intro = user.getIntro();
        this.imageName = user.getImageName();
        this.imagePath = user.getImagePath();
        this.role = user.getRole();
    }
}
