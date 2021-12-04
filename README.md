# Ar4k Edge Agent by Rossonet

[![Test on master branch](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/test-on-master.yml/badge.svg)](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/test-on-master.yml)
[![Test on devel branch](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/test-on-devel.yml/badge.svg)](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/test-on-devel.yml)
[![Build docs and publish on web.rossonet.net](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/docs-on-master.yml/badge.svg)](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/docs-on-master.yml)
[![Publish on Maven Central](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/publish-on-maven.yml/badge.svg)](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/publish-on-maven.yml)
[![Publish RPM on app.rossonet.net/repo](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/publish-rpm.yml/badge.svg)](https://github.com/rossonet/EdgeAgentAr4k/actions/workflows/publish-rpm.yml)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frossonet%2FEdgeAgentAr4k.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Frossonet%2FEdgeAgentAr4k?ref=badge_shield)
[![Codecov](https://codecov.io/gh/rossonet/EdgeAgentAr4k/branch/devel/graph/badge.svg?token=LW8YNNLV4V)](https://codecov.io/gh/rossonet/EdgeAgentAr4k)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/3f2fd1b5f5c5421fa1102047c8b7e54f)](https://www.codacy.com/gh/rossonet/EdgeAgentAr4k/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rossonet/EdgeAgentAr4k&amp;utm_campaign=Badge_Grade)

## Reference

[Template project](https://github.com/rossonet/TemplateEdgeAgentAr4k)

[Italian website with examples](https://app.rossonet.net)

[Documentation](https://www.rossonet.net/dati/edge-docs/doc-site/)

[TODO](TODO.md)

[CODE STYLE](https://google.github.io/styleguide/javaguide.html)

![alt text](https://app.rossonet.net/wp-content/uploads/2021/10/rossonet-logo_280_115.png "Rossonet")

[https://www.rossonet.net](https://www.rossonet.net)

## Repository install on RedHat, CentOS or Fedora

```
sudo rpm --import https://raw.githubusercontent.com/rossonet/EdgeAgentAr4k/master/RPM-GPG-KEY-AR4K
sudo dnf config-manager --add-repo https://app.rossonet.net/repo/
sudo dnf install ar4k-agent-small -y # to install small version
sudo dnf install rossonet-rtu -y # to install rtu version
```


## Repository install on Debian, Ubuntu, Raspibian

```
wget -q -O - https://raw.githubusercontent.com/rossonet/EdgeAgentAr4k/master/RPM-GPG-KEY-AR4K | apt-key add -
echo "deb https://app.rossonet.net/debs ./" >> /etc/apt/sources.list
apt update
apt install -y rossonet-rtu
```
