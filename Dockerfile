#https://springframework.guru/docker-hub-for-spring-boot/
FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
#ARG JAR_FILE=target/*.jar
COPY src/ ./src/
ENTRYPOINT ["java","-jar","/todos.jar"]
