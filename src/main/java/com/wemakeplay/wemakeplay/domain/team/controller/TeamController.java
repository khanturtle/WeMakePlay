package com.wemakeplay.wemakeplay.domain.team.controller;

import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamResponseDto;
import com.wemakeplay.wemakeplay.domain.team.service.TeamService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping("")
    public ResponseEntity<?> createTeam(
        @RequestBody TeamRequestDto teamRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TeamResponseDto teamResponseDto = teamService.creatTeam(teamRequestDto, userDetails);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("201")
            .message("팀 생성 성공")
            .data(teamResponseDto)
            .build());
    }
//팀 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getAllTeams(){
        List<TeamResponseDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 조회 성공")
            .data(teams)
            .build());
    }
//특정 팀 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeam(@PathVariable Long teamId){
        TeamResponseDto teamResponseDto = teamService.getTeam(teamId);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 조회 성공")
            .data(teamResponseDto)
            .build());
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(
        @PathVariable Long teamId,
        @RequestBody TeamRequestDto teamRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        TeamResponseDto teamResponseDto = teamService.updateTeam(teamId, teamRequestDto, userDetails);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 수정이 완료되었습니다.")
            .data(teamResponseDto)
            .build());
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.deletTeam(teamId, userDetails);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 삭제가 완료되었습니다.")
            .build());
    }

    @PostMapping("/{teamId}")
    public ResponseEntity<?> attendTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.allowTeam(teamId, userDetails);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 가입 신청을 왼료했습니다.")
            .build());
    }

    @PatchMapping("/attend/{teamId}")
    public ResponseEntity<?> allowTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<AttendTeam> allowedTeams = teamService.allowTeam(teamId, userDetails);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 가입 요청을 수락(거절) 하였습니다.")
            .data(allowedTeams)
            .build());
    }

}
