package com.wemakeplay.wemakeplay.domain.board.dto;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardViewResponseDto {

    private Long id;
    private String boardTitle;
    private String boardSport;
    private String boardArea;
    private int boardAttendPersonnel;
    private int boardPersonnel;

    public BoardViewResponseDto(Board board) {
        this.id = board.getId();
        this.boardTitle = board.getBoardTitle();
        this.boardSport = board.getBoardSport();
        this.boardArea = board.getBoardArea();
        this.boardAttendPersonnel = board.getBoardAttendPersonnel();
        this.boardPersonnel = board.getBoardPersonnel();
    }

}
