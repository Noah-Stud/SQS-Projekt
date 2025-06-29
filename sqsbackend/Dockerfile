# Stage 1 (BUILD)
FROM gradle:latest AS BUILD
WORKDIR /app
COPY . .
RUN ./gradlew clean
RUN ./gradlew assemble

# Stage 2 (EXCECUTE)
FROM openjdk:21 AS EXCECUTE
WORKDIR /app
COPY --from=BUILD /app/build/libs/sqsbackend-0.0.1-SNAPSHOT.jar /app/sqsbackend-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/sqsbackend-0.0.1-SNAPSHOT.jar"]