#!/bin/bash

# 스크립트 실행 중 오류 발생 시 중단
echo "Starting the build and deploy process..."
set -e

# Maven clean build and install
echo "Running Maven clean and install..."
mvn clean install

# Docker Compose down
echo "Stopping and removing existing Docker containers..."
docker compose down

# Docker Compose up with build in detached mode
echo "Building and starting Docker containers in detached mode..."
docker compose up --build -d

# Process complete
echo "Build and deploy process completed successfully."
