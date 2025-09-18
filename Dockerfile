# Stage 1: Build app (nếu dùng Maven/Gradle trong container)
# Nếu bạn đã build sẵn file jar thì có thể bỏ stage này
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run app
FROM eclipse-temurin:21-jdk
WORKDIR /app

# copy file jar từ stage build sang
COPY --from=build /app/target/*.jar app.jar

# cho phép Render đọc PORT từ biến môi trường
EXPOSE 8080
ENV PORT=8080

ENTRYPOINT ["java","-jar","app.jar"]
