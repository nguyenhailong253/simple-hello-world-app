#!/usr/bin/env bash

image_tag=${BUILDKITE_BUILD_NUMBER}
ecr_image=945367126992.dkr.ecr.ap-southeast-2.amazonaws.com/long-hello-world-app

echo "====== Log in to ECR ======"
$(aws ecr get-login --no-include-email --region ap-southeast-2)

echo "====== Building Docker image for application ======"
docker build -f Dockerfile.app -t ${ecr_image}:${image_tag} .

echo "====== Pushing Docker image to ECR repository ======"
docker push ${ecr_image}:${image_tag}
