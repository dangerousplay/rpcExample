#!/usr/bin/env bash

sudo docker build -t tcp-jsonrpc .
sudo kubectl apply -f ./k8s/server-deploy.yaml