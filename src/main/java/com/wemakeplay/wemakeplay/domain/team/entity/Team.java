package com.wemakeplay.wemakeplay.domain.team.entity;

import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@Entity
@Table(name = "teams")
@NoArgsConstructor
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String teamName;
    @Column(nullable = false, unique = true)
    private String teamIntro;

    // 생성지
    @ManyToOne
    private User teamOwner;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AttendTeam> attendTeams = new ArrayList<>();

    public Team(TeamRequestDto teamRequestDto, User user){
        this.teamName = teamRequestDto.getTeamName();
        this.teamIntro = teamRequestDto.getTeamIntro();
        this.teamOwner = user;
    }

    public void updateTeam(TeamRequestDto teamRequestDto){
        this.teamName = teamRequestDto.getTeamName();
        this.teamIntro = teamRequestDto.getTeamIntro();
    }
}
