#!/usr/bin/env bash
echo "====== Deploying to fma namespace ======"
ktmpl $(pwd)/ops/deploy/deployment.yml --parameter imageTag $BUILDKITE_BUILD_NUMBER | kubectl apply -f -