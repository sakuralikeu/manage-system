package com.company.performance.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum AllocationMethod implements IEnum<String> {
    BY_POSITION("BY_POSITION"),
    SHARED("SHARED");

    private final String value;

    AllocationMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
