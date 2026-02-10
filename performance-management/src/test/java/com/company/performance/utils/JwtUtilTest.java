package com.company.performance.utils;

import com.company.performance.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private static final String SECRET = "changeit-please-set-changeit-please-set";
    private static final long EXPIRATION = 86400000;

    private JwtUtil buildUtil() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(SECRET);
        properties.setExpiration(EXPIRATION);
        properties.setRefreshExpiration(EXPIRATION);
        return new JwtUtil(properties);
    }

    @Test
    void generateAndValidate() {
        JwtUtil jwtUtil = buildUtil();
        String token = jwtUtil.generateToken(1L, "alice");
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(1L, jwtUtil.getUserId(token));
    }

    @Test
    void validateWithWrongSecret() {
        JwtUtil jwtUtil = buildUtil();
        String token = jwtUtil.generateToken(2L, "bob");
        JwtProperties properties = new JwtProperties();
        properties.setSecret("wrong-secret-please-change-wrong-secret-please-change");
        properties.setExpiration(86400000);
        properties.setRefreshExpiration(86400000);
        JwtUtil wrong = new JwtUtil(properties);
        assertFalse(wrong.validateToken(token));
    }

    @Test
    void expiredTokenIsInvalid() throws InterruptedException {
        JwtUtil jwtUtil = buildUtil();
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject("carol")
                .claim("userId", 3L)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(1000)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
        Thread.sleep(1200);
        assertFalse(jwtUtil.validateToken(token));
    }
}
