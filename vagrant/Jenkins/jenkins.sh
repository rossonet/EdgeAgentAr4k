#!/bin/bash

sudo dnf install wget -y
sudo wget -O /etc/yum.repos.d/jenkins.repo \
    https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
sudo dnf upgrade -y
sudo dnf install jenkins java-devel -y

sudo /etc/init.d/jenkins start
sudo systemctl daemon-reload
sudo systemctl enable jenkins
sudo systemctl start jenkins

sleep 10

echo "The content of /var/lib/jenkins/secrets/initialAdminPassword is:"
JENKINSPWD=$(sudo cat /var/lib/jenkins/secrets/initialAdminPassword)
echo $JENKINSPWD
