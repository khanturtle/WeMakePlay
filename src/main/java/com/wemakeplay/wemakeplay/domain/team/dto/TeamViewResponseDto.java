package com.wemakeplay.wemakeplay.domain.team.dto;

import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamViewResponseDto {
    private Long id;
    private String teamName;
    private String teamIntro;
    private int teamAttendPersonnel;
    private int teamPersonnel;

    public TeamViewResponseDto(Team team){
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.teamIntro = team.getTeamIntro();
        this.teamPersonnel = team.getTeamPersonnel();
        this.teamAttendPersonnel = team.getTeamAttendPersonnel();
    }
}
