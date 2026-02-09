# 人员管理与绩效考核系统 - 技术栈方案

## 1. 技术栈总览

### 1.1 核心原则
- **简单优先**：选择成熟、文档完善、社区活跃的技术
- **健壮可靠**：久经验证、生产环境广泛使用
- **易于维护**：代码规范统一、架构清晰
- **开发高效**：工具链完善、自动化程度高

### 1.2 技术选型一览表

| 层次 | 技术/框架 | 版本 | 选择理由 |
|-----|----------|------|---------|
| **后端** | Java | 17 LTS | 长期支持版本，性能优秀 |
| | Spring Boot | 3.2.x | 最新稳定版，生态完善 |
| | MyBatis-Plus | 3.5.5 | 增强MyBatis，减少样板代码 |
| | MySQL | 8.0+ | 成熟可靠的关系型数据库 |
| | Redis | 7.2+ | 缓存和会话管理 |
| | Maven | 3.9+ | 依赖管理和构建工具 |
| **前端** | Vue | 3.4+ | 组合式API，性能优秀 |
| | Element Plus | 2.5+ | 企业级UI组件库 |
| | Vite | 5.0+ | 快速的开发服务器 |
| | Pinia | 2.1+ | Vue官方状态管理 |
| | Axios | 1.6+ | HTTP客户端 |
| | ECharts | 5.5+ | 数据可视化 |
| **开发工具** | IntelliJ IDEA | 2024+ | Java开发IDE |
| | VS Code | Latest | 前端开发编辑器 |
| | Git | 2.40+ | 版本控制 |
| | Postman | Latest | API测试工具 |
| **运维部署** | Docker | 24.0+ | 容器化部署 |
| | Nginx | 1.24+ | 反向代理和静态资源服务 |

---

## 2. 后端技术栈详解

### 2.1 Spring Boot 架构

#### 核心依赖 (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.2</version>
        <relativePath/>
    </parent>
    
    <groupId>com.company</groupId>
    <artifactId>performance-management</artifactId>
    <version>1.0.0</version>
    <name>performance-management</name>
    <description>人员管理与绩效考核系统</description>
    
    <properties>
        <java.version>17</java.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <jwt.version>0.12.3</jwt.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Spring Boot Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Spring Boot Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <!-- Spring Boot Redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
        <!-- MyBatis-Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        
        <!-- MySQL Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Hutool工具类 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.25</version>
        </dependency>
        
        <!-- Apache POI (Excel导出) -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.5</version>
        </dependency>
        
        <!-- SpringDoc OpenAPI (API文档) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
        
        <!-- 测试依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 项目结构

```
performance-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── company/
│   │   │           └── performance/
│   │   │               ├── PerformanceApplication.java          # 启动类
│   │   │               ├── common/                              # 公共模块
│   │   │               │   ├── constants/                       # 常量定义
│   │   │               │   │   ├── RedisConstants.java
│   │   │               │   │   ├── RoleConstants.java
│   │   │               │   │   └── StatusConstants.java
│   │   │               │   ├── enums/                          # 枚举类
│   │   │               │   │   ├── ProjectStatus.java
│   │   │               │   │   ├── AllocationMethod.java
│   │   │               │   │   └── TaskType.java
│   │   │               │   ├── exception/                      # 异常处理
│   │   │               │   │   ├── BusinessException.java
│   │   │               │   │   └── GlobalExceptionHandler.java
│   │   │               │   └── result/                         # 统一响应
│   │   │               │       ├── Result.java
│   │   │               │       └── PageResult.java
│   │   │               ├── config/                             # 配置类
│   │   │               │   ├── MybatisPlusConfig.java
│   │   │               │   ├── RedisConfig.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   └── WebMvcConfig.java
│   │   │               ├── controller/                         # 控制器层
│   │   │               │   ├── AuthController.java            # 认证接口
│   │   │               │   ├── UserController.java            # 用户管理
│   │   │               │   ├── ProjectController.java         # 项目管理
│   │   │               │   ├── KeyTaskController.java         # 重点事项
│   │   │               │   ├── PenaltyController.java         # 惩罚事项
│   │   │               │   └── SettlementController.java      # 绩效结算
│   │   │               ├── service/                            # 服务层
│   │   │               │   ├── IUserService.java
│   │   │               │   ├── IProjectService.java
│   │   │               │   ├── IKeyTaskService.java
│   │   │               │   ├── IPenaltyService.java
│   │   │               │   ├── ISettlementService.java
│   │   │               │   └── impl/                          # 实现类
│   │   │               │       ├── UserServiceImpl.java
│   │   │               │       ├── ProjectServiceImpl.java
│   │   │               │       ├── KeyTaskServiceImpl.java
│   │   │               │       ├── PenaltyServiceImpl.java
│   │   │               │       └── SettlementServiceImpl.java
│   │   │               ├── mapper/                             # 数据访问层
│   │   │               │   ├── UserMapper.java
│   │   │               │   ├── ProjectMapper.java
│   │   │               │   ├── KeyTaskMapper.java
│   │   │               │   ├── PenaltyMapper.java
│   │   │               │   └── SettlementMapper.java
│   │   │               ├── entity/                             # 实体类
│   │   │               │   ├── User.java
│   │   │               │   ├── Role.java
│   │   │               │   ├── Project.java
│   │   │               │   ├── ProjectNode.java
│   │   │               │   ├── ProjectParticipation.java
│   │   │               │   ├── KeyTask.java
│   │   │               │   ├── Penalty.java
│   │   │               │   └── MonthlySettlement.java
│   │   │               ├── dto/                                # 数据传输对象
│   │   │               │   ├── request/                       # 请求DTO
│   │   │               │   │   ├── LoginRequest.java
│   │   │               │   │   ├── CreateProjectRequest.java
│   │   │               │   │   └── WorkHourRequest.java
│   │   │               │   └── response/                      # 响应DTO
│   │   │               │       ├── LoginResponse.java
│   │   │               │       └── ContributionResponse.java
│   │   │               ├── vo/                                 # 视图对象
│   │   │               │   ├── ProjectVO.java
│   │   │               │   ├── SettlementVO.java
│   │   │               │   └── UserVO.java
│   │   │               └── utils/                              # 工具类
│   │   │                   ├── JwtUtil.java                   # JWT工具
│   │   │                   ├── RedisUtil.java                 # Redis工具
│   │   │                   └── CalculationUtil.java           # 计算工具
│   │   └── resources/
│   │       ├── application.yml                                # 主配置文件
│   │       ├── application-dev.yml                            # 开发环境
│   │       ├── application-prod.yml                           # 生产环境
│   │       ├── mapper/                                        # MyBatis XML
│   │       │   ├── UserMapper.xml
│   │       │   ├── ProjectMapper.xml
│   │       │   └── SettlementMapper.xml
│   │       └── db/
│   │           └── schema.sql                                 # 数据库脚本
│   └── test/
│       └── java/
│           └── com/
│               └── company/
│                   └── performance/
│                       ├── service/                           # 服务测试
│                       └── controller/                        # 控制器测试
├── pom.xml
└── README.md
```

### 2.2 核心配置示例

#### application.yml

```yaml
spring:
  application:
    name: performance-management
  
  profiles:
    active: dev
  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/performance_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: your_password
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
  
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms
  
  jackson:
    time-zone: Asia/Shanghai
    default-property-inclusion: non_null
    # 统一使用ISO 8601输出（Spring默认即ISO格式），无需显式date-format

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  mapper-locations: classpath*:/mapper/**/*.xml

# JWT配置
jwt:
  secret: your-256-bit-secret-key-here-make-it-long-enough
  expiration: 86400000  # Access Token 24小时
  refreshExpiration: 604800000  # Refresh Token 7天

# SpringDoc配置
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
  # 生产环境应限制swagger访问，仅DEV开放

# 日志配置
logging:
  level:
    com.company.performance: debug
    com.baomidou.mybatisplus: debug
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n"
```

---

## 3. 前端技术栈详解

### 3.1 Vue 3 + Vite 架构

#### package.json

```json
{
  "name": "performance-management-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext .vue,.js,.jsx,.cjs,.mjs --fix --ignore-path .gitignore"
  },
  "dependencies": {
    "vue": "^3.4.15",
    "vue-router": "^4.2.5",
    "pinia": "^2.1.7",
    "element-plus": "^2.5.6",
    "axios": "^1.6.7",
    "echarts": "^5.5.0",
    "dayjs": "^1.11.10",
    "nprogress": "^0.2.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.3",
    "vite": "^5.0.12",
    "eslint": "^8.56.0",
    "eslint-plugin-vue": "^9.20.1",
    "sass": "^1.70.0",
    "unplugin-auto-import": "^0.17.5",
    "unplugin-vue-components": "^0.26.0"
  }
}
```

#### 项目结构

```
performance-management-frontend/
├── public/
│   └── favicon.ico
├── src/
│   ├── main.js                          # 入口文件
│   ├── App.vue                          # 根组件
│   ├── router/                          # 路由配置
│   │   └── index.js
│   ├── store/                           # Pinia状态管理
│   │   ├── index.js
│   │   ├── modules/
│   │   │   ├── user.js                 # 用户状态
│   │   │   ├── project.js              # 项目状态
│   │   │   └── settlement.js           # 结算状态
│   ├── api/                             # API接口
│   │   ├── request.js                  # Axios封装
│   │   ├── auth.js                     # 认证接口
│   │   ├── user.js                     # 用户接口
│   │   ├── project.js                  # 项目接口
│   │   ├── progress.js                 # 项目进度接口
│   │   ├── keyTask.js                  # 重点事项接口
│   │   ├── penalty.js                  # 惩罚接口
│   │   └── settlement.js               # 结算接口
│   ├── views/                           # 页面组件
│   │   ├── Login.vue                   # 登录页
│   │   ├── Layout.vue                  # 布局页
│   │   ├── Dashboard.vue               # 仪表板
│   │   ├── project/                    # 项目管理
│   │   │   ├── ProjectList.vue
│   │   │   ├── ProjectCreate.vue
│   │   │   ├── ProjectDetail.vue
│   │   │   ├── ProjectProgress.vue
│   │   │   └── WorkHourRecord.vue
│   │   ├── keyTask/                    # 重点事项
│   │   │   ├── TaskList.vue
│   │   │   └── TaskCreate.vue
│   │   ├── penalty/                    # 惩罚事项
│   │   │   ├── PenaltyList.vue
│   │   │   └── PenaltyCreate.vue
│   │   ├── settlement/                 # 绩效结算
│   │   │   ├── MonthlySettlement.vue
│   │   │   ├── PersonalDetail.vue
│   │   │   └── Reports.vue
│   │   └── system/                     # 系统管理
│   │       ├── UserManagement.vue
│   │       └── SystemConfig.vue
│   ├── components/                      # 公共组件
│   │   ├── CommonTable.vue             # 通用表格
│   │   ├── CommonForm.vue              # 通用表单
│   │   ├── ContributionChart.vue       # 贡献度图表
│   │   └── SettlementChart.vue         # 结算图表
│   ├── utils/                           # 工具函数
│   │   ├── auth.js                     # 认证工具
│   │   ├── date.js                     # 日期工具
│   │   ├── calculation.js              # 计算工具
│   │   └── validate.js                 # 验证工具
│   ├── constants/                       # 常量定义
│   │   ├── index.js
│   │   └── enums.js
│   ├── styles/                          # 样式文件
│   │   ├── index.scss                  # 全局样式
│   │   ├── variables.scss              # 变量
│   │   └── element-plus.scss           # Element Plus样式覆盖
│   └── assets/                          # 静态资源
│       ├── logo.png
│       └── icons/
├── .env.development                     # 开发环境变量
├── .env.production                      # 生产环境变量
├── vite.config.js                       # Vite配置
├── package.json
└── README.md
```

### 3.2 核心配置示例

#### vite.config.js

```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
      imports: ['vue', 'vue-router', 'pinia']
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    })
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    rollupOptions: {
      output: {
        manualChunks: {
          'element-plus': ['element-plus'],
          'echarts': ['echarts']
        }
      }
    }
  }
})
```

#### .env.development

```
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=人员管理与绩效考核系统
```

---

## 4. 数据库设计

### 4.1 MySQL 配置

**推荐配置（my.cnf）：**

```ini
[mysqld]
# 基本设置
port = 3306
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
default-time-zone = '+8:00'

# InnoDB设置
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 2
innodb_flush_method = O_DIRECT

# 连接设置
max_connections = 200
max_connect_errors = 1000

# 查询缓存
query_cache_type = 0
query_cache_size = 0

# 慢查询日志
slow_query_log = 1
slow_query_log_file = /var/log/mysql/slow.log
long_query_time = 2
```

### 4.2 索引设计原则

```sql
-- 用户表索引
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_role_status ON users(role_id, status);

-- 项目表索引
CREATE INDEX idx_status ON projects(status);
CREATE INDEX idx_manager ON projects(manager_id);
CREATE INDEX idx_created_at ON projects(created_at);

-- 项目参与记录表索引
CREATE INDEX idx_project_user ON project_participation(project_id, user_id);
CREATE INDEX idx_node_user ON project_participation(node_id, user_id);

-- 月度结算表索引
CREATE INDEX idx_user_month ON monthly_settlements(user_id, settlement_month);
CREATE UNIQUE INDEX uk_user_month ON monthly_settlements(user_id, settlement_month);

-- 审计日志表索引
CREATE INDEX idx_user_action ON audit_logs(user_id, action);
CREATE INDEX idx_created_at ON audit_logs(created_at);
```

---

## 5. 开发规范

### 5.1 后端开发规范

#### 命名规范

```java
// 类名：大驼峰（PascalCase）
public class ProjectService {}

// 方法名：小驼峰（camelCase）
public void calculateContribution() {}

// 常量：全大写下划线分隔
public static final String DEFAULT_STATUS = "ACTIVE";

// 变量：小驼峰
private String userName;

// 包名：全小写
package com.company.performance.service;
```

#### 注释规范

```java
/**
 * 计算项目贡献度
 * 
 * @param projectId 项目ID
 * @param userId 用户ID
 * @return 贡献度百分比
 * @throws BusinessException 业务异常
 */
public BigDecimal calculateContribution(Long projectId, Long userId) {
    // 1. 获取项目信息
    Project project = projectMapper.selectById(projectId);
    
    // 2. 计算原始贡献度
    BigDecimal rawContribution = calculateRawContribution(projectId, userId);
    
    // 3. 应用岗位比例
    return applyPositionRatio(rawContribution, project, userId);
}
```

#### 统一响应格式

```java
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}
```

#### 异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统异常，请联系管理员");
    }
}
```

### 5.2 前端开发规范

#### 命名规范

```javascript
// 组件名：大驼峰
const ProjectList = {}

// 变量/函数：小驼峰
const userName = ''
const getUserInfo = () => {}

// 常量：全大写下划线分隔
const API_BASE_URL = ''

// 文件名：kebab-case
// project-list.vue
// user-management.vue
```

#### Vue组件结构

```vue
<template>
  <!-- 模板区域 -->
</template>

<script setup>
// 1. 导入
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

// 2. 响应式数据
const loading = ref(false)
const tableData = ref([])

// 3. 计算属性
const filteredData = computed(() => {
  return tableData.value.filter(item => item.status === 'active')
})

// 4. 方法
const fetchData = async () => {
  loading.value = true
  try {
    const res = await api.getList()
    tableData.value = res.data
  } finally {
    loading.value = false
  }
}

// 5. 生命周期
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
/* 样式区域 */
</style>
```

#### API调用规范

```javascript
// api/request.js - Axios封装
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data
    } else {
      ElMessage.error(message)
      return Promise.reject(new Error(message))
    }
  },
  error => {
    if (error.response?.status === 401) {
      // 优先尝试刷新token
      const refreshToken = localStorage.getItem('refresh_token')
      if (refreshToken) {
        return axios.post('/api/auth/refresh', { refreshToken })
          .then(res => {
            const newToken = res.data?.token
            if (newToken) {
              localStorage.setItem('token', newToken)
              // 重新发起原请求
              error.config.headers.Authorization = `Bearer ${newToken}`
              return request(error.config)
            }
          })
          .catch(() => {
            router.push('/login')
            return Promise.reject(error)
          })
      } else {
        router.push('/login')
      }
    }
    ElMessage.error(error.message)
    return Promise.reject(error)
  }
)

export default request
```

---

## 6. 大模型代码生成规则

### 6.1 后端代码生成提示词模板

#### 实体类生成

```
请基于以下数据库表设计，生成对应的Java实体类：

表名：{table_name}
表说明：{table_description}

字段列表：
{field_list}

要求：
1. 使用Lombok注解：@Data, @TableName, @TableField
2. 主键使用@TableId(type = IdType.AUTO)
3. 逻辑删除字段使用@TableLogic
4. 日期字段使用LocalDateTime类型
5. 金额字段使用BigDecimal类型
6. 枚举字段使用对应的枚举类型
7. 添加详细的字段注释
8. 包名：com.company.performance.entity
9. 导入必要的依赖

示例：
```java
package com.company.performance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * {table_description}
 */
@Data
@TableName("{table_name}")
public class {ClassName} {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 其他字段...
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
```
```

#### Service层生成

```
请基于以下实体类，生成对应的Service接口和实现类：

实体类：{EntityClass}
功能说明：{功能描述}

要求：
1. Service接口继承IService<{EntityClass}>
2. ServiceImpl继承ServiceImpl<{Mapper}, {EntityClass}> implements {Service}
3. 实现以下方法：
   - 分页查询：page(PageRequest request)
   - 详情查询：getDetailById(Long id)
   - 创建：create({CreateRequest} request)
   - 更新：update(Long id, {UpdateRequest} request)
   - 删除：delete(Long id)
4. 使用@Service注解
5. 添加@Transactional注解（涉及多表操作时）
6. 添加详细的方法注释
7. 抛出BusinessException处理业务异常
8. 包名：com.company.performance.service

示例结构：
interface I{EntityClass}Service extends IService<{EntityClass}> {
    PageResult<{EntityClass}> page(PageRequest request);
    {EntityClass}VO getDetailById(Long id);
    Long create({Create}Request request);
    void update(Long id, {Update}Request request);
    void delete(Long id);
}
```

#### Controller层生成

```
请基于以下Service，生成对应的Controller：

Service：{ServiceClass}
模块：{module_name}
基础路径：/api/{module}

要求：
1. 使用@RestController和@RequestMapping注解
2. 注入Service使用@Resource或@Autowired
3. 所有方法返回Result<T>统一格式
4. 使用@PostMapping/@GetMapping/@PutMapping/@DeleteMapping
5. 参数校验使用@Valid和@Validated
6. 添加Swagger注解：@Tag, @Operation
7. 实现以下接口：
   - GET  /api/{module}/page - 分页查询
   - GET  /api/{module}/{id} - 详情查询
   - POST /api/{module} - 创建
   - PUT  /api/{module}/{id} - 更新
   - DELETE /api/{module}/{id} - 删除
8. 包名：com.company.performance.controller

示例：
@Tag(name = "{模块名称}")
@RestController
@RequestMapping("/api/{module}")
public class {EntityClass}Controller {
    
    @Resource
    private I{EntityClass}Service {service};
    
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<{EntityClass}>> page(@Valid PageRequest request) {
        return Result.success({service}.page(request));
    }
    
    // 其他方法...
}
```

### 6.2 前端代码生成提示词模板

#### Vue组件生成

```
请生成一个Vue 3组件，用于{功能描述}：

组件名：{ComponentName}
路由路径：{route_path}

要求：
1. 使用<script setup>语法
2. 使用Element Plus组件库
3. 包含以下功能：
   - 数据列表展示（el-table）
   - 搜索筛选（el-form）
   - 分页（el-pagination）
   - 新增/编辑对话框（el-dialog）
   - 删除确认（ElMessageBox）
4. 使用ref和reactive管理状态
5. 使用computed处理计算属性
6. API调用使用async/await
7. 添加loading状态
8. 添加错误处理
9. 响应式布局
10. 代码注释清晰

数据字段：
{field_list}

示例结构：
<template>
  <div class="container">
    <!-- 搜索区域 -->
    <el-form>...</el-form>
    
    <!-- 操作按钮 -->
    <el-button @click="handleCreate">新增</el-button>
    
    <!-- 数据表格 -->
    <el-table :data="tableData" :loading="loading">
      <el-table-column prop="name" label="名称" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button @click="handleEdit(row)">编辑</el-button>
          <el-button @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <el-pagination />
    
    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible">...</el-dialog>
  </div>
</template>
```

#### API接口生成

```
请生成前端API接口文件，对应后端{模块名称}模块：

后端基础路径：/api/{module}

要求：
1. 导入封装的request实例
2. 导出以下方法：
   - getList(params) - 分页查询
   - getDetail(id) - 详情查询
   - create(data) - 创建
   - update(id, data) - 更新
   - remove(id) - 删除
3. 使用TypeScript类型注解（可选）
4. 添加JSDoc注释
5. 文件名：{module}.js

示例：
import request from './request'

/**
 * 分页查询{模块}列表
 */
export const getList = (params) => {
  return request.get('/api/{module}/page', { params })
}

/**
 * 查询{模块}详情
 */
export const getDetail = (id) => {
  return request.get(`/api/{module}/${id}`)
}

// 其他方法...
```

### 6.3 通用开发规则

#### 代码质量要求

```
所有生成的代码必须满足：

1. 代码规范：
   - 遵循Java/JavaScript编码规范
   - 统一的命名风格
   - 合理的代码缩进和格式

2. 注释要求：
   - 类和方法必须有注释
   - 复杂逻辑必须有行内注释
   - 使用中文注释

3. 异常处理：
   - 所有外部调用必须捕获异常
   - 数据库操作必须处理异常
   - 给用户友好的错误提示

4. 性能考虑：
   - 避免N+1查询
   - 合理使用索引
   - 大数据量使用分页

5. 安全性：
   - 参数校验
   - SQL注入防护
   - XSS防护
   - 权限验证

6. 可维护性：
   - 单一职责原则
   - 避免代码重复
   - 提取公共方法
   - 配置化优于硬编码
```

#### 项目特定规则

```
人员管理与绩效考核系统特定规则：

1. 贡献度计算：
   - 使用BigDecimal避免精度丢失
   - 保留4位小数
   - 验证总和必须等于1
   - 公式：CPj = R × ∑(LPi × LCi,j)

2. 金额处理：
   - 统一使用BigDecimal
   - 保留2位小数
   - 四舍五入模式：HALF_UP

3. 日期处理：
   - 使用LocalDateTime而非Date
   - 时区统一为Asia/Shanghai
   - 格式：yyyy-MM-dd HH:mm:ss

4. 状态管理：
   - 使用枚举类型
   - 避免魔法值
   - 状态流转需要验证

5. 权限控制：
   - 方法级权限注解
   - 数据级权限过滤
   - 操作日志记录

6. 数据验证：
   - 节点权重和必须等于1
   - 节点贡献度和必须等于1
   - 项目状态流转验证
   - 金额不能为负

7. 业务规则：
   - 项目结束后不可修改工时
   - 已确认的数据需要审批才能修改
   - 月度结算后数据锁定
   - 重点事项办结需总监确认
```

---

## 7. 开发工作流

### 7.1 环境搭建步骤

#### 后端环境

```bash
# 1. 安装JDK 17
# 下载并安装OpenJDK 17

# 2. 安装Maven
# 下载并配置Maven 3.9+

# 3. 安装MySQL 8.0
# 创建数据库
mysql -u root -p
CREATE DATABASE performance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 4. 导入数据库脚本
mysql -u root -p performance_db < src/main/resources/db/schema.sql

# 5. 安装Redis
# 下载并启动Redis服务

# 6. 克隆项目并启动
git clone <repository>
cd performance-management
mvn clean install
mvn spring-boot:run
```

#### 前端环境

```bash
# 1. 安装Node.js
# 下载并安装Node.js 18+

# 2. 克隆项目并安装依赖
git clone <repository>
cd performance-management-frontend
npm install

# 3. 启动开发服务器
npm run dev

# 访问 http://localhost:3000
```

### 7.2 Git工作流

```bash
# 分支策略
main        # 主分支，生产环境
develop     # 开发分支
feature/*   # 功能分支
hotfix/*    # 紧急修复分支

# 开发新功能
git checkout develop
git pull origin develop
git checkout -b feature/project-management
# 开发...
git add .
git commit -m "feat: 实现项目管理模块"
git push origin feature/project-management
# 创建Pull Request到develop分支

# Commit规范
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试
chore: 构建或辅助工具变动
```

---

## 8. 测试策略

### 8.1 单元测试

```java
@SpringBootTest
class ProjectServiceTest {
    
    @Resource
    private IProjectService projectService;
    
    @Test
    void testCalculateContribution() {
        // 准备测试数据
        Long projectId = 1L;
        Long userId = 10L;
        
        // 执行
        BigDecimal contribution = projectService.calculateContribution(projectId, userId);
        
        // 验证
        assertNotNull(contribution);
        assertTrue(contribution.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(contribution.compareTo(BigDecimal.ONE) <= 0);
    }
}
```

### 8.2 接口测试

使用Postman或自动化测试工具：

```javascript
// 项目创建接口测试
pm.test("创建项目成功", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData.code).to.eql(200);
    pm.expect(jsonData.data).to.have.property('projectId');
});
```

---

## 9. 部署指南

### 9.1 Docker部署

#### Dockerfile (后端)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Dockerfile (前端)

```dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
```

#### docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: performance_db
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"

  redis:
    image: redis:7.2-alpine
    ports:
      - "6379:6379"

  backend:
    build: ./performance-management
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/performance_db
      SPRING_REDIS_HOST: redis

  frontend:
    build: ./performance-management-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

### 9.2 部署命令

```bash
# 构建并启动
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

---

## 10. 维护与监控

### 10.1 日志管理

```yaml
# logback-spring.xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### 10.2 健康检查

```java
@RestController
public class HealthController {
    
    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        return Result.success(status);
    }
}
```

---

## 11. 总结

本技术栈方案具有以下特点：

✅ **简单易用**：主流技术栈，文档丰富，学习成本低  
✅ **稳定可靠**：经过大规模生产环境验证  
✅ **开发高效**：工具链完善，代码生成规则清晰  
✅ **易于维护**：规范统一，结构清晰  
✅ **可扩展性强**：模块化设计，易于功能扩展

**技术栈核心：**
- 后端：Java 17 + Spring Boot 3.2 + MyBatis-Plus
- 前端：Vue 3 + Vite + Element Plus
- 数据库：MySQL 8.0 + Redis 7.2
- 部署：Docker + Nginx

**推荐开发流程：**
1. 根据设计文档创建数据库表
2. 使用大模型生成后端实体类、Mapper、Service、Controller
3. 使用大模型生成前端API接口、Vue组件
4. 编写单元测试和接口测试
5. 本地调试通过后提交代码
6. CI/CD自动构建和部署

此技术栈完全能够支撑人员管理与绩效考核系统的开发需求，同时为后续使用大模型辅助开发提供了清晰的规则和模板。
