package com.wemakeplay.wemakeplay.domain.player.service;

import com.wemakeplay.wemakeplay.domain.follow.repository.FollowRepository;
import com.wemakeplay.wemakeplay.domain.like.repository.LikeRepository;
import com.wemakeplay.wemakeplay.domain.player.dto.TopPlayerResponseDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    public List<TopPlayerResponseDto> getTopPlayers(User user) {

        List<TopPlayerResponseDto> topPlayerList = new ArrayList<>();

        List<User> userList = likeRepository.findTopPlayer(user.getId());

        for (User topUser : userList) {

            Long likes = likeRepository.countByLikeUserId(topUser.getId());
            Long followers = followRepository.countByFollowerId(topUser.getId());

            topPlayerList.add(TopPlayerResponseDto.builder()
                .id(topUser.getId())
                .username(topUser.getUsername())
                .nickname(topUser.getNickname())
                .age(topUser.getAge())
                .area(topUser.getArea())
                .likes(likes)
                .followers(followers)
                .build());
        }

        return topPlayerList;
    }
}
