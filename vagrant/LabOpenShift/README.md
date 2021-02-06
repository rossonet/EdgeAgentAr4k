# Lab OpenShift

## requirements

[Vagrant](https://www.vagrantup.com/downloads)

[VirtualBox](https://www.virtualbox.org/wiki/Downloads)

## download pullsecret before

Login on [OpenShift Cloud console](https://cloud.redhat.com/openshift/)

From main page got to **Create cluster**.

![Main page](login-openshift.png?raw=true)

Select **SandBox** tab and click on **Download pull secret**.

![Main page](get-pullsecret.png?raw=true)

Save the file inside this directory as _pull-secret_

## run vagrant

Run _vagrant up_

```
[andrea@legion-rossonet-com LabOpenShift]$ vagrant up
Bringing machine 'default' up with 'virtualbox' provider...
==> default: Importing base box 'fedora/33-cloud-base'...
==> default: Matching MAC address for NAT networking...
==> default: Checking if box 'fedora/33-cloud-base' version '33.20201019.0' is up to date...
==> default: Setting the name of the VM: LabOpenShift_default_1612549353403_58404
==> default: Fixed port collision for 22 => 2222. Now on port 2200.
==> default: Clearing any previously set network interfaces...
==> default: Preparing network interfaces based on configuration...
    default: Adapter 1: nat
==> default: Forwarding ports...
    default: 22 (guest) => 2200 (host) (adapter 1)
==> default: Running 'pre-boot' VM customizations...
==> default: Booting VM...
==> default: Waiting for machine to boot. This may take a few minutes...
    default: SSH address: 127.0.0.1:2200
    default: SSH username: vagrant
    default: SSH auth method: private key

[...]



```

## login into new virtual machine

Run _vagrant ssh_

```

```

## stop the virtual machine

Run _vagrant halt_

```


```
