#!/bin/bash
export LC_ALL=en_US.UTF-8
export LANGUAGE=en_US.UTF-8
LOW_BOUND=49152
RANGE=16384
while true; do
  CANDIDATE=$[$LOW_BOUND + ($RANDOM % $RANGE)]
  (echo "" >/dev/tcp/127.0.0.1/${CANDIDATE}) >/dev/null 2>&1
  if [ $? -ne 0 ]; then
    break
  fi
done
java -jar /opt/rossonet/ar4k-agent --logging.file=/home/rossonet/console.log --logging.level.root=ERROR --server.port=$CANDIDATE --ar4k.consoleOnly=true
