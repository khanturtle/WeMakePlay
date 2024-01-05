package com.wemakeplay.wemakeplay.domain.attendboard;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.wemakeplay.wemakeplay.domain.attendboard.Participation.attend;
import static com.wemakeplay.wemakeplay.domain.attendboard.Participation.wait;

@Entity
@NoArgsConstructor
@Getter
public class AttendBoard {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private Participation participation;

    public AttendBoard(Board board, User user, Participation participation) {
        this.user = user;
        this.board = board;
        this.participation = participation;
    }
}
