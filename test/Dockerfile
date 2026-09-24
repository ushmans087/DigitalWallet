FROM eclipse-temurin:25-jre
WORKDIR /app
COPY target/auth-server.jar auth-server.jar
ENTRYPOINT ["java","-jar","auth-server.jar"]