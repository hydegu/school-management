# JWT双令牌安全改进说明

## 概述

本文档说明JWT双令牌机制中的两个重要安全改进：
1. ✅ RefreshToken支持HttpOnly Cookie模式（防止XSS攻击）
2. ✅ 修复RefreshToken中role字段的设计问题

---

## 问题1：RefreshToken的HttpOnly Cookie支持

### 背景

**最初实现的问题：**
- RefreshToken通过JSON响应体返回
- 需要客户端使用localStorage/sessionStorage存储
- JavaScript可以访问，容易受到XSS攻击
- 不够安全

### 两种模式对比

#### 模式1：JSON响应模式（use-cookie=false）

```yaml
jwt:
  refresh-token:
    use-cookie: false
```

**登录响应：**
```json
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",  // ⚠️ 在响应体中
    "accessTokenExpire": 900000,
    "refreshTokenExpire": 604800000,
    "refreshTokenMode": "json"
  }
}
```

**优点：**
- 适合前后端分离的SPA应用
- 适合移动应用
- 跨域场景友好
- 客户端灵活控制

**缺点：**
- ❌ JavaScript可访问，容易被XSS窃取
- ❌ 需要客户端妥善存储
- ❌ 安全性较低

---

#### 模式2：HttpOnly Cookie模式（use-cookie=true）✅ 推荐

```yaml
jwt:
  refresh-token:
    use-cookie: true
    cookie-secure: true  # 生产环境必须true
```

**登录响应：**
```json
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGc...",
    "accessTokenExpire": 900000,
    "refreshTokenMode": "cookie"  // RefreshToken在Cookie中
  }
}
```

**响应头（自动设置）：**
```
Set-Cookie: Refresh-Token=eyJhbGc...;
            Path=/;
            HttpOnly;
            Secure;
            Max-Age=604800
```

**优点：**
- ✅ HttpOnly防止JavaScript访问，防XSS攻击
- ✅ Secure确保仅HTTPS传输（生产环境）
- ✅ 浏览器自动管理和发送
- ✅ 安全性高

**缺点：**
- 需要同域或配置CORS
- 需要注意CSRF防护（可通过SameSite解决）

---

### 使用方式

#### 配置开关

```yaml
# application.yml或Nacos配置
jwt:
  refresh-token:
    # 开启HttpOnly Cookie模式（推荐）
    use-cookie: true
    # 生产环境必须开启Secure
    cookie-secure: true
```

#### 登录（自动适配）

```bash
POST /auth/login
Content-Type: application/json

{
  "identifier": "admin",
  "password": "password123"
}
```

**use-cookie=false响应：**
```json
{
  "data": {
    "accessToken": "...",
    "refreshToken": "...",  // 在响应体中
    "refreshTokenMode": "json"
  }
}
```

**use-cookie=true响应：**
```json
{
  "data": {
    "accessToken": "...",
    "refreshTokenMode": "cookie"  // RefreshToken在Cookie中
  }
}
```
+ Set-Cookie响应头自动设置

#### 刷新令牌（自动适配）

**use-cookie=false（从请求体获取）：**
```bash
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGc..."
}
```

**use-cookie=true（从Cookie自动获取）：**
```bash
POST /auth/refresh
Cookie: Refresh-Token=eyJhbGc...  # 浏览器自动发送
```

---

## 问题2：RefreshToken中的role字段设计

### 背景

**最初实现的BUG：**
```java
// TokenService.refreshAccessToken() - 第106行
String role = JwtUtils.getRoleFromClaims(claims);  // ❌ BUG
```

问题：RefreshToken中根本没有role字段，这行代码会返回null！

### 为什么RefreshToken不包含role？

这是**正确的设计**，原因：

1. **RefreshToken生命周期长（7天）**
   - 期间用户角色可能变更
   - 不应该使用7天前的旧role

2. **最小权限原则**
   - RefreshToken的唯一职责：刷新AccessToken
   - 不需要携带业务信息（role）

3. **安全考虑**
   - 减少敏感信息泄露
   - 减小payload大小

### 对比

#### AccessToken（短期，包含role）

```java
public static String createAccessToken(..., String role) {
    claims.put(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS);
    claims.put(CLAIM_USER_ID, userId);
    claims.put(CLAIM_ROLE, role);  // ✅ 包含role
    // ...
}
```

**生命周期：** 15分钟
**用途：** API访问认证
**包含信息：** userId, username, role

#### RefreshToken（长期，不包含role）

```java
public static String createRefreshToken(...) {
    claims.put(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH);
    claims.put(CLAIM_USER_ID, userId);
    // ❌ 不包含role
    // ...
}
```

**生命周期：** 7天
**用途：** 仅用于刷新AccessToken
**包含信息：** userId, username

### 修复方案

**修复前（BUG）：**
```java
public Map<String, Object> refreshAccessToken(String refreshToken) {
    Claims claims = JwtUtils.parseJWT(secretKey, refreshToken);
    String role = JwtUtils.getRoleFromClaims(claims);  // ❌ 返回null
    // ...
}
```

**修复后（✅ 正确）：**
```java
public Map<String, Object> refreshAccessToken(String refreshToken, RoleProvider roleProvider) {
    Claims claims = JwtUtils.parseJWT(secretKey, refreshToken);
    Long userId = JwtUtils.getUserIdFromClaims(claims);

    // ✅ 从数据库获取最新角色
    String role = roleProvider.getRole(userId);

    // 生成新令牌时使用最新的role
    return generateTokens(userId, username, role);
}
```

**调用方式：**
```java
tokenService.refreshAccessToken(refreshToken, userId -> {
    // 从数据库查询用户最新角色
    Role role = userService.selectRolesByUserId(userId.intValue());
    return role != null ? role.getRoleName() : "USER";
});
```

### 优势

1. **保证数据一致性**
   - 刷新令牌时使用用户最新的角色
   - 角色变更立即生效

2. **安全性提升**
   - 管理员修改用户角色后，下次刷新令牌即可生效
   - 不需要等待RefreshToken过期（7天）

3. **符合设计原则**
   - RefreshToken保持最小职责
   - 业务数据从数据库实时获取

---

## 安全建议总结

### 生产环境配置

```yaml
jwt:
  secret-key: "your-strong-secret-key-at-least-256-bits"

  access-token:
    ttl: 900000  # 15分钟（建议5-30分钟）

  refresh-token:
    ttl: 604800000  # 7天（建议7-30天）
    use-cookie: true  # ✅ 强烈推荐
    cookie-secure: true  # ✅ 生产环境必须
```

### 安全清单

- [x] ✅ RefreshToken使用HttpOnly Cookie（防XSS）
- [x] ✅ Cookie的Secure属性开启（仅HTTPS）
- [x] ✅ RefreshToken存储在Redis（支持撤销）
- [x] ✅ 刷新时从数据库获取最新role
- [x] ✅ 令牌类型验证（access/refresh不混用）
- [ ] ⚠️ 考虑添加CSRF Token（如果使用Cookie）
- [ ] ⚠️ 考虑添加设备指纹绑定
- [ ] ⚠️ 监控异常刷新行为

### 攻击防护

| 攻击类型 | 防护措施 |
|---------|---------|
| XSS攻击 | ✅ HttpOnly Cookie + Secure |
| CSRF攻击 | ⚠️ SameSite=Strict/Lax |
| 中间人攻击 | ✅ HTTPS + Secure Cookie |
| 令牌窃取 | ✅ Redis存储 + 撤销机制 |
| 重放攻击 | ✅ JWT过期时间 + nonce |

---

## 迁移指南

### 从JSON模式迁移到Cookie模式

#### 1. 更新服务端配置

```yaml
jwt:
  refresh-token:
    use-cookie: true
    cookie-secure: true  # 生产环境
```

#### 2. 更新客户端代码

**迁移前（JSON模式）：**
```javascript
// 登录
const response = await fetch('/auth/login', {
  method: 'POST',
  body: JSON.stringify({identifier, password})
});
const { accessToken, refreshToken } = await response.json();

// 手动存储
localStorage.setItem('refreshToken', refreshToken);

// 刷新时手动发送
await fetch('/auth/refresh', {
  method: 'POST',
  body: JSON.stringify({refreshToken})
});
```

**迁移后（Cookie模式）：**
```javascript
// 登录
const response = await fetch('/auth/login', {
  method: 'POST',
  credentials: 'include',  // ✅ 关键：允许Cookie
  body: JSON.stringify({identifier, password})
});
const { accessToken } = await response.json();
// RefreshToken自动存储在Cookie中，无需手动处理

// 刷新时自动发送Cookie
await fetch('/auth/refresh', {
  method: 'POST',
  credentials: 'include'  // ✅ 浏览器自动发送Cookie
  // 无需发送refreshToken
});
```

#### 3. CORS配置（如果跨域）

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // ✅ 允许Cookie
        config.addAllowedOrigin("https://yourdomain.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        // ...
    }
}
```

---

## 总结

### 关键改进

1. **HttpOnly Cookie支持**
   - 提供use-cookie配置开关
   - 兼容JSON和Cookie两种模式
   - 显著提升安全性

2. **Role字段设计修复**
   - RefreshToken不包含role（正确设计）
   - 刷新时从数据库获取最新role
   - 确保数据一致性

### 推荐配置

```yaml
jwt:
  refresh-token:
    use-cookie: true        # ✅ 推荐开启
    cookie-secure: true     # ✅ 生产环境必须
```

### 安全等级

| 模式 | 安全等级 | 适用场景 |
|------|---------|---------|
| JSON模式 | ⭐⭐⭐ | 移动应用、跨域SPA |
| HttpOnly Cookie模式 | ⭐⭐⭐⭐⭐ | Web应用、同域/配置CORS |

**生产环境强烈推荐使用HttpOnly Cookie模式！**
