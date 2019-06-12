#!/bin/bash
# by Ambrosini
clear;clear
./gradlew clean ar4kBootJarQa && \
scp /mnt/6c685c9b-75dd-4f75-a94b-d4f0ce13b17d/git/EdgeAgentAr4k/ar4k-agent-qa/build/libs/ar4k-agent-qa-0.6.9.jar rossonet@harari.rossonet.net:~/ && \
scp /mnt/6c685c9b-75dd-4f75-a94b-d4f0ce13b17d/git/EdgeAgentAr4k/ar4k-agent-qa/build/libs/ar4k-agent-qa-0.6.9.jar pi@192.168.1.7:~/ && \
/mnt/6c685c9b-75dd-4f75-a94b-d4f0ce13b17d/git/EdgeAgentAr4k/ar4k-agent-qa/build/libs/ar4k-agent-qa-0.6.9.jar

exit 0
