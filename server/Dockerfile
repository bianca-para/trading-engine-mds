FROM openjdk:21-jdk-slim

WORKDIR /app

# (opțional dar util pe Windows pentru probleme CRLF)
RUN apt-get update && apt-get install -y dos2unix

# Copiem mvnw, pom.xml și config Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN dos2unix mvnw && chmod +x mvnw

# Preluăm dependențele
RUN ./mvnw dependency:go-offline

# Copiem codul sursă
COPY src ./src

# Build cu skipTests
RUN ./mvnw clean package -DskipTests

# Rulează .jar-ul generat
ENTRYPOINT ["java", "-jar", "target/server-0.0.1-SNAPSHOT.jar"]
