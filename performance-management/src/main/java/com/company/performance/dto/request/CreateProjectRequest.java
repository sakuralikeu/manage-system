package com.company.performance.dto.request;

import com.company.performance.common.enums.AllocationMethod;
import com.company.performance.common.enums.ProjectType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProjectRequest {
    @NotBlank(message = "项目名称不能为空")
    @Size(min = 2, max = 200, message = "项目名称长度需在2到200之间")
    private String projectName;
    @NotBlank(message = "项目编号不能为空")
    @Pattern(regexp = "^PROJ-\\d{4}-\\d{3}$", message = "项目编号格式必须为PROJ-YYYY-XXX")
    private String projectCode;
    @NotNull(message = "项目类型不能为空")
    private ProjectType projectType;
    @NotNull(message = "分配方式不能为空")
    private AllocationMethod allocationMethod;
    @DecimalMin(value = "0", inclusive = true, message = "售前比例必须在0到1之间")
    @DecimalMax(value = "1", inclusive = true, message = "售前比例必须在0到1之间")
    private BigDecimal presaleRatio;
    @DecimalMin(value = "0", inclusive = true, message = "研发比例必须在0到1之间")
    @DecimalMax(value = "1", inclusive = true, message = "研发比例必须在0到1之间")
    private BigDecimal rdRatio;
    @Valid
    private List<NodeWeightRequest> nodeWeights;
}
