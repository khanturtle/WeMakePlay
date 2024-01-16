package com.wemakeplay.wemakeplay.domain.team.service;

import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeamRepository;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamResponseDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamViewResponseDto;
import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import com.wemakeplay.wemakeplay.domain.team.repository.TeamRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final AttendTeamRepository attendTeamRepository;
    private final UserRepository userRepository;

    //팀 생성
    @Transactional
    public TeamResponseDto createTeam(TeamRequestDto teamRequestDto, User user){
        Team team = new Team(teamRequestDto, user);
        teamRepository.save(team);
        return new TeamResponseDto(team);
    }

    // 모든 팀 조회
    public List<TeamViewResponseDto> getAllTeams() {
        List<Team> teamList = teamRepository.findAll();
        List<TeamViewResponseDto> teamViewResponseDtos = new ArrayList<>();
        for (Team team : teamList){
            teamViewResponseDtos.add(new TeamViewResponseDto(team));
        }
        return teamViewResponseDtos;
    }

    //해당 팀 조회 (teamId)에 해당하는 팀 조회
    public TeamResponseDto getTeam(Long teamId){
        Team team = findTeam(teamId);
        return new TeamResponseDto(team);
    }

    //수정
    @Transactional
    public TeamResponseDto updateTeam(Long teamId, TeamRequestDto teamRequestDto, User user){
        Team team = findTeam(teamId);
        if(user.getNickname().equals(team.getTeamOwner().getNickname())){
            team.updateTeam(teamRequestDto);
            return new TeamResponseDto(team);
        }else{
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }
// 삭제
    @Transactional
    public void deleteTeam(Long teamId, User user){
        Team team = findTeam(teamId);
        if(user.getNickname().equals(team.getTeamOwner().getNickname())){
            teamRepository.delete(team);
        }else{
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

// 모든 팀 조회
    public List<TeamResponseDto> getTeams() {
        List<Team> teamList = teamRepository.findAll();
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
        for (Team team : teamList){
            teamResponseDtoList.add(new TeamResponseDto(team));
        }
        return teamResponseDtoList;
    }


    //사용자가 팀에 신청
    //작성자일 경우 예외처리

    @Transactional
    public void attendTeam(Long teamId, User user){
        Team team = findTeam(teamId);
        if (team.getTeamAttendPersonnel() >= team.getTeamPersonnel()){
            throw new ServiceException(ErrorCode.TEAM_FULL_PERSONNEL);
        }
        if (user.getNickname().equals(team.getTeamOwner().getNickname())){
            throw new ServiceException(ErrorCode.TEAM_OWNER);
        }
        List<AttendTeam> attendTeamList = user.getAttendTeams();
        for (AttendTeam myAttendTeam : attendTeamList){
            if (myAttendTeam.getTeam().getId() == teamId){
                if (myAttendTeam.getParticipation() == com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait){
                    throw new ServiceException(ErrorCode.ALREADY_ATTENDING_TEAM);
                } else if (myAttendTeam.getParticipation() == com.wemakeplay.wemakeplay.domain.attendteam.Participation.attend) {
                    throw new ServiceException(ErrorCode.ALREADY_ATTENDING_TEAM);
                }
            }
        }
        AttendTeam attendTeam = AttendTeam.builder()
            .user(user)
            .team(team)
            .participation(com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait)
            .build();
        attendTeamRepository.save(attendTeam);
    }
    //요청 목록
    public List<AttendTeam> checkTeamAttender(Long teamId, User user){
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())){
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            List<AttendTeam> attendTeamWaitingList = new ArrayList<>();
            for (AttendTeam attendTeam : attendTeamList) {
                if (attendTeam.getParticipation().equals(
                    com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait)){
                    attendTeamWaitingList.add(attendTeam);
                }
            }
            return attendTeamWaitingList;
        }else {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

    // 요청 수락
    @Transactional
    public void allowTeamAttend(Long teamId, Long userId, User user) {
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            if (team.getTeamAttendPersonnel() >= team.getTeamPersonnel()) {
                throw new ServiceException(ErrorCode.TEAM_FULL_PERSONNEL);
            }
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            for (AttendTeam attendTeam : attendTeamList) {
                if (attendTeam.getUser().getId() == userId) {
                    attendTeam.allowAttend();
                }
            }
            team.attendUser();
        } else {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

    // 요청 거절
    @Transactional
    public void rejectTeamAttend(Long teamId, Long userId, User user){
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())){
            List<AttendTeam>attendTeamList = attendTeamRepository.findByTeamId(teamId);
            for (AttendTeam attendTeam:attendTeamList){
                if (attendTeam.getUser().getId() == userId){
                    attendTeam.rejectAttend();
                }
            }
        }else {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

    //탈퇴
    @Transactional
    public void quitTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
        for (AttendTeam attendTeam : attendTeamList){
            if (attendTeam.getUser().getId() == user.getId()){
                if (attendTeam.getParticipation().equals(Participation.attend)){
                    attendTeamRepository.delete(attendTeam);
                }
            }
        }
    }

    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(
            () -> new ServiceException(ErrorCode.NOT_EXIST_TEAM)
        );
    }
    //강퇴
    @Transactional
    public void kickUserFromTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_TEAM));

        User userToKick = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_USER));

        List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if (!team.getTeamOwner().getUsername().equals(loggedInUsername)) {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }

        for (AttendTeam attendTeam : attendTeamList) {
            if (attendTeam.getParticipation().equals(Participation.attend)) {
                attendTeamRepository.delete(attendTeam);

                team.keepUser();
            }
        }
    }
}

