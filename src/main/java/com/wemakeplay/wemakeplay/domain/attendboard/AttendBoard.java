package com.wemakeplay.wemakeplay.domain.attendboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.wemakeplay.wemakeplay.domain.attendboard.Participation.*;

@Entity
@Getter
@NoArgsConstructor
public class AttendBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;
    @Enumerated(EnumType.STRING)
    private Participation participation;

    @Builder
    public AttendBoard(Board board, User user, Participation participation) {
        this.user = user;
        this.board = board;
        this.participation = participation;
    }

    public void allowAttend() {
        this.participation = attend;
    }
    public void rejectAttend() {
        this.participation = reject;
    }
}
