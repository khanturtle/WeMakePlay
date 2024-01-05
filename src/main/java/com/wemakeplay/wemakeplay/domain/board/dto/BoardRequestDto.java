package com.wemakeplay.wemakeplay.domain.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String boardTitle;
    private String boardContent;
    private String boardSport;
    private String boardArea;
    private String boardStadium;
    private int boardPersonnel;
}
