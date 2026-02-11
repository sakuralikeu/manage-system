package com.company.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("project_participation")
public class ProjectParticipation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Long nodeId;
    private Long userId;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
    private BigDecimal contributionRatio;
    private Boolean userConfirmed;
    private LocalDateTime confirmedAt;
    private LocalDateTime createdAt;
}
