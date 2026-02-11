package com.company.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.performance.common.PageResult;
import com.company.performance.common.enums.AllocationMethod;
import com.company.performance.common.enums.ProjectStatus;
import com.company.performance.common.exception.BusinessException;
import com.company.performance.common.exception.ForbiddenException;
import com.company.performance.common.exception.ResourceNotFoundException;
import com.company.performance.dto.request.CreateProjectRequest;
import com.company.performance.dto.request.NodeWeightRequest;
import com.company.performance.dto.request.PageRequest;
import com.company.performance.entity.Project;
import com.company.performance.entity.ProjectNode;
import com.company.performance.entity.Role;
import com.company.performance.entity.SystemConfig;
import com.company.performance.entity.User;
import com.company.performance.mapper.ProjectMapper;
import com.company.performance.mapper.ProjectNodeMapper;
import com.company.performance.mapper.RoleMapper;
import com.company.performance.mapper.SystemConfigMapper;
import com.company.performance.mapper.UserMapper;
import com.company.performance.service.ProjectService;
import com.company.performance.vo.ProjectDetailVO;
import com.company.performance.vo.ProjectNodeVO;
import com.company.performance.vo.ProjectVO;
import com.company.performance.vo.UserSummaryVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final String DEFAULT_NODE_CONFIG_KEY = "default_node_weights";
    private static final BigDecimal WEIGHT_TOLERANCE = new BigDecimal("0.0001");
    private static final List<String> DEFAULT_NODE_ORDER = List.of(
            "商机",
            "项目建议书",
            "可研",
            "招投标",
            "前向签约",
            "系统研发",
            "后向采购",
            "后向签约",
            "项目试运行",
            "初验",
            "终验"
    );
    private static final Set<String> DEFAULT_NODE_NAMES = Set.copyOf(DEFAULT_NODE_ORDER);

    private final ProjectMapper projectMapper;
    private final ProjectNodeMapper projectNodeMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final SystemConfigMapper systemConfigMapper;
    private final ObjectMapper objectMapper;

    public ProjectServiceImpl(ProjectMapper projectMapper,
                              ProjectNodeMapper projectNodeMapper,
                              UserMapper userMapper,
                              RoleMapper roleMapper,
                              SystemConfigMapper systemConfigMapper,
                              ObjectMapper objectMapper) {
        this.projectMapper = projectMapper;
        this.projectNodeMapper = projectNodeMapper;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.systemConfigMapper = systemConfigMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Long createProject(CreateProjectRequest request, Long currentUserId) {
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        Role role = roleMapper.selectById(user.getRoleId());
        if (role == null) {
            throw new ResourceNotFoundException("角色不存在");
        }
        if (!"DIRECTOR".equals(role.getRoleName()) && !"PROJECT_LEAD".equals(role.getRoleName())) {
            throw new ForbiddenException("无权限创建项目");
        }
        if (projectMapper.selectCount(new QueryWrapper<Project>().eq("project_code", request.getProjectCode())) > 0) {
            throw new BusinessException("项目编号已存在");
        }
        AllocationMethod allocationMethod = request.getAllocationMethod();
        BigDecimal presaleRatio = request.getPresaleRatio();
        BigDecimal rdRatio = request.getRdRatio();
        if (allocationMethod == AllocationMethod.BY_POSITION) {
            if (presaleRatio == null || rdRatio == null) {
                throw new BusinessException("按岗位切分必须提供售前和研发比例");
            }
            validateSum(presaleRatio.add(rdRatio), "售前比例与研发比例之和必须为1");
        } else {
            presaleRatio = null;
            rdRatio = null;
        }

        Project project = new Project();
        project.setProjectName(request.getProjectName());
        project.setProjectCode(request.getProjectCode());
        project.setProjectType(request.getProjectType());
        project.setAllocationMethod(allocationMethod);
        project.setPresaleRatio(presaleRatio);
        project.setRdRatio(rdRatio);
        project.setManagerId(currentUserId);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setSupervisorApproved(Boolean.FALSE);
        projectMapper.insert(project);

        List<NodeWeightConfig> nodeConfigs = resolveNodeWeights(request.getNodeWeights());
        int order = 1;
        for (NodeWeightConfig config : nodeConfigs) {
            ProjectNode node = new ProjectNode();
            node.setProjectId(project.getId());
            node.setNodeName(config.getName());
            node.setNodeOrder(order++);
            node.setWeight(config.getWeight());
            projectNodeMapper.insert(node);
        }
        return project.getId();
    }

    @Override
    public PageResult<ProjectVO> getProjectPage(PageRequest request) {
        Page<Project> page = new Page<>(request.getPage(), request.getSize());
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        if (request.getStatus() != null) {
            wrapper.eq("status", request.getStatus().getValue());
        }
        if (request.getManagerId() != null) {
            wrapper.eq("manager_id", request.getManagerId());
        }
        wrapper.orderByDesc("created_at");
        Page<Project> result = projectMapper.selectPage(page, wrapper);
        List<Project> projects = result.getRecords();
        Map<Long, User> managers = loadUsers(projects.stream()
                .map(Project::getManagerId)
                .distinct()
                .toList());
        List<ProjectVO> records = projects.stream()
                .map(project -> toProjectVO(project, managers.get(project.getManagerId())))
                .toList();
        return PageResult.of(result.getTotal(), records);
    }

    @Override
    public ProjectDetailVO getProjectDetail(Long projectId) {
        Project project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("项目不存在");
        }
        User manager = userMapper.selectById(project.getManagerId());
        if (manager == null) {
            throw new ResourceNotFoundException("项目负责人不存在");
        }
        List<ProjectNodeVO> nodes = projectNodeMapper.findByProjectId(projectId).stream()
                .sorted(Comparator.comparing(ProjectNode::getNodeOrder))
                .map(this::toProjectNodeVO)
                .toList();
        ProjectDetailVO detail = new ProjectDetailVO();
        detail.setId(project.getId());
        detail.setProjectName(project.getProjectName());
        detail.setProjectCode(project.getProjectCode());
        detail.setProjectType(project.getProjectType());
        detail.setAllocationMethod(project.getAllocationMethod());
        detail.setPresaleRatio(project.getPresaleRatio());
        detail.setRdRatio(project.getRdRatio());
        detail.setStatus(project.getStatus());
        detail.setTotalVirtualHours(project.getTotalVirtualHours());
        detail.setSupervisorApproved(project.getSupervisorApproved());
        detail.setSupervisorId(project.getSupervisorId());
        detail.setCreatedAt(project.getCreatedAt());
        detail.setFinishedAt(project.getFinishedAt());
        detail.setManager(toUserSummary(manager));
        detail.setNodes(nodes);
        return detail;
    }

    private Map<Long, User> loadUsers(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }
        List<User> users = userMapper.selectBatchIds(userIds);
        return users.stream().collect(Collectors.toMap(User::getId, user -> user));
    }

    private ProjectVO toProjectVO(Project project, User manager) {
        ProjectVO vo = new ProjectVO();
        vo.setId(project.getId());
        vo.setProjectName(project.getProjectName());
        vo.setProjectCode(project.getProjectCode());
        vo.setProjectType(project.getProjectType());
        vo.setAllocationMethod(project.getAllocationMethod());
        vo.setStatus(project.getStatus());
        if (manager != null) {
            vo.setManager(toUserSummary(manager));
        }
        vo.setCreatedAt(project.getCreatedAt());
        return vo;
    }

    private ProjectNodeVO toProjectNodeVO(ProjectNode node) {
        ProjectNodeVO vo = new ProjectNodeVO();
        vo.setId(node.getId());
        vo.setNodeName(node.getNodeName());
        vo.setNodeOrder(node.getNodeOrder());
        vo.setWeight(node.getWeight());
        return vo;
    }

    private UserSummaryVO toUserSummary(User user) {
        UserSummaryVO vo = new UserSummaryVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRoleId(user.getRoleId());
        vo.setDepartment(user.getDepartment());
        vo.setPositionType(user.getPositionType());
        vo.setStatus(user.getStatus());
        return vo;
    }

    private List<NodeWeightConfig> resolveNodeWeights(List<NodeWeightRequest> customWeights) {
        List<NodeWeightConfig> weights = new ArrayList<>();
        if (customWeights != null && !customWeights.isEmpty()) {
            for (NodeWeightRequest request : customWeights) {
                NodeWeightConfig config = new NodeWeightConfig();
                config.setName(request.getNodeName());
                config.setWeight(request.getWeight());
                weights.add(config);
            }
        } else {
            SystemConfig config = systemConfigMapper.findByKey(DEFAULT_NODE_CONFIG_KEY);
            if (config == null || config.getConfigValue() == null || config.getConfigValue().isBlank()) {
                throw new BusinessException("默认节点配置不存在");
            }
            try {
                weights = objectMapper.readValue(config.getConfigValue(), new TypeReference<List<NodeWeightConfig>>() {});
            } catch (Exception ex) {
                throw new BusinessException("默认节点配置解析失败");
            }
        }
        validateNodeWeights(weights);
        return weights.stream()
                .sorted(Comparator.comparingInt(node -> DEFAULT_NODE_ORDER.indexOf(node.getName())))
                .toList();
    }

    private void validateNodeWeights(List<NodeWeightConfig> weights) {
        if (weights.size() != 11) {
            throw new BusinessException("节点数量必须为11");
        }
        Set<String> names = weights.stream().map(NodeWeightConfig::getName).collect(Collectors.toSet());
        if (!names.equals(DEFAULT_NODE_NAMES)) {
            throw new BusinessException("节点名称必须包含默认的11个节点");
        }
        BigDecimal sum = weights.stream()
                .map(NodeWeightConfig::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        validateSum(sum, "节点权重之和必须为1");
    }

    private void validateSum(BigDecimal sum, String message) {
        if (sum == null) {
            throw new BusinessException(message);
        }
        BigDecimal diff = sum.subtract(BigDecimal.ONE).abs();
        if (diff.compareTo(WEIGHT_TOLERANCE) > 0) {
            throw new BusinessException(message);
        }
    }

    private static class NodeWeightConfig {
        private String name;
        private BigDecimal weight;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }
    }
}
