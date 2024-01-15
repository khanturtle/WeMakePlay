package com.wemakeplay.wemakeplay.domain.team.dto;

import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.attendteam.Participation;
import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamResponseDto {
    private Long Id;
    private String teamName;
    private String teamIntro;
    private String teamOwner;
    private int teamPersonnel;
    private int teamAttendPersonnel;
    private List<String> memberNameList;

    public TeamResponseDto(Team team){
        this.Id = team.getId();
        this.teamName = team.getTeamName();
        this.teamIntro = team.getTeamIntro();
        this.teamPersonnel = team.getTeamPersonnel();
        this.teamAttendPersonnel = team.getTeamAttendPersonnel();
        this.teamOwner = team.getTeamOwner().getNickname();

        List<String> memberNameList = new ArrayList<>();
        List<AttendTeam> attendTeamList = team.getAttendTeams();
        for(AttendTeam attendTeam:attendTeamList){
            if (attendTeam.getParticipation().equals(Participation.attend)){
                memberNameList.add(attendTeam.getUser().getNickname());
            }
        }
        this.memberNameList = memberNameList;
    }
}
