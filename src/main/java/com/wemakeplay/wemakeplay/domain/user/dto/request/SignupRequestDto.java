package com.wemakeplay.wemakeplay.domain.user.dto.request;

import com.wemakeplay.wemakeplay.domain.user.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 60)
    private String password;

    @NotBlank
    @Size(max = 10)
    private String nickname;

    @NotBlank
    private String email;

    @NotBlank
    private String area;

    @NotBlank
    private String age;

    private String intro;



    @Builder.Default
    private String adminToken = "";

    @Builder.Default
    boolean admin = false;
}
