package com.company.performance.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectNodeVO {
    private Long id;
    private String nodeName;
    private Integer nodeOrder;
    private BigDecimal weight;
}
