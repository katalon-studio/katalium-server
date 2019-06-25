#!/usr/bin/env bash

set -xe

echo "Please specify value of hub URL in the nodeConfig.json file"

java -jar kata-server.jar -role node -nodeConfig nodeConfig.json