package com.wemakeplay.wemakeplay.global.config;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_AUTHORIZATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import com.wemakeplay.wemakeplay.global.filter.JwtAuthenticationFilter;
import com.wemakeplay.wemakeplay.global.filter.JwtAuthorizationFilter;
import com.wemakeplay.wemakeplay.global.security.UserDetailsServiceImpl;
import com.wemakeplay.wemakeplay.global.util.JwtUtil;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf((csrf) -> csrf.disable());

        httpSecurity.sessionManagement((sessionManageMent) ->
            sessionManageMent.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers("/error").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                .requestMatchers("/api/users/**").permitAll()
                .anyRequest().permitAll()
        ).exceptionHandling(
            (exception) -> exception.authenticationEntryPoint(getAuthenticationEntryPoint())
        );

        httpSecurity.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    private static AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            ObjectMapper ob = new ObjectMapper();
            RootResponseDto responseDto = RootResponseDto.builder()
                .code(NOT_AUTHORIZATION.getCode())
                .message(NOT_AUTHORIZATION.getMessage())
                .build();

            String json = ob.writeValueAsString(responseDto);
            PrintWriter writer = response.getWriter();

            writer.println(json);
        };
    }
}
