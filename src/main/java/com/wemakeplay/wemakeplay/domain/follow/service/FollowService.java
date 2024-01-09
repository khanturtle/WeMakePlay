package com.wemakeplay.wemakeplay.domain.follow.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.ALREADY_FOLLOWING_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_FOLLOWING_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_FOLLOWING_YOURSELF;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_UNFOLLOWING_YOURSELF;

import com.wemakeplay.wemakeplay.domain.follow.dto.FollowResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import com.wemakeplay.wemakeplay.domain.follow.repository.FollowRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 내가 다른 사람을 팔로잉 하는 경우
    public FollowResponseDto followUser(Long userId, User user) {

        User followingUser = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        if (followingUser.getId().equals(user.getId())) {
            throw new ServiceException(NOT_FOLLOWING_YOURSELF);
        }

        if (followRepository.existsByUserAndFollowingUser(user, followingUser)) {
            throw new ServiceException(ALREADY_FOLLOWING_USER);
        }

        Follow follow = Follow.builder()
            .user(user)
            .followingUser(followingUser)
            .build();

        followRepository.save(follow);

        return new FollowResponseDto(follow);
    }

    // 내가 팔로잉 했던 사람을 취소 하는 경우
    public FollowResponseDto unFollowUser(Long userId, User user) {

        User followingUser = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        if (followingUser.getId().equals(user.getId())) {
            throw new ServiceException(NOT_UNFOLLOWING_YOURSELF);
        }

        Optional<Follow> findFollowUser =
            followRepository.findByUserAndFollowingUser(user, followingUser);

        if (findFollowUser.isEmpty()) {
            throw new ServiceException(NOT_FOLLOWING_USER);
        }

        Follow follow = findFollowUser.get();

        followRepository.delete(follow);

        return new FollowResponseDto(follow);
    }
}
