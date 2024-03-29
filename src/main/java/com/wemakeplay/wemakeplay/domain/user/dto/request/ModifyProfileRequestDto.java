package com.wemakeplay.wemakeplay.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProfileRequestDto {

    String nickname;
    String email;
    String intro;
    String area;
}
