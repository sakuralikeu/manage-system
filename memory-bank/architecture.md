日期：2026-02-09
范围：后端模块初始化（performance-management）与前端模块初始化（performance-management-frontend）

架构洞察：
- 后端采用独立模块目录 performance-management，便于与前端或其他服务并行管理
- 入口类 PerformanceApplication 作为唯一启动点，后续 Spring 组件扫描均以 com.company.performance 为根包
- 包结构按职责分层：controller → service → mapper → entity，公共能力集中在 common 与 utils
- DTO 与 VO 分离，保证接口输入输出与持久化模型解耦
- 数据库连接配置集中在 application.yml，统一时区与驼峰映射规则，避免跨层不一致

文件与目录作用：
- [performance-management/pom.xml](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/pom.xml)：Maven 构建与依赖声明
- [performance-management/src/main/java/com/company/performance/PerformanceApplication.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/PerformanceApplication.java)：Spring Boot 启动入口
- [performance-management/src/main/java/com/company/performance/controller](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/controller)：控制器层
- [performance-management/src/main/java/com/company/performance/service](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/service)：服务接口层
- [performance-management/src/main/java/com/company/performance/service/impl](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/service/impl)：服务实现层
- [performance-management/src/main/java/com/company/performance/mapper](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/mapper)：数据访问层
- [performance-management/src/main/java/com/company/performance/entity](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/entity)：实体模型层
- [performance-management/src/main/java/com/company/performance/common](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/common)：通用能力与公共抽象
- [performance-management/src/main/java/com/company/performance/dto/request](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/request)：请求 DTO
- [performance-management/src/main/java/com/company/performance/dto/response](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/response)：响应 DTO
- [performance-management/src/main/java/com/company/performance/vo](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/vo)：前端展示用 VO
- [performance-management/src/main/java/com/company/performance/utils](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/utils)：工具类集合
- [performance-management/src/main/resources/application.yml](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/resources/application.yml)：基础 Spring 配置入口
- [performance-management/src/main/java/com/company/performance/controller/DatabaseTimeController.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/controller/DatabaseTimeController.java)：数据库时间校验接口
- [performance-management/src/main/java/com/company/performance/common/enums/PositionType.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/common/enums/PositionType.java)：岗位类型枚举（PRESALE、RD），实现 IEnum 以便映射
- [performance-management/src/main/java/com/company/performance/entity/User.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/entity/User.java)：用户实体映射 users 表，password 在 JSON 序列化中忽略
- [performance-management/src/main/java/com/company/performance/mapper/UserMapper.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/mapper/UserMapper.java)：用户数据访问，继承 BaseMapper 并提供 findByUsername
- [performance-management/src/main/java/com/company/performance/entity/Role.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/entity/Role.java)：角色实体映射 roles 表，permissions 使用 JSON 类型处理
- [performance-management/src/main/java/com/company/performance/mapper/RoleMapper.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/mapper/RoleMapper.java)：角色数据访问，提供按角色名查询
- [performance-management/src/main/java/com/company/performance/config/JwtProperties.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/config/JwtProperties.java)：JWT 配置读取（secret、expiration）
- [performance-management/src/main/java/com/company/performance/utils/JwtUtil.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/utils/JwtUtil.java)：JWT 生成、校验与解析工具
- [performance-management/src/main/java/com/company/performance/dto/request/LoginRequest.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/request/LoginRequest.java)：登录请求 DTO
- [performance-management/src/main/java/com/company/performance/dto/response/LoginResponse.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/response/LoginResponse.java)：登录响应 DTO
- [performance-management/src/main/java/com/company/performance/common/exception/BusinessException.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/common/exception/BusinessException.java)：业务异常定义
- [performance-management/src/main/java/com/company/performance/service/UserService.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/service/UserService.java)：用户服务接口
- [performance-management/src/main/java/com/company/performance/service/impl/UserServiceImpl.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/service/impl/UserServiceImpl.java)：登录逻辑实现与 JWT 生成
- [performance-management/src/main/java/com/company/performance/common/Result.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/common/Result.java)：统一响应结构
- [performance-management/src/main/java/com/company/performance/controller/AuthController.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/controller/AuthController.java)：登录接口控制器
- [performance-management/src/main/java/com/company/performance/controller/HealthController.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/controller/HealthController.java)：健康检查接口
- [performance-management/src/main/java/com/company/performance/config/JwtAuthenticationFilter.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/config/JwtAuthenticationFilter.java)：JWT 认证过滤器
- [performance-management/src/main/java/com/company/performance/config/SecurityConfig.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/config/SecurityConfig.java)：安全配置与访问规则
- [performance-management/src/main/java/com/company/performance/common/exception/GlobalExceptionHandler.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/common/exception/GlobalExceptionHandler.java)：全局异常处理与统一响应
- [performance-management/src/main/java/com/company/performance/dto/response/AuthMeResponse.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/response/AuthMeResponse.java)：登录态校验响应
- [performance-management/src/main/java/com/company/performance/service/TokenService.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/service/TokenService.java)：Token 黑名单与刷新抽象
- [performance-management/src/main/java/com/company/performance/service/impl/RedisTokenService.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/service/impl/RedisTokenService.java)：Redis 黑名单与刷新 token 存储实现
- [performance-management/src/main/java/com/company/performance/dto/response/AuthRefreshResponse.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/response/AuthRefreshResponse.java)：刷新 token 响应
- [performance-management/src/main/java/com/company/performance/dto/request/RefreshTokenRequest.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/request/RefreshTokenRequest.java)：刷新 token 请求
- [performance-management/src/main/java/com/company/performance/dto/request/LogoutRequest.java](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management/src/main/java/com/company/performance/dto/request/LogoutRequest.java)：登出请求
  
- [performance-management-frontend/package.json](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/package.json)：前端依赖、脚本配置，版本固定以适配 Vue 3.4.x
- [performance-management-frontend/src/main.js](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/src/main.js)：应用入口，注册 Element Plus、Pinia、Router
- [performance-management-frontend/vite.config.js](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/vite.config.js)：Vite 开发服务器配置（端口 3000、/api 代理、@ 别名、Element Plus 自动导入）
- [performance-management-frontend/src/router](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/src/router)：路由配置（登录与首页）
- [performance-management-frontend/src/store](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/src/store)：状态管理（Pinia 初始化）
- [performance-management-frontend/src/api](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/src/api)：HTTP 客户端（Axios 实例）
- [performance-management-frontend/src/views/HomeView.vue](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/src/views/HomeView.vue)：首页视图，验证 Element Plus
- [performance-management-frontend/src/views/LoginView.vue](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/performance-management-frontend/src/views/LoginView.vue)：登录视图，提交登录并保存 token
  
新增架构洞察（2026-02-09）：
- 数据源与连接池：application.yml 配置 MySQL（serverTimezone=Asia/Shanghai）与 HikariCP（最大 20，最小空闲 5）
- MyBatis-Plus：开启 map-underscore-to-camel-case，统一主键自增与逻辑删除字段占位
- Jackson：time-zone 设为 Asia/Shanghai，接口输出统一使用 ISO 8601
- 测试接口：GET /api/db/time 直接从数据库取当前时间并转换为 +08:00 偏移的 OffsetDateTime
- 数据库脚本：[db/schema.sql](file:///e:/Users/Fengye/Documents/软开/内部管理系统/manage-system/db/schema.sql) 创建基础表与5个预设角色数据
  
- 前端兼容性策略：固定 Vue 至 3.4.15 并约束 vue-router 至 4.2.5，避免新版本对 3.5+ 的 peer 依赖造成安装失败；开发服务器端口使用 3000，后续在 1.1.5 步中配置代理与别名
  
- 自动导入策略：通过 unplugin-auto-import 与 unplugin-vue-components 搭配 ElementPlusResolver 自动按需引入组件与样式，减少手动注册与引入
  
- JWT 策略：secret 与有效期通过配置注入，Token 使用 HS256 签名并在 payload 中携带 userId
  
- 登录策略：使用 BCrypt 校验密码，用户名不存在或密码错误抛出业务异常
  
- 响应策略：控制器统一返回 Result 包装结构，包含 code、message、data、timestamp
  
- 安全策略：/api/auth/login 与 /health 匿名访问，/api/** 强制认证；CSRF 关闭，Session 无状态
  
- 异常策略：业务异常与参数校验异常返回 code=400，系统异常返回 code=500
  
- 登录前端策略：Element Plus 表单提交登录，成功后写入 localStorage 并跳转首页
  
- 前端认证策略：Axios 自动附带 token，401 自动跳转登录；路由守卫校验 token 并调用 /api/auth/me
  
- Token 策略：登录返回 access/refresh token；黑名单与 refresh token 存储在 Redis，Redis 不可用时降级并记录警告
