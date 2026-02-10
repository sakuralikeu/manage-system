package com.company.performance.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum PositionType implements IEnum<String> {
    PRESALE("PRESALE"),
    RD("RD");

    private final String value;

    PositionType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
