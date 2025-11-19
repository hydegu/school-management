# Gateway配置迁移方案

## 当前配置分布（开发阶段）

### application.yml (本地)
- ✅ 所有配置都在本地
- ✅ 适合开发调试
- ✅ 不依赖Nacos可用性

## 生产环境建议配置分布

### 1. application.yml (本地 - 最小化)
```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  application:
    name: gateway-service
  config:
    import:
      - nacos:gateway-service.yaml?refresh=true
      - nacos:school-common.yaml?refresh=true
  cloud:
    nacos:
      serverAddr: ${NACOS_SERVER:127.0.0.1:8848}
      discovery:
        enabled: true
    sentinel:
      transport:
        port: 8720
        dashboard: ${SENTINEL_DASHBOARD:localhost:8858}
      eager: true
```

### 2. gateway-service.yaml (Nacos)
```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        # Auth Service Routes
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/api/auth/**
          filters:
            - StripPrefix=0

        # Business Service Routes
        - id: business-service
          uri: lb://business-service
          predicates:
            - Path=/api/business/**
          filters:
            - StripPrefix=0

        # Class Service Routes
        - id: class-service
          uri: lb://class-service
          predicates:
            - Path=/api/class/**
          filters:
            - StripPrefix=0

        # Student Service Routes
        - id: student-service
          uri: lb://student-service
          predicates:
            - Path=/api/student/**
          filters:
            - StripPrefix=0

        # Teacher Service Routes
        - id: teacher-service
          uri: lb://teacher-service
          predicates:
            - Path=/api/teacher/**
          filters:
            - StripPrefix=0

        # Workflow Service Routes
        - id: workflow-service
          uri: lb://workflow-service
          predicates:
            - Path=/api/workflow/**
          filters:
            - StripPrefix=0

      globalcors:
        cors-configurations:
          '[/**]':
            # 开发环境
            # allowedOriginPatterns: "*"

            # 生产环境（根据实际情况配置）
            allowedOrigins:
              - "https://school.example.com"
              - "https://admin.school.example.com"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
            maxAge: 3600

# 白名单配置
whitelist:
  urls:
    - /api/auth/login
    - /api/auth/register
    - /api/auth/refresh
    - /actuator/health
    - /favicon.ico
    # 生产环境不要暴露所有actuator端点
    # - /actuator/**

# 日志配置
logging:
  level:
    com.example.gateway: INFO  # 生产环境用INFO
    org.springframework.cloud.gateway: WARN
    org.springframework.web.reactive: WARN
  file:
    name: logs/gateway.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"

# Actuator配置
management:
  endpoints:
    web:
      exposure:
        include: health,info  # 生产环境只暴露必要端点
  endpoint:
    health:
      show-details: when-authorized  # 安全考虑
```

### 3. school-common.yaml (Nacos - 已存在)
```yaml
# JWT、Redis、数据库等公共配置
# (保持不变)
```

## 迁移步骤

### Step 1: 备份当前配置
```bash
cp application.yml application.yml.backup
```

### Step 2: 在Nacos创建配置
1. 登录Nacos控制台
2. 创建 gateway-service.yaml
3. 粘贴上述配置
4. 发布配置

### Step 3: 精简本地配置
```yaml
# application.yml (精简版)
server:
  port: 8080

spring:
  application:
    name: gateway-service
  config:
    import:
      - nacos:gateway-service.yaml?refresh=true
      - nacos:school-common.yaml?refresh=true
  cloud:
    nacos:
      serverAddr: 127.0.0.1:8848
      discovery:
        enabled: true
    sentinel:
      transport:
        port: 8720
        dashboard: localhost:8858
      eager: true
```

### Step 4: 测试验证
```bash
# 启动Gateway
mvn spring-boot:run

# 验证配置加载
curl http://localhost:8080/actuator/health

# 检查路由
curl http://localhost:8080/actuator/gateway/routes
```

## 配置优先级

```
优先级（从高到低）：
1. 命令行参数 (--server.port=8080)
2. 环境变量 (SERVER_PORT=8080)
3. Nacos配置 (gateway-service.yaml)
4. Nacos公共配置 (school-common.yaml)
5. 本地配置 (application.yml)
```

## 动态刷新支持

### 支持动态刷新的配置
- ✅ 路由配置 (需要配合@RefreshScope或重启)
- ✅ 白名单
- ✅ 日志级别
- ✅ CORS配置

### 不支持动态刷新的配置
- ❌ 服务器端口
- ❌ Nacos地址
- ❌ Sentinel dashboard地址

## 环境差异化配置

### 开发环境
```yaml
# gateway-service-dev.yaml
logging:
  level:
    com.example.gateway: DEBUG

spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOriginPatterns: "*"  # 开发环境允许所有源

whitelist:
  urls:
    - /actuator/**  # 开发环境暴露所有端点
```

### 生产环境
```yaml
# gateway-service-prod.yaml
logging:
  level:
    com.example.gateway: INFO

spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins:
              - "https://school.example.com"  # 只允许特定域名

whitelist:
  urls:
    - /actuator/health  # 只暴露健康检查
```

## 安全建议

### 1. 敏感信息
- ❌ 不要在Nacos明文存储密码
- ✅ 使用Nacos加密配置功能
- ✅ 使用环境变量

### 2. Actuator端点
```yaml
# 开发环境
management:
  endpoints:
    web:
      exposure:
        include: "*"  # 方便调试

# 生产环境
management:
  endpoints:
    web:
      exposure:
        include: health,info  # 最小暴露
  endpoint:
    health:
      show-details: when-authorized
```

### 3. 白名单管理
```yaml
# 生产环境要严格控制
whitelist:
  urls:
    - /api/auth/login
    - /api/auth/register
    # ❌ 不要添加过于宽泛的规则
    # - /api/**
```

## 回滚方案

如果Nacos配置有问题：

### 方案1: Nacos回滚
- Nacos控制台 → 历史版本 → 回滚

### 方案2: 应急降级
```yaml
# application.yml 保留完整配置作为备份
# 注释掉 nacos 配置导入
# config:
#   import:
#     - nacos:gateway-service.yaml?refresh=true
```

### 方案3: 本地覆盖
```bash
# 启动时指定本地配置文件
java -jar gateway.jar --spring.config.location=file:./application-emergency.yml
```

## 总结

### 当前阶段建议：保持现状 ✅
- 开发调试中
- 配置在本地便于快速迭代
- 不依赖Nacos可用性

### 生产环境建议：迁移到Nacos ⭐
- 动态配置管理
- 环境差异化
- 快速响应变更
- 配置审计和版本控制

### 迁移优先级
1. 日志级别（最高优先级）
2. 白名单（安全需求）
3. CORS（环境差异）
4. 路由配置（业务需求）
