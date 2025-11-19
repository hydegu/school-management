# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制父POM和common模块
COPY pom.xml ./
COPY common ./common

# 复制当前服务
COPY student-service/pom.xml ./student-service/
COPY student-service/src ./student-service/src

# 构建项目（跳过测试以加快构建速度）
RUN mvn clean package -pl student-service -am -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 安装curl用于健康检查
RUN apk add --no-cache curl

# 从构建阶段复制JAR文件
COPY --from=builder /app/student-service/target/*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 8083

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
