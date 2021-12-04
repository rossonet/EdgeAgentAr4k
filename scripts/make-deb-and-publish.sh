#!/bin/bash
echo clean /build/deb-repo
rm -r build/deb-repo
echo create /build/deb-repo
mkdir -p build/deb-repo
echo copy dep packages
cp build/distributions/rossonet-rtu_*_all.deb build/deb-repo/rossonet-rtu_all.deb
echo create Release file
cd build/deb-repo
dpkg-scanpackages . /dev/null > Packages
gzip --keep --force -9 Packages
apt-ftparchive release . > Release
echo sign repo with gpg
gpg -v -a --detach-sign --batch --yes --no-tty --pinentry-mode loopback --passphrase "$GPG_KEY_SIGN" --default-key "F6113733" -o Release.gpg Release 
gpg -v --clearsign --batch --yes --no-tty --pinentry-mode loopback --passphrase "$GPG_KEY_SIGN" --default-key "F6113733" -o InRelease Release
echo yum repository created
echo compress repository
cd ..
tar -czf deb-repo.tgz deb-repo
echo send repository to web.rossonet.net
eval $(ssh-agent -s)
echo "$ROSSOENT_KEY" | ssh-add -
scp -P 6666 -o StrictHostKeyChecking=no deb-repo.tgz rpm-ar4k@web.rossonet.net:/var/www/html/dati/ar4k-repo/deb-repo.tgz
echo clean and deploy on web.rossonet.net
ssh -p 6666 -o StrictHostKeyChecking=no rpm-ar4k@web.rossonet.net 'cd /var/www/html/dati/ar4k-repo && rm -rf deb-repo && tar -xzf deb-repo.tgz' 
echo deploy completed

