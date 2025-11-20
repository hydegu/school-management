# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制完整的项目结构
COPY pom.xml ./
COPY common ./common
COPY teacher-service/pom.xml ./teacher-service/pom.xml

# 复制源代码
COPY teacher-service/src ./teacher-service/src

# 构建teacher-service
RUN mvn clean package -pl teacher-service -am -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 安装curl用于健康检查
RUN apk add --no-cache curl

# 从构建阶段复制JAR文件
COPY --from=builder /app/teacher-service/target/*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 8084

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
