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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
        team.updateTeam(teamRequestDto);
        return new TeamResponseDto(team);
    }

    @Transactional
    public void deleteTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (!user.getNickname().equals(team.getTeamOwner().getNickname())) {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
        attendTeamRepository.deleteAll(attendTeamRepository.findByTeamId(teamId));
        teamRepository.delete(team);
    }

    @Transactional
    public void attendTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (team.getTeamAttendPersonnel() >= team.getTeamPersonnel()) {
            throw new ServiceException(ErrorCode.TEAM_FULL_PERSONNEL);
        }
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            throw new ServiceException(ErrorCode.TEAM_OWNER);
        }
        if (user.getAttendTeams().stream()
            .anyMatch(t -> t.getTeam().getId().equals(teamId) &&
                (t.getParticipation() == com.wemakeplay.wemakeplay.domain.attendteam.Participation.wait ||
                    t.getParticipation() == com.wemakeplay.wemakeplay.domain.attendteam.Participation.attend))) {
            throw new ServiceException(ErrorCode.ALREADY_ATTENDING_TEAM);
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
        if (!user.getNickname().equals(team.getTeamOwner().getNickname())) {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
        return attendTeamRepository.findByTeamId(teamId)
            .stream()
            .filter(attendTeam -> attendTeam.getParticipation().equals(Participation.wait))
            .collect(Collectors.toList());
    }

    @Transactional
    public void allowTeamAttend(Long teamId, Long userId, User user) {
        Team team = findTeam(teamId);
        if (user.getNickname().equals(team.getTeamOwner().getNickname())) {
            if (team.getTeamAttendPersonnel() >= team.getTeamPersonnel()) {
                throw new ServiceException(ErrorCode.TEAM_FULL_PERSONNEL);
            }
            List<AttendTeam> attendTeamList = attendTeamRepository.findByTeamId(teamId);
            attendTeamList.stream()
                .filter(attendTeam -> attendTeam.getUser().getId().equals(userId))
                .forEach(AttendTeam::allowAttend);
            team.attendUser();
        } else {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
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
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }

    @Transactional
    public void quitTeam(Long teamId, User user) {
        Team team = findTeam(teamId);
        attendTeamRepository.findByTeamId(teamId)
            .stream()
            .filter(attendTeam -> attendTeam.getUser().getId().equals(user.getId())
                && attendTeam.getParticipation().equals(Participation.attend))
            .forEach(attendTeamRepository::delete);
    }

    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_EXIST_TEAM));
    }

    @Transactional
    public void kickUserFromTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_TEAM));

        User userToKick = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_USER));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if (!team.getTeamOwner().getUsername().equals(loggedInUsername)) {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }

        attendTeamRepository.findByTeamId(teamId)
            .stream()
            .filter(attendTeam -> attendTeam.getParticipation().equals(Participation.attend)
                && attendTeam.getUser().getId().equals(userId))
            .findFirst()
            .ifPresent(attendTeam -> {
                attendTeamRepository.delete(attendTeam);
                team.keepUser();
            });

        if (userToKick.equals(team.getTeamOwner())) {
            throw new ServiceException(ErrorCode.CANNOT_KICK_TEAM_OWNER);
        } else {
            throw new ServiceException(ErrorCode.NOT_TEAM_MEMBER);
        }
    }

    public void checkTeamOwner(Long teamId, User user) {
        Team team = findTeam(teamId);
        if (!team.getTeamOwner().getNickname().equals(user.getNickname())) {
            throw new ServiceException(ErrorCode.NOT_TEAM_OWNER);
        }
    }
}
