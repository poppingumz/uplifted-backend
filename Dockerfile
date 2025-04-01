FROM openjdk:17
WORKDIR /app
COPY build/libs/uplifted-learning-platform-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
