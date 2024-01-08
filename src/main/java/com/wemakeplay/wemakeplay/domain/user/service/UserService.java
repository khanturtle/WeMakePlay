package com.wemakeplay.wemakeplay.domain.user.service;

import com.wemakeplay.wemakeplay.domain.user.dto.request.ModifyProfileRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.request.SignupRequestDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.ProfileResponseDto;
import com.wemakeplay.wemakeplay.domain.user.dto.response.SignupResponseDto;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import com.wemakeplay.wemakeplay.domain.user.entity.UserRoleEnum;
import com.wemakeplay.wemakeplay.domain.user.repository.UserRepository;
import com.wemakeplay.wemakeplay.global.exception.ErrorCode;
import com.wemakeplay.wemakeplay.global.exception.ServiceException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 관리자 권한을 부여하는 데 사용되는 토큰
    private final String ADMIN_TOKEN = "KSD8hdTyIl4wWA71SsAop0";

    // 회원가입
    public SignupResponseDto signUp(SignupRequestDto requestDto) {
        // requestDto 에서 필요한 정보 추출
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();
        String area = requestDto.getArea();
        String age = requestDto.getAge();
        String intro = "자기소개를 적어주세요";

        // intro 를 입력하지 않을 시 "자기소개를 적어주세요" 가 기본 값
        if (requestDto.getIntro() != null) {
            intro = requestDto.getIntro();
        }

        // 닉네임 중복 확인
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new ServiceException(ErrorCode.DUPLICATE_NICKNAME);
        }

        UserRoleEnum role = UserRoleEnum.USER;

        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new ServiceException(ErrorCode.WRONG_ADMIN_CODE);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = User.builder()
            .username(username)
            .password(password)
            .nickname(nickname)
            .email(email)
            .area(area)
            .age(age)
            .intro(intro)
            .role(role)
            .build();

        User saveUser = userRepository.save(user);

        return SignupResponseDto.builder()
            .username(saveUser.getUsername())
            .nickname(saveUser.getNickname())
            .role(saveUser.getRole())
            .build();
    }

    public ProfileResponseDto getMyProfile(User user) {
        User profileUser = findUser(user.getId());

        return ProfileResponseDto.builder()
            .user(profileUser)
            .build();
    }

    @Transactional
    public ProfileResponseDto modifyProfile(User user, ModifyProfileRequestDto requestDto) {
        User profileUser = findUser(user.getId());

        profileUser.update(requestDto);

        return ProfileResponseDto.builder()
            .user(profileUser)
            .build();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new ServiceException(ErrorCode.NOT_EXIST_USER)
        );
    }

    @Transactional
    public void withdrawUser(User user) {
        // 사용자 삭제
        userRepository.delete(user);
    }
}
