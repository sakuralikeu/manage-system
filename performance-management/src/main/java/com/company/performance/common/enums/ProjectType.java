package com.company.performance.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum ProjectType implements IEnum<String> {
    NORMAL("NORMAL"),
    HISTORY("HISTORY"),
    POLITICAL("POLITICAL"),
    KEY_SUPPORT("KEY_SUPPORT");

    private final String value;

    ProjectType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
