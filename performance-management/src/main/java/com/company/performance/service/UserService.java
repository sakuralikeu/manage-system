package com.company.performance.service;

import com.company.performance.dto.response.LoginResponse;

public interface UserService {
    LoginResponse login(String username, String password);
}
