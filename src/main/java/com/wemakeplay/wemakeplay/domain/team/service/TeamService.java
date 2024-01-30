package com.wemakeplay.wemakeplay.domain.team.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.ALREADY_ATTENDING_TEAM;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_TEAM;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_TEAM_MEMBER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_TEAM_OWNER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.TEAM_FULL_PERSONNEL;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.TEAM_OWNER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.TEAM_OWNER_CANNOT_WITHDRAW;

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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final AttendTeamRepository attendTeamRepository;
    private final UserRepository userRepository;

    public TeamResponseDto createTeam(TeamRequestDto teamRequestDto, User user) {
        Team team = new Team(teamRequestDto, user);
        teamRepository.save(team);
        return new TeamResponseDto(team);
    }

    public List<TeamViewResponseDto> getAllTeams() {
        List<Team> teamList = teamRepository.findAll();
        return teamList.stream()
            .map(TeamViewResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<TeamViewResponseDto> get3Teams() {
        List<Team> teamList = teamRepository.findAll();
        List<TeamViewResponseDto> teamViewResponseDtos = teamList.stream()
            .sorted(Comparator.comparing(Team::getTeamName).reversed())
            .limit(3)
            .map(TeamViewResponseDto::new)
            .collect(Collectors.toList());
        return teamViewResponseDtos;
    }

    public Page<TeamViewResponseDto> getAllTeams(Pageable pageable) {
        Page<Team> teamPage = teamRepository.findAll(pageable);
        return teamPage.map(TeamViewResponseDto::new);
    }

    public TeamResponseDto getTeam(Long teamId) {
        Team team = findTeam(teamId);
        return new TeamResponseDto(team);
    }

    @Transactional
    public TeamResponseDto updateTeam(Long teamId, TeamRequestDto teamRequestDto, User user) {
        Team team = findTeam(teamId);
        if (!user.getNickname().equals(team.getTeamOwner().getNickname())) {
            throw new ServiceException(NOT_TEAM_OWNER);
        }
        team.updateTeam(teamRequestDto);
        return new TeamResponseDto(team);
    }

    @Transactional
    public void deleteTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (!user.getNickname().equals(team.getTeamOwner().getNickname())) {
            throw new ServiceException(NOT_TEAM_OWNER);
        }
        attendTeamRepository.deleteAll(attendTeamRepository.findByTeamId(teamId));
        teamRepository.delete(team);
    }

    @Transactional
    public void attendTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (team.getTeamAttendPersonnel() >= team.getTeamPersonnel()) {
            throw new ServiceException(TEAM_FULL_PERSONNEL);
        }
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            throw new ServiceException(TEAM_OWNER);
        }
        if (user.getAttendTeams().stream()
            .anyMatch(t -> t.getTeam().getId().equals(teamId) &&
                (t.getParticipation() == com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait ||
                    t.getParticipation() == com.wemakeplay.wemakeplay.domain.attendteam.Participation.attend))) {
            throw new ServiceException(ALREADY_ATTENDING_TEAM);
        }
        AttendTeam attendTeam = AttendTeam.builder()
            .user(user)
            .team(team)
            .participation(com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait)
            .build();
        attendTeamRepository.save(attendTeam);
    }


    public List<AttendTeam> checkTeamAttender(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            List<AttendTeam> attendTeamWaitingList = new ArrayList<>();
            for (AttendTeam attendTeam : attendTeamList) {
                if (attendTeam.getParticipation().equals(
                    com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait)) {
                    attendTeamWaitingList.add(attendTeam);
                }
            }
            return attendTeamWaitingList;
        } else {
            throw new ServiceException(NOT_TEAM_OWNER);
        }
    }

    @Transactional
    public void allowTeamAttend(Long teamId, Long userId, User user) {
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            if (team.getTeamAttendPersonnel() >= team.getTeamPersonnel()) {
                throw new ServiceException(TEAM_FULL_PERSONNEL);
            }
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            attendTeamList.stream()
                .filter(attendTeam -> attendTeam.getUser().getId().equals(userId))
                .forEach(AttendTeam::allowAttend);
            team.attendUser();
        } else {
            throw new ServiceException(NOT_TEAM_OWNER);
        }
    }

    @Transactional
    public void rejectTeamAttend(Long teamId, Long userId, User user) {
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            attendTeamList.stream()
                .filter(attendTeam -> attendTeam.getUser().getId().equals(userId))
                .forEach(AttendTeam::rejectAttend);
        } else {
            throw new ServiceException(NOT_TEAM_OWNER);
        }
    }

    @Transactional
    public void quitTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);

        if (user.getId().equals(team.getTeamOwner().getId())) {
            throw new ServiceException(TEAM_OWNER_CANNOT_WITHDRAW);
        } else {
            boolean isAttender = false;
            for (AttendTeam attendTeam : attendTeamList) {
                if (attendTeam.getUser().getId().equals(user.getId()) && attendTeam.getParticipation().equals(
                    com.wemakeplay.wemakeplay.domain.attendteam.Participation.attend)) {
                    attendTeamRepository.delete(attendTeam);
                    team.quitTeam();
                    isAttender = true;
                    break;
                }
            }
            if (!isAttender) {
                throw new ServiceException(NOT_TEAM_MEMBER);
            }
        }
    }

    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_TEAM));
    }

    @Transactional
    public void kickUserFromTeam(Long teamId, Long userId, User user) {
        Team team = findTeam(teamId);
        User userToKick = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_USER));
        List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);

        if (team.getTeamOwner().getId().equals(user.getId())) {
            for (AttendTeam attendTeam : attendTeamList) {
                if (attendTeam.getUser().getId().equals(userToKick.getId())){
                    attendTeamRepository.delete(attendTeam);
                    team.kickUser();
                    return;
                }
            }
        }else{
            throw new ServiceException(NOT_TEAM_OWNER);
        }
    }

    public void checkTeamOwner(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (!team.getTeamOwner().getNickname().equals(user.getNickname())) {
            throw new ServiceException(NOT_TEAM_OWNER);
        }
    }
}
