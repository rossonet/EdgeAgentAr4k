#!/bin/bash

original_directory=$(pwd)

rm -rf $original_directory/openshift-install-dir
mkdir -p $original_directory/openshift-install-dir
cp $original_directory/install-config.yaml $original_directory/openshift-install-dir/
openshift-install create manifests --dir=openshift-install-dir/
openshift-install create ignition-configs --dir=openshift-install-dir/

rm -rf /var/www/html/okd4
mkdir -p /var/www/html/okd4
cp -R $original_directory/openshift-install-dir/* /var/www/html/okd4/
cp $original_directory/fedora-coreos-32.20200715.3.0-metal.x86_64.raw.xz /var/www/html/okd4/fcos.raw.xz
cp $original_directory/fedora-coreos-32.20200715.3.0-metal.x86_64.raw.xz.sig /var/www/html/okd4/fcos.raw.xz.sig

rm -rf /var/www/html/ipxe
mkdir -p /var/www/html/ipxe
cp $original_directory/*.ipxe /var/www/html/ipxe/
cp $original_directory/fedora-coreos-32.20200715.3.0-live-initramfs.x86_64.img /var/www/html/ipxe/initramfs
cp $original_directory/fedora-coreos-32.20200715.3.0-live-kernel-x86_64 /var/www/html/ipxe/kernel

chown -R apache: /var/www/html/
chmod -R 755 /var/www/html/

cat $original_directory/named.conf > /etc/named.conf
cat $original_directory/named.conf.local > /etc/named/named.conf.local
cat $original_directory/db.ar4k.org.local > /etc/named/db.ar4k.org.local
chgrp named /etc/named.conf
chgrp named /etc/named/named.conf.local
chgrp named /etc/named/db.ar4k.org.local

systemctl restart named.service

cp $original_directory/iptables /etc/sysconfig/iptables
chmod go-rwx /etc/sysconfig/iptables
systemctl restart iptables.service
