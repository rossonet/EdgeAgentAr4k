#!/bin/bash
# by Ambrosini
clear;clear
./gradlew ar4kBootJarDruido
./gradlew ar4kDockerContainerDruido

echo "Publica su Harari"
docker save org.ar4k.agent/ar4k-agent-druido:0.7.2 > ar4k-agent-druido.tar && \
echo "Publica su Harari -comprime-"
gzip -9 ar4k-agent-druido.tar && \
echo "Publica su Harari -invia-"
scp ar4k-agent-druido.tar.gz root@192.168.1.12:~/ && \
rm ar4k-agent-druido.tar.gz && \
echo "Publica su Harari -registra in docker-"
ssh 192.168.1.12 -l root "gzip -fd ar4k-agent-druido.tar.gz && cat ar4k-agent-druido.tar | docker load && rm ar4k-agent-druido.tar"


echo "Publica jar singolo in gambe, harari, odroid, tavolo, monitor, carrello, laboratorio, oki e materia101"
echo monitor
ssh pi@192.168.1.7  "sudo systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar pi@192.168.1.7:~/
ssh pi@192.168.1.7  "sudo systemctl start ar4k"
ssh pi@192.168.1.7  "rm -f druido.keystore"

echo laboratorio
ssh root@192.168.1.11  "systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar root@192.168.1.11:~/
ssh root@192.168.1.11  "systemctl start ar4k"
ssh root@192.168.1.11  "rm -f druido.keystore"

echo gambe
ssh pi@192.168.1.8  "sudo systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar pi@192.168.1.8:~/
ssh pi@192.168.1.8  "sudo systemctl start ar4k"
ssh pi@192.168.1.8  "rm -f druido.keystore"

echo odroid
ssh root@192.168.0.152  "systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar root@192.168.0.152:~/
ssh root@192.168.0.152  "systemctl start ar4k"
ssh root@192.168.0.152  "rm -f druido.keystore"

echo harari
ssh root@192.168.1.12  "systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar rossonet@192.168.1.12:~/
ssh root@192.168.1.12  "systemctl start ar4k"
ssh rossonet@192.168.1.12  "rm -f druido.keystore"

echo tavolo 
ssh pi@192.168.0.106  "sudo systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar pi@192.168.0.106:~/
ssh pi@192.168.0.106  "sudo systemctl start ar4k"
ssh pi@192.168.0.106  "rm -f druido.keystore"

echo carrello
ssh pi@192.168.1.17  "sudo systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar pi@192.168.1.17:~/
ssh pi@192.168.1.17  "sudo systemctl start ar4k"
ssh pi@192.168.1.17  "rm -f druido.keystore"

echo materia101
ssh pi@192.168.1.19  "sudo systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar pi@192.168.1.19:~/
ssh pi@192.168.1.19  "sudo systemctl start ar4k"
ssh pi@192.168.1.19  "rm -f druido.keystore"

echo on Oki
ssh root@192.168.0.107  "systemctl stop ar4k"
scp ar4k-agent-druido/build/libs/ar4k-agent-druido-0.7.2.jar rossonet@192.168.0.107:~/
ssh root@192.168.0.107  "systemctl start ar4k"
ssh rossonet@192.168.0.107  "rm -f druido.keystore"
