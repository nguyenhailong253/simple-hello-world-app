#!/usr/bin/env bash

image_tag=${BUILDKITE_BUILD_NUMBER}
image_name=long-hello-world-test

export OWNER_NAME=$(aws ssm get-parameters --names "/fma/long/hello-world-app/owner-name-test" --with-decryption --query "Parameters[*].{
Value:Value}" --output text)

echo "====== Building Docker image for test suite ======"
docker build --build-arg OWNER_NAME -f Dockerfile.test -t ${image_name}:${image_tag} .

echo "====== Running All tests ======"
# Clean up container's file system after container exits
docker run --rm ${image_name}:${image_tag}
