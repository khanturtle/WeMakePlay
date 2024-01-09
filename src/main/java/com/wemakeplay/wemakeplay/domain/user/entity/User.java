package com.wemakeplay.wemakeplay.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoard;
import com.wemakeplay.wemakeplay.domain.attendboard.AttendBoardRepository;
import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.user.dto.request.ModifyProfileRequestDto;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<AttendBoard> attendBoards = new ArrayList<>();

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

    public void update(ModifyProfileRequestDto requestDto) {

        if (requestDto.getIntro() != null) {
            this.intro = requestDto.getIntro();
            this.email = requestDto.getEmail();
            this.area = requestDto.getArea();
            this.nickname = requestDto.getNickname();
        }
    }
}
