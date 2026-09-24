FROM eclipse-temurin:25-jre
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/discovery-server.jar discovery-server.jar
ENTRYPOINT ["java","-jar","discovery-server.jar"]
