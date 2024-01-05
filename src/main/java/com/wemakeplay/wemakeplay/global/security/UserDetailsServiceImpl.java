package com.wemakeplay.wemakeplay.global.security;

import com.wemakeplay.wemakeplay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.wemakeplay.wemakeplay.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + "은 존재하지 않습니다."));
        return new UserDetailsImpl(user);
    }
}
