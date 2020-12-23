#!/bin/bash

sudo dnf install wget eclipse-jdt eclipse-egit asciinema bash nmap vim-enhanced vim-javabrowser vim-syntastic-java vim-syntastic-javascript vim-syntastic-json vim-syntastic-sh gource mkdocs mkdocs-material git xorg-x11-xauth xterm netpbm-progs qiv xpra xorg-x11-drv-evdev wireshark tcpdump gimp arduino -y

sudo dnf upgrade -y

echo "install STS4, Egit e Gradle plugins in Eclipse"
echo "run eclipse installer"
echo "#!/bin/bash" > /home/vagrant/setup_eclipse.sh
echo 'eclipse -application org.eclipse.equinox.p2.director -r "http://download.eclipse.org/eclipse/updates/4.17,https://download.springsource.com/release/TOOLS/sts4/update/e4.17/,http://download.eclipse.org/releases/2020-09" -installIU "202012132012.Spring Tools,202012132012.Spring Tools - Developer Resources,org.gradle.toolingapi,org.eclipse.egit.feature.group,org.eclipse.buildship.feature.group" -data /home/vagrant/workspace' >> /home/vagrant/setup_eclipse.sh
echo 'cd /home/vagrant/EdgeAgentAr4k && ./gradlew clean cleanEclipse eclipse' >> /home/vagrant/setup_eclipse.sh
chmod +x /home/vagrant/setup_eclipse.sh

echo "install Docker"
sudo dnf remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-selinux docker-engine-selinux docker-engine -y && \
sudo dnf install dnf-plugins-core -y && \
sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo && \
sudo dnf install docker-ce docker-ce-cli containerd.io -y && \
sudo systemctl start docker && \
sudo systemctl enable docker && \
sudo usermod -a -G docker vagrant

echo "create asciinema directory"
mkdir /home/vagrant/asciinema
echo "configure project EdgeAgentAr4k"
asciinema rec -i 2.5 /home/vagrant/asciinema/clone.json -c "git clone https://github.com/rossonet/EdgeAgentAr4k.git"
#echo "configure project for Eclipse"
#asciinema rec -i 2.5 /home/vagrant/asciinema/eclipse.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew clean cleanEclipse eclipse"
echo "build fat jar"
asciinema rec -i 2.5 /home/vagrant/asciinema/make-jars.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew ar4kBootJarDruido"
echo "build docker container"
asciinema rec -i 2.5 /home/vagrant/asciinema/make-docker.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew ar4kDockerContainerDruido"
echo "build project rpm"
asciinema rec -i 2.5 /home/vagrant/asciinema/make-rpm.json -c "cd /home/vagrant/EdgeAgentAr4k && ./gradlew makeRpmDruido"
echo "install rpm on system"
asciinema rec -i 2.5 /home/vagrant/asciinema/install-rpm.json -c "cd /home/vagrant/EdgeAgentAr4k && sudo dnf localinstall build/distributions/ar4k-agent-druido-*.noarch.rpm -y"
echo "restart and enable agent"
asciinema rec -i 2.5 /home/vagrant/asciinema/enable-service.json -c "sudo systemctl restart ar4k-druido && sudo systemctl enable ar4k-druido"

echo "link for git import in Eclipse"
mkdir /home/vagrant/git
ln -s /home/vagrant/EdgeAgentAr4k /home/vagrant/git
echo "clean permission"
sudo chown -R vagrant:vagrant /home/vagrant
