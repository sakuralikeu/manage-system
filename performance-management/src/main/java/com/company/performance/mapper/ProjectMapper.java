package com.company.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.performance.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    @Select("SELECT * FROM projects WHERE status = #{status}")
    List<Project> findByStatus(String status);

    @Select("SELECT * FROM projects WHERE manager_id = #{managerId}")
    List<Project> findByManagerId(Long managerId);
}
