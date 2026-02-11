package com.company.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.performance.entity.ProjectParticipation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectParticipationMapper extends BaseMapper<ProjectParticipation> {
    @Select("SELECT * FROM project_participation WHERE project_id = #{projectId} AND user_id = #{userId}")
    ProjectParticipation findByProjectIdAndUserId(Long projectId, Long userId);

    @Select("SELECT * FROM project_participation WHERE node_id = #{nodeId}")
    List<ProjectParticipation> findByNodeId(Long nodeId);
}
