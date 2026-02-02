FROM ubuntu:latest
LABEL authors="pdantasi"

FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/transaction-api-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
