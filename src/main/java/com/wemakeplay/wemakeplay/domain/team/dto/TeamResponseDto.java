package com.wemakeplay.wemakeplay.domain.team.dto;

import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamResponseDto {
    private String teamName;
    private String teamIntro;
    private User teamOwner;
    private int teamPersonnel;

    public TeamResponseDto(Team team){
        this.teamName = team.getTeamName();
        this.teamIntro = team.getTeamIntro();
        this.teamPersonnel = team.getTeamPersonnel();
        this.teamOwner = team.getTeamOwner();
    }
}
