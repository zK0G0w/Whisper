# Dockerfile for Whisper project (JDK 1.8)
FROM eclipse-temurin:8-jre-jammy AS base

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/Whisper-0.0.1.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
