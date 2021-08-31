#!/bin/bash
echo clean /build/yum-ar4k-repo
rm -r build/yum-ar4k-repo
echo create /build/yum-ar4k-repo
mkdir -p build/yum-ar4k-repo
echo copy ar4k console rpm
cp build/distributions/ar4k-agent-small-*.noarch.rpm build/yum-ar4k-repo/ar4k-console.noarch.rpm
echo createrepo command
createrepo --database build/yum-ar4k-repo
echo sign repo
gpg -v -a --detach-sign --batch --yes --no-tty --pinentry-mode loopback --passphrase "$GPG_KEY_SIGN" --default-key "F6113733" build/yum-ar4k-repo/repodata/repomd.xml
echo yum repository created
echo compress repository
cd build
tar -czf yum-ar4k-repo.tgz yum-ar4k-repo
echo send repository to web.rossonet.net
eval $(ssh-agent -s)
echo "$ROSSOENT_KEY" | ssh-add -
scp -P 6666 -o StrictHostKeyChecking=no yum-ar4k-repo.tgz rpm-ar4k@web.rossonet.net:/var/www/html/dati/ar4k-repo/yum-ar4k-repo.tgz
echo clean and deploy on web.rossonet.net
ssh -p 6666 -o StrictHostKeyChecking=no rpm-ar4k@web.rossonet.net 'cd /var/www/html/dati/ar4k-repo && rm -rf yum-ar4k-repo && tar -xzf yum-ar4k-repo.tgz' 
echo deploy completed

