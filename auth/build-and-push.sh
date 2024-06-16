#!/bin/bash
./gradlew build

# Docker 이미지 태그
IMAGE_NAME="jjra1n/auth-server:tag"

# Docker 빌드
echo "Building Docker image..."
docker build -t $IMAGE_NAME .

# Docker 이미지 푸시
echo "Pushing Docker image to Docker Hub..."
docker push $IMAGE_NAME

echo "Done."
