package com.company.performance.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NodeWeightRequest {
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;
    @NotNull(message = "节点权重不能为空")
    @DecimalMin(value = "0", inclusive = true, message = "节点权重必须在0到1之间")
    @DecimalMax(value = "1", inclusive = true, message = "节点权重必须在0到1之间")
    private BigDecimal weight;
}
