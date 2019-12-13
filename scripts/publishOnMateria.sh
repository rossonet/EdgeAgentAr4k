#!/bin/bash
# by Ambrosini
clear;clear
./gradlew ar4kBootJarDruido

ssh pi@192.168.1.19  "sudo systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar pi@192.168.1.19:~/
#ssh pi@192.168.1.19  "sudo systemctl start ar4k"
ssh pi@192.168.1.19  "rm -f druido.keystore"
