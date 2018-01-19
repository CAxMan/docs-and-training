#!/bin/bash
NAME="cs3"
#docker build -t calculator_service . && docker run --network="host" -e REMOTE_HOST="localhostXY:8000" -it calculator_service

docker build -t calculator_service . && docker run --network="host" -e REMOTE_HOST="localhostXY.com" -it calculator_service
