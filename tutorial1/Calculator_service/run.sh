#!/bin/bash
NAME="cs3"
docker build -t calculator_service . && docker run --network="host" -e REMOTE_HOST="localhost:8000" -it calculator_service


