# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 配置Maven使用阿里云镜像加速

# 复制完整的项目结构
COPY pom.xml ./
COPY common ./common
COPY auth-service/pom.xml ./auth-service/pom.xml
COPY business-service/pom.xml ./business-service/pom.xml
COPY class-service/pom.xml ./class-service/pom.xml
COPY student-service/pom.xml ./student-service/pom.xml
COPY teacher-service/pom.xml ./teacher-service/pom.xml
COPY workflow-service/pom.xml ./workflow-service/pom.xml
COPY gateway/pom.xml ./gateway/pom.xml

# 复制源代码
COPY gateway/src ./gateway/src

# 构建gateway
RUN mvn clean package -pl gateway -am -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 安装curl用于健康检查
RUN apk add --no-cache curl

# 从构建阶段复制JAR文件
COPY --from=builder /app/gateway/target/*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
