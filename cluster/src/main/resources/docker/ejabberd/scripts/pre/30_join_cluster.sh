#!/bin/bash
set -e

source "${EJABBERD_HOME}/scripts/lib/base_config.sh"
source "${EJABBERD_HOME}/scripts/lib/config.sh"
source "${EJABBERD_HOME}/scripts/lib/base_functions.sh"
source "${EJABBERD_HOME}/scripts/lib/functions.sh"

file_exist ${FIRST_START_DONE_FILE} \
    && exit 0

curl "http://172.20.0.20:8080/add?secret=ls6JdtLsPr&domain=$(hostname)&addr=$(ifconfig eth0| grep 'inet '| sed 's/^.*inet //' | sed 's/[ ]*netmask.*$//')"
curl "http://172.20.0.20:8080/txt?secret=ls6JdtLsPr&domain=cluster-xmpp&addr=$(hostname)"
if ping -c 1 cluster-xmpp.ecss.italy1.newdigital.enterprises
then
  #join_cluster cluster-xmpp.ecss.italy1.newdigital.enterprises
  echo "join here"
else
  curl "http://172.20.0.20:8080/txt?secret=ls6JdtLsPr&domain=cluster-xmpp&addr=$(hostname)"
fi

exit 0
