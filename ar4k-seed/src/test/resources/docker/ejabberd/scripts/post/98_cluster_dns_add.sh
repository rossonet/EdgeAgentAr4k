#!/bin/bash
set -e

source "${EJABBERD_HOME}/scripts/lib/base_config.sh"
source "${EJABBERD_HOME}/scripts/lib/config.sh"
source "${EJABBERD_HOME}/scripts/lib/base_functions.sh"
source "${EJABBERD_HOME}/scripts/lib/functions.sh"

#curl "http://172.20.0.20:8080/add?secret=ls6JdtLsPr&domain=cluster-xmpp&addr=$(ifconfig eth0| grep 'inet '| sed 's/^.*inet //' | sed 's/[ ]*netmask.*$//')"

if [ ! -e "${FIRST_START_DONE_FILE}" ]; then
    touch ${FIRST_START_DONE_FILE}
fi
