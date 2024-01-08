package com.wemakeplay.wemakeplay.domain.like.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.ALREADY_PRESS_LIKE;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_USER;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_LIKE_YOURSELF;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_PRESS_LIKE;
import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_UNLIKE_YOURSELF;

import com.wemakeplay.wemakeplay.domain.like.dto.LikeResponseDto;
import com.wemakeplay.wemakeplay.domain.like.entity.Like;
import com.wemakeplay.wemakeplay.domain.like.repository.LikeRepository;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponseDto pressLike(Long userId, User user) {

        User checkUser = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        if (checkUser.getId().equals(user.getId())) {
            throw new ServiceException(NOT_LIKE_YOURSELF);
        }

        if (likeRepository.existsByUserAndLikeUser(user, checkUser)) {
            throw new ServiceException(ALREADY_PRESS_LIKE);
        }

        Like like = Like.builder()
            .user(user)
            .likeUser(checkUser)
            .build();

        likeRepository.save(like);

        Long likeCount = calculateLikeCount(checkUser.getId());

        return new LikeResponseDto(like, likeCount);
    }

    public LikeResponseDto unPressLike(Long userId, User user) {

        User checkUser = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        if (checkUser.getId().equals(user.getId())) {
            throw new ServiceException(NOT_UNLIKE_YOURSELF);
        }

        Optional<Like> findLikeUser =
            likeRepository.findByUserAndLikeUser(user, checkUser);

        if (findLikeUser.isEmpty()) {
            throw new ServiceException(NOT_PRESS_LIKE);
        }

        Like like = findLikeUser.get();

        likeRepository.delete(like);

        Long likeCount = calculateLikeCount(checkUser.getId());

        return new LikeResponseDto(like, likeCount);
    }

    private Long calculateLikeCount(Long userId) {

        return likeRepository.countByLikeUserId(userId);
    }
}
