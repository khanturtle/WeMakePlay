package com.wemakeplay.wemakeplay.domain.admin.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_USER;

import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import com.wemakeplay.wemakeplay.domain.board.repository.BoardRepository;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowerResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowingResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.repository.FollowRepository;
import com.wemakeplay.wemakeplay.domain.like.repository.LikeRepository;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamViewResponseDto;
import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import com.wemakeplay.wemakeplay.domain.team.repository.TeamRepository;
import com.wemakeplay.wemakeplay.domain.user.dto.response.ProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.UserProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final TeamRepository teamRepository;

    public ProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        return ProfileResponseDto.builder()
            .user(user)
            .build();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        userRepository.delete(user);
    }

    public List<UserProfileResponseDto> getUsers() {

        List<User> userList = userRepository.findAll();
        List<UserProfileResponseDto> userProfileList = new ArrayList<>();

        for (User user : userList) {

            Long followers = followRepository.countByFollowerId(user.getId());
            Long followings = followRepository.countByFollowingId(user.getId());
            Long likes = likeRepository.countByLikeUserId(user.getId());

            List<FollowingResponseDto> followingList
                = followRepository.findByFollowingId(user.getId())
                .stream()
                .map(FollowingResponseDto::new)
                .toList();

            List<FollowerResponseDto> followerList
                = followRepository.findByFollowerId(user.getId())
                .stream()
                .map(FollowerResponseDto::new)
                .toList();

            List<BoardViewResponseDto> boardList
                = boardRepository.findByBoardOwnerId(user.getId())
                .stream()
                .map(BoardViewResponseDto::new)
                .toList();
            List<TeamViewResponseDto> teamList
                = teamRepository.findByTeamOwnerId(user.getId())
                .stream()
                .map(TeamViewResponseDto::new)
                .toList();

            userProfileList.add(UserProfileResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .area(user.getArea())
                .age(user.getAge())
                .likes(likes)
                .followers(followers)
                .followersList(followerList)
                .followings(followings)
                .followingList(followingList)
                .boardList(boardList)
                .teamList(teamList)
                .build());
        }

        return userProfileList;
    }

    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        boardRepository.delete(board);
    }
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));
        teamRepository.delete(team);
    }
}
