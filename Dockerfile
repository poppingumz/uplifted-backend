FROM openjdk:17
WORKDIR /app
COPY build/libs/uplifted-learning-platform-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
