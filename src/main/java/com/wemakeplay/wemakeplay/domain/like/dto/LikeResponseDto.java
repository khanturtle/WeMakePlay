package com.wemakeplay.wemakeplay.domain.like.dto;

import com.wemakeplay.wemakeplay.domain.like.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private Long userId;
    private String username;
    private String nickname;
    private Long likeCount;

    public LikeResponseDto(Like like, Long likeCount) {
        this.userId = like.getLikeUser().getId();
        this.username = like.getLikeUser().getUsername();
        this.nickname = like.getLikeUser().getNickname();
        this.likeCount = likeCount;
    }

    public void calculateLikeCount(Long count) {
        this.likeCount = count;
    }
}
