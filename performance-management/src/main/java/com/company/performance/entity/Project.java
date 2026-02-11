package com.company.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.company.performance.common.enums.AllocationMethod;
import com.company.performance.common.enums.ProjectStatus;
import com.company.performance.common.enums.ProjectType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("projects")
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String projectName;
    private String projectCode;
    private ProjectType projectType;
    private AllocationMethod allocationMethod;
    private BigDecimal presaleRatio;
    private BigDecimal rdRatio;
    private Long managerId;
    private ProjectStatus status;
    private BigDecimal totalVirtualHours;
    private Boolean supervisorApproved;
    private Long supervisorId;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}
