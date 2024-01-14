package com.wemakeplay.wemakeplay.global.filter;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.TOKEN_ERROR;

import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemakeplay.wemakeplay.global.security.UserDetailsServiceImpl;
import com.wemakeplay.wemakeplay.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 웹으로 받은 토큰 값 처리를 위한 로직
        String tokenValueFromHeader = jwtUtil.getJwtFromHeader(request); // 헤더에 담긴 토큰
        String tokenValueFromCookie = jwtUtil.getJwtFromCookie(request); // 쿠키에 담긴 토큰

        // 두 경로의 값을 모두 가져와 조건문 대입
        if(StringUtils.hasText(tokenValueFromHeader)) {
            validateToken(tokenValueFromHeader, response);
        } else if(StringUtils.hasText(tokenValueFromCookie)){
            validateToken(tokenValueFromCookie, response);
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(String tokenValue, HttpServletResponse response) throws IOException {
        if (!jwtUtil.validateToken(tokenValue)) {
            ObjectMapper ob = new ObjectMapper();
            response.setStatus(400);

            String json = ob.writeValueAsString(new ServiceException(TOKEN_ERROR));
            PrintWriter writer = response.getWriter();

            writer.println(json);
            return;
        }

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }

}
