package com.wemakeplay.wemakeplay.domain.admin.service;

import static com.wemakeplay.wemakeplay.global.exception.ErrorCode.NOT_EXIST_USER;

import com.wemakeplay.wemakeplay.domain.user.dto.response.ProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public ProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        return ProfileResponseDto.builder()
            .user(user)
            .build();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(NOT_EXIST_USER));

        userRepository.delete(user);
    }
}
