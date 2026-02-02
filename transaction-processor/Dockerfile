FROM ubuntu:latest
LABEL authors="pdantasi"

FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/service-user-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
