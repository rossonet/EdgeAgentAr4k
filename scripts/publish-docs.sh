#!/bin/bash
echo send docs to web.rossonet.net
eval $(ssh-agent -s)
echo "$SSH_TOKEN" | ssh-add -
scp -P 6666 -o StrictHostKeyChecking=no build/doc-site.tgz github-ar4k@web.rossonet.net:/var/www/html/dati/edge-docs/
echo clean and deploy on web.rossonet.net
ssh -p 6666 -o StrictHostKeyChecking=no github-ar4k@web.rossonet.net 'cd /var/www/html/dati/edge-docs && rm -rf doc-site && tar -xzf doc-site.tgz' 
echo deploy completed
