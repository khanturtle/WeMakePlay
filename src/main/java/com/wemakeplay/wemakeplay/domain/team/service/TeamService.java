package com.wemakeplay.wemakeplay.domain.team.service;

import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeam;
import com.wemakeplay.wemakeplay.domain.attendteam.AttendTeamRepository;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamRequestDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamResponseDto;
import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import com.wemakeplay.wemakeplay.domain.team.repository.TeamRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final AttendTeamRepository attendTeamRepository;
    //팀 생성
    @Transactional
    public TeamResponseDto creatTeam(TeamRequestDto teamRequestDto, User user){
        Team team = new Team(teamRequestDto, user);
        teamRepository.save(team);
        return new TeamResponseDto(team);
    }

    // 모든 팀의 정보 조회
//    public List<TeamResponseDto> getTeams() {
//        List<Team> teamList = teamRepository.findAll();
//        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
//        for (Team team : teamList) {
//            teamResponseDtoList.add(new TeamResponseDto(team));
//        }
//        return teamResponseDtoList;
//    }
// 특정팀 조회
//    public TeamResponseDto getTeam(Long teamId){
//        Team team = findTeam(teamId);
//        return new TeamResponseDto(team);
//    }

//수정
    @Transactional
    public TeamResponseDto updateTeam(Long teamId, TeamRequestDto teamRequestDto, User user){
        Team team = findTeam(teamId);
        if(user.equals(team.getTeamOwner())){
            team.updateTeam(teamRequestDto);
            return new TeamResponseDto(team);
        }else{
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }
// 삭제
    @Transactional
    public void deletTeam(Long teamId, User user){
        Team team = findTeam(teamId);
        if(user.equals(team.getTeamOwner())){
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


// 팀 가입 요청 목록
    @Transactional
    public List<AttendTeam> checkTeamAttender(Long teamId, User user) {
        Team team = findTeam(teamId);
        if(user.getId().equals(team.getTeamOwner().getId())){
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            List<AttendTeam> attendBoardWaitingList = new ArrayList<>();
            for(AttendTeam attendTeam:attendTeamList){
                if(attendTeam.getParticipation().equals(Participation.wait)){
                    attendBoardWaitingList.add(attendTeam);
                }
            }
            return attendBoardWaitingList;
        }else{
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }
    //해당 팀 조회 (teamId)에 해당하는 팀 조회
    public Team findTeam(Long teamId){
        return teamRepository.findById(teamId).orElseThrow(
            ()-> new ServiceException(ErrorCode.NOT_EXIST_TEAM)
        );
    }


    public TeamResponseDto getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_EXIST_TEAM));

        return new TeamResponseDto(team);
    }

    //사용자가 팀에 신청
    //자성자일 경우 예외처리
    public void attendTeam(Long teamId, User user){
        Team team = findTeam(teamId);
        user.attendTeam(team);
    }

    // 요청 수락
    @Transactional
    public void allowTeamAttend(Long teamId, Long userId, User user) {
        Team team = findTeam(teamId);
        if (user.equals(team.getTeamOwner())){
            List<AttendTeam>attendTeamList = attendTeamRepository.findByTeamId(teamId);
            for (AttendTeam attendTeam:attendTeamList){
                if (attendTeam.getUser().getId()==userId){
                    attendTeam.allowAttend();
                }
            }
        }else {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

    // 요청 거절
    @Transactional
    public void rejectTeamAttend(Long teamId, Long userId, User user){
        Team team = findTeam(teamId);
        if (user.equals(team.getTeamOwner())){
            List<AttendTeam>attendTeamList = attendTeamRepository.findByTeamId(teamId);
            for (AttendTeam attendTeam:attendTeamList){
                if (attendTeam.getUser().getId()==userId){
                    attendTeam.rejectAttend();
                }
            }
        }else {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

    //털퇴
    @Transactional
    public void quitTeam(Long teamId, User user) {
        Team team = findTeamByUser(teamId);
        List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamIdAndParticipation(teamId, Participation.attend);
        attendTeamList.forEach(attendTeam -> attendTeamRepository.delete(attendTeam));

    }

    public Team findTeamByUser(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(
            () -> new ServiceException(ErrorCode.NOT_EXIST_TEAM)
        );
    }
}

