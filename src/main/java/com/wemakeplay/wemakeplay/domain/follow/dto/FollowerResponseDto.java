package com.wemakeplay.wemakeplay.domain.follow.dto;

import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowerResponseDto {

    private Long id;
    private String username;
    private String nickname;

    public FollowerResponseDto(Follow follow) {
        this.id = follow.getFollowing().getId();
        this.username = follow.getFollowing().getUsername();
        this.nickname = follow.getFollowing().getNickname();
    }
}
