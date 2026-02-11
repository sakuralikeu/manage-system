package com.company.performance.vo;

import com.company.performance.common.enums.AllocationMethod;
import com.company.performance.common.enums.ProjectStatus;
import com.company.performance.common.enums.ProjectType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectVO {
    private Long id;
    private String projectName;
    private String projectCode;
    private ProjectType projectType;
    private AllocationMethod allocationMethod;
    private ProjectStatus status;
    private UserSummaryVO manager;
    private LocalDateTime createdAt;
}
