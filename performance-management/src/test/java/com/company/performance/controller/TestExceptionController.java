package com.company.performance.controller;

import com.company.performance.common.Result;
import com.company.performance.common.exception.BusinessException;
import com.company.performance.dto.request.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestExceptionController {

    @GetMapping("/business")
    public Result<Void> business() {
        throw new BusinessException("业务错误");
    }

    @PostMapping("/validation")
    public Result<Void> validation(@Valid @RequestBody LoginRequest request) {
        return Result.success(null);
    }

    @GetMapping("/system")
    public Result<Void> system() {
        throw new NullPointerException("boom");
    }
}
