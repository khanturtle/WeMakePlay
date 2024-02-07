package com.wemakeplay.wemakeplay.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import com.wemakeplay.wemakeplay.domain.user.dto.request.ModifyProfileRequestDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String age;

    @Column
    private String intro;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<AttendBoard> attendBoards = new ArrayList<>();
    //팀에 새로운 참여 객체 생성
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<AttendTeam> attendTeams = new ArrayList<>();

    @ManyToOne
    private Board board;

    @ManyToMany
    @JoinTable(
        name = "user_board",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "board_id")
    )
    private List<Board> boards = new ArrayList<>();

    @ManyToOne
    private Team team;

    @ManyToMany
    @JoinTable(
        name = "user_team",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams = new ArrayList<>();

    @Builder
    public User(String username, String password, String nickname, String email,
        String area,  String age, String intro, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.area = area;
        this.age = age;
        this.intro = intro;
        this.role = role;
    }

    public void attendBoard(Board board) {
        AttendBoard attendBoard = new  AttendBoard(board,this, Participation.wait);
        this.attendBoards.add(attendBoard);
    }

    public void attendTeam(Team team){
        AttendTeam attendTeam = new AttendTeam(team, this, com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait);
        this.attendTeams.add(attendTeam);
    }

    public void update(ModifyProfileRequestDto requestDto) {

        if (requestDto.getIntro() != null) {
            this.intro = requestDto.getIntro();
            this.email = requestDto.getEmail();
            this.area = requestDto.getArea();
            this.nickname = requestDto.getNickname();
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void addBoard(Board board) {
        this.boards.add(board);
        board.getUsers().add(this); // 연관된 양방향 매핑도 업데이트
    }

    public void removeBoard(Board board) {
        this.getBoards().remove(board);
        board.getUsers().remove(this); // 연관된 양방향 매핑도 업데이트
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public void addTeam(Team team) {
        this.teams.add(team);
        team.getUsers().add(this);
    }
    public void removeTeam(Team team) {
        this.getTeams().remove(team);
        team.getUsers().remove(this);
    }
}
