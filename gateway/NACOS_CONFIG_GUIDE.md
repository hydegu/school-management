# Gateway服务 - Nacos配置指南

本文档说明Gateway服务需要在Nacos配置中心创建的配置文件。

## 需要创建的配置文件

### 1. gateway-service.yaml

**配置ID**: `gateway-service.yaml`
**Group**: `DEFAULT_GROUP`
**配置格式**: `YAML`

```yaml
# Gateway服务专用配置（其他配置从 school-common.yaml 继承）

# Sentinel限流配置（可选）
spring:
  cloud:
    sentinel:
      datasource:
        flow:
          nacos:
            server-addr: localhost:8848
            dataId: gateway-sentinel-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

# 日志配置（可选：如需文件日志）
logging:
  file:
    name: logs/gateway.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"
```

**说明**：
- ✅ Redis、JWT、数据库等配置已从 `school-common.yaml` 继承，无需重复配置
- ✅ 白名单配置在 `application.yml` 中（Gateway特有）
- ✅ 路由配置在 `application.yml` 中（Gateway核心功能）

### 2. school-common.yaml（已存在）

**配置ID**: `school-common.yaml`
**Group**: `DEFAULT_GROUP`
**配置格式**: `YAML`

```yaml
# 所有服务的公共配置（您已有的配置）

spring:
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/school_management?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

  # 缓存配置
  cache:
    type: redis

  # Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      password: 123456
      database: 0
      timeout: 3000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms

# MyBatis-Plus配置
mybatis-plus:
  configuration:
    default-enum-type-handler: com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.example.forum.*.entity

# JWT配置（所有服务必须使用相同密钥）
jwt:
  secret-key: +kJ8A7Cw9b9hhPNULgUTYsXWLJI0TIsuLtGX99A+I3M=
  access-token:
    ttl: 900000  # 15分钟
    token-name: "Authorization"
  refresh-token:
    ttl: 604800000  # 7天
    token-name: "Refresh-Token"
    redis-key-prefix: "refresh_token:"
    use-cookie: true

# 日志配置
logging:
  level:
    com.example.filter.JwtAuthenticationFilter: DEBUG
    com.example.service.TokenService: INFO
```

**说明**：
- ✅ Gateway会自动继承这些配置
- ⚠️ Gateway不使用数据库和MyBatis，但继承这些配置不影响运行
- ✅ JWT密钥已配置：`+kJ8A7Cw9b9hhPNULgUTYsXWLJI0TIsuLtGX99A+I3M=`

## 重要说明

### JWT密钥一致性

⚠️ **关键配置**：`jwt.secret-key` 必须在以下所有服务中保持完全一致：
- gateway-service
- auth-service
- business-service
- class-service
- 其他所有微服务

**原因**：
- auth-service 使用此密钥生成JWT Token
- gateway-service 使用此密钥验证JWT Token
- 如果密钥不一致，Gateway将无法验证auth-service签发的Token

### 配置优先级

配置加载顺序（后加载的会覆盖先加载的）：
1. application.yml（本地配置）
2. school-common.yaml（公共配置）
3. gateway-service.yaml（服务专用配置）

### 环境变量

可以通过环境变量覆盖配置：

```bash
# 设置JWT密钥
export JWT_SECRET_KEY=your-production-secret-key

# 设置Redis配置
export SPRING_REDIS_HOST=redis.example.com
export SPRING_REDIS_PASSWORD=your-redis-password
```

## 验证配置

启动Gateway服务后，可以通过以下方式验证配置是否正确加载：

### 1. 检查启动日志

```bash
# 查看Nacos配置是否成功加载
grep "Located property source" logs/gateway.log
```

### 2. 访问Actuator端点

```bash
# 查看配置信息
curl http://localhost:8080/actuator/env

# 查看路由信息
curl http://localhost:8080/actuator/gateway/routes
```

### 3. 测试JWT认证

```bash
# 1. 先登录获取Token（通过Gateway）
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 2. 使用Token访问受保护的接口
curl http://localhost:8080/api/business/leave \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 配置项说明

### 白名单配置

```yaml
whitelist:
  urls:
    - /api/auth/login      # 登录接口
    - /api/auth/register   # 注册接口
    - /api/auth/refresh    # 刷新Token接口
    - /actuator/**         # 监控端点
    - /favicon.ico         # 网站图标
```

**说明**：白名单中的路径不需要JWT认证，可直接访问。

### 路由配置

Gateway会自动从Nacos服务发现中获取服务列表并创建路由。

手动配置的路由规则（在application.yml中）：
- `/api/auth/**` → auth-service
- `/api/business/**` → business-service
- `/api/class/**` → class-service
- `/api/student/**` → student-service
- `/api/teacher/**` → teacher-service
- `/api/workflow/**` → workflow-service

## 故障排查

### 问题1：Token验证失败

**现象**：访问接口返回401 Unauthorized，提示"Token无效或已过期"

**原因**：JWT密钥不一致

**解决方案**：
1. 检查Nacos中 `gateway-service.yaml` 和 `auth-service.yaml` 的 `jwt.secret-key` 是否一致
2. 检查 `school-common.yaml` 中的 `jwt.secret-key`
3. 重启Gateway和Auth服务

### 问题2：路由不可达

**现象**：访问接口返回503 Service Unavailable

**原因**：后端服务未注册到Nacos或服务名称不匹配

**解决方案**：
1. 检查Nacos控制台，确认后端服务已注册
2. 检查服务名称是否正确（如 `auth-service`）
3. 检查网络连接

### 问题3：跨域问题

**现象**：浏览器控制台报CORS错误

**解决方案**：
1. 检查Gateway的CORS配置
2. 确保后端服务没有单独配置CORS（会与Gateway冲突）
3. 如果需要，在 `gateway-service.yaml` 中调整CORS配置

## 生产环境注意事项

1. **密钥安全**：不要将JWT密钥硬编码在代码中，使用环境变量或密钥管理服务
2. **CORS配置**：生产环境应该限制 `allowedOriginPatterns`，不要使用 `*`
3. **日志级别**：生产环境应设置为 `INFO` 或 `WARN`
4. **监控**：启用Actuator端点并配置监控系统
5. **限流**：配置Sentinel流控规则，防止服务过载

## 下一步

配置完成后：
1. 启动Nacos服务器
2. 在Nacos控制台创建上述配置文件
3. 启动Gateway服务
4. 测试路由和JWT认证功能
