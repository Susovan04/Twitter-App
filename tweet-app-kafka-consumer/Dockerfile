FROM openjdk:8-jdk-alpine
ADD target/tweet-app-kafka-consumer-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]