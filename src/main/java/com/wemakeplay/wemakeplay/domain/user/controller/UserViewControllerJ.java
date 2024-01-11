package com.wemakeplay.wemakeplay.domain.user.controller;

import com.wemakeplay.wemakeplay.domain.user.dto.request.LoginRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.request.SignupRequestDto;
import com.wemakeplay.wemakeplay.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserViewControllerJ {
    private final UserService userService;
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
    public String showLoginForm(){
        return "login-j";
    }
    //성공 시 메인 페이지로
    @PostMapping("/login")
    public String login(){

        return "redirect:mainPage";
    }
    //메인 페이지
    @GetMapping("/mainPage")
    public String showMainPage(){
        return "mainPage";
    }

}
