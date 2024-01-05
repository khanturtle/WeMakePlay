package com.wemakeplay.wemakeplay.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProfileRequestDto {

    String username;
    String nickname;
    String email;
    String intro;
    String area;
    String age;
    String imageName;
    String imagePath;

}
