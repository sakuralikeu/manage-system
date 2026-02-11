package com.company.performance.service;

import com.company.performance.common.PageResult;
import com.company.performance.dto.request.CreateProjectRequest;
import com.company.performance.dto.request.PageRequest;
import com.company.performance.vo.ProjectDetailVO;
import com.company.performance.vo.ProjectVO;

public interface ProjectService {
    Long createProject(CreateProjectRequest request, Long currentUserId);

    PageResult<ProjectVO> getProjectPage(PageRequest request);

    ProjectDetailVO getProjectDetail(Long projectId);
}
