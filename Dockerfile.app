FROM gradle:5.2.1-jdk11-slim AS BUILD_APP
USER root
WORKDIR /home/gradle/src/
COPY . .
RUN gradle -q build -x test

FROM openjdk:11-slim
EXPOSE 8000
RUN mkdir /app
COPY --from=BUILD_APP /home/gradle/src/build/libs/*.jar /app/
ENTRYPOINT ["java", "-jar", "/app/BasicWebApp-1.0-SNAPSHOT.jar"]
