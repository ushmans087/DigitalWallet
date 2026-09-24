FROM eclipse-temurin:25-jre
WORKDIR /app
COPY target/api-gateway.jar api-gateway.jar
ENTRYPOINT ["java","-jar","api-gateway.jar"]