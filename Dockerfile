# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a minimal OpenJDK image as the base image for the final container
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage to the final container
COPY --from=builder /app/target/inventory.management-0.0.1-SNAPSHOT.jar ./inventory.management-0.0.1-SNAPSHOT.jar

# Expose the port on which the application will run (if applicable)
EXPOSE 8080

# Set the command to run the application when the container starts
CMD ["java", "-jar", "inventory.management-0.0.1-SNAPSHOT.jar"]
