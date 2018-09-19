#!/usr/bin/env bash

docker build -t tcp-jsonrpc .
kubectl apply -f ./k8s/server-deploy.yaml