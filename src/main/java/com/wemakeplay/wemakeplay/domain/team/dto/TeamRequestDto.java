package com.wemakeplay.wemakeplay.domain.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamRequestDto {
    private String teamName;
    private String teamIntro;
    private int teamPersonnel;
}
