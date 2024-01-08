package com.wemakeplay.wemakeplay.domain.board.dto;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private String boardTitle;
    private String boardContent;
    private String boardSport;
    private String boardArea;
    private String boardStadium;
    private int boardPersonnel;
    private String boardOwner;
    public BoardResponseDto(Board board) {
        this.boardTitle=board.getBoardTitle();
        this.boardContent=board.getBoardContent();
        this.boardSport=board.getBoardSport();
        this.boardArea=board.getBoardArea();
        this.boardStadium=board.getBoardStadium();
        this.boardPersonnel=board.getBoardPersonnel();
        this.boardOwner=board.getBoardOwner().getNickname();
    }

}
