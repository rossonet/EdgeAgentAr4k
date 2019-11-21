#!/bin/bash
# by Ambrosini
clear;clear
./gradlew ar4kBootJarDruido

echo on Oki
ssh root@192.168.0.107  "systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar rossonet@192.168.0.107:~/
ssh root@192.168.0.107  "systemctl start ar4k"
