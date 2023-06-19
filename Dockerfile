FROM openjdk:17-jdk-alpine

RUN mkdir -p /opt/app
WORKDIR /opt/app

ARG JAR_FILE
COPY ./${JAR_FILE} /opt/app

ENTRYPOINT java -jar /opt/app/*.jar
