FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/ama-desafio-1.0.0-SNAPSHOT.jar desafio-ama-developer-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "desafio-ama-developer-1.0.0.jar"]