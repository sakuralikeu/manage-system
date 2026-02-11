package com.company.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("project_nodes")
public class ProjectNode {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String nodeName;
    private Integer nodeOrder;
    private BigDecimal weight;
}
