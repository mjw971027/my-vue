# ============================================================
# Dockerfile - VUE-BACK 后端服务
# 多阶段构建：先用 Maven 编译打包，再用 JRE 运行
# ============================================================

# ---- 第一阶段：构建 ----
FROM maven:3.8.4-openjdk-8-slim AS build

WORKDIR /build

# 先复制 pom.xml 单独下载依赖（利用 Docker 缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# 复制源码并打包
COPY src ./src
RUN mvn clean package -DskipTests

# ---- 第二阶段：运行 ----
FROM openjdk:8-jre-slim

WORKDIR /app

# 从构建阶段复制 fat jar
COPY --from=build /build/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8081

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
