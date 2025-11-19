# Gateway Service - API网关服务

## 概述

Gateway Service是小学教务系统的API网关，作为系统的统一入口，提供路由转发、JWT认证、跨域处理、日志记录等功能。

## 技术栈

- **Spring Cloud Gateway** (WebFlux) - 响应式API网关
- **Spring Cloud Alibaba Nacos** - 服务发现与配置中心
- **Spring Cloud Alibaba Sentinel** - 流量控制与熔断降级
- **JWT (jjwt 0.12.5)** - Token认证
- **Redis (Reactive)** - 缓存与限流
- **Lombok** - 简化代码

## 核心功能

### 1. 路由转发

Gateway自动将请求路由到对应的微服务：

| 路径前缀 | 目标服务 | 说明 |
|---------|---------|------|
| /api/auth/** | auth-service | 认证服务 |
| /api/business/** | business-service | 审批业务服务 |
| /api/class/** | class-service | 教务服务 |
| /api/student/** | student-service | 学生服务 |
| /api/teacher/** | teacher-service | 教师服务 |
| /api/workflow/** | workflow-service | 工作流服务 |

### 2. JWT认证

- **双令牌机制**：AccessToken (15分钟) + RefreshToken (7天)
- **自动验证**：所有请求（除白名单外）自动验证JWT Token
- **用户信息传递**：验证成功后，将用户信息添加到请求头传递给下游服务

请求头格式：
```
Authorization: Bearer {accessToken}
```

下游服务可获取的请求头：
```
X-User-Id: {userId}
X-Username: {username}
X-User-Role: {role}
```

### 3. 白名单

以下路径不需要JWT认证：
- `/api/auth/login` - 登录
- `/api/auth/register` - 注册
- `/api/auth/refresh` - 刷新Token
- `/actuator/**` - 监控端点

可在配置文件中自定义白名单。

### 4. 全局异常处理

统一处理以下异常：
- 401 Unauthorized - Token无效或缺失
- 403 Forbidden - 权限不足
- 503 Service Unavailable - 服务不可用
- 504 Gateway Timeout - 请求超时

返回格式：
```json
{
  "code": 401,
  "message": "Token无效或已过期",
  "data": null,
  "timestamp": 1234567890
}
```

### 5. 跨域处理 (CORS)

- 支持跨域请求
- 允许携带认证信息
- 可配置允许的源、方法、头部

### 6. 请求日志

自动记录每个请求的：
- 请求方法和路径
- 客户端IP
- 响应状态码
- 请求耗时

## 项目结构

```
gateway/
├── src/main/java/com/example/gateway/
│   ├── GatewayApplication.java           # 启动类
│   ├── config/
│   │   ├── GatewayConfig.java           # 网关配置
│   │   ├── JwtProperties.java           # JWT配置属性
│   │   ├── WhiteListProperties.java     # 白名单配置
│   │   └── CorsConfig.java              # 跨域配置
│   ├── constants/
│   │   └── GatewayConstants.java        # 常量定义
│   ├── filter/
│   │   ├── AuthGlobalFilter.java        # JWT认证过滤器
│   │   └── LoggingGlobalFilter.java     # 日志记录过滤器
│   ├── exception/
│   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   └── utils/
│       └── JwtUtils.java                # JWT工具类
└── src/main/resources/
    └── application.yml                   # 配置文件
```

## 关键组件说明

### AuthGlobalFilter (JWT认证过滤器)

WebFlux版本的JWT认证过滤器，主要功能：

1. **白名单检查** - 检查请求路径是否在白名单中
2. **Token提取** - 从Authorization请求头提取Bearer Token
3. **Token验证** - 使用JwtUtils验证Token有效性和类型
4. **用户信息提取** - 从Token Claims中提取userId、username、role
5. **请求头注入** - 将用户信息添加到请求头，传递给下游服务

**优先级**：-100（较高优先级，在大多数过滤器之前执行）

### LoggingGlobalFilter (日志过滤器)

记录请求和响应信息：

```
INFO: 请求开始 => Method: POST, Path: /api/auth/login, ClientIP: 127.0.0.1
INFO: 请求结束 => Method: POST, Path: /api/auth/login, Status: 200, Duration: 156ms
```

**优先级**：HIGHEST_PRECEDENCE（最高优先级，最先执行）

### GlobalExceptionHandler (全局异常处理器)

实现 `ErrorWebExceptionHandler` 接口，统一处理所有异常。

### JwtUtils (JWT工具类)

提供JWT操作的核心方法：
- `parseJWT()` - 解析并验证JWT
- `parseJWTSafely()` - 安全解析，返回Optional
- `validateToken()` - 验证Token是否有效
- `getUserIdFromClaims()` - 获取用户ID
- `getRoleFromClaims()` - 获取用户角色

## 配置说明

### application.yml

本地配置文件，主要包含：
- 服务端口：8080
- Nacos地址
- 路由规则
- 白名单
- JWT配置（可被Nacos覆盖）

### Nacos配置

详见 [NACOS_CONFIG_GUIDE.md](NACOS_CONFIG_GUIDE.md)

必须配置：
- `gateway-service.yaml` - Gateway专用配置
- `school-common.yaml` - 公共配置（JWT密钥等）

## 启动方式

### 前置条件

1. Nacos服务器已启动 (localhost:8848)
2. Redis服务器已启动 (localhost:6379)
3. 已在Nacos中创建必要的配置文件

### 启动命令

```bash
# 使用Maven
cd gateway
mvn spring-boot:run

# 或使用jar包
mvn clean package
java -jar target/gateway-0.0.1-SNAPSHOT.jar

# 使用环境变量
JWT_SECRET_KEY=your-secret-key java -jar target/gateway-0.0.1-SNAPSHOT.jar
```

### 验证启动

```bash
# 检查健康状态
curl http://localhost:8080/actuator/health

# 查看路由信息
curl http://localhost:8080/actuator/gateway/routes
```

## 使用示例

### 1. 登录获取Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

响应：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 900000
  }
}
```

### 2. 使用Token访问受保护接口

```bash
curl http://localhost:8080/api/business/leave \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 3. 刷新Token

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
  }'
```

## WebFlux适配说明

### 与Servlet的区别

Gateway基于WebFlux（响应式编程），与传统的Servlet栈有本质区别：

| 特性 | Servlet (common-security) | WebFlux (Gateway) |
|------|--------------------------|-------------------|
| HTTP抽象 | HttpServletRequest/Response | ServerHttpRequest/Response |
| 过滤器 | OncePerRequestFilter | GlobalFilter |
| 上下文传递 | ThreadLocal | Reactor Context |
| 返回类型 | void | Mono<Void> |
| 编程模型 | 阻塞式 | 响应式 (非阻塞) |

### 为什么不能直接使用common-security

1. **依赖冲突**：common-security依赖 `spring-boot-starter-web`（Servlet），而Gateway需要 `spring-boot-starter-webflux`，两者不能共存
2. **API不兼容**：JwtAuthenticationFilter继承自 `OncePerRequestFilter`（Servlet API），无法在WebFlux中使用
3. **上下文机制不同**：ThreadLocal在WebFlux的异步环境中无法正常工作

### 解决方案

1. **复用JwtUtils** - JWT工具类本身不依赖Servlet，可以直接复制使用
2. **重写过滤器** - 实现 `GlobalFilter` 接口，替代 `OncePerRequestFilter`
3. **共享密钥** - 通过Nacos配置中心共享JWT密钥，确保Gateway和Auth服务使用相同密钥

## 监控与运维

### Actuator端点

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/gateway/routes` - 路由信息
- `/actuator/gateway/globalfilters` - 全局过滤器信息

### 日志

日志文件位置：`logs/gateway.log`

日志级别配置：
```yaml
logging:
  level:
    com.example.gateway: INFO
    org.springframework.cloud.gateway: INFO
```

### Sentinel监控

访问Sentinel控制台：http://localhost:8858

可查看：
- 实时流量
- 限流规则
- 熔断规则

## 常见问题

### 1. Token验证失败（401）

**原因**：JWT密钥不一致

**解决**：检查Nacos中gateway-service和auth-service的jwt.secret-key是否一致

### 2. 路由404

**原因**：后端服务未注册或路由配置错误

**解决**：
- 检查Nacos服务列表
- 检查路由配置中的uri是否正确

### 3. 跨域错误

**原因**：CORS配置问题或后端服务也配置了CORS

**解决**：
- 后端服务不要配置CORS，统一由Gateway处理
- 检查CorsConfig配置

### 4. 服务不可用（503）

**原因**：后端服务未启动或网络不通

**解决**：
- 启动相应的后端服务
- 检查服务是否注册到Nacos
- 检查网络连接

## 性能优化建议

1. **连接池配置**：合理配置Redis和HTTP连接池大小
2. **缓存策略**：对频繁验证的Token结果进行缓存（可选）
3. **限流保护**：配置Sentinel限流规则
4. **日志优化**：生产环境调整日志级别为INFO或WARN
5. **JVM调优**：根据负载调整堆内存大小

## 安全建议

1. **密钥管理**：不要在代码中硬编码JWT密钥，使用环境变量或密钥管理服务
2. **HTTPS**：生产环境强制使用HTTPS，防止Token泄露
3. **白名单控制**：严格控制白名单，只开放必要的公开接口
4. **CORS限制**：生产环境不要使用 `allowedOriginPatterns: "*"`
5. **日志脱敏**：避免在日志中输出敏感信息（Token、密码等）

## 下一步开发

可扩展的功能：
- [ ] 基于Redis的分布式限流
- [ ] 动态路由配置（从数据库或Nacos加载）
- [ ] 请求/响应Body日志记录
- [ ] API访问统计
- [ ] 黑名单/IP限制
- [ ] Token刷新策略优化
- [ ] 链路追踪集成（Sleuth/Zipkin）

## 相关文档

- [Nacos配置指南](NACOS_CONFIG_GUIDE.md)
- [Spring Cloud Gateway文档](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Project Reactor文档](https://projectreactor.io/docs/core/release/reference/)
