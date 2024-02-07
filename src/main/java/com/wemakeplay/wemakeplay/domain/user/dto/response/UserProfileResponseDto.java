package com.wemakeplay.wemakeplay.domain.user.dto.response;

import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowerResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowingResponseDto;
import com.wemakeplay.wemakeplay.domain.team.dto.TeamViewResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String intro;
    private String area;
    private String age;
    private Long likes;
    private Long followers;
    private Long followings;
    private List<FollowerResponseDto> followersList;
    private List<FollowingResponseDto> followingList;
    private List<BoardViewResponseDto> boardList;
    private List<TeamViewResponseDto> teamList;

}
