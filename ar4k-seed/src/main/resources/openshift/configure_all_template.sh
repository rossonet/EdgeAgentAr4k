#!/bin/bash

# by Andrea Ambrosini (Rossonet scarl)

original_directory=$(pwd)

rm -rf $original_directory/openshift-install-dir
mkdir -p $original_directory/openshift-install-dir
cp $original_directory/install-config.yaml $original_directory/openshift-install-dir/
./openshift-install create manifests --dir=openshift-install-dir/
./openshift-install create ignition-configs --dir=openshift-install-dir/

sudo sed -i 's/^Listen 80$/Listen 8080/' /etc/httpd/conf/httpd.conf
sudo rm -rf /var/www/html/okd4
sudo mkdir -p /var/www/html/okd4
sudo cp -R $original_directory/openshift-install-dir/* /var/www/html/okd4/
sudo cp $original_directory/fedora-coreos-34.20210808.3.0-metal.x86_64.raw.xz /var/www/html/okd4/fcos.raw.xz
sudo cp $original_directory/fedora-coreos-34.20210808.3.0-metal.x86_64.raw.xz.sig /var/www/html/okd4/fcos.raw.xz.sig
sudo rm -rf /var/www/html/ipxe
sudo mkdir -p /var/www/html/ipxe
sudo cp $original_directory/*.ipxe /var/www/html/ipxe/
sudo cp $original_directory/fedora-coreos-34.20210808.3.0-live-initramfs.x86_64.img /var/www/html/ipxe/initramfs
sudo cp $original_directory/fedora-coreos-34.20210808.3.0-live-kernel-x86_64 /var/www/html/ipxe/kernel
sudo cp $original_directory/fedora-coreos-34.20210808.3.0-live-rootfs.x86_64.img /var/www/html/ipxe/rootfs
sudo chown -R apache: /var/www/html/
sudo chmod -R 755 /var/www/html/

sudo systemctl restart httpd.service

sudo cat $original_directory/named.conf > /etc/named.conf
sudo cat $original_directory/named.conf.local > /etc/named/named.conf.local
sudo cat $original_directory/db.ar4k.org.local > /etc/named/db.domain.local
sudo cat $original_directory/db.reverse > /etc/named/db.reverse
sudo chgrp named /etc/named.conf
sudo chgrp named /etc/named/named.conf.local
sudo chgrp named /etc/named/db.domain.local
sudo chgrp named /etc/named/db.reverse

sudo systemctl restart named.service

sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 44111 -j ACCEPT
sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 80 -j ACCEPT
sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 443 -j ACCEPT
sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 8080 -j ACCEPT
sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 6443 -j ACCEPT
sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 22623 -j ACCEPT
sudo iptables -A INPUT -p tcp -m tcp --dport 2049 -j ACCEPT
sudo iptables -A INPUT -p tcp -m tcp --dport 111 -j ACCEPT
sudo iptables -A INPUT -p udp -m udp --dport 2049 -j ACCEPT
sudo iptables -A INPUT -p udp -m udp --dport 111 -j ACCEPT
sudo iptables -A INPUT -p udp --dport 67 -j ACCEPT
sudo iptables -A INPUT -p udp --dport 53 -j ACCEPT
sudo iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 53 -j ACCEPT
sudo iptables-save > /etc/sysconfig/iptables
sudo chmod go-rwx /etc/sysconfig/iptables

sudo systemctl restart iptables.service

sudo cat $original_directory/haproxy.cfg > /etc/haproxy/haproxy.cfg

sudo systemctl restart haproxy.service

sudo cat $original_directory/dhcpd.conf > /etc/dhcp/dhcpd.conf

sudo systemctl restart dhcpd.service