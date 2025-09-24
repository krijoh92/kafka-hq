#!/bin/bash

set -e

# Registry configuration
REGISTRY="registry.wittyshizard.dev"
PLATFORM="linux/arm64"

# Get the project root directory
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# Parse command line arguments for specific services to build
SERVICES_TO_BUILD=("$@")

# Define all available services
declare -A ALL_SERVICES=(
    ["ace-calculator"]="${PROJECT_ROOT}/ace-calculator/Dockerfile:${PROJECT_ROOT}/ace-calculator"
    ["ace-backend"]="${PROJECT_ROOT}/ace-backend/Dockerfile:${PROJECT_ROOT}/ace-backend"
    ["webapp"]="${PROJECT_ROOT}/Dockerfile.webapp:${PROJECT_ROOT}"
)

# Function to build and push an image
build_and_push() {
    local service_name="$1"
    local dockerfile_path="$2"
    local context_path="$3"
    local image_tag="${REGISTRY}/kafka-hq/${service_name}:latest"

    echo "Building and pushing ${service_name}..."
    echo "  Context: ${context_path}"
    echo "  Dockerfile: ${dockerfile_path}"
    echo "  Image: ${image_tag}"
    echo "  Platform: ${PLATFORM}"

    docker buildx build \
        --platform "${PLATFORM}" \
        --file "${dockerfile_path}" \
        --tag "${image_tag}" \
        --push \
        "${context_path}"

    echo "✅ Successfully built and pushed ${service_name}"
    echo ""
}

# Ensure buildx is available
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed or not in PATH"
    exit 1
fi

if ! docker buildx version &> /dev/null; then
    echo "❌ Docker buildx is not available"
    exit 1
fi

# Create a buildx builder if it doesn't exist
BUILDER_NAME="kafka-hq-builder"
if ! docker buildx inspect "${BUILDER_NAME}" &> /dev/null; then
    echo "Creating buildx builder: ${BUILDER_NAME}"
    docker buildx create --name "${BUILDER_NAME}" --use
else
    docker buildx use "${BUILDER_NAME}"
fi

# If no services specified, build all services
if [ ${#SERVICES_TO_BUILD[@]} -eq 0 ]; then
    SERVICES_TO_BUILD=(ace-calculator ace-backend webapp)
fi

echo "🚀 Building and pushing Docker images to ${REGISTRY}/kafka-hq"
echo "Platform: ${PLATFORM}"
echo "Services to build: ${SERVICES_TO_BUILD[*]}"
echo ""

BUILT_IMAGES=()

# Build specified services
for service in "${SERVICES_TO_BUILD[@]}"; do
    if [[ -n "${ALL_SERVICES[$service]}" ]]; then
        IFS=':' read -r dockerfile_path context_path <<< "${ALL_SERVICES[$service]}"
        build_and_push "$service" "$dockerfile_path" "$context_path"
        BUILT_IMAGES+=("${REGISTRY}/kafka-hq/${service}:latest")
    else
        echo "❌ Unknown service: $service"
        echo "Available services: ${!ALL_SERVICES[*]}"
        exit 1
    fi
done

echo "🎉 All specified images have been successfully built and pushed!"
echo ""
echo "Images pushed:"
for image in "${BUILT_IMAGES[@]}"; do
    echo "  - $image"
done
echo ""
echo "Usage examples:"
echo "  ./build-and-push.sh                    # Build all services"
echo "  ./build-and-push.sh ace-backend        # Build only ace-backend"
echo "  ./build-and-push.sh webapp ace-backend # Build webapp and ace-backend"