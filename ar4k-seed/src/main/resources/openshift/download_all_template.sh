#!/bin/bash

# by Andrea Ambrosini (Rossonet scarl)

rm kubectl oc openshift-install openshift-client-* openshift-install-linux* fedora-coreos-3* -f

directory=$(pwd)

wget https://github.com/openshift/okd/releases/download/4.7.0-0.okd-2021-08-22-163618/openshift-client-linux-4.7.0-0.okd-2021-08-22-163618.tar.gz
wget https://github.com/openshift/okd/releases/download/4.7.0-0.okd-2021-08-22-163618/openshift-install-linux-4.7.0-0.okd-2021-08-22-163618.tar.gz
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/34.20210808.3.0/x86_64/fedora-coreos-34.20210808.3.0-metal.x86_64.raw.xz
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/34.20210808.3.0/x86_64/fedora-coreos-34.20210808.3.0-metal.x86_64.raw.xz.sig
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/34.20210808.3.0/x86_64/fedora-coreos-34.20210808.3.0-live-initramfs.x86_64.img
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/34.20210808.3.0/x86_64/fedora-coreos-34.20210808.3.0-live-kernel-x86_64
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/34.20210808.3.0/x86_64/fedora-coreos-34.20210808.3.0-live-rootfs.x86_64.img

tar -xzf openshift-client-linux-4.7.0-0.okd-2021-08-22-163618.tar.gz
tar -xzf openshift-install-linux-4.7.0-0.okd-2021-08-22-163618.tar.gz


wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64
chmod +x jq


sudo rm -f /usr/local/bin/oc
sudo rm -f /usr/local/bin/kubectl
sudo rm -f /usr/local/bin/openshift-install
sudo rm -f /usr/local/bin/jq
sudo ln -s $directory/oc /usr/local/bin/oc
sudo ln -s $directory/kubectl /usr/local/bin/kubectl
sudo ln -s $directory/openshift-install /usr/local/bin/openshift-install
sudo ln -s $directory/jq /usr/local/bin/jq
