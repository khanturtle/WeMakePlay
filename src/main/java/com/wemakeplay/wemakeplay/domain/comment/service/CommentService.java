package com.wemakeplay.wemakeplay.domain.comment.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_COMMENT_CONTENT;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_BOARD;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.board.repository.BoardRepository;
import com.wemakeplay.wemakeplay.domain.comment.dto.request.CommentRequestDto;
import com.wemakeplay.wemakeplay.domain.comment.dto.response.CommentResponseDto;
import com.wemakeplay.wemakeplay.domain.comment.entity.Comment;
import com.wemakeplay.wemakeplay.domain.comment.repository.CommentRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto createComment(User user, Long boardId, CommentRequestDto commentRequestDto) {
        if (commentRequestDto.getContent() == null) {
            throw new ServiceException(NOT_COMMENT_CONTENT);
        }

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_BOARD));

        Comment comment = Comment.builder()
            .content(commentRequestDto.getContent())
            .user(user)
            .board(board)
            .build();
        commentRepository.save(comment);

        return new  CommentResponseDto(comment);
    }
}
