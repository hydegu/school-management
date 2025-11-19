# Common JWT Module

## 概述

`common-jwt` 是一个纯净的JWT工具模块，提供JWT令牌的创建、解析和验证功能。该模块**零Spring依赖**，可以在Servlet和WebFlux环境中无缝使用。

## 设计理念

### 为什么创建这个模块？

在微服务架构中，我们遇到了以下问题：

1. **代码重复**：`common-core` 和 `gateway` 中都包含相同的 `JwtUtils` 代码
2. **依赖冲突**：
   - `common-core` 依赖 `spring-boot-starter-web` (Servlet栈)
   - `gateway` 依赖 `spring-boot-starter-webflux` (WebFlux栈)
   - 两者不能在同一应用中共存

### 解决方案

创建一个**纯净的JWT模块**：
- ✅ 只依赖JWT库 (`jjwt`)
- ✅ 无Spring依赖
- ✅ 线程安全
- ✅ 可在任何环境中使用

## 依赖关系

```
common-jwt (纯净模块)
    ↓ 依赖
jjwt-api
jjwt-impl
jjwt-jackson
slf4j-api (日志接口)
lombok (可选)

✅ 无Spring依赖
✅ 无Servlet/WebFlux依赖
```

## 功能特性

### 1. 双令牌机制

支持 AccessToken 和 RefreshToken 两种令牌类型：

```java
// 创建 AccessToken (15分钟有效期)
String accessToken = JwtUtils.createAccessToken(
    secretKey,
    900000L,
    userId,
    username,
    role
);

// 创建 RefreshToken (7天有效期)
String refreshToken = JwtUtils.createRefreshToken(
    secretKey,
    604800000L,
    userId,
    username
);
```

### 2. 令牌验证

```java
// 解析JWT Token
Claims claims = JwtUtils.parseJWT(secretKey, token);

// 安全解析（返回Optional）
Optional<Claims> optionalClaims = JwtUtils.parseJWTSafely(secretKey, token);

// 验证Token是否有效
boolean isValid = JwtUtils.validateToken(secretKey, token);
```

### 3. 用户信息提取

```java
Claims claims = JwtUtils.parseJWT(secretKey, token);

// 获取用户ID
Long userId = JwtUtils.getUserIdFromClaims(claims);

// 获取角色
String role = JwtUtils.getRoleFromClaims(claims);

// 获取用户名
String username = claims.getSubject();

// 验证令牌类型
boolean isAccessToken = JwtUtils.isTokenType(claims, JwtUtils.TOKEN_TYPE_ACCESS);
```

### 4. 高级功能

```java
// 检查Token是否即将过期
boolean expiringSoon = JwtUtils.isTokenExpiringSoon(
    secretKey,
    token,
    300000L  // 5分钟阈值
);

// 获取Subject
Optional<String> subject = JwtUtils.getSubject(secretKey, token);
```

## 使用方式

### Maven依赖

在需要使用JWT功能的模块中添加依赖：

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-jwt</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 使用示例

#### 示例1：在Servlet环境中使用 (auth-service)

```java
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;

@Service
public class AuthService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String login(String username, String password) {
        // 验证用户名密码...

        // 生成Token
        String accessToken = JwtUtils.createAccessToken(
            secretKey,
            900000L,
            userId,
            username,
            role
        );

        return accessToken;
    }

    public boolean validateToken(String token) {
        return JwtUtils.validateToken(secretKey, token);
    }
}
```

#### 示例2：在WebFlux环境中使用 (gateway)

```java
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange.getRequest());

        try {
            // 验证Token
            Claims claims = JwtUtils.parseJWT(secretKey, token);

            // 提取用户信息
            Long userId = JwtUtils.getUserIdFromClaims(claims);
            String username = claims.getSubject();
            String role = JwtUtils.getRoleFromClaims(claims);

            // 添加到请求头传递给下游服务
            // ...

            return chain.filter(exchange);
        } catch (Exception e) {
            return writeUnauthorizedResponse(exchange.getResponse());
        }
    }
}
```

## 令牌结构

### AccessToken Claims

```json
{
  "tokenType": "access",
  "userId": 123,
  "role": "ADMIN",
  "sub": "admin",
  "iat": 1234567890,
  "exp": 1234568790
}
```

### RefreshToken Claims

```json
{
  "tokenType": "refresh",
  "userId": 123,
  "sub": "admin",
  "iat": 1234567890,
  "exp": 1235172690
}
```

## 常量定义

```java
// 令牌类型
JwtUtils.TOKEN_TYPE_ACCESS   = "access"
JwtUtils.TOKEN_TYPE_REFRESH  = "refresh"

// Claims键名
JwtUtils.CLAIM_TOKEN_TYPE    = "tokenType"
JwtUtils.CLAIM_USER_ID       = "userId"
JwtUtils.CLAIM_ROLE          = "role"
```

## 异常处理

`parseJWTSafely()` 方法会捕获以下异常并返回 `Optional.empty()`：

- `ExpiredJwtException` - Token已过期
- `UnsupportedJwtException` - 不支持的Token格式
- `MalformedJwtException` - Token格式错误
- `SignatureException` - 签名验证失败
- `IllegalArgumentException` - 参数错误

## 线程安全

所有方法都是静态的，且不维护任何可变状态，因此**完全线程安全**。

## 性能优化

- 使用 HS256 算法（对称加密，性能较高）
- 密钥使用 `SecretKey` 对象缓存（建议在业务代码中缓存密钥）
- 支持 Optional 返回值，避免异常处理开销

## 依赖的项目模块

### 使用 common-jwt 的模块

| 模块 | 依赖方式 | 说明 |
|------|---------|------|
| common-core | 直接依赖 | 替代原来的 JwtUtils |
| gateway | 直接依赖 | 替代原来复制的 JwtUtils |
| auth-service | 通过 common-core 间接依赖 | JWT生成 |
| business-service | 通过 common-core 间接依赖 | JWT验证 |
| class-service | 通过 common-core 间接依赖 | JWT验证 |

## 迁移指南

### 从 common-core 迁移

如果你之前直接使用 `common-core` 中的 `JwtUtils`：

```java
// 旧代码 (不需要改动)
import com.example.utils.JwtUtils;  // 包路径不变

String token = JwtUtils.createAccessToken(...);
```

**无需修改代码**，因为包路径保持一致。

### 从 gateway 独立版本迁移

如果你之前在 gateway 中复制了 `JwtUtils`：

```java
// 旧代码
import com.example.gateway.utils.JwtUtils;

// 新代码
import com.example.utils.JwtUtils;
```

只需修改 import 语句即可。

## 配置要求

### JWT密钥配置

所有使用JWT的服务必须使用**相同的密钥**：

```yaml
# application.yml 或 Nacos配置
jwt:
  secret-key: your-super-secret-key-must-be-at-least-256-bits-long
```

⚠️ **重要**：
- 密钥长度至少32字符（256位）
- 所有服务的密钥必须完全一致
- 生产环境使用环境变量或密钥管理服务

## 最佳实践

### 1. 密钥管理

```java
// ❌ 不要硬编码
String secret = "my-secret-key";

// ✅ 使用配置
@Value("${jwt.secret-key}")
private String secretKey;

// ✅ 或使用环境变量
String secretKey = System.getenv("JWT_SECRET_KEY");
```

### 2. 异常处理

```java
// ❌ 不推荐
try {
    Claims claims = JwtUtils.parseJWT(secretKey, token);
} catch (Exception e) {
    // 处理异常
}

// ✅ 推荐
Optional<Claims> optionalClaims = JwtUtils.parseJWTSafely(secretKey, token);
if (optionalClaims.isPresent()) {
    Claims claims = optionalClaims.get();
    // 处理claims
} else {
    // Token无效
}
```

### 3. 令牌类型验证

```java
Claims claims = JwtUtils.parseJWT(secretKey, token);

// 验证令牌类型
if (!JwtUtils.isTokenType(claims, JwtUtils.TOKEN_TYPE_ACCESS)) {
    throw new UnauthorizedException("令牌类型不正确");
}
```

### 4. Token刷新检查

```java
// 检查是否需要刷新
if (JwtUtils.isTokenExpiringSoon(secretKey, token, 300000L)) {
    // Token将在5分钟内过期，建议刷新
    String newToken = refreshToken(...);
}
```

## 故障排查

### 问题1：Token验证失败

**现象**：`parseJWT()` 抛出 `SignatureException`

**原因**：密钥不一致

**解决**：检查所有服务的 `jwt.secret-key` 配置是否完全一致

### 问题2：类找不到

**现象**：`ClassNotFoundException: com.example.utils.JwtUtils`

**原因**：未添加 `common-jwt` 依赖

**解决**：在 pom.xml 中添加依赖

### 问题3：依赖冲突

**现象**：Maven编译报错，提示依赖冲突

**原因**：可能同时依赖了多个版本的 jjwt 库

**解决**：检查 `<dependencyManagement>` 统一版本

## 版本历史

### v0.0.1-SNAPSHOT
- ✅ 初始版本
- ✅ 支持双令牌机制
- ✅ 零Spring依赖
- ✅ 线程安全
- ✅ 支持Servlet和WebFlux环境

## 许可证

与主项目保持一致

## 维护者

School Management System Team

---

**注意**：本模块是项目重构的一部分，旨在消除代码重复并避免依赖冲突。如有问题，请联系项目维护团队。
