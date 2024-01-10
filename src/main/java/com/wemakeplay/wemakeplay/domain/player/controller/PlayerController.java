package com.wemakeplay.wemakeplay.domain.player.controller;

import com.wemakeplay.wemakeplay.domain.player.dto.TopPlayerResponseDto;
import com.wemakeplay.wemakeplay.domain.player.service.PlayerService;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<?> getTopPlayers(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        List<TopPlayerResponseDto> responseDto
            = playerService.getTopPlayers(userDetails.getUser());

        return ResponseEntity.ok(RootResponseDto.builder()
            .code("200")
            .message("탑 플레이어 조회 성공")
            .data(responseDto)
            .build()
        );
    }

}
