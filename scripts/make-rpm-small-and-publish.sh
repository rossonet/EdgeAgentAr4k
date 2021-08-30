#!/bin/bash
echo clean /build/yum-repo
rm -r build/yum-repo
echo create /build/yum-repo
mkdir -p build/yum-repo
echo copy fep rpm
cp build/distributions/ar4k-agent-small-*.noarch.rpm build/yum-repo/ar4k-console.noarch.rpm
echo createrepo command
createrepo --database build/yum-repo
echo yum repository created
echo compress repository
cd build
tar -czf yum-repo.tgz yum-repo
echo send repository to web.rossonet.net
eval $(ssh-agent -s)
echo "$ROSSOENT_KEY" | ssh-add -
scp -P 6666 -o StrictHostKeyChecking=no yum-repo.tgz rpm-ar4k@web.rossonet.net:/var/www/html/dati/ar4k-repo/yum-repo.tgz
echo clean and deploy on web.rossonet.net
ssh -p 6666 -o StrictHostKeyChecking=no rpm-ar4k@web.rossonet.net 'cd /var/www/html/dati/ar4k-repo && rm -rf yum-repo && tar -xzf yum-repo.tgz' 
echo deploy completed

