package com.wemakeplay.wemakeplay.domain.follow.dto;

import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowingResponseDto {

    private Long userId;
    private String username;
    private String nickname;

    public FollowingResponseDto(Follow follow) {
        this.userId = follow.getFollower().getId();
        this.username = follow.getFollower().getUsername();
        this.nickname = follow.getFollower().getNickname();
    }
}
