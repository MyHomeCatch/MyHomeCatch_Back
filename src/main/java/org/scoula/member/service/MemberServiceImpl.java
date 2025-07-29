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

        // 1. 현재 비밀번호 검증 (필수)
        // 프론트엔드에서 모든 업데이트 요청에 currentPassword를 보내도록 했으므로, 여기서 검증합니다.
        if (userInfoDto.getCurrentPassword() == null || userInfoDto.getCurrentPassword().isEmpty()) {
            throw new IllegalArgumentException("회원 정보를 수정하려면 현재 비밀번호를 입력해야 합니다.");
        }
        if (!passwordEncoder.matches(userInfoDto.getCurrentPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 다른 정보 업데이트
        if (userInfoDto.getNickname() != null) {
            existingUser.setNickname(userInfoDto.getNickname());
        }
        if (userInfoDto.getAddress() != null) {
            existingUser.setAddress(userInfoDto.getAddress());
        }

        // 3. 새 비밀번호 업데이트 (newPassword로 변경)
        if (userInfoDto.getNewPassword() != null && !userInfoDto.getNewPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userInfoDto.getNewPassword()));
        }

        authMapper.update(existingUser); // AuthMapper에 update 메서드가 있다고 가정
    }
}
