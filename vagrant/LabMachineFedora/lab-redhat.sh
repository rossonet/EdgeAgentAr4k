#!/bin/bash

sudo dnf install wget eclipse-jdt java-1.8.0-openjdk java-1.8.0-openjdk-devel asciinema bash nmap vim-javabrowser vim-syntastic-java vim-syntastic-javascript vim-syntastic-json vim-syntastic-sh gource mkdocs mkdocs-material git vagrant xorg-x11-xauth xterm xorg-x11-apps -y
sudo dnf upgrade -y

echo "install Docker"
sudo dnf remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-selinux docker-engine-selinux docker-engine -y && \
sudo dnf install dnf-plugins-core -y && \
sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo && \
sudo dnf install docker-ce docker-ce-cli containerd.io -y && \
sudo systemctl start docker && \
sudo systemctl enable docker && \
usermod -a -G docker vagrant

echo "create asciinema directory"
mkdir /home/vagrant/asciinema
echo "configure project EdgeAgentAr4k"
asciinema rec -i 2.5 /home/vagrant/asciinema/clone.json -c "git clone https://github.com/rossonet/EdgeAgentAr4k.git"
echo "configure project for Eclipse"
asciinema rec -i 2.5 /home/vagrant/asciinema/eclipse.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew clean cleanEclipse eclipse"
echo "build fat jar"
asciinema rec -i 2.5 /home/vagrant/asciinema/make-jars.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew buildAllBootJars"
echo "build project rpm"
asciinema rec -i 2.5 /home/vagrant/asciinema/make-rpm.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew makeRpmDruido"
echo "install rpm on system"
asciinema rec -i 2.5 /home/vagrant/asciinema/install-rpm.json -c "cd /home/vagrant/EdgeAgentAr4k && sudo dnf localinstall build/distributions/ar4k-agent-druido-*.noarch.rpm -y"
echo "restart and enable agent"
asciinema rec -i 2.5 /home/vagrant/asciinema/enable-service.json -c "sudo systemctl restart ar4k-druido && sudo systemctl enable ar4k-druido"
