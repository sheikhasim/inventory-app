## Fetching latest version of Java
#FROM openjdk:17
#
## Setting up work directory
##WORKDIR /app
##
### Copy the jar file into our app
###COPY ./target/Inventory-23.1.0.jar /app
##
### Exposing port 8080
###EXPOSE 8080
##
### Starting the application
###CMD ["java", "-jar", "Inventory-23.1.0.jar"]
##
##ARG JAR_FILE
##COPY ${JAR_FILE} app.jar
##ENTRYPOINT ["java","-jar","/app.jar"]
#
#EXPOSE 8080
#ADD target/inventory-docker.jar inventory-docker.jar
#ENTRYPOINT ["java","-jar","/inventory-docker.jar"]

FROM openjdk:17
ADD target/Inventory-23.1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]