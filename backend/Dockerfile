# ---------- Build stage ----------
FROM gradle:8.5.0-jdk21-alpine AS builder
WORKDIR /app
COPY backend/ /app
RUN gradle clean bootJar --no-daemon
RUN ls -al /app/build/libs
# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar
CMD ["java", "-jar", "app.jar"]