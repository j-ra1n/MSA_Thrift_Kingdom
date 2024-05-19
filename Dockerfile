FROM openjdk:17-slim
VOLUME /tmp
EXPOSE 8080
COPY build/libs/bb-server-0.0.1-SNAPSHOT.jar bb-server.jar
ENTRYPOINT ["java","-jar","bb-server.jar"]