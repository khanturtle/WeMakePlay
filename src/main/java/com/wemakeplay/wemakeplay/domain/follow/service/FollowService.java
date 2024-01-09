package com.wemakeplay.wemakeplay.domain.follow.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.ALREADY_FOLLOWING_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_FOLLOWING_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_FOLLOWING_YOURSELF;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_UNFOLLOWING_YOURSELF;

import com.wemakeplay.wemakeplay.domain.follow.dto.FollowerResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowingResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import com.wemakeplay.wemakeplay.domain.follow.repository.FollowRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 내가 다른 사람을 팔로잉 하는 경우
    public FollowingResponseDto followUser(Long userId, User user) {

        User follower = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        if (follower.getId().equals(user.getId())) {
            throw new ServiceException(NOT_FOLLOWING_YOURSELF);
        }

        if (followRepository.existsByFollowingAndFollower(user, follower)) {
            throw new ServiceException(ALREADY_FOLLOWING_USER);
        }

        Follow follow = Follow.builder()
            .following(user)
            .follower(follower)
            .build();

        followRepository.save(follow);

        return new FollowingResponseDto(follow);
    }

    // 내가 팔로잉 했던 사람을 취소 하는 경우
    public FollowingResponseDto unFollowUser(Long userId, User user) {

        User follower = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        if (follower.getId().equals(user.getId())) {
            throw new ServiceException(NOT_UNFOLLOWING_YOURSELF);
        }

        Optional<Follow> findFollowUser =
            followRepository.findByFollowingAndFollower(user, follower);

        if (findFollowUser.isEmpty()) {
            throw new ServiceException(NOT_FOLLOWING_USER);
        }

        Follow follow = findFollowUser.get();

        followRepository.delete(follow);

        return new FollowingResponseDto(follow);
    }

    // 내가 팔로우를 한 사람들 = 팔로잉
    public List<FollowingResponseDto> getFollowing(User user) {

        return followRepository.findByFollowingId(user.getId())
            .stream()
            .map(FollowingResponseDto::new)
            .collect(Collectors.toList());
    }

    // 나를 팔로우 한 사람들 = 팔로워
    public List<FollowerResponseDto> getFollower(User user) {

        return followRepository.findByFollowerId(user.getId())
            .stream()
            .map(FollowerResponseDto::new)
            .collect(Collectors.toList());
    }
}
