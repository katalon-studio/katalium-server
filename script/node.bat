@echo on

::set hubURL="http://localhost:4444"
set hubURL=""

if %hubURL%=="" (
    echo "Hub URL is not configured. Please specify value of hubURL variable in the sh file"
) else (
    java -jar kata-server.jar -role node -hub %hubURL%/grid/register -nodeConfig nodeConfigForWindow.json
)
