package com.wemakeplay.wemakeplay.domain.player.controller;

import com.wemakeplay.wemakeplay.domain.player.dto.TopPlayerResponseDto;
import com.wemakeplay.wemakeplay.domain.player.service.PlayerService;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlayerViewController {

    private final PlayerService playerService;

    // 유저 로그인 후 메인페이지에서 TopPlayer 메뉴 클릭 시
    @GetMapping("/topPlayer")
    public String getTopPlayers(
        Model model,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        List<TopPlayerResponseDto> responseDto = playerService.getTopPlayers(userDetails.getUser());

        model.addAttribute("topPlayers", responseDto);

        return "topPlayer";
    }
}
