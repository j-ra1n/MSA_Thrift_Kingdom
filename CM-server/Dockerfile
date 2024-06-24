FROM openjdk:17-slim
VOLUME /tmp
EXPOSE 8080
COPY build/libs/cm-server-0.0.1-SNAPSHOT.jar cm-server.jar
ENTRYPOINT ["java","-jar","cm-server.jar"]