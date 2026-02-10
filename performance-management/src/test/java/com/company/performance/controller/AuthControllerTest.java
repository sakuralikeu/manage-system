package com.company.performance.controller;

import com.company.performance.common.enums.PositionType;
import com.company.performance.dto.request.LoginRequest;
import com.company.performance.dto.request.RefreshTokenRequest;
import com.company.performance.dto.request.LogoutRequest;
import com.company.performance.dto.response.LoginResponse;
import com.company.performance.service.UserService;
import com.company.performance.service.TokenService;
import com.company.performance.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private TokenService tokenService;

    @Test
    void loginSuccess() throws Exception {
        LoginResponse response = new LoginResponse();
        response.setToken("token-123");
        response.setUserId(1L);
        response.setUsername("alice");
        response.setRealName("Alice");
        response.setRoleId(2L);
        response.setDepartment("研发部");
        response.setPositionType(PositionType.RD);
        response.setStatus(true);
        when(userService.login("alice", "pass123")).thenReturn(response);

        LoginRequest request = new LoginRequest();
        request.setUsername("alice");
        request.setPassword("pass123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.data.token").value("token-123"));
    }

    @Test
    void loginValidationError() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshSuccess() throws Exception {
        when(jwtUtil.validateToken("refresh-1")).thenReturn(true);
        when(jwtUtil.getTokenType("refresh-1")).thenReturn("refresh");
        when(jwtUtil.getUserId("refresh-1")).thenReturn(1L);
        when(jwtUtil.getUsername("refresh-1")).thenReturn("alice");
        when(tokenService.isRefreshTokenValid(1L, "refresh-1")).thenReturn(true);
        when(jwtUtil.generateAccessToken(1L, "alice")).thenReturn("access-2");
        when(jwtUtil.generateRefreshToken(1L, "alice")).thenReturn("refresh-2");

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-1");

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("access-2"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-2"));
    }

    @Test
    void refreshInvalid() throws Exception {
        when(jwtUtil.validateToken("bad")).thenReturn(false);

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("bad");

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void logoutWithTokens() throws Exception {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("refresh-1");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer access-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(tokenService).blacklist("access-1");
        verify(tokenService).revokeRefreshToken("refresh-1");
    }
}
