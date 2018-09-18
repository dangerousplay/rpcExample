FROM danny02/graalvm AS builder

WORKDIR /build

COPY . .

RUN ./gradlew fatServerJar

WORKDIR /build/build/libs

RUN native-image --static -jar server-fat-1.0-SNAPSHOT.jar

FROM alpine

WORKDIR /app

COPY --from=builder ./build/build/libs/server-fat-1.0-SNAPSHOT .

CMD ["./server-fat-1.0-SNAPSHOT"]

#FROM openjdk:8
#
#WORKDIR /app
#
#COPY --from=builder ./build/build/libs .
#
#ENTRYPOINT["java", "-jar", "server-fat-1.0-SNAPSHOT.jar"]