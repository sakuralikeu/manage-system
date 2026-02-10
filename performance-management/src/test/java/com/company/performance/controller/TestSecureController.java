package com.company.performance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecureController {
    @GetMapping("/api/secure/ping")
    public String ping() {
        return "pong";
    }
}
