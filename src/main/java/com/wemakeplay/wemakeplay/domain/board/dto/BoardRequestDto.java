package com.wemakeplay.wemakeplay.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class BoardRequestDto {
    private String boardTitle;
    private String boardContent;
    private String boardSport;
    private String boardArea;
    private String boardStadium;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date playDate;
    private int boardPersonnel;
}
