FROM openjdk:11-jdk
ARG JAR_FILE=builds/libs/*.jar
COPY ${JAR_FILE} preOnBoardingTask-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]