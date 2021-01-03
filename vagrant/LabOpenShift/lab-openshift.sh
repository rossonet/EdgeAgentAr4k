#!/bin/bash
#mv /vagrant/crc-linux-amd64.tar.xz .
yum install -y wget
wget https://mirror.openshift.com/pub/openshift-v4/clients/crc/latest/crc-linux-amd64.tar.xz
tar -xJf crc-linux-amd64.tar.xz
cd crc-linux-1.20.0-amd64
mv /vagrant/pull-secret .
sudo chown -R vagrant:vagrant /home/vagrant
su vagrant -c "./crc setup"
su vagrant -c "./crc start -p pull-secret"
su vagrant -c "./crc console --credentials"
