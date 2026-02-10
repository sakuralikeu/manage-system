package com.company.performance.dto.response;

import lombok.Data;

@Data
public class AuthMeResponse {
    private Long userId;
    private String username;

    public static AuthMeResponse of(Long userId, String username) {
        AuthMeResponse response = new AuthMeResponse();
        response.setUserId(userId);
        response.setUsername(username);
        return response;
    }
}
