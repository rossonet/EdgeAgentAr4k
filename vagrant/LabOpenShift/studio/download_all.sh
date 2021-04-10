#!/bin/bash

directory=$(pwd)

#wget https://github.com/openshift/okd/releases/download/4.5.0-0.okd-2020-07-29-070316/openshift-client-linux-4.5.0-0.okd-2020-07-29-070316.tar.gz
#wget https://github.com/openshift/okd/releases/download/4.5.0-0.okd-2020-07-29-070316/openshift-install-linux-4.5.0-0.okd-2020-07-29-070316.tar.gz
#wget https://github.com/openshift/okd/releases/download/4.7.0-0.okd-2021-03-28-152009/openshift-client-linux-4.7.0-0.okd-2021-03-28-152009.tar.gz
#wget https://github.com/openshift/okd/releases/download/4.7.0-0.okd-2021-03-28-152009/openshift-install-linux-4.7.0-0.okd-2021-03-28-152009.tar.gz
#wget https://github.com/openshift/okd/releases/download/4.6.0-0.okd-2021-02-14-205305/openshift-client-linux-4.6.0-0.okd-2021-02-14-205305.tar.gz
#wget https://github.com/openshift/okd/releases/download/4.6.0-0.okd-2021-02-14-205305/openshift-install-linux-4.6.0-0.okd-2021-02-14-205305.tar.gz
wget https://github.com/openshift/okd/releases/download/4.5.0-0.okd-2020-10-15-235428/openshift-client-linux-4.5.0-0.okd-2020-10-15-235428.tar.gz
wget https://github.com/openshift/okd/releases/download/4.5.0-0.okd-2020-10-15-235428/openshift-install-linux-4.5.0-0.okd-2020-10-15-235428.tar.gz
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/32.20200715.3.0/x86_64/fedora-coreos-32.20200715.3.0-metal.x86_64.raw.xz
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/32.20200715.3.0/x86_64/fedora-coreos-32.20200715.3.0-metal.x86_64.raw.xz.sig
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/32.20200715.3.0/x86_64/fedora-coreos-32.20200715.3.0-live-initramfs.x86_64.img
wget https://builds.coreos.fedoraproject.org/prod/streams/stable/builds/32.20200715.3.0/x86_64/fedora-coreos-32.20200715.3.0-live-kernel-x86_64

#tar -xzf openshift-client-linux-4.5.0-0.okd-2020-07-29-070316.tar.gz
#tar -xzf openshift-install-linux-4.5.0-0.okd-2020-07-29-070316.tar.gz
#tar -xzf openshift-client-linux-4.7.0-0.okd-2021-03-28-152009.tar.gz
#tar -xzf openshift-install-linux-4.7.0-0.okd-2021-03-28-152009.tar.gz
#tar -xzf openshift-client-linux-4.6.0-0.okd-2021-02-14-205305.tar.gz
#tar -xzf openshift-install-linux-4.6.0-0.okd-2021-02-14-205305.tar.gz
tar -xzf openshift-client-linux-4.5.0-0.okd-2020-10-15-235428.tar.gz
tar -xzf openshift-install-linux-4.5.0-0.okd-2020-10-15-235428.tar.gz

ln -s $directory/oc /usr/local/bin/oc
ln -s $directory/kubectl /usr/local/bin/kubectl
ln -s $directory/openshift-install /usr/local/bin/openshift-install
