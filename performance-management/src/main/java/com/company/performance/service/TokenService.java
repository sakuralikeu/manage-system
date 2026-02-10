package com.company.performance.service;

public interface TokenService {
    boolean isBlacklisted(String token);

    void blacklist(String token);

    void storeRefreshToken(Long userId, String refreshToken);

    boolean isRefreshTokenValid(Long userId, String refreshToken);

    void revokeRefreshToken(String refreshToken);
}
