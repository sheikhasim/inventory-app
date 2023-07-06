FROM adoptopenjdk:17-jdk-hotspot as builder
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM adoptopenjdk:17-jre-hotspot
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
