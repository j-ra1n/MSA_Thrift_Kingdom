FROM openjdk:17-slim

WORKDIR /app

# Build 단계에서 생성된 JAR 파일을 복사
COPY ./build/libs/auth-0.0.1-SNAPSHOT.jar /app/auth-server.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/auth-server.jar"]
