# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy the entire project
COPY . /app

# Build the specific service
WORKDIR /app/OrderService
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/OrderService/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]