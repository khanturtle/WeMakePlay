package com.wemakeplay.wemakeplay.domain.attendteam;

import static com.wemakeplay.wemakeplay.domain.attendteam.Participation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class AttendTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @Enumerated(EnumType.STRING)
    private Participation participation;

    @Builder
    public AttendTeam(Team team, User user, Participation participation){
        this.user = user;
        this.team = team;
        this.participation = participation;
    }



    public void allowAttend(){
        this.participation = attend;
    }

    public void rejectAttend(){
        this.participation = reject;
    }
}
