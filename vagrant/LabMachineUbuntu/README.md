# Lab Ubuntu

## requirements

[Vagrant](https://www.vagrantup.com/downloads)

[VirtualBox](https://www.virtualbox.org/wiki/Downloads)

## run vagrant

Run _vagrant up_

```
[andrea@legion-rossonet-com LabMachineUbuntu]$ vagrant up
Bringing machine 'default' up with 'virtualbox' provider...
==> default: Importing base box 'ubuntu/focal64'...
==> default: Matching MAC address for NAT networking...
==> default: Checking if box 'ubuntu/focal64' version '20201210.0.0' is up to date...
==> default: Setting the name of the VM: LabMachineUbuntu_default_1612549124174_1286
==> default: Clearing any previously set network interfaces...
==> default: Preparing network interfaces based on configuration...
    default: Adapter 1: nat
==> default: Forwarding ports...
    default: 22 (guest) => 2222 (host) (adapter 1)
==> default: Running 'pre-boot' VM customizations...
==> default: Booting VM...
==> default: Waiting for machine to boot. This may take a few minutes...
    default: SSH address: 127.0.0.1:2222
    default: SSH username: vagrant
    default: SSH auth method: private key

[...]

    default: Reading state information...
    default: The following additional packages will be installed:
    default:   default-jdk default-jdk-headless default-jre default-jre-headless xbitmaps
    default:   xterm
    default: Suggested packages:
    default:   xfonts-cyrillic
    default: The following NEW packages will be installed:
    default:   ar4k-agent default-jdk default-jdk-headless default-jre default-jre-headless
    default:   xbitmaps xterm
    default: 0 upgraded, 7 newly installed, 0 to remove and 55 not upgraded.
    default: Need to get 799 kB/76.2 MB of archives.
    default: After this operation, 87.3 MB of additional disk space will be used.
    default: Get:1 http://archive.ubuntu.com/ubuntu focal/main amd64 default-jre-headless amd64 2:1.11-72 [3192 B]
    default: Get:2 http://archive.ubuntu.com/ubuntu focal/main amd64 default-jre amd64 2:1.11-72 [1084 B]
    default: Get:3 http://archive.ubuntu.com/ubuntu focal/main amd64 default-jdk-headless amd64 2:1.11-72 [1140 B]
    default: Get:4 http://archive.ubuntu.com/ubuntu focal/main amd64 default-jdk amd64 2:1.11-72 [1096 B]
    default: Get:5 http://archive.ubuntu.com/ubuntu focal/main amd64 xbitmaps all 1.1.1-2 [28.1 kB]
    default: Get:6 http://archive.ubuntu.com/ubuntu focal/universe amd64 xterm amd64 353-1ubuntu1 [765 kB]
    default: Get:7 /home/vagrant/TemplateEdgeAgentAr4k/build/distributions/ar4k-agent_0.0.3-1_all.deb ar4k-agent all 0.0.3-1 [75.4 MB]
    default: dpkg-preconfigure: unable to re-open stdin: No such file or directory
    default: Fetched 799 kB in 1s (812 kB/s)
    default: Selecting previously unselected package default-jre-headless.
    default: (Reading database ... 
(Reading database ... 55%abase ... 5%
    default: (Reading database ... 60%
    default: (Reading database ... 65%
    default: (Reading database ... 70%
    default: (Reading database ... 75%
    default: (Reading database ... 80%
    default: (Reading database ... 85%
    default: (Reading database ... 90%
    default: (Reading database ... 95%
(Reading database ... 65153 files and directories currently installed.)
    default: Preparing to unpack .../0-default-jre-headless_2%3a1.11-72_amd64.deb ...
    default: Unpacking default-jre-headless (2:1.11-72) ...
    default: Selecting previously unselected package default-jre.
    default: Preparing to unpack .../1-default-jre_2%3a1.11-72_amd64.deb ...
    default: Unpacking default-jre (2:1.11-72) ...
    default: Selecting previously unselected package default-jdk-headless.
    default: Preparing to unpack .../2-default-jdk-headless_2%3a1.11-72_amd64.deb ...
    default: Unpacking default-jdk-headless (2:1.11-72) ...
    default: Selecting previously unselected package default-jdk.
    default: Preparing to unpack .../3-default-jdk_2%3a1.11-72_amd64.deb ...
    default: Unpacking default-jdk (2:1.11-72) ...
    default: Selecting previously unselected package xbitmaps.
    default: Preparing to unpack .../4-xbitmaps_1.1.1-2_all.deb ...
    default: Unpacking xbitmaps (1.1.1-2) ...
    default: Selecting previously unselected package xterm.
    default: Preparing to unpack .../5-xterm_353-1ubuntu1_amd64.deb ...
    default: Unpacking xterm (353-1ubuntu1) ...
    default: Selecting previously unselected package ar4k-agent.
    default: Preparing to unpack .../6-ar4k-agent_0.0.3-1_all.deb ...
    default: Unpacking ar4k-agent (0.0.3-1) ...
    default: Setting up default-jre-headless (2:1.11-72) ...
    default: Setting up default-jre (2:1.11-72) ...
    default: Setting up default-jdk-headless (2:1.11-72) ...
    default: Setting up xbitmaps (1.1.1-2) ...
    default: Setting up default-jdk (2:1.11-72) ...
    default: Setting up xterm (353-1ubuntu1) ...
    default: update-alternatives: using /usr/bin/xterm to provide /usr/bin/x-terminal-emulator (x-terminal-emulator) in auto mode
    default: update-alternatives: using /usr/bin/lxterm to provide /usr/bin/x-terminal-emulator (x-terminal-emulator) in auto mode
    default: Setting up ar4k-agent (0.0.3-1) ...
    default: Processing triggers for man-db (2.9.1-1) ...
    default: Processing triggers for mime-support (3.64ubuntu1) ...
    default: restart and enable agent
    default: Created symlink /etc/systemd/system/multi-user.target.wants/ar4k.service → /etc/systemd/system/ar4k.service.
    default: clean

```

## login into new virtual machine

Run _vagrant ssh_

```
[andrea@legion-rossonet-com LabMachineUbuntu]$ vagrant ssh
Welcome to Ubuntu 20.04.1 LTS (GNU/Linux 5.4.0-54-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage

  System information as of Fri Feb  5 19:13:00 UTC 2021

  System load:  1.13               Processes:               112
  Usage of /:   12.9% of 38.71GB   Users logged in:         0
  Memory usage: 33%                IPv4 address for enp0s3: 10.0.2.15
  Swap usage:   0%


58 updates can be installed immediately.
19 of these updates are security updates.
To see these additional updates run: apt list --upgradable


vagrant@ubuntu-test-edge-agent:~$ 

```

## stop the virtual machine

Run _vagrant halt_

```
[andrea@legion-rossonet-com LabMachineUbuntu]$ vagrant halt
==> default: Attempting graceful shutdown of VM...
```

