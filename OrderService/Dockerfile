# Use a Java base image
FROM openjdk:17-jdk-slim

# Argument to specify the JAR file
ARG JAR_FILE

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

EXPOSE 8081
# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
