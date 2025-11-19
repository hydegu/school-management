# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 配置Maven使用阿里云镜像加速
RUN mkdir -p /root/.m2 && \
    echo '<?xml version="1.0" encoding="UTF-8"?>' > /root/.m2/settings.xml && \
    echo '<settings>' >> /root/.m2/settings.xml && \
    echo '  <mirrors>' >> /root/.m2/settings.xml && \
    echo '    <mirror>' >> /root/.m2/settings.xml && \
    echo '      <id>aliyun</id>' >> /root/.m2/settings.xml && \
    echo '      <mirrorOf>central</mirrorOf>' >> /root/.m2/settings.xml && \
    echo '      <name>Aliyun Maven Mirror</name>' >> /root/.m2/settings.xml && \
    echo '      <url>https://maven.aliyun.com/repository/public</url>' >> /root/.m2/settings.xml && \
    echo '    </mirror>' >> /root/.m2/settings.xml && \
    echo '  </mirrors>' >> /root/.m2/settings.xml && \
    echo '</settings>' >> /root/.m2/settings.xml

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
COPY auth-service/src ./auth-service/src
COPY business-service/src ./business-service/src
COPY class-service/src ./class-service/src
COPY student-service/src ./student-service/src
COPY teacher-service/src ./teacher-service/src
COPY workflow-service/src ./workflow-service/src
COPY gateway/src ./gateway/src

# 构建student-service
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
