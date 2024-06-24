FROM openjdk:17-slim
VOLUME /tmp
EXPOSE 8080
COPY build/libs/sb-server-0.0.1-SNAPSHOT.jar sb-server.jar
ENTRYPOINT ["java","-jar","sb-server.jar"]