#!/bin/bash
#sudo docker build -t add_test . && sudo docker run --network="host" -it add_test

sudo docker run --network="host" -v `pwd`:/app -it cif_training test_add_numbers.py
