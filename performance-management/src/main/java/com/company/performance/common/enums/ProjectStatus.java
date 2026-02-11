package com.company.performance.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum ProjectStatus implements IEnum<String> {
    IN_PROGRESS("IN_PROGRESS"),
    FINISHED("FINISHED"),
    SETTLED("SETTLED");

    private final String value;

    ProjectStatus(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
