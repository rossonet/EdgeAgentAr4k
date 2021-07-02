# Lab Fedora

## requirements

[Vagrant](https://www.vagrantup.com/downloads)

[VirtualBox](https://www.virtualbox.org/wiki/Downloads)

## run vagrant

Run _vagrant up_

```
[andrea@legion-rossonet-com LabMachineFedora]$ vagrant up
Bringing machine 'default' up with 'virtualbox' provider...
==> default: Importing base box 'fedora/33-cloud-base'...
==> default: Matching MAC address for NAT networking...
==> default: Checking if box 'fedora/33-cloud-base' version '33.20201019.0' is up to date...
==> default: Setting the name of the VM: LabMachineFedora_default_1612546312510_94012
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
    default: 
    default: Vagrant insecure key detected. Vagrant will automatically replace
    default: this with a newly generated keypair for better security.
    default: 
    default: Inserting generated public key within guest...
    default: Removing insecure key from the guest if it's present...
    default: Key inserted! Disconnecting and reconnecting using new SSH key...
==> default: Machine booted and ready!
==> default: Checking for guest additions in VM...
    default: The guest additions on this VM do not match the installed version of
    default: VirtualBox! In most cases this is fine, but in rare cases it can
    default: prevent things such as shared folders from working properly. If you see
    default: shared folder errors, please make sure the guest additions within the
    default: virtual machine match the version of VirtualBox you have installed on
    default: your host and reload your VM.
    default: 
    default: Guest Additions Version: 6.0.0 r127566
    default: VirtualBox Version: 6.1
==> default: Setting hostname...
==> default: Rsyncing folder: /home/andrea/git/TemplateEdgeAgentAr4k/vagrant/LabMachineFedora/ => /vagrant
==> default: Running provisioner: Set System Locale (shell)...
    default: Running: inline script
==> default: Running provisioner: shell...
    default: Running: /tmp/vagrant-shell20210205-511647-kjfu4t.sh
    default: Fedora 33 openh264 (From Cisco) - x86_64        2.0 kB/s | 2.5 kB     00:01    
    default: Fedora Modular 33 - x86_64                      1.4 MB/s | 3.3 MB     00:02    
    default: Fedora Modular 33 - x86_64 - Updates            2.0 MB/s | 3.0 MB     00:01    
    default: Fedora 33 - x86_64 - Updates                    4.7 MB/s |  23 MB     00:04    
    default: Fedora 33 - x86_64                              5.6 MB/s |  72 MB     00:12    
    default: Package bash-5.0.17-2.fc33.x86_64 is already installed.
    default: Dependencies resolved.
    default: ===============================================================================================
    default:  Package                                Arch    Version                          Repo      Size
    default: ===============================================================================================
    default: Installing:
    default:  arduino                                noarch  1:1.8.13-4.fc33                  fedora    73 k
    default:  asciinema                              noarch  2.0.2-4.fc33                     fedora    77 k
    default:  eclipse-egit                           noarch  5.9.0-1.fc33                     updates  9.6 M

[...]

  Running scriptlet: java-1.8.0-openjdk-1:1.8.0.275.b01-6.fc33.x86_64       4/4 
  Running scriptlet: ar4k-agent-druido-0.9.4-1.noarch                       4/4 
  Verifying        : java-1.8.0-openjdk-1:1.8.0.275.b01-6.fc33.x86_64       1/4 
  Verifying        : java-1.8.0-openjdk-headless-1:1.8.0.275.b01-6.fc33.x   2/4 
  Verifying        : telnet-1:0.17-81.fc33.x86_64                           3/4 
  Verifying        : ar4k-agent-druido-0.9.4-1.noarch                       4/4 
    default: 
    default: Installed:
    default:   ar4k-agent-druido-0.9.4-1.noarch                                              
    default:   java-1.8.0-openjdk-1:1.8.0.275.b01-6.fc33.x86_64                              
    default:   java-1.8.0-openjdk-headless-1:1.8.0.275.b01-6.fc33.x86_64                     
    default:   telnet-1:0.17-81.fc33.x86_64                                                  
    default: Complete!
    default: asciinema: recording finished
    default: asciinema: asciicast saved to /home/vagrant/asciinema/install-rpm.json
    default: restart and enable agent
    default: asciinema: recording asciicast to /home/vagrant/asciinema/enable-service.json
    default: asciinema: exit opened program when you're done
    default: Created symlink /etc/systemd/system/multi-user.target.wants/ar4k-druido.service → /etc/systemd/system/ar4k-druido.service.
    default: asciinema: recording finished
    default: asciinema: asciicast saved to /home/vagrant/asciinema/enable-service.json
    default: link for git import in Eclipse
    default: clean permission

```

## login into new virtual machine

Run _vagrant ssh_

```
[andrea@legion-rossonet-com LabMachineFedora]$ vagrant ssh
/usr/bin/xauth:  file /home/vagrant/.Xauthority does not exist
[vagrant@lab-console-edge-agent ~]$ uname -a
Linux lab-console-edge-agent 5.8.15-301.fc33.x86_64 #1 SMP Thu Oct 15 16:58:06 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux
[vagrant@lab-console-edge-agent ~]$

```

## stop the virtual machine

Run _vagrant halt_

```
[andrea@legion-rossonet-com LabMachineFedora]$ vagrant halt
==> default: Attempting graceful shutdown of VM...

```
