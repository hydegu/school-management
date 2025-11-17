# Common 模块

通用模块，包含项目中共享的工具类、异常处理、配置等。

## 模块结构

```
common/
├── common-core/       # 核心工具类和基础设施
├── common-api/        # API 定义和 DTO
└── common-security/   # 安全相关配置和工具
```

## 子模块说明

### common-core

核心模块，包含：

- **工具类 (utils)**
  - `StringUtils` - 字符串处理工具
  - `CollectionUtils` - 集合操作工具
  - `DateUtils` - 日期时间工具
  - `CodeUtils` - 验证码生成工具
  - `IpUtils` - IP 地址工具
  - `JwtUtils` - JWT 工具
  - `SecurityUtils` - Spring Security 工具
  - `R` - 统一响应结果类

- **异常处理 (exception)**
  - `GlobalExceptionHandler` - 全局异常处理器
  - `ApiException` - API 异常
  - `BusinessException` - 业务异常
  - `ResourceNotFoundException` - 资源未找到异常
  - `UnauthorizedException` - 未授权异常
  - `ForbiddenException` - 权限不足异常
  - `ValidationException` - 参数验证异常

- **基础类 (base)**
  - `BaseEntity` - 实体基类（包含 createdAt、updatedAt、deleted 字段）

- **常量 (constants)**
  - `CommonConstants` - 通用常量
  - `CacheConstants` - 缓存常量

- **枚举 (enums)**
  - `ResultCode` - 响应状态码枚举
  - `UserStatus` - 用户状态枚举
  - `Gender` - 性别枚举

- **处理器 (handler)**
  - `MyMetaObjectHandler` - MyBatis Plus 字段自动填充处理器

- **配置 (config)**
  - `MybatisPlusConfig` - MyBatis Plus 配置
  - `RedisConfig` - Redis 配置
  - `WebJacksonConfig` - Jackson 配置

### common-api

API 模块，用于定义：
- 共享的 DTO (Data Transfer Object)
- 接口定义
- API 契约

### common-security

安全模块，包含：
- JWT 认证过滤器
- Spring Security 配置
- 认证和授权相关工具

## 依赖说明

### common-core 依赖

```xml
<!-- Spring Boot 核心依赖 -->
- spring-boot-starter-web
- spring-boot-starter-validation
- spring-boot-starter-data-redis
- spring-boot-starter-security

<!-- 持久层 -->
- mybatis-plus-spring-boot3-starter

<!-- JWT -->
- jjwt-api
- jjwt-impl (runtime)
- jjwt-jackson (runtime)

<!-- 工具 -->
- lombok
```

### common-security 依赖

```xml
<!-- 内部模块 -->
- common-core

<!-- Spring Security -->
- spring-boot-starter-security

<!-- JWT -->
- jjwt-api
- jjwt-impl
- jjwt-jackson
```

### common-api 依赖

```xml
<!-- 内部模块 -->
- common-core

<!-- 工具 -->
- lombok
```

## 使用方法

### 1. 在其他模块中引入

```xml
<!-- 引入 common-core -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-core</artifactId>
    <version>${project.version}</version>
</dependency>

<!-- 引入 common-security（如需要） -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-security</artifactId>
    <version>${project.version}</version>
</dependency>

<!-- 引入 common-api（如需要） -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-api</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 使用统一响应

```java
@RestController
public class UserController {

    @GetMapping("/users/{id}")
    public R<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.notFound("用户不存在");
        }
        return R.ok(user);
    }

    @PostMapping("/users")
    public R<Void> createUser(@Valid @RequestBody UserDTO dto) {
        userService.create(dto);
        return R.ok("创建成功");
    }
}
```

### 3. 使用工具类

```java
// 字符串工具
String email = "user@example.com";
boolean isValid = StringUtils.isEmail(email);
String masked = StringUtils.maskEmail(email); // us***@example.com

// 日期工具
LocalDateTime now = DateUtils.nowDateTime();
String formatted = DateUtils.formatDateTime(now);
LocalDate firstDay = DateUtils.getFirstDayOfMonth(LocalDate.now());

// 集合工具
List<Integer> list1 = Arrays.asList(1, 2, 3);
List<Integer> list2 = Arrays.asList(2, 3, 4);
List<Integer> intersection = CollectionUtils.intersection(list1, list2); // [2, 3]

// JWT 工具
String token = JwtUtils.createJwt(secretKey, ttl, claims);
Optional<Claims> claims = JwtUtils.parseJWTSafely(secretKey, token);

// Security 工具
Optional<String> username = SecurityUtils.getCurrentUsername();
boolean hasRole = SecurityUtils.hasRole("ADMIN");
String encoded = SecurityUtils.encodePassword("password");
```

### 4. 使用实体基类

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String email;

    // createdAt, updatedAt, deleted 字段已在 BaseEntity 中定义
}
```

### 5. 抛出业务异常

```java
@Service
public class UserService {

    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户", id);
        }
        return user;
    }

    public void checkPermission(Long userId) {
        if (!hasPermission(userId)) {
            throw new ForbiddenException("您没有权限执行此操作");
        }
    }
}
```

## 注意事项

1. **依赖传递**：引入 `common-security` 或 `common-api` 会自动传递 `common-core` 的依赖
2. **可选依赖**：如果只需要工具类，引入 `common-core` 即可
3. **版本管理**：所有版本在父 pom 的 `dependencyManagement` 中统一管理
4. **自动配置**：MyBatis Plus 字段自动填充需要扫描到 `MyMetaObjectHandler`

## 构建

```bash
# 构建整个 common 模块
mvn clean install

# 只构建特定子模块
mvn clean install -pl common-core
```
