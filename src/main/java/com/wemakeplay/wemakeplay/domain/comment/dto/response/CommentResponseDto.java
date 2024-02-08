package com.wemakeplay.wemakeplay.domain.comment.dto.response;

import com.wemakeplay.wemakeplay.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private Long commentUserId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.commentUserId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
