# Common Security - JWT双令牌认证模块

## 概述

本模块提供基于JWT的双令牌认证机制，包括：
- **AccessToken**：短期访问令牌（默认15分钟）
- **RefreshToken**：长期刷新令牌（默认7天）

## 特性

- ✅ 双令牌机制（AccessToken + RefreshToken）
- ✅ RefreshToken存储在Redis中，支持撤销
- ✅ Spring Security集成
- ✅ 自动配置，开箱即用
- ✅ 用户上下文ThreadLocal支持
- ✅ 完整的异常处理

## 快速开始

### 1. 添加依赖

在你的服务的`pom.xml`中添加：

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. 配置JWT

在`application.yml`或Nacos配置中心添加：

```yaml
jwt:
  secret-key: "your-256-bit-secret-key-change-this-in-production"
  access-token:
    ttl: 900000  # 15分钟
  refresh-token:
    ttl: 604800000  # 7天
    redis-key-prefix: "refresh_token:"

spring:
  data:
    redis:
      host: localhost
      port: 6379
```

> 完整配置示例见：`src/main/resources/application-jwt-example.yml`

### 3. 使用TokenService生成令牌

```java
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/auth/login")
    public R<Map<String, Object>> login(@RequestBody LoginRequest request) {
        // 验证用户身份...

        // 生成双令牌
        Map<String, Object> tokens = tokenService.generateTokens(
            userId,
            username,
            role
        );

        return R.ok(tokens);
    }
}
```

### 4. 刷新AccessToken

```java
@PostMapping("/auth/refresh")
public R<Map<String, Object>> refresh(@RequestBody RefreshRequest request) {
    Map<String, Object> newTokens = tokenService.refreshAccessToken(
        request.getRefreshToken()
    );
    return R.ok(newTokens);
}
```

### 5. 登出（撤销RefreshToken）

```java
@PostMapping("/auth/logout")
public R<String> logout(@RequestBody LogoutRequest request) {
    tokenService.revokeRefreshToken(request.getUserId());
    return R.ok("登出成功");
}
```

## API使用示例

### 登录获取令牌

```bash
POST /auth/login
Content-Type: application/json

{
  "identifier": "admin",
  "password": "password123"
}
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "accessTokenExpire": 900000,
    "refreshTokenExpire": 604800000
  }
}
```

### 使用AccessToken访问受保护资源

```bash
GET /api/users/profile
Authorization: Bearer <accessToken>
```

### 刷新AccessToken

```bash
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**响应：** 返回新的双令牌

### 登出

```bash
POST /auth/logout
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "userId": 1
}
```

## 获取当前用户信息

在任何被JWT拦截器保护的接口中，可以通过`UserContext`获取当前用户信息：

```java
@GetMapping("/api/users/profile")
public R<UserProfile> getCurrentUser() {
    String userId = UserContext.getUserId();
    String username = UserContext.getUsername();
    String role = UserContext.getRole();

    // 使用用户信息...
    return R.ok(userProfile);
}
```

## 配置白名单

默认情况下，以下路径不需要认证：
- `/auth/login`
- `/auth/refresh`
- `/auth/register`
- `/actuator/**`
- `/health`

如需自定义，请在你的服务中创建SecurityFilterChain Bean覆盖默认配置。

## 架构说明

### 核心组件

1. **JwtUtils** - JWT工具类
   - 生成AccessToken和RefreshToken
   - 解析和验证JWT
   - 提取用户信息

2. **TokenService** - 令牌服务
   - 管理双令牌生成
   - RefreshToken刷新逻辑
   - RefreshToken存储和撤销

3. **JwtAuthenticationFilter** - JWT认证过滤器
   - 拦截请求并验证AccessToken
   - 只接受tokenType为"access"的令牌
   - 提取用户信息到UserContext

4. **JwtProperties** - JWT配置属性
   - 密钥配置
   - AccessToken和RefreshToken的TTL配置

5. **UserContext** - 用户上下文
   - ThreadLocal存储当前用户信息
   - 自动在请求结束时清理

### 工作流程

```
1. 用户登录
   └─> 验证用户名密码
       └─> 生成AccessToken + RefreshToken
           └─> RefreshToken存储到Redis

2. 访问受保护资源
   └─> 携带AccessToken
       └─> JwtAuthenticationFilter验证
           └─> 提取用户信息到UserContext
               └─> 业务逻辑处理

3. AccessToken过期
   └─> 使用RefreshToken请求刷新接口
       └─> 验证RefreshToken
           └─> 生成新的双令牌
               └─> 旧RefreshToken失效

4. 用户登出
   └─> 从Redis删除RefreshToken
       └─> 客户端清除本地令牌
```

## 安全建议

1. **密钥管理**
   - 使用至少256位的强随机密钥
   - 不要将密钥硬编码在代码中
   - 定期轮换密钥

2. **令牌有效期**
   - AccessToken建议5-30分钟
   - RefreshToken建议7-30天
   - 根据业务需求调整

3. **HTTPS**
   - 生产环境必须使用HTTPS
   - 防止令牌在传输中被窃取

4. **RefreshToken存储**
   - 使用Redis存储，便于撤销
   - 登出时及时删除
   - 检测到可疑活动时撤销

5. **日志审计**
   - 记录令牌生成和刷新日志
   - 监控异常的刷新行为

## 故障排查

### 问题1：Bean注入失败

**现象：** `Could not autowire. No beans of 'TokenService' type found.`

**解决：** 确保在`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`中包含：
```
com.example.config.SecurityAutoConfiguration
```

### 问题2：Redis连接失败

**现象：** `Unable to connect to Redis`

**解决：** 检查Redis配置和连接状态

### 问题3：401 Unauthorized

**现象：** 访问接口返回401

**解决方案：**
1. 检查AccessToken是否正确传递（`Authorization: Bearer <token>`）
2. 检查令牌是否过期
3. 检查密钥配置是否一致
4. 查看日志中的具体错误信息

## 更新日志

### v0.0.1 (2025-11-17)
- ✅ 初始版本
- ✅ 实现双令牌机制
- ✅ RefreshToken Redis存储
- ✅ Spring Security集成
- ✅ 自动配置支持

## 许可证

本项目采用MIT许可证。
