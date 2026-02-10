package com.company.performance.controller;

import com.company.performance.common.Result;
import com.company.performance.common.exception.BusinessException;
import com.company.performance.dto.request.LoginRequest;
import com.company.performance.dto.request.LogoutRequest;
import com.company.performance.dto.request.RefreshTokenRequest;
import com.company.performance.dto.response.AuthMeResponse;
import com.company.performance.dto.response.AuthRefreshResponse;
import com.company.performance.dto.response.LoginResponse;
import com.company.performance.service.TokenService;
import com.company.performance.service.UserService;
import com.company.performance.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public AuthController(UserService userService, JwtUtil jwtUtil, TokenService tokenService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        return Result.success(response);
    }

    @PostMapping("/refresh")
    public Result<AuthRefreshResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtUtil.validateToken(refreshToken) || !"refresh".equals(jwtUtil.getTokenType(refreshToken))) {
            throw new BusinessException("refresh token 无效");
        }
        Long userId = jwtUtil.getUserId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        if (userId == null || !tokenService.isRefreshTokenValid(userId, refreshToken)) {
            throw new BusinessException("refresh token 无效");
        }
        tokenService.revokeRefreshToken(refreshToken);
        String accessToken = jwtUtil.generateAccessToken(userId, username);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username);
        tokenService.storeRefreshToken(userId, newRefreshToken);
        return Result.success(AuthRefreshResponse.of(accessToken, newRefreshToken));
    }

    @PostMapping("/logout")
    public Result<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody(required = false) LogoutRequest request) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            tokenService.blacklist(authorization.substring(7));
        }
        if (request != null && request.getRefreshToken() != null && !request.getRefreshToken().isBlank()) {
            tokenService.revokeRefreshToken(request.getRefreshToken());
        }
        return Result.success(null);
    }

    @GetMapping("/me")
    public Result<AuthMeResponse> me(Authentication authentication) {
        Object principal = authentication != null ? authentication.getPrincipal() : null;
        Object details = authentication != null ? authentication.getDetails() : null;
        Long userId = (details instanceof Long) ? (Long) details : null;
        String username = principal != null ? principal.toString() : null;
        return Result.success(AuthMeResponse.of(userId, username));
    }
}
