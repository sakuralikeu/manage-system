package com.company.performance.service.impl;

import com.company.performance.common.exception.BusinessException;
import com.company.performance.dto.response.LoginResponse;
import com.company.performance.entity.User;
import com.company.performance.mapper.UserMapper;
import com.company.performance.service.UserService;
import com.company.performance.service.TokenService;
import com.company.performance.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, TokenService tokenService) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public LoginResponse login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());
        tokenService.storeRefreshToken(user.getId(), refreshToken);
        return LoginResponse.from(token, refreshToken, user);
    }
}
