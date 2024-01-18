package com.wemakeplay.wemakeplay.domain.team.controller;

import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamResponseDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamViewResponseDto;
import com.wemakeplay.wemakeplay.domain.team.service.TeamService;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@Slf4j
@RequiredArgsConstructor
public class TeamViewController {
    private final TeamService teamService;

    //팀 생성 페이지
    @GetMapping("/teamCreate")
    public String showCreateForm(Model model){
        model.addAttribute("teamRequestDto", new TeamRequestDto());
        return "teamForm";
    }
    //팀 생성 요청
    @PostMapping("/teamCreate")
    public String createTeam(
        @ModelAttribute TeamRequestDto teamRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.createTeam(teamRequestDto, userDetails.getUser());
        return "redirect:/teamAttend";
    }
    //팀 단건 조회
    @GetMapping("/team/{teamId}")
    public String getTeam(@PathVariable Long teamId, Model model){
        TeamResponseDto teamResponseDto = teamService.getTeam(teamId);
        model.addAttribute("teamResponseDto", teamResponseDto);
        return "team";
    }
    //팀 전체 조회
    @GetMapping("/teamAttend")
    public String getAllTeams(Model model){
        List<TeamViewResponseDto> teamList = teamService.getAllTeams();
        model.addAttribute("teamList", teamList);
        return "teamAttend";
    }
    //수정
    @GetMapping("/team/{teamId}/edit")
    public String editTeamForm(@PathVariable Long teamId, Model model) {
        TeamResponseDto teamResponseDto = teamService.getTeam(teamId);
        model.addAttribute("team", teamResponseDto);
        return "editTeamForm";
    }
    @PostMapping("/team/{teamId}/edit")
    public String editTeam(
        @ModelAttribute TeamRequestDto teamRequestDto,
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        teamService.updateTeam(teamId, teamRequestDto, userDetails.getUser());
        return "redirect:/team/{teamId}";
    }
    //삭제
    @GetMapping("/team/{teamId}/delete")
    public String deleteTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        teamService.deleteTeam(teamId, userDetails.getUser());
        return "redirect:/teamAttend";
    }
    //팀 참여
    @GetMapping("/teamAttend/{teamId}")
    public String attendTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        teamService.attendTeam(teamId, userDetails.getUser());
        return "redirect:/team/{teamId}";
    }
    //신청자 목록
    @GetMapping("/teamAttender/{teamId}")
    public String checkTeamAttender(
        @PathVariable Long teamId,
        Model model,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<AttendTeam> attenderList = teamService.checkTeamAttender(teamId, userDetails.getUser());
        model.addAttribute("attenderList", attenderList);
        return "teamAttender";
    }
    //수락
    @GetMapping("/allowTeam/{teamId}/{userId}")
    public String allowTeamAttend(
        @PathVariable Long teamId,
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.allowTeamAttend(teamId, userId, userDetails.getUser());
        return "redirect:/team/{teamId}";
    }
    //거절
    @GetMapping("/rejectTeam/{teamId}/{userId}")
    public String rejectTeamAttend(
        @PathVariable Long teamId,
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        teamService.rejectTeamAttend(teamId, userId, userDetails.getUser());
        return "redirect:/team/{teamId}";
    }
    // 참여자 확인
    @GetMapping("/allowed/attender/team/{teamId}")
    public String allowedAttenderTeam(
        @PathVariable Long teamId,
        Model model){
        TeamResponseDto teamResponseDto = teamService.getTeam(teamId);
        model.addAttribute("teamResponseDto", teamResponseDto);
        return "allowedAttenderTeam";
    }
}
