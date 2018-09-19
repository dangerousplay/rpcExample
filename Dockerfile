FROM danny02/graalvm AS builder

WORKDIR /build

COPY . .

RUN ./gradlew fatServerJar

WORKDIR /build/build/libs

#RUN native-image -jar server-fat-1.0-SNAPSHOT.jar

FROM openjdk:8

WORKDIR /app

COPY --from=builder ./build/build/libs/server-fat-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "server-fat-1.0-SNAPSHOT.jar"]

#CMD ["./server-fat-1.0-SNAPSHOT"]

#FROM openjdk:8
#
#WORKDIR /app
#
#COPY --from=builder ./build/build/libs .
#
#ENTRYPOINT["java", "-jar", "server-fat-1.0-SNAPSHOT.jar"]