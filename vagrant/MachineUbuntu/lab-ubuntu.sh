#!/bin/bash

sudo apt install bash gawk sed grep bc coreutils wget binutils default-jdk git -y

echo "clone git repository"
git clone https://github.com/rossonet/EdgeAgentAr4k.git
cd /home/vagrant/EdgeAgentAr4k
echo "build project Debian package"
./gradlew makeDebianDruido
echo "install rpm on system"
sudo apt install ./build/distributions/ar4k-agent-druido_*_all.deb -y
echo "restart and enable agent"
sudo systemctl restart ar4k-druido && sudo systemctl enable ar4k-druido
echo "clean"
sudo chown -R vagrant:vagrant /home/vagrant
