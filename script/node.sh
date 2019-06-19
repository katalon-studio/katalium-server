#!/usr/bin/env bash

set -xe

#hubURL=http://localhost:4444
hubURL=

if [[ -z ${hubURL} ]]
then
    echo "Hub URL is not configured. Please specify value of hubURL variable in the sh file"
else
    java -jar kata-server.jar -role node -hub ${hubURL}/grid/register -proxy com.katalon.kata.proxy.KatalonProxy
fi