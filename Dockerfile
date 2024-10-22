FROM openjdk:21-jdk-slim
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
COPY src src
RUN chmod +x gradlew
RUN ./gradlew bootJar
EXPOSE 8080
CMD ["java", "-jar", "build/libs/htmldiff-0.0.1-SNAPSHOT.jar"]