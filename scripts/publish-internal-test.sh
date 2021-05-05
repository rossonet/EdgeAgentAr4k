#!/bin/bash
# by Ambrosini
clear;clear
./gradlew ar4kBootJarWatson
scp /home/andrea/git/EdgeAgentAr4k/ar4k-agent-watson/build/libs/ar4k-agent-watson-*.jar pi@192.168.0.199:/var/www/html/w/


