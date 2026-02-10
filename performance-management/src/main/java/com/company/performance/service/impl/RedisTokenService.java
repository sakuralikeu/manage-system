package com.company.performance.service.impl;

import com.company.performance.service.TokenService;
import com.company.performance.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(RedisTokenService.class);
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    private static final String REFRESH_PREFIX = "jwt:refresh:";

    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;

    public RedisTokenService(StringRedisTemplate redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean isBlacklisted(String token) {
        try {
            Boolean exists = redisTemplate.hasKey(BLACKLIST_PREFIX + token);
            return Boolean.TRUE.equals(exists);
        } catch (Exception ex) {
            logger.warn("Redis unavailable, skip blacklist check", ex);
            return false;
        }
    }

    @Override
    public void blacklist(String token) {
        try {
            long ttl = jwtUtil.getExpirationMillis(token) - System.currentTimeMillis();
            if (ttl <= 0) {
                return;
            }
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", ttl, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            logger.warn("Redis unavailable, skip blacklist write", ex);
        }
    }

    @Override
    public void storeRefreshToken(Long userId, String refreshToken) {
        try {
            long ttl = jwtUtil.getExpirationMillis(refreshToken) - System.currentTimeMillis();
            if (ttl <= 0) {
                return;
            }
            redisTemplate.opsForValue().set(REFRESH_PREFIX + refreshToken, String.valueOf(userId), ttl, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            logger.warn("Redis unavailable, skip refresh token store", ex);
        }
    }

    @Override
    public boolean isRefreshTokenValid(Long userId, String refreshToken) {
        try {
            String stored = redisTemplate.opsForValue().get(REFRESH_PREFIX + refreshToken);
            return stored != null && stored.equals(String.valueOf(userId));
        } catch (Exception ex) {
            logger.warn("Redis unavailable, skip refresh token validate", ex);
            return true;
        }
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        try {
            redisTemplate.delete(REFRESH_PREFIX + refreshToken);
        } catch (Exception ex) {
            logger.warn("Redis unavailable, skip refresh token revoke", ex);
        }
    }
}
