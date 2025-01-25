# Base image
FROM openjdk:17-jdk-slim

# Install curl for healthcheck
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Maven build output (jar file) into the container
COPY prescriptionservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application runs on
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]