FROM openjdk:8-slim

WORKDIR /build

COPY . .

RUN chmod +x gradlew && ./gradlew fatServerJar

COPY ./build/libs /app

WORKDIR /app

ENTRYPOINT ["java", "-jar", "server-fat-1.0-SNAPSHOT.jar"]

EXPOSE 6379