package com.company.performance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@TableName(value = "roles", autoResultMap = true)
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String roleName;
    private String description;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
