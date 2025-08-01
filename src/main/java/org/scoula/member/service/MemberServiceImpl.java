package org.scoula.member.service;

import lombok.RequiredArgsConstructor;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.member.dto.UserInfoDto;
import org.scoula.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder; 


    @Override
    public UserInfoDto findUserInfoByEmail(String email) {
        User user = authMapper.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다.");
        }

        return UserInfoDto.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();
    }


    @Override
    @Transactional
    public void updateUserInfo(UserInfoDto userInfoDto) {
        User existingUser = authMapper.findByEmail(userInfoDto.getEmail());

        if (existingUser == null) {
            throw new IllegalArgumentException("업데이트할 사용자를 찾을 수 없습니다: " + userInfoDto.getEmail());
        }

        if (userInfoDto.getCurrentPassword() == null || userInfoDto.getCurrentPassword().isEmpty()) {
            throw new IllegalArgumentException("회원 정보를 수정하려면 현재 비밀번호를 입력해야 합니다.");
        }
        if (!passwordEncoder.matches(userInfoDto.getCurrentPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (userInfoDto.getNickname() != null) {
            existingUser.setNickname(userInfoDto.getNickname());
        }
        if (userInfoDto.getAddress() != null) {
            existingUser.setAddress(userInfoDto.getAddress());
        }


        if (userInfoDto.getNewPassword() != null && !userInfoDto.getNewPassword().isEmpty()) {
            if (passwordEncoder.matches(userInfoDto.getNewPassword(), existingUser.getPassword())) {
                throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
            }

            existingUser.setPassword(passwordEncoder.encode(userInfoDto.getNewPassword()));
        }


        authMapper.update(existingUser);
    }

}