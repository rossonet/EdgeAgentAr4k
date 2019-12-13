#!/bin/bash
# by Ambrosini
clear;clear
./gradlew ar4kBootJarDruido

echo odroid
ssh root@192.168.0.152  "systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar root@192.168.0.152:~/
#ssh root@192.168.0.152  "systemctl start ar4k"
ssh root@192.168.0.152  "rm -f druido.keystore"

