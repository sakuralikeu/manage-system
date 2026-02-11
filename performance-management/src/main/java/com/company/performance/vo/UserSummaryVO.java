package com.company.performance.vo;

import com.company.performance.common.enums.PositionType;
import lombok.Data;

@Data
public class UserSummaryVO {
    private Long id;
    private String username;
    private String realName;
    private Long roleId;
    private String department;
    private PositionType positionType;
    private Boolean status;
}
