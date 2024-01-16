package com.wemakeplay.wemakeplay.domain.user.controller;

import com.wemakeplay.wemakeplay.domain.follow.dto.FollowerResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.dto.FollowingResponseDto;
import com.wemakeplay.wemakeplay.domain.follow.service.FollowService;
import com.wemakeplay.wemakeplay.domain.like.service.LikeService;
import com.wemakeplay.wemakeplay.domain.user.dto.request.ModifyProfileRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.request.SignupRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.UserProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.service.UserService;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserViewControllerJ {

    private final UserService userService;
    private final FollowService followService;
    private final LikeService likeService;

    //회원가입
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup-j";
    }

    //성공 시 로그인 페이지로
    @PostMapping("/signup")
    public String signUp(@ModelAttribute("signupRequestDto") SignupRequestDto requestDto) {
        userService.signUp(requestDto);
        return "redirect:login";
    }

    //로그인 페이지
    @GetMapping("/login")
    public String showLoginForm() {
        return "login-j";
    }

    //성공 시 메인 페이지로
    @PostMapping("/login")
    public String login() {

        return "redirect:mainPage";
    }

    //메인 페이지
    @GetMapping("/mainPage")
    public String showMainPage() {
        return "mainPage";
    }

    @GetMapping("/user/{userId}")
    public String showUserProfile(
        Model model,
        @PathVariable Long userId
    ) {
        UserProfileResponseDto responseDto =
            userService.getUserProfile(userId);
        model.addAttribute("userProfile", responseDto);
        return "userProfile";
    }

    @GetMapping("/myPage")
    public String showMyPage(
        Model model,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserProfileResponseDto responseDto =
            userService.getMyProfile(userDetails.getUser());
        model.addAttribute("myProfile", responseDto);
        return "myPage";
    }

    // 사용자 정보 수정 페이지
    @GetMapping("/user/edit")
    public String showEditMyPage(
        Model model,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserProfileResponseDto userProfileResponseDto =
            userService.getMyProfile(userDetails.getUser());
        model.addAttribute("myProfile", userProfileResponseDto);
        return "editMyPage";
    }

    @PostMapping("/user/edit")
    public String editMyProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @ModelAttribute ModifyProfileRequestDto modifyProfileRequestDto
    ){
        userService.modifyProfile(userDetails.getUser(), modifyProfileRequestDto);
        return "redirect:/myPage";
    }

    @DeleteMapping("/user/withdraw")
    public String withdrawUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.withdrawUser(userDetails.getUser());
        return "/";
    }

    @GetMapping("/user/followers")
    public String showFollowerList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Model model
    ) {
        List<FollowerResponseDto> followerResponseDtoList =
            followService.getFollower(userDetails.getUser());
        model.addAttribute("followers", followerResponseDtoList);
        return "followerList";
    }

    @GetMapping("/user/followings")
    public String showFollowingList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Model model
    ) {
        List<FollowingResponseDto> followingResponseDtoList =
            followService.getFollowing(userDetails.getUser());
        model.addAttribute("followings", followingResponseDtoList);
        return "followingList";
    }
}
