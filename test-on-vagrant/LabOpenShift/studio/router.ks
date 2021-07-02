#version=RHEL8
# X Window System configuration information
xconfig  --startxonboot
# License agreement
eula --agreed
# Use graphical install
# graphical
# Install in text mode
text
# Network information
#network  --bootproto=dhcp --hostname=fep-servizio.staer.com --device=trunk --noipv6 --onboot=on --bondslaves=eth0,eth1 --bondopts=mode=active-backup,miimon=100,fail_over_mac=1
#network  --bootproto=static --device=trunk --noipv6 --ip=10.69.3.1 --netmask=255.255.255.0 --onboot=on --vlanid=19 --interfacename=test
#network  --bootproto=dhcp --device=trunk --noipv6 --onboot=on --vlanid=2 --interfacename=terrestre

url --url="http://mirror.centos.org/centos-8/8/BaseOS/x86_64/os/"
# Run the Setup Agent on first boot
firstboot --disable
# System services
services --enabled="chronyd"
# Keyboard layouts
keyboard --vckeymap=it --xlayouts='it'
# System language
lang it_IT.UTF-8
selinux --permissive
firewall --disable

#repo --name="AppStream" --baseurl=file:///run/install/repo/AppStream
#repo --name="Epel" --mirrorlist=https://mirrors.fedoraproject.org/mirrorlist?repo=epel-7&arch=x86_64 --install
# System timezone
timezone Europe/Rome --isUtc
user --groups=wheel --name=rossonet --password=password --plaintext --gecos="rossonet"
# System bootloader configuration
bootloader --location=mbr --append="net.ifnames=0 biosdevname=0"
autopart --type=plain
# Partition clearing information
clearpart --all --initlabel
poweroff

%packages
-biosdevname
kexec-tools
wireshark
nmap
tcpdump
%end

%addon ADDON_placeholder --enable --reserve-mb=auto
%end

%anaconda
pwpolicy root --minlen=6 --minquality=1 --notstrict --nochanges --notempty
pwpolicy user --minlen=6 --minquality=1 --notstrict --nochanges --emptyok
pwpolicy luks --minlen=6 --minquality=1 --notstrict --nochanges --notempty
%end

%post
echo "first install $(date)" > /root/installed_date.txt
%end

