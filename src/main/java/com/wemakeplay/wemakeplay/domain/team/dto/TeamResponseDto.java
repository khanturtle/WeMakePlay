package com.wemakeplay.wemakeplay.domain.team.dto;

import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamResponseDto {
    private String teamName;
    private String teamIntro;
    private String teamOwner;

    public TeamResponseDto(Team team){
        this.teamName = getTeamName();
        this.teamIntro = getTeamIntro();
        this.teamOwner = getTeamOwner();
    }
}
