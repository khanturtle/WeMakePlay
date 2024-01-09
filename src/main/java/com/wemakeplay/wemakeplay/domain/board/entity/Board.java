package com.wemakeplay.wemakeplay.domain.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.board.dto.BoardRequestDto;
import com.wemakeplay.wemakeplay.domain.comment.entity.Comment;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String boardTitle;
    private String boardContent;
    private String boardSport;
    private String boardArea;
    private String boardStadium;
    private int boardPersonnel;
    private int boardAttendPersonnel = 1;

    //만든 사람 정보
    @ManyToOne
    private User boardOwner;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)//, fetch = FetchType.EAGER
    private List<AttendBoard> attendBoards = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    //보드 생성
    public Board(BoardRequestDto boardRequestDto, User user) {
        this.boardTitle = boardRequestDto.getBoardTitle();
        this.boardContent = boardRequestDto.getBoardContent();
        this.boardSport = boardRequestDto.getBoardSport();
        this.boardArea = boardRequestDto.getBoardArea();
        this.boardStadium = boardRequestDto.getBoardStadium();
        this.boardPersonnel = boardRequestDto.getBoardPersonnel();
        this.boardOwner = user;
    }

    //보드 수정
    public void updateBoard(BoardRequestDto boardRequestDto) {
        this.boardTitle = boardRequestDto.getBoardTitle();
        this.boardContent = boardRequestDto.getBoardContent();
        this.boardSport = boardRequestDto.getBoardSport();
        this.boardArea = boardRequestDto.getBoardArea();
        this.boardStadium = boardRequestDto.getBoardStadium();
        this.boardPersonnel = boardRequestDto.getBoardPersonnel();
    }

    public void attendUser() {
        this.boardAttendPersonnel++;
    }
}