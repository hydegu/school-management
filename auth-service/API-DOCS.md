   # Auth Service API 文档

## 概述

**服务名称：** auth-service
**基础路径：** `/api/auth`
**版本：** 1.0.0
**描述：** 用户认证服务，负责用户登录、令牌刷新和登出功能

### 认证机制

本服务使用 **JWT 双令牌机制**：

- **AccessToken（访问令牌）**
  - 生命周期：15分钟（默认）
  - 用途：API 访问认证
  - 传递方式：请求头 `Authorization: Bearer {accessToken}`

- **RefreshToken（刷新令牌）**
  - 生命周期：7天（默认）
  - 用途：刷新 AccessToken
  - 传递方式：支持两种模式
    - **Cookie 模式（推荐）**：自动存储在 HttpOnly Cookie 中，防止 XSS 攻击
    - **JSON 模式**：在响应体中返回，由客户端自行存储

### RefreshToken 模式选择

系统支持两种 RefreshToken 传递模式，由配置项 `jwt.refresh-token.use-cookie` 决定：

| 模式 | 配置值 | RefreshToken 存储位置 | 适用场景 | 安全性 |
|------|--------|---------------------|---------|--------|
| Cookie 模式 | `true` | HttpOnly Cookie | Web 应用 | 高（防 XSS） |
| JSON 模式 | `false` | 响应体（需客户端存储） | 移动应用、跨域 SPA | 中等 |

---

## 通用响应格式

所有接口均使用统一的响应格式 `R<T>`：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { /* 业务数据 */ }
}
```

### 字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 响应状态码（200=成功，其他=失败） |
| msg | String | 响应消息 |
| data | Object | 业务数据（可选，失败时可能为 null） |

### 常见状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（令牌无效或过期） |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 409 | 资源冲突 |
| 500 | 服务器内部错误 |

---

## 接口列表

### 1. 用户登录

**接口地址：** `POST /api/auth/login`

**接口描述：** 用户通过用户名或邮箱登录，成功后返回 AccessToken 和 RefreshToken

#### 请求参数

**Content-Type：** `application/json`

**请求体：**

```json
{
  "identifier": "admin",
  "password": "password123"
}
```

**字段说明：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| identifier | String | 是 | 用户名或邮箱 |
| password | String | 是 | 密码 |

#### 响应示例

**成功响应（Cookie 模式）：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": null,
    "accessTokenExpire": 900000,
    "refreshTokenExpire": null,
    "refreshTokenMode": "cookie"
  }
}
```

**响应头（Cookie 模式）：**

```
Set-Cookie: refreshToken=eyJhbGci...; HttpOnly; Secure; SameSite=Strict; Max-Age=604800; Path=/
```

**成功响应（JSON 模式）：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "accessTokenExpire": 900000,
    "refreshTokenExpire": 604800000,
    "refreshTokenMode": "json"
  }
}
```

**失败响应：**

```json
{
  "code": 401,
  "msg": "用户名或密码错误"
}
```

#### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| accessToken | String | 访问令牌（15分钟有效） |
| refreshToken | String | 刷新令牌（Cookie 模式下为 null） |
| accessTokenExpire | Long | AccessToken 有效期（毫秒） |
| refreshTokenExpire | Long | RefreshToken 有效期（Cookie 模式下为 null） |
| refreshTokenMode | String | RefreshToken 模式（`cookie` 或 `json`） |

#### 错误码

| 状态码 | 说明 |
|--------|------|
| 401 | 用户名或密码错误 |
| 500 | 登录失败（服务器错误） |

---

### 2. 刷新令牌

**接口地址：** `POST /api/auth/refresh`

**接口描述：** 使用 RefreshToken 刷新 AccessToken，延长会话有效期

#### 请求参数

**Content-Type：** `application/json`

**Cookie 模式请求（推荐）：**

请求体为空或不传（RefreshToken 自动从 Cookie 中读取）

```json
{}
```

**请求头：**

```
Cookie: refreshToken=eyJhbGci...
```

**JSON 模式请求：**

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**字段说明：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| refreshToken | String | JSON模式必填 | 刷新令牌（Cookie 模式可不传） |

#### 响应示例

**成功响应（Cookie 模式）：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": null,
    "accessTokenExpire": 900000,
    "refreshTokenExpire": null,
    "refreshTokenMode": "cookie"
  }
}
```

**响应头（Cookie 模式）：**

```
Set-Cookie: refreshToken=eyJhbGci...; HttpOnly; Secure; SameSite=Strict; Max-Age=604800; Path=/
```

**成功响应（JSON 模式）：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "accessTokenExpire": 900000,
    "refreshTokenExpire": 604800000,
    "refreshTokenMode": "json"
  }
}
```

**失败响应：**

```json
{
  "code": 401,
  "msg": "RefreshToken 无效或已过期"
}
```

#### 响应字段说明

同 [用户登录](#响应字段说明)

#### 错误码

| 状态码 | 说明 |
|--------|------|
| 401 | RefreshToken 无效或已过期 |
| 500 | 刷新令牌失败（服务器错误） |

#### 重要说明

1. **角色权限实时更新**：刷新令牌时会从数据库重新获取用户的最新角色信息，确保权限变更立即生效
2. **RefreshToken 不含角色字段**：RefreshToken 中不包含 `role` 字段（设计如此），角色信息仅存在于 AccessToken 中
3. **令牌轮换**：每次刷新都会生成新的 RefreshToken（可选配置）

---

### 3. 用户登出

**接口地址：** `POST /api/auth/logout`

**接口描述：** 用户登出，清除 RefreshToken 并使其失效

#### 请求参数

**Content-Type：** `application/json`

**请求体：**

```json
{
  "userId": 1
}
```

**字段说明：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

#### 响应示例

**成功响应：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": "登出成功"
}
```

**响应头（Cookie 模式）：**

Cookie 会被清除：

```
Set-Cookie: refreshToken=; Max-Age=0; Path=/
```

**失败响应：**

```json
{
  "code": 500,
  "msg": "登出失败"
}
```

#### 错误码

| 状态码 | 说明 |
|--------|------|
| 500 | 登出失败（服务器错误） |

#### 说明

1. 登出后，用户的 RefreshToken 会从 Redis 中删除，无法再用于刷新 AccessToken
2. Cookie 模式下，浏览器中的 RefreshToken Cookie 会被清除
3. AccessToken 仍然有效，直到过期（建议客户端主动清除）

---

## 客户端集成指南

### Cookie 模式（推荐）

**适用场景：** Web 应用（同域或允许携带凭证的跨域）

**登录流程：**

```javascript
// 1. 登录
const response = await fetch('http://gateway/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  credentials: 'include', // 重要：允许携带 Cookie
  body: JSON.stringify({
    identifier: 'admin',
    password: 'password123'
  })
});

const result = await response.json();
if (result.code === 200) {
  const { accessToken } = result.data;
  // 存储 AccessToken（localStorage 或内存）
  localStorage.setItem('accessToken', accessToken);
  // RefreshToken 已自动存储在 HttpOnly Cookie 中，无需处理
}
```

**刷新令牌：**

```javascript
// 2. 刷新令牌（AccessToken 即将过期时）
const response = await fetch('http://gateway/api/auth/refresh', {
  method: 'POST',
  credentials: 'include', // 重要：自动携带 RefreshToken Cookie
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({}) // 空请求体
});

const result = await response.json();
if (result.code === 200) {
  const { accessToken } = result.data;
  localStorage.setItem('accessToken', accessToken);
}
```

**API 请求：**

```javascript
// 3. 访问受保护的 API
const response = await fetch('http://gateway/api/students', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
  }
});
```

**登出：**

```javascript
// 4. 登出
const userId = 1; // 从用户信息中获取
const response = await fetch('http://gateway/api/auth/logout', {
  method: 'POST',
  credentials: 'include',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ userId })
});

if (response.ok) {
  localStorage.removeItem('accessToken');
  // RefreshToken Cookie 已被服务器清除
}
```

---

### JSON 模式

**适用场景：** 移动应用、无法使用 Cookie 的跨域场景

**登录流程：**

```javascript
// 1. 登录
const response = await fetch('http://gateway/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    identifier: 'admin',
    password: 'password123'
  })
});

const result = await response.json();
if (result.code === 200) {
  const { accessToken, refreshToken } = result.data;
  // 存储两个令牌（建议使用安全存储，如 Keychain/Keystore）
  localStorage.setItem('accessToken', accessToken);
  localStorage.setItem('refreshToken', refreshToken); // 注意：localStorage 不够安全
}
```

**刷新令牌：**

```javascript
// 2. 刷新令牌
const refreshToken = localStorage.getItem('refreshToken');
const response = await fetch('http://gateway/api/auth/refresh', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ refreshToken })
});

const result = await response.json();
if (result.code === 200) {
  const { accessToken, refreshToken: newRefreshToken } = result.data;
  localStorage.setItem('accessToken', accessToken);
  localStorage.setItem('refreshToken', newRefreshToken);
}
```

**API 请求和登出：** 同 Cookie 模式

---

## 安全建议

1. **生产环境配置**
   - 启用 Cookie 模式：`jwt.refresh-token.use-cookie: true`
   - 启用 HTTPS：`jwt.refresh-token.cookie-secure: true`
   - 设置 SameSite：`jwt.refresh-token.cookie-same-site: Strict`

2. **令牌存储**
   - AccessToken：可存储在内存或 localStorage
   - RefreshToken（JSON 模式）：避免存储在 localStorage，建议使用更安全的存储机制

3. **HTTPS 强制**
   - 生产环境必须使用 HTTPS，防止令牌被窃取

4. **令牌过期处理**
   - 客户端应监听 401 错误，自动调用刷新接口
   - 如果刷新失败，引导用户重新登录

5. **XSS 防护**
   - Cookie 模式下，RefreshToken 存储在 HttpOnly Cookie 中，JavaScript 无法访问
   - 前端应对用户输入进行转义，防止 XSS 攻击

---

## 常见问题

### Q1: RefreshToken 中为什么没有角色信息？

**A:** 这是设计决策。RefreshToken 不包含 `role` 字段，每次刷新时从数据库实时获取最新角色信息，确保权限变更立即生效。详见 `auth-service/AuthServiceImpl.java:88-91`。

### Q2: Cookie 模式和 JSON 模式可以动态切换吗？

**A:** 不建议在运行时切换。模式由配置文件决定，切换需要重启服务并确保客户端适配。

### Q3: AccessToken 过期后如何自动刷新？

**A:** 建议在前端实现 HTTP 拦截器：

```javascript
// Axios 拦截器示例
axios.interceptors.response.use(
  response => response,
  async error => {
    if (error.response?.status === 401) {
      // 尝试刷新令牌
      const refreshed = await refreshToken();
      if (refreshed) {
        // 重试原请求
        return axios.request(error.config);
      } else {
        // 跳转登录页
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);
```

### Q4: 如何强制用户下线（踢出）？

**A:** 调用 `/api/auth/logout` 接口删除用户的 RefreshToken。用户的 AccessToken 仍然有效（最多15分钟），如需立即生效，可实现 AccessToken 黑名单机制。

---

## 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|---------|
| 1.0.0 | 2025-11-24 | 初始版本 |

---

## 联系方式

如有问题，请联系开发团队或查阅项目文档：
- 项目地址：https://github.com/your-org/school-management
- 安全文档：`common/common-security/SECURITY-IMPROVEMENTS.md`
