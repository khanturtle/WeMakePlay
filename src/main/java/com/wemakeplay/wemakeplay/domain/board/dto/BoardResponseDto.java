package com.wemakeplay.wemakeplay.domain.board.dto;

import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.comment.dto.response.CommentResponseDto;
import com.wemakeplay.wemakeplay.domain.comment.entity.Comment;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long Id;
    private String boardTitle;
    private String boardContent;
    private String boardOwner;
    private Long boardOwnerId;
    private String boardSport;
    private String boardArea;
    private Date playDate;
    private String boardStadium;
    private int boardPersonnel;
    private int boardAttendPersonnel;
    private int commentNum;
    private Page<CommentResponseDto> commentPage;
    private List<CommentResponseDto> commentList;
    private List<String> userNickNameList;

    public BoardResponseDto(Board board) {
        this.Id = board.getId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardSport = board.getBoardSport();
        this.boardArea = board.getBoardArea();
        this.playDate = board.getPlayDate();
        this.boardStadium = board.getBoardStadium();
        this.boardPersonnel = board.getBoardPersonnel();
        this.commentNum = board.getComments().size();
        this.boardAttendPersonnel = board.getBoardAttendPersonnel();
        this.boardOwner = board.getBoardOwner().getNickname();
        this.boardOwnerId = board.getBoardOwner().getId();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        List<String> userNickNameList = new ArrayList<>();
        List<AttendBoard> attendBoardList = board.getAttendBoards();
        for(AttendBoard attendBoard:attendBoardList){
            if(attendBoard.getParticipation().equals(Participation.attend)){
                userNickNameList.add(attendBoard.getUser().getNickname());
            }
        }
        this.commentList = commentResponseDtoList;
        this.userNickNameList = userNickNameList;
    }

    public BoardResponseDto(Board board, Page<Comment> commentPage) {
        this.Id = board.getId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardSport = board.getBoardSport();
        this.boardArea = board.getBoardArea();
        this.playDate = board.getPlayDate();
        this.boardStadium = board.getBoardStadium();
        this.boardPersonnel = board.getBoardPersonnel();
        this.commentNum = board.getComments().size();
        this.boardAttendPersonnel = board.getBoardAttendPersonnel();
        this.boardOwner = board.getBoardOwner().getNickname();
        this.commentPage = commentPage.map(CommentResponseDto::new);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        List<String> userNickNameList = new ArrayList<>();
        List<AttendBoard> attendBoardList = board.getAttendBoards();
        for(AttendBoard attendBoard:attendBoardList){
            if(attendBoard.getParticipation().equals(Participation.attend)){
                userNickNameList.add(attendBoard.getUser().getNickname());
            }
        }
        this.commentList = commentResponseDtoList;
        this.userNickNameList = userNickNameList;
    }

}
