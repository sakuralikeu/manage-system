package com.company.performance.service;

import com.company.performance.common.exception.BusinessException;
import com.company.performance.common.enums.PositionType;
import com.company.performance.config.JwtProperties;
import com.company.performance.dto.response.LoginResponse;
import com.company.performance.entity.User;
import com.company.performance.mapper.UserMapper;
import com.company.performance.service.impl.UserServiceImpl;
import com.company.performance.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserServiceImpl buildService(UserMapper mapper, JwtUtil jwtUtil, TokenService tokenService) {
        return new UserServiceImpl(mapper, jwtUtil, tokenService);
    }

    private JwtUtil buildJwtUtil() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("changeit-please-set-changeit-please-set");
        properties.setExpiration(86400000);
        properties.setRefreshExpiration(86400000);
        return new JwtUtil(properties);
    }

    @Test
    void loginSuccess() {
        UserMapper mapper = Mockito.mock(UserMapper.class);
        JwtUtil jwtUtil = buildJwtUtil();
        TokenService tokenService = Mockito.mock(TokenService.class);
        UserServiceImpl service = buildService(mapper, jwtUtil, tokenService);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setId(1L);
        user.setUsername("alice");
        user.setPassword(encoder.encode("pass123"));
        user.setRealName("Alice");
        user.setRoleId(2L);
        user.setDepartment("研发部");
        user.setPositionType(PositionType.RD);
        user.setStatus(true);
        when(mapper.findByUsername("alice")).thenReturn(user);

        LoginResponse response = service.login("alice", "pass123");
        assertNotNull(response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("alice", response.getUsername());
        assertTrue(jwtUtil.validateToken(response.getToken()));
    }

    @Test
    void loginUserNotFound() {
        UserMapper mapper = Mockito.mock(UserMapper.class);
        TokenService tokenService = Mockito.mock(TokenService.class);
        UserServiceImpl service = buildService(mapper, buildJwtUtil(), tokenService);
        when(mapper.findByUsername("missing")).thenReturn(null);

        assertThrows(BusinessException.class, () -> service.login("missing", "x"));
    }

    @Test
    void loginWrongPassword() {
        UserMapper mapper = Mockito.mock(UserMapper.class);
        TokenService tokenService = Mockito.mock(TokenService.class);
        UserServiceImpl service = buildService(mapper, buildJwtUtil(), tokenService);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setId(1L);
        user.setUsername("alice");
        user.setPassword(encoder.encode("pass123"));
        when(mapper.findByUsername("alice")).thenReturn(user);

        assertThrows(BusinessException.class, () -> service.login("alice", "bad"));
    }
}
