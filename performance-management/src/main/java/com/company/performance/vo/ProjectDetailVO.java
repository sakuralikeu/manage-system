package com.company.performance.vo;

import com.company.performance.common.enums.AllocationMethod;
import com.company.performance.common.enums.ProjectStatus;
import com.company.performance.common.enums.ProjectType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDetailVO {
    private Long id;
    private String projectName;
    private String projectCode;
    private ProjectType projectType;
    private AllocationMethod allocationMethod;
    private BigDecimal presaleRatio;
    private BigDecimal rdRatio;
    private ProjectStatus status;
    private BigDecimal totalVirtualHours;
    private Boolean supervisorApproved;
    private Long supervisorId;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private UserSummaryVO manager;
    private List<ProjectNodeVO> nodes;
}
