# ignore vulnerabilities here.
FROM gradle:8.13.0-jdk21-alpine AS build

WORKDIR /app
COPY --chown=gradle:gradle . .

RUN gradle build --no-daemon

FROM eclipse-temurin:24-alpine

WORKDIR /app
COPY ../.env /app/.env
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
