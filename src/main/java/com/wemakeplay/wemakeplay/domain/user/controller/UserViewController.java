package com.wemakeplay.wemakeplay.domain.user.controller;

import com.wemakeplay.wemakeplay.domain.user.dto.request.SignupRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.SignupResponseDto;
import com.wemakeplay.wemakeplay.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }
    @PostMapping("/signup")
    public String signUp(@ModelAttribute("signupRequestDto") SignupRequestDto requestDto) {
        userService.signUp(requestDto);
        return "redirect:main";
    }

    @GetMapping("/main")
    public String mainPage(){
        return "signup_success";
    }
}
