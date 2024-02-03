package com.wemakeplay.wemakeplay.domain.team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@NoArgsConstructor
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column
    private String teamName;
    @Column
    private String teamIntro;
    @Column
    private int teamPersonnel;
    @Column
    private int teamAttendPersonnel = 1;
    @Column
    private String memberNames;

    // 생성자
    @ManyToOne(fetch = FetchType.LAZY)
    private User teamOwner;
    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AttendTeam> attendTeams = new ArrayList<>();


    public Team(TeamRequestDto teamRequestDto, User user){
        this.teamName = teamRequestDto.getTeamName();
        this.teamIntro = teamRequestDto.getTeamIntro();
        this.teamPersonnel = teamRequestDto.getTeamPersonnel();
        this.teamOwner = user;
        this.memberNames = user.getUsername();
    }

    public void updateTeam(TeamRequestDto teamRequestDto){
        this.teamName = teamRequestDto.getTeamName();
        this.teamIntro = teamRequestDto.getTeamIntro();
        this.teamPersonnel = teamRequestDto.getTeamPersonnel();
    }
    public void quitTeam() {
        this.teamAttendPersonnel --;
    }

    public void kickUser() {
        this.teamAttendPersonnel --;
    }

    public void attendUser(){
        this.teamAttendPersonnel ++;
    }
}
