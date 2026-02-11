package com.company.performance.controller;

import com.company.performance.common.PageResult;
import com.company.performance.common.Result;
import com.company.performance.common.exception.ForbiddenException;
import com.company.performance.dto.request.CreateProjectRequest;
import com.company.performance.dto.request.PageRequest;
import com.company.performance.service.ProjectService;
import com.company.performance.vo.ProjectDetailVO;
import com.company.performance.vo.ProjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "项目管理")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "创建项目")
    @PostMapping
    public Result<Long> createProject(@Valid @RequestBody CreateProjectRequest request,
                                      Authentication authentication) {
        Long userId = extractUserId(authentication);
        Long projectId = projectService.createProject(request, userId);
        return Result.success(projectId);
    }

    @Operation(summary = "分页查询项目列表")
    @GetMapping("/page")
    public Result<PageResult<ProjectVO>> getProjectPage(@Valid PageRequest request) {
        return Result.success(projectService.getProjectPage(request));
    }

    @Operation(summary = "查询项目详情")
    @GetMapping("/{id}")
    public Result<ProjectDetailVO> getProjectDetail(@PathVariable("id") Long id) {
        return Result.success(projectService.getProjectDetail(id));
    }

    private Long extractUserId(Authentication authentication) {
        Object details = authentication != null ? authentication.getDetails() : null;
        if (details instanceof Long userId) {
            return userId;
        }
        throw new ForbiddenException("未登录");
    }
}
