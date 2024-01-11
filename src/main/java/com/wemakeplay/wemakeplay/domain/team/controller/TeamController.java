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
        TeamResponseDto teamResponseDto = teamService.creatTeam(teamRequestDto, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("201")
            .message("팀 생성 성공")
            .data(teamResponseDto)
            .build());
    }
//팀 조회
    @GetMapping("")
    public ResponseEntity<?> getTeams(){
        List<TeamResponseDto> teams = teamService.getTeams();
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
        TeamResponseDto teamResponseDto = teamService.updateTeam(teamId, teamRequestDto, userDetails.getUser());
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
        teamService.deletTeam(teamId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 삭제가 완료되었습니다.")
            .build());
    }

    @PostMapping("/attend/{teamId}")
    public ResponseEntity<?> attendTeam(
        @PathVariable Long userId,
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.attendTeam(teamId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message(userId + "팀 가입 신청을 완료했습니다.")
            .build());
    }

    //팀에 참가한 멤버들의 목록 조회
    @GetMapping("/attend/{teamId}")
    public List<AttendTeam> checkTeamAttender(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return teamService.checkTeamAttender(teamId, userDetails.getUser());
    }


    @PatchMapping("/allowAttend/{teamId}/{userId}")
    public ResponseEntity<?> allowTeamAttend(
        @PathVariable Long teamId,
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.allowTeamAttend(teamId, userId,userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 가입 신청을 수락하였습니다.")
            .build());
    }

    @PatchMapping("/rejectAttend/{teamId}/{userId}")
    public ResponseEntity<?> rejectTeamAttend(
        @PathVariable Long teamId,
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.rejectTeamAttend(teamId, userId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀 가입 신청을 거절하였습니다.")
            .build());
    }

    @PostMapping("/{teamId}/kick/{userId}")
    public ResponseEntity<?> kickUserFromTeam(
        @PathVariable Long teamId,
        @PathVariable Long userId) {
        teamService.kickUserFromTeam(teamId, userId);
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message(userId + "번 유저가 강퇴되었습니다.")
            .build());
    }

    @DeleteMapping("/quit/{teamId}")
    public ResponseEntity<?> quitTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.quitTeam(teamId, userDetails.getUser());
        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("팀을 탈퇴 하였습니다.")
            .build());

    }
}
