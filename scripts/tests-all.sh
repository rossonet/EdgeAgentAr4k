#!/bin/bash
# by Ambrosini
clear;clear
#./gradlew :core:test --tests "org.ar4k.gw.studio.TestConsciousnessStateMachineBot" -info
#./gradlew :core:test --tests "org.ar4k.gw.agent.AgentConsoleTest" -info
#./gradlew :core:test --tests "org.ar4k.gw.agent.TunnelTest.testActivationAr4kNet" -info
#./gradlew :core:test --tests "org.ar4k.gw.studio.OpenNlp.testSentencesSplit" -info
#./gradlew :core:test --tests "org.ar4k.gw.studio.OpenNlp.testTrainSentenceModel" -info
#./gradlew :core:test --tests "org.ar4k.gw.studio.OpenNlp.testConjugationOfIrregularVerb" -info
#./gradlew :core:test --tests "org.ar4k.gw.studio.OpenNlp.printItalianModel" -info
#./gradlew :core:test --tests "org.ar4k.gw.studio.OpenNlp.testTokenizer" -info
#./gradlew clean :ar4k-terminal:test --tests "org.ar4k.agent.full.BeaconClientXpraViaBeaconTests.allNodeSimulatedSshTunnel" --info
#./gradlew :core:test -info
#./gradlew :cortex:test -info
#./gradlew :iot:test -info
#./gradlew :terminal:test -info
#./gradlew :reactor-web:test -info
#./gradlew :agent:test -info
./gradlew clean test -info #--stacktrace
