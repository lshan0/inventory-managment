# Use an official OpenJDK image as a base
FROM openjdk:17
COPY consumer/target/consumer-1.0-SNAPSHOT.jar /app/consumer.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "consumer.jar"]
