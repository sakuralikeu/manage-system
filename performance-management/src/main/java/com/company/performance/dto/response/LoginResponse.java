package com.company.performance.dto.response;

import com.company.performance.common.enums.PositionType;
import com.company.performance.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String refreshToken;
    private Long userId;
    private String username;
    private String realName;
    private Long roleId;
    private String department;
    private PositionType positionType;
    private Boolean status;

    public static LoginResponse from(String token, String refreshToken, User user) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRoleId(user.getRoleId());
        response.setDepartment(user.getDepartment());
        response.setPositionType(user.getPositionType());
        response.setStatus(user.getStatus());
        return response;
    }
}
