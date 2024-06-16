#!/bin/bash

# 스크립트 실행 중 오류 발생 시 중지
set -e

# Docker 이미지 이름과 태그 설정
IMAGE_NAME=jjra1n/front-server"
TAG="tag"

# Step 1: Docker 이미지 빌드
echo "Docker 이미지를 빌드 중입니다..."
docker build -t $IMAGE_NAME:$TAG .

# Step 2: Docker 이미지 푸시
echo "Docker 이미지를 푸시 중입니다..."
docker push $IMAGE_NAME:$TAG

echo "Docker 이미지 빌드 및 푸시가 완료되었습니다."
