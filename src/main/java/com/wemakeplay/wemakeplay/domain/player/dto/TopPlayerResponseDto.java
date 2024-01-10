package com.wemakeplay.wemakeplay.domain.player.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopPlayerResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String age;
    private String area;
    private Long likes;
    private Long followers;

}
