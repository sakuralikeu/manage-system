package com.company.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.performance.entity.ProjectNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectNodeMapper extends BaseMapper<ProjectNode> {
    @Select("SELECT * FROM project_nodes WHERE project_id = #{projectId} ORDER BY node_order ASC")
    List<ProjectNode> findByProjectId(Long projectId);
}
