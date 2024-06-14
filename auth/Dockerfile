# 베이스 이미지 선택
FROM openjdk:17-slim
VOLUME /tmp
EXPOSE 8080
# 애플리케이션의 jar 파일과 설정 파일을 Docker 이미지 안에 복사
COPY ./build/libs/auth-0.0.1-SNAPSHOT.jar /app/auth-server.jar
ENTRYPOINT ["java","-jar","cm-server.jar"]
