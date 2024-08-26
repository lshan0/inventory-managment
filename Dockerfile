# Use an official OpenJDK image as a base
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code to the working directory
COPY pom.xml /app
COPY producer /app/producer
COPY consumer /app/consumer

# Package the application using Maven
RUN apt-get update && \
    apt-get install -y maven && \
    mvn -f /app/pom.xml clean package

# Specify the JAR file location
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
