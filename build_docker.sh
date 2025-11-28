#!/bin/zsh
# Docker build script for Whisper project

# Configuration
IMAGE_NAME="zkogow/whisper"  # Replace with your Docker Hub username
VERSION="0.0.2"
PLATFORMS="linux/amd64,linux/arm64"

# Build the project first
echo "Building Whisper project with Maven..."
mvn clean package -Dmaven.test.skip=true

# Build and push multi-architecture Docker image
echo "Building Docker image for both AMD64 and ARM64..."
docker buildx create --use
docker buildx build --platform ${PLATFORMS} \
    -t ${IMAGE_NAME}:${VERSION} \
    -t ${IMAGE_NAME}:latest \
    --push .
