FROM openjdk:8-jdk-alpine as builder
WORKDIR /backend/mbotc
COPY . .
RUN chmod +x ./gradlew

RUN ./gradlew clean build

FROM adoptopenjdk:8-jre-hotspot 
COPY --from=builder /backend/mbotc/build/libs/mbotc-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]