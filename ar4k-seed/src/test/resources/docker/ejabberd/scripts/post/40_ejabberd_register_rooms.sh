#!/bin/bash
set -e

source "${EJABBERD_HOME}/scripts/lib/base_config.sh"
source "${EJABBERD_HOME}/scripts/lib/config.sh"
source "${EJABBERD_HOME}/scripts/lib/base_functions.sh"
source "${EJABBERD_HOME}/scripts/lib/functions.sh"


create_rooms() {
    echo "Creating rooms: frontend test1 test2 test3 be_amministrazione be_tecnico be_attivazioni"
    ${EJABBERDCTL} create_room frontend conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises 
    ${EJABBERDCTL} create_room test1 conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises
    ${EJABBERDCTL} create_room test2 conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises 
    ${EJABBERDCTL} create_room test3 conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises 
    ${EJABBERDCTL} create_room be_amministrazione conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises
    ${EJABBERDCTL} create_room be_tecnico conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises
    ${EJABBERDCTL} create_room be_attivazioni conference.ecss.xmpp.italy1.newdigital.enterprises ecss.xmpp.italy1.newdigital.enterprises
}

file_exist ${FIRST_START_DONE_FILE} && exit 0
#create_rooms

exit 0
