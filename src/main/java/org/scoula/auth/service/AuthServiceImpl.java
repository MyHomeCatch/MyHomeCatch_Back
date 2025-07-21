package org.scoula.auth.service;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.common.util.JwtUtil;
import org.scoula.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean emailExists(String email) {
        return authMapper.findByEmail(email) != null;
    }

    @Override
    public boolean nicknameExists(String nickname) {
        return authMapper.findByNickname(nickname) != null;
    }

    public AuthResponse login(LoginRequest request) {
        User user = authMapper.findByEmail(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getNickname());
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (authMapper.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPw = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPw);
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setAddress(request.getAddress());
        user.setAdditionalPoint(0);

        authMapper.insertUser(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getNickname());
    }

    @Override
    public void deleteByEmail(String email) {
        authMapper.deleteByEmail(email);
    }
}
