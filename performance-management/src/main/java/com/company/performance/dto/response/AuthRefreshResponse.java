package com.company.performance.dto.response;

import lombok.Data;

@Data
public class AuthRefreshResponse {
    private String accessToken;
    private String refreshToken;

    public static AuthRefreshResponse of(String accessToken, String refreshToken) {
        AuthRefreshResponse response = new AuthRefreshResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }
}
