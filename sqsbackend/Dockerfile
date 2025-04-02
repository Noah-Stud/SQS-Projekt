# Use a base image with Java installed
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Java application JAR file into the container
COPY build/libs/sqsbackend-0.0.1-SNAPSHOT.jar /app/sqsbackend-0.0.1-SNAPSHOT.jar

# Command to run the Java application
CMD ["java", "-jar", "/app/sqsbackend-0.0.1-SNAPSHOT.jar"]