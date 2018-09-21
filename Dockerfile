FROM openjdk:8-slim

WORKDIR /build

COPY . .

RUN chmod +x gradlew && ./gradlew fatServerJar

COPY ./build/libs /app

WORKDIR /app

EXPOSE 6379

ENTRYPOINT ["java", "-jar", "-Xmx128m", "server-fat-1.0-SNAPSHOT.jar"]