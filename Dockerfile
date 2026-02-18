
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app


COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

COPY src ./src


RUN ./gradlew build -x test --continue





RUN ./gradlew bootJar -x test

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app


COPY --from=build /app/build/libs/*.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
