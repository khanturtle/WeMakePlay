package com.wemakeplay.wemakeplay.domain.follow.dto;

import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {

    private Long userId;
    private String username;
    private String nickname;

    public FollowResponseDto(Follow follow) {
        this.userId = follow.getFollowingUser().getId();
        this.username = follow.getFollowingUser().getUsername();
        this.nickname = follow.getFollowingUser().getNickname();
    }
}
