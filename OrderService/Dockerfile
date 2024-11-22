FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/app.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
CMD ["java", "-jar", "/app.jar"]
