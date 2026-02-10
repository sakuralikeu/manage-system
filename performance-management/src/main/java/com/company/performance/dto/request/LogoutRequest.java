package com.company.performance.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
