FROM openjdk:21-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y dos2unix

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN dos2unix mvnw && chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/server-0.0.1-SNAPSHOT.jar"]
