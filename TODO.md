## TODO LIST

[ACTIVEMQ verifica stato](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-activemq/src/main/java/org/ar4k/agent/activemq/ActiveMqService.java)

[completare gestione password integrata in ActiveMQ](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-activemq/src/main/java/org/ar4k/agent/activemq/ActiveMqBroker.java)

[Completare test beacon](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-qa/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverBeaconRpcConfig.java)

[provare con firma intermedia, ovvero firmando non con master ma con un](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-qa/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverBeaconRegistration.java)

[Completare test beacon](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-agent-qa/src/test/java/org/ar4k/agent/control/remote/RemoteControlOverSsh.java)

[what to do with multi matches?](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai-nlp/src/main/java/org/ar4k/agent/cortex/opennlp/bot/intents/OpenNLPEntityMatcher.java)

[to be improved, someday](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai-nlp/src/main/java/org/ar4k/agent/cortex/opennlp/NumberMatcherIT.java)

[completare servizio implementazioni regole Drools sui dati e comandi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsConfig.java)

[aggiungere global data anima se flag true](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsService.java)

[inserire oggetti per NLP](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-ai/src/main/java/org/ar4k/agent/cortex/drools/DroolsService.java)

[: impostare sistema di aggiornamento automatico via http(s) del jar con sostituzione dell'esistente](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/config/EdgeConfig.java)

[verificare bene i filtri con una serie di test](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/DataChannelFilter.java)

[implementare spring integration tra i channel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/AbstractChannel.java)

[esempio con endpoint camel in spring integration](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/data/AbstractChannel.java)

[sistemare il feedback di approved](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/interfaces/IBeaconProvisioningAuthorization.java)

[completare l'implementazione archivio su git con i log](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/archives/GitArchive.java)

[completare la gestione del repository su fs locale](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/archives/LocalFileSystemArchive.java)

[Implementare ManagedArchives su AWS S3](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/core/archives/AwsS3Archive.java)

[implementare versione metodi con argomenti](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/logger/EdgeLogger.java)

[valutare l'implementazione del cmd su RemoteBeaconExecutor via Spring](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/client/RemoteBeaconRpcExecutor.java)

[implementare check aggiuntivi in fase di interrogazione con frequenze a](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/client/BeaconClient.java)

[Implementare discovery peer2peer (viene richiesto in brodcast ai nodi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/client/BeaconClient.java)

[Implementare proxy http](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServerTunnelService.java)

[da valutare per il cluster beacon server https:grpc.iobloggrpc-load-balancing](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServer.java)

[da valutare opzione di compressione https:github.comgrpcgrpc-javatreemasterexamplessrcmainjavaiogrpcexamplesexperimental](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServer.java)

[DATASERVICE in beacon serverclient](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/BeaconServerDataService.java)

[da provare il giro su una sola macchina](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/server/TunnelRunnerBeaconServer.java)

[cache messages](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/classic/BeaconClassicEndpointFromObserver.java)

[close channel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/classic/BeaconClassicEndpointFromObserver.java)

[Completare e rivedere classe BeaconNetworkClassicTunnel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/classic/BeaconNetworkClassicTunnel.java)

[revisione tunnel on grpc ](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/socket/AbstractBeaconNetworkSocketConfig.java)

[Develop modem interface](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/beacon/modem/ModemWrapper.java)

[public static final int LIST_FIELD_NUMBER = 1;](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/grpc/beacon/FlowMessage.java)

[hash = (37 * hash) + LIST_FIELD_NUMBER;](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/tunnels/http2/grpc/beacon/FlowMessage.java)

[PROPOSE Sviluppare script engine Power Shell per piattaforme Windows (JSR 223)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/powershell/UserSpacePowerShellScriptEngine.java)

[PROPOSE Sviluppare script engine Power Shell per piattaforme Windows (JSR 223)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/powershell/UserSpacePowerShellScriptEngineFactory.java)

[binding dati Anima. Valutare binding in boot per pre e post script.](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/ScriptEngineManagerProcess.java)

[input.interrupt();   better thing to do?](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/bash/NativeShellRunner.java)

[: provare BashScriptEngineFactory e scrivere tests](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-core/src/main/java/org/ar4k/agent/rpc/process/bash/UserSpaceBashScriptEngineFactory.java)

[Permettere la creazione di canali brodcast tra i nodi coinvolti nel](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-hazelcast/src/main/java/org/ar4k/agent/hazelcast/HazelcastComponent.java)

[implementare comandi in console per gestire mappe dati serializzate in json dei servizi hazelcast attivi](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-hazelcast/src/main/java/org/ar4k/agent/hazelcast/HazelcastShellInterface.java)

[sviluppare beacon server cluster basato su hazelcast per grandi volumi di client](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-hazelcast/src/main/java/org/ar4k/agent/tunnels/http2/beacon/server/cluster/BeaconServerCluster.java)

[completare sottoscrizione gruppi in opcua](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/client/OpcUaClientService.java)

[sostituire con certificati di sistema in OPCUA](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/Ar4kOpcUaServer.java)

[inserire wrapper autenticazione per OPCUA](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/Ar4kOpcUaServer.java)

[updateAndGetStatus di OPCUA](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/Ar4kOpcUaServer.java)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/OpcUaNamespace.java)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/OpcUaNamespace.java)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/opcua/server/OpcUaNamespace.java)

[valutare implementazione 4Diac Forte](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/industrial/IndustrialShellInterface.java)

[integrazione con UNIPI AXON S105](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/industrial/IndustrialShellInterface.java)

[implementare gestione eventi OPCUA con presa visione e conferma](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/industrial/IndustrialShellInterface.java)

[completare servizio client Paho](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/mqtt/client/PahoClientService.java)

[OPCUA Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-industrial/src/main/java/org/ar4k/agent/mqtt/client/PahoClientService.java)

[servizio che mette a disposizione della console job da repository pre configurati (da zip file). Utilizzare l'interfaccia ManagedArchives per implementare i file systems](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-kettle/src/main/java/org/ar4k/agent/core/kettle/Kettle.java)

[completare gestione modulo da gestione comandi remoti come servizi (esempio Staer SG e OpenVPN e cisco)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/remoteSshCommandManaged/RemoteSshCommandManagedShellInterface.java)

[private TftpServer tftpServer;   implementare server sftp](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/bootpService/BootpComponent.java)

[completare server distribuzioni configurazioni bootd](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/bootpService/BootpShellInterface.java)

[completare gestione modulo da GreenGrass Azure](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/azure/AzureShellInterface.java)

[completare gestione modulo da Docker](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/docker/DockerShellInterface.java)

[completare gestione modulo da GreenGrass AWS](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/aws/AwsShellInterface.java)

[completare gestione modulo da gestione comandi locali come servizi (esempio Staer SG e OpenVPN)](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/commandManaged/CommandManagedShellInterface.java)

[completare gestione modulo da OpenShift con creazione cluster via bootp](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-seed/src/main/java/org/ar4k/agent/farm/openshift/OpenShiftShellInterface.java)

[implementare lo spegnimento di un servizio](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/ShellInterface.java)

[implementare l'accensione di un servizio](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/ShellInterface.java)

[implementare la visulizzazione dei dettagli di un servizio](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/ShellInterface.java)

[Migliorare l'interazione della command line bash](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/ShellInterface.java)

[salvare databag realmente il dato](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/DataShellInterface.java)

[implementare comando di shell per lanciare comandi ssh](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-terminal/src/main/java/org/ar4k/agent/console/SshShellInterface.java)

[Metodo per creare la configurazione di un nuovo agente](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-vaadin/src/main/java/org/ar4k/agent/console/MainView.java)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-vaadin/src/main/java/org/ar4k/agent/web/main/MainAgentWrapper.java)

[Auto-generated method stub](https://github.com/rossonet/EdgeAgentAr4k/blob/master/ar4k-vaadin/src/main/java/org/ar4k/agent/web/main/BeaconClientWrapper.java)

