#!/bin/bash

# by Andrea Ambrosini - Rossonet -
echo "add user for ar4k console"
adduser rossonet
usermod -s /opt/rossonet/run-console-ar4k.sh rossonet
echo "pre script completed"
