# Use an official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container to /app
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Set the environment variable M2_HOME and M2_REPO
ENV M2_HOME /usr/share/maven
ENV M2_REPO /usr/share/maven/repo

# Run the Maven command to download dependencies
RUN mvn dependency:go-offline

# Copy the application code
COPY src/ src/

# Run the Maven command to build and package the application
RUN mvn clean package -DskipTests=true

# Set the entrypoint to run the Java application
ENTRYPOINT ["java", "-jar", "target/*.jar"]

# Expose the port that the application will use
EXPOSE 8080

# Set the user to use when running the container
USER root

# Set the group to use when running the container
GROUP spring

# Create a new user and group with the same name as the group
RUN addgroup -S spring && adduser -S spring -G spring

# Make sure the spring user has ownership of all files in /app
RUN chown -R spring:spring /app

# Make sure to use the correct permissions for the jar file
RUN chmod 755 target/*.jar
