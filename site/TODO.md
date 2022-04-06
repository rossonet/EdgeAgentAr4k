## TODO LIST

### INDUSTRIAL

1. completare client MQTT
2. completare server OPCUA
3. ciclo test da opcua server, mqtt client, activemq, mqtt client
4. completare MODBUS Master
5. completare MODBUS SLAVE
6. ciclo test di prima con aggiunta modbus

### CORE

1. meccanismo di routing con i messaggi con spring integration (valutare come configurarlo)
3. verificare import ed esport dei vari formati quando sono completate le altre implementazioni. Aggiungere relativi test
4. [sistemare filtri selezione canali](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/DataChannelFilter.java#L10)
5. (bassa priorità) data via beaconserver
6. (bassa prirità) gestione modem
7. (bassa priorità) meccanismo di routing con Camel
8. aggiornamento jar da comando. Implementare specifico comando in Beacon client/server
9. esecuzioni servizi con parametrizzazione (per cicli di esecuzione e/o timeout). Per esempio per eseguire un comando singolo

### VADDIN

1. gestione varie IDE sviluppo integrata con seed (KieWorkbench, Kettle Spoon -via xpra-, 4Diac IDE -via xpra-, Jupyter )
2. configuratore con output ide integrato
3. miglioramente progressivo interfaccia console

### TERMINAL

1. comando salvataggio dati in databag

### SEED

1. completare contenitori con interfaccia pubblica per future implementazioni
2. aggiungere in TERMINAL meccanismo di configurazione dei contenitori per il generico, KieWorkbench, Kettle Spoon -via xpra-, 4Diac IDE -via xpra-, Jupyter , spark, openvpn, 4diac Runtime (con verifica su Axon), ProcessMaker, MariaDB , MongoDB, gitlabi, NiFi Apache
3. aggiungere procedura configurazione open shift da TERMINAL con PXE via sftp/http
4. integrare esecuzione in OpenShift
5. integrare esecuzione in GreenGrass
6. integrare esecuzione in Azure IOT Hub
7. verifica funzionalita su raspberry
8. verifica su UNIPI Axon

### KETTLE

1. gestore runtime da TERMINAL (implementare prima IDE Spoon)
2. gestore servizio da configurazione (implementare prima IDE Spoon)
3. runtime kettle da repository git e/o file system 
4. verificare dipendenze disponibili (inserirle commentate in build.gradle)
 
### HAZELCAST (bassa priorità)

1. implementare servizio "cluster beacon server"
2. provare su OpenShift il cluster
3. implementare autoscaling del servizio su OpenShift

### AI

1. runtime drools (dopo configurazione IDE)
2. client per connesione spark
3. gestione runtime Jupyter (senza il modulo web)
4. runtime drools da repository git e/o file system
5. (bassa priorità) esempio utilizzo runtime deeplearning4j in progetto separato
6. (bassa priorità) esempio utilizzo in ide drools interfaccia a deeplearning4j
7. (bassa priorità) esempio utilizzo kettle da drools con IDE
8. utilizzo comandi shell da Drools con IDE
9. (bassa priorità) esempio utilizzo interfaccia dati da Drools con IDE
10. (bassa priorità) esempio utilizzo ProcessMaker da Drools con IDE
11. connessione dati per Drools

### AI-NLP
1. (bassa priorità) esempio utilizzo opennlp da Drools con IDE

### TELEGRAM

1. realizzare connettore Telegram come sotto progetto

## SOFTWARE DA PROVARE CON SEED

1. activemq
2. apacheZeppelin
3. spark
4. bigBlueButton
5. apache che
6. etherpad
7. gitlab
8. jenkins
9. jitsi
10. kafka
11. keycloak
12. [kibana](https://hub.docker.com/_/kibana/)
13. kieWorkbench
14. mariadb
15. mongodb
16. octoPrint
17. odoo
18. postgresql
21. [wordpress](https://hub.docker.com/_/wordpress/)
22. [xpra](https://github.com/Xpra-org/xpra)
23. [zabbix](https://www.zabbix.com/container_images)
24. [zimbra](https://github.com/jorgedlcruz/zimbra-docker)
25. [Flink](https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/learn-flink/overview/)
26. [NiFi](https://nifi.apache.org/)
27. [ProcessMaker](https://github.com/rossonet/ProcessMaker-apache)
28. [SkyWalking](https://skywalking.apache.org/docs/)


## TODO LIST ESTESA

[ACTIVEMQ verifica stato](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-activemq/src/main/java/org/ar4k/agent/activemq/ActiveMqService.java#L52)

[completare gestione password integrata in ActiveMQ](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-activemq/src/main/java/org/ar4k/agent/activemq/ActiveMqBroker.java#L142)

[Completare test beacon](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-qa/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverBeaconRpcConfig.java#L63)

[provare con firma intermedia, ovvero firmando non con master ma con un](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-qa/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverBeaconRegistration.java#L330)

[Completare test beacon](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-qa/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverSsh.java#L55)

[Completare test beacon](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-rtu/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverBeaconRpcConfig.java#L63)

[provare con firma intermedia, ovvero firmando non con master ma con un](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-rtu/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverBeaconRegistration.java#L330)

[Completare test beacon](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-rtu/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverSsh.java#L55)

[what to do with multi matches?](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai-nlp/src/main/java/org/ar4k/agent/cortex/opennlp/bot/intents/OpenNLPEntityMatcher.java#L92)

[to be improved, someday](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai-nlp/src/main/java/org/ar4k/agent/cortex/opennlp/NumberMatcherIT.java#L33)

[completare servizio implementazioni regole Drools sui dati e comandi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsConfig.java#L20)

[permettere a Drools di gestire la classificazione e regrassione con Weka](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsConfig.java#L22)

[permettere a Drools di gestire deepLearning4J con modelli supervisionati da configurazione, non supervizionati e rinforzo in runtime.](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsConfig.java#L24)

[aggiungere global data anima se flag true](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsService.java#L49)

[inserire oggetti per NLP](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsService.java#L250)

[public static final int LIST_FIELD_NUMBER = 1;](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-api/src/main/java/org/ar4k/agent/tunnels/http2/grpc/beacon/FlowMessage.java#L90)

[hash = (37 * hash) + LIST_FIELD_NUMBER;](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-api/src/main/java/org/ar4k/agent/tunnels/http2/grpc/beacon/FlowMessage.java#L184)

[sistemare il feedback di approved](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-api/src/main/java/org/ar4k/agent/web/IBeaconProvisioningAuthorization.java#L27)

[impostare sistema di aggiornamento automatico via http(s) del jar con sostituzione dell'esistente](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/config/EdgeConfig.java#L43)

[gestione configurazione isSpringBean](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/services/AbstractEdgeService.java#L47)

[verificare bene i filtri con una serie di test](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/DataChannelFilter.java#L10)

[completare router messaggi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/router/MessagesRouterService.java#L18)

[completare router messaggi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/router/MessagesRouterConfig.java#L17)

[implementare spring integration tra i channel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/AbstractChannel.java#L29)

[esempio con endpoint camel in spring integration](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/AbstractChannel.java#L34)

[completare l'implementazione archivio su git con i log](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/archives/GitArchive.java#L68)

[completare la gestione del repository su fs locale](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/archives/LocalFileSystemArchive.java#L38)

[Implementare ManagedArchive su AWS S3](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/archives/AwsS3Archive.java#L5)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/helper/NetworkHelper.java#L205)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/helper/NetworkHelper.java#L210)

[valutare l'implementazione del cmd su RemoteBeaconExecutor via Spring](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/client/RemoteBeaconRpcExecutor.java#L62)

[implementare check aggiuntivi in fase di interrogazione con frequenze a](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/client/BeaconClient.java#L132)

[Implementare discovery peer2peer (viene richiesto in brodcast ai nodi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/client/BeaconClient.java#L504)

[Implementare proxy http](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServerTunnelService.java#L87)

[da valutare per il cluster beacon server https:grpc.iobloggrpc-load-balancing](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServer.java#L43)

[da valutare opzione di compressione https:github.comgrpcgrpc-javatreemasterexamplessrcmainjavaiogrpcexamplesexperimental](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServer.java#L45)

[DATASERVICE in beacon serverclient](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServerDataService.java#L13)

[da provare il giro su una sola macchina](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/server/TunnelRunnerBeaconServer.java#L204)

[cache messages](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/classic/BeaconClassicEndpointFromObserver.java#L108)

[close channel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/classic/BeaconClassicEndpointFromObserver.java#L209)

[Completare e rivedere classe BeaconNetworkClassicTunnel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/classic/BeaconNetworkClassicTunnel.java#L65)

[revisione tunnel on grpc ](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/AbstractBeaconNetworkSocketConfig.java#L12)

[Develop modem interface](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/modem/ModemWrapper.java#L3)

[PROPOSE Sviluppare script engine Power Shell per piattaforme Windows (JSR 223)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/powershell/UserSpacePowerShellScriptEngine.java#L11)

[PROPOSE Sviluppare script engine Power Shell per piattaforme Windows (JSR 223)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/powershell/UserSpacePowerShellScriptEngineFactory.java#L10)

[binding dati Anima. Valutare binding in boot per pre e post script.](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/ScriptEngineManagerProcess.java#L35)

[input.interrupt();   better thing to do?](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/bash/NativeShellRunner.java#L99)

[provare BashScriptEngineFactory e scrivere tests](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/bash/UserSpaceBashScriptEngineFactory.java#L12)

[completare command managed service](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/service/commandManaged/CommandManagedService.java#L16)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/service/commandManaged/CommandManagedService.java#L86)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/service/commandManaged/CommandManagedService.java#L91)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/service/commandManaged/CommandManagedService.java#L96)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/service/commandManaged/CommandManagedService.java#L101)

[Permettere la creazione di canali brodcast tra i nodi coinvolti nel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-hazelcast/src/main/java/org/ar4k/agent/hazelcast/HazelcastComponent.java#L43)

[implementare comandi in console per gestire mappe dati serializzate in json dei servizi hazelcast attivi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-hazelcast/src/main/java/org/ar4k/agent/hazelcast/HazelcastShellInterface.java#L47)

[sviluppare beacon server cluster basato su hazelcast per grandi volumi di client](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-hazelcast/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/cluster/BeaconServerCluster.java#L14)

[sostituire con certificati di sistema in OPCUA](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/Ar4kOpcUaServer.java#L83)

[inserire wrapper autenticazione per OPCUA](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/Ar4kOpcUaServer.java#L89)

[updateAndGetStatus di OPCUA](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/Ar4kOpcUaServer.java#L193)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/OpcUaNamespace.java#L151)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/OpcUaNamespace.java#L157)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/OpcUaNamespace.java#L163)

[valutare implementazione 4Diac Forte](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/industrial/OpcUaShellInterface.java#L74)

[integrazione con UNIPI AXON S105](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/industrial/OpcUaShellInterface.java#L76)

[implementare gestione eventi OPCUA con presa visione e conferma](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/industrial/OpcUaShellInterface.java#L78)

[completare servizio modbus slave](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/modbus/slave/ModbusSlaveService.java#L18)

[completare servizio modbus master](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/modbus/master/ModbusMasterService.java#L18)

[completare invio messaggi verso core](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/mqtt/client/MqttTopicSubscription.java#L58)

[completare scrittura messaggi su coda mqtt](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/mqtt/client/MqttWriter.java#L19)

[servizio che mette a disposizione della console job da repository pre configurati (da zip file). Utilizzare l'interfaccia ManagedArchive per implementare i file systems](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-kettle/src/main/java/org/ar4k/agent/core/kettle/Kettle.java#L28)

[completare gestione modulo da Operatori](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/KubernetesShellInterface.java#L56)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/openshift/OpenShiftClusterTwin.java#L179)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/openshift/OpenShiftClusterTwin.java#L184)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/openshift/OpenShiftClusterTwin.java#L189)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/openshift/OpenShiftClusterTwin.java#L194)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/openshift/OpenShiftClusterTwin.java#L339)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/openshift/OpenShiftClusterTwin.java#L344)

[Auto-generated constructor stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/operator/KubernetesOperator.java#L10)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/operator/KubernetesOperator.java#L15)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/operator/KubernetesOperator.java#L20)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/operator/KubernetesOperator.java#L25)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/kubernetes/operator/KubernetesOperator.java#L30)

[completare gestione modulo da GreenGrass Azure](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/azure/AzureShellInterface.java#L30)

[completare gestione modulo da Docker](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/docker/DockerShellInterface.java#L30)

[completare gestione modulo da GreenGrass AWS](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/aws/AwsShellInterface.java#L30)

[installazione file per essere un servizio sulla macchina locale](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/ShellInterface.java#L121)

[Migliorare l'interazione della command line bash](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/ShellInterface.java#L895)

[completare gestione modulo da gestione comandi locali come servizi (esempio Staer SG e OpenVPN)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L49)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L78)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L106)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L117)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L174)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L181)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L188)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/CommandManagedShellInterface.java#L195)

[salvare databag realmente il dato](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/DataShellInterface.java#L197)

[implementare comando di shell per lanciare comandi ssh](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/SshShellInterface.java#L127)

[Metodo per creare la configurazione di un nuovo agente](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-vaadin/src/main/java/org/ar4k/agent/console/Ar4kConsoleMainView.java#L188)

[add filter](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-vaadin/src/main/java/org/ar4k/agent/web/main/MainAgentWrapper.java#L143)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-vaadin/src/main/java/org/ar4k/agent/web/main/BeaconClientWrapper.java#L171)

