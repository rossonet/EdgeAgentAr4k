#!/bin/bash
# by Ambrosini
clear;clear
#./gradlew clean
#./gradlew ar4kBootJarDruido
#./gradlew ar4kDockerContainerDruido

echo "Publica su Harari"
docker save org.ar4k.agent/ar4k-agent-druido:0.7.1 > ar4k-agent-druido.tar && \
echo "Publica su Harari -comprime-"
gzip -9 ar4k-agent-druido.tar && \
echo "Publica su Harari -invia-"
scp ar4k-agent-druido.tar.gz root@192.168.1.12:~/ && \
rm ar4k-agent-druido.tar.gz && \
echo "Publica su Harari -registra in docker-"
ssh 192.168.1.12 -l root "gzip -fd ar4k-agent-druido.tar.gz && cat ar4k-agent-druido.tar | docker load && rm ar4k-agent-druido.tar"


echo "Publica jar singolo in gambe, harari, odroid, tavolo, monitor, carrello, laboratorio e materia101"
echo monitor
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar pi@192.168.1.7:~/
echo laboratorio
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar root@192.168.1.11:~/
echo gambe
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar pi@192.168.1.4:~/
echo odroid
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar root@192.168.0.152:~/
echo harari
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar rossonet@192.168.1.12:~/
echo tavolo 
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar pi@192.168.0.106:~/
echo carrello
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar pi@192.168.1.17:~/
echo materia101
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.1.jar pi@192.168.1.19:~/
