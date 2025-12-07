#!/bin/bash
cd "$(dirname "$0")"
echo "2" | mvn exec:java -Dexec.mainClass="credit.Application" -q 2>&1 | head -300
