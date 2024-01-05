package com.wemakeplay.wemakeplay.global.filter;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.LOGIN_FAILED;

import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.user.dto.request.LoginRequestDto;
import com.wemakeplay.wemakeplay.user.dto.response.LoginResponseDto;
import com.wemakeplay.wemakeplay.user.entity.UserRoleEnum;
import com.wemakeplay.wemakeplay.global.security.UserDetailsImpl;
import com.wemakeplay.wemakeplay.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(),
                LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        String message = "로그인에 성공했습니다.";
        response.setStatus(200);

        LoginResponseDto loginResponseDto = new LoginResponseDto(username);
        RootResponseDto<?> responseDto = RootResponseDto.builder()
            .code(String.valueOf(response.getStatus()))
            .message(message)
            .data(loginResponseDto)
            .build();

        String json = objectMapper.writeValueAsString(responseDto);
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.println("{" + token + "}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(400);

        RootResponseDto<?> responseDto = RootResponseDto.builder()
            .code(LOGIN_FAILED.getCode())
            .message(LOGIN_FAILED.getMessage())
            .build();

        String json = objectMapper.writeValueAsString(responseDto);
        PrintWriter writer = response.getWriter();

        writer.println(json);
    }
}
