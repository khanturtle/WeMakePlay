package com.wemakeplay.wemakeplay.domain.board.dto;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.comment.dto.response.CommentResponseDto;
import com.wemakeplay.wemakeplay.domain.comment.entity.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private String boardTitle;
    private String boardContent;
    private String boardSport;
    private String boardArea;
    private String boardStadium;
    private int boardPersonnel;
    private int boardAttendPersonnel;
    private String boardOwner;
    private List<CommentResponseDto> commentList;

    public BoardResponseDto(Board board) {
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardSport = board.getBoardSport();
        this.boardArea = board.getBoardArea();
        this.boardStadium = board.getBoardStadium();
        this.boardPersonnel = board.getBoardPersonnel();
        this.boardAttendPersonnel = board.getBoardAttendPersonnel();
        this.boardOwner = board.getBoardOwner().getNickname();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        this.commentList = commentResponseDtoList;
    }

}
