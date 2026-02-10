package com.company.performance.config;

import com.company.performance.controller.AuthController;
import com.company.performance.controller.HealthController;
import com.company.performance.controller.TestSecureController;
import com.company.performance.service.UserService;
import com.company.performance.service.TokenService;
import com.company.performance.utils.JwtUtil;
import com.company.performance.config.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class, HealthController.class, TestSecureController.class})
@Import({SecurityConfig.class, SecurityConfigTest.TestBeans.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @Test
    void healthIsPublic() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }

    @Test
    void loginIsPublic() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void apiRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/secure/ping"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void apiAllowsToken() throws Exception {
        String token = jwtUtil.generateToken(1L, "alice");
        mockMvc.perform(get("/api/secure/ping")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestBeans {
        @Bean
        JwtProperties jwtProperties() {
            JwtProperties properties = new JwtProperties();
            properties.setSecret("changeit-please-set-changeit-please-set");
            properties.setExpiration(86400000);
        properties.setRefreshExpiration(86400000);
            return properties;
        }

        @Bean
        JwtUtil jwtUtil(JwtProperties properties) {
            return new JwtUtil(properties);
        }
    }
}
