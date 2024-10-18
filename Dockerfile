FROM openjdk:17-jdk-alpine
COPY target/majimabot-0.0.1-SNAPSHOT.jar app.jar
COPY .env.docker .env
ENTRYPOINT ["java","-jar","/app.jar"]