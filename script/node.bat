@echo on

echo "Please specify value of hub URL in the nodeConfigForWindow.json file"

java -jar kata-server.jar -role node -nodeConfig nodeConfigForWindow.json
