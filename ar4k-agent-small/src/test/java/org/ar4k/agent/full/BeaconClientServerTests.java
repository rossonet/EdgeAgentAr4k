package org.ar4k.agent.full;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.ContextCreationTestUtils;
import org.ar4k.agent.tunnels.http.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class BeaconClientServerTests {

  private static final String CLIENT1_LABEL = "client1";
  private static final String CLIENT2_LABEL = "client2";
  private static final String SERVER_LABEL = "server";
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private final Map<String, Anima> testAnimas = new HashMap<>();
  private final File keyStoreServer = new File("/tmp/server.ks");
  private final File keyStoreClient1 = new File("/tmp/client1.ks");
  private final File keyStoreClient2 = new File("/tmp/client1.ks");
  private final String serverAliasInKeystore = "server";
  private final String client2AliasInKeystore = "client2";
  private final String client1AliasInKeystore = "client1";
  private final String caAlias = "ca";

  @Before
  public void before() throws IOException {
    try {
      File keyStoreMaster = ResourceUtils.getFile("classpath:test-beacon.tkeystore");
      FileUtils.copyFile(keyStoreMaster, keyStoreServer);
      FileUtils.copyFile(keyStoreMaster, keyStoreClient1);
      FileUtils.copyFile(keyStoreMaster, keyStoreClient2);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @After
  public void tearDown() throws Exception {
    System.err.println("\n\nEND TESTS\n\n");
    for (Anima a : testAnimas.values()) {
      a.close();
    }
    Files.deleteIfExists(Paths.get("./tmp/test.config.base64.ar4k"));
    Files.deleteIfExists(keyStoreServer.toPath());
    Files.deleteIfExists(keyStoreClient1.toPath());
    Files.deleteIfExists(keyStoreClient2.toPath());
  }

  @Test
  public void base3AnimasWithDiscovery() throws Exception {
    List<String> baseArgs = new ArrayList<>();
    baseArgs.add("--spring.shell.command.quit.enabled=false");
    baseArgs.add("--logging.level.root=INFO");
    baseArgs.add("--ar4k.confPath=./tmp");
    baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
    baseArgs.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgs.add("--ar4k.dnsConfig=demo1.rossonet.name");
//    addArgs.add("--ar4k.baseConfig=");
    baseArgs.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgs.add("--ar4k.dnsKeystore=ks1.rossonet.name");
    // baseArgs.add("--ar4k.keystoreMainAlias=");
    baseArgs.add("--ar4k.keystorePassword=secA4.rk!8");
    baseArgs.add("--ar4k.beaconCaChainPem= ");// not used
    baseArgs.add("--ar4k.adminPassword=password");
//    addArgs.add("--ar4k.webRegistrationEndpoint=");
//    addArgs.add("--ar4k.dnsRegistrationEndpoint=");
    baseArgs.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
    baseArgs.add("--ar4k.beaconDiscoveryPort=33667");
    baseArgs.add("--ar4k.fileConfigOrder=1");
    baseArgs.add("--ar4k.webConfigOrder=2");
    baseArgs.add("--ar4k.dnsConfigOrder=3");
    baseArgs.add("--ar4k.baseConfigOrder=0");
    baseArgs.add("--ar4k.threadSleep=1000");
    baseArgs.add("--ar4k.logoUrl=/static/img/ar4k.png");
    Ar4kConfig serverConfig = new Ar4kConfig();
    serverConfig.name = "server-beacon";
    serverConfig.beaconServer = null;
    serverConfig.autoRegisterBeaconServer = false;
    BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
    beaconServiceConfig.discoveryPort = 33667;
    beaconServiceConfig.port = 33666;
    beaconServiceConfig.aliasBeaconServerInKeystore = caAlias;
    beaconServiceConfig.aliasBeaconServerRequestCertInKeystore = null; // probabile cancellare
    beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
    serverConfig.pots.add(beaconServiceConfig);
    Ar4kConfig config2 = null; // TODO
    Ar4kConfig config1 = null; // TODO

    testAnimas.put(SERVER_LABEL,
        executor
            .submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "a.log", keyStoreServer.getAbsolutePath(),
                1124, baseArgs, serverConfig, caAlias, caAlias, "https://localhost:33666"))
            .get());
    testAnimas.put(CLIENT2_LABEL,
        executor.submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "b.log",
            keyStoreClient2.getAbsolutePath(), 1125, baseArgs, config2, caAlias, caAlias, "https://localhost:33666"))
            .get());
    testAnimas.put(CLIENT1_LABEL,
        executor.submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "c.log",
            keyStoreClient1.getAbsolutePath(), 1126, baseArgs, config1, caAlias, caAlias, "https://localhost:33666"))
            .get());
    Thread.sleep(20000);
    for (Anima a : testAnimas.values()) {
      String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
      if (animaName.equals("server-beacon")) {
        Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
      } else {
        Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
      }
    }
    Thread.sleep(1000 * 60 * 15);
  }

  @Test
  public void oneServerAsClient() throws Exception {
    List<String> baseArgs = new ArrayList<>();
    baseArgs.add("--spring.shell.command.quit.enabled=false");
    baseArgs.add("--logging.level.root=INFO");
    baseArgs.add("--ar4k.confPath=./tmp");
    baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
    baseArgs.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgs.add("--ar4k.dnsConfig=demo1.rossonet.name");
//    addArgs.add("--ar4k.baseConfig=");
    baseArgs.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgs.add("--ar4k.dnsKeystore=ks1.rossonet.name");
    // baseArgs.add("--ar4k.keystoreMainAlias=");
    baseArgs.add("--ar4k.keystorePassword=secA4.rk!8");
    baseArgs.add("--ar4k.beaconCaChainPem= ");// not used
    baseArgs.add("--ar4k.adminPassword=password");
//    addArgs.add("--ar4k.webRegistrationEndpoint=");
//    addArgs.add("--ar4k.dnsRegistrationEndpoint=");
    baseArgs.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
    baseArgs.add("--ar4k.beaconDiscoveryPort=33667");
    baseArgs.add("--ar4k.fileConfigOrder=1");
    baseArgs.add("--ar4k.webConfigOrder=2");
    baseArgs.add("--ar4k.dnsConfigOrder=3");
    baseArgs.add("--ar4k.baseConfigOrder=0");
    baseArgs.add("--ar4k.threadSleep=1000");
    baseArgs.add("--ar4k.logoUrl=/static/img/ar4k.png");
    Ar4kConfig serverConfig = new Ar4kConfig();
    serverConfig.name = "server-beacon";
    serverConfig.beaconServer = null;
    serverConfig.autoRegisterBeaconServer = false;
    BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
    beaconServiceConfig.discoveryPort = 33667;
    beaconServiceConfig.port = 33666;
    beaconServiceConfig.aliasBeaconServerInKeystore = caAlias;
    beaconServiceConfig.aliasBeaconServerRequestCertInKeystore = null; // probabile cancellare
    beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
    serverConfig.pots.add(beaconServiceConfig);
    Ar4kConfig config2 = null; // TODO
    Ar4kConfig config1 = null; // TODO

    testAnimas.put(SERVER_LABEL,
        executor
            .submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "a.log", keyStoreServer.getAbsolutePath(),
                1124, baseArgs, serverConfig, caAlias, caAlias, "https://localhost:33666"))
            .get());
    /*
     * testAnimas.put(CLIENT2_LABEL, executor.submit(new
     * ContextCreationTestUtils(Ar4kAgent.class, executor, "b.log",
     * keyStoreClient2.getAbsolutePath(), 1125, baseArgs, config2, caAlias, caAlias,
     * "https://localhost:33666")) .get()); testAnimas.put(CLIENT1_LABEL,
     * executor.submit(new ContextCreationTestUtils(Ar4kAgent.class, executor,
     * "c.log", keyStoreClient1.getAbsolutePath(), 1126, baseArgs, config1, caAlias,
     * caAlias, "https://localhost:33666")) .get());
     */
    Thread.sleep(20000);
    for (Anima a : testAnimas.values()) {
      String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
      if (animaName.equals("server-beacon")) {
        Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
      } else {
        Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
      }
    }
    Thread.sleep(10000);
    List<Agent> agents = testAnimas.get(SERVER_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
    String agentToQuery = null;
    for (Agent a : agents) {
      System.out.println(a.getAgentUniqueName());
      agentToQuery = a.getAgentUniqueName();
    }
    ListCommandsReply commands = testAnimas.get(SERVER_LABEL).getBeaconClient().listCommadsOnAgent(agentToQuery);
    for (Command c : commands.getCommandsList()) {
      System.out.println(c.getCommand());
    }
    System.out.println(testAnimas.get(SERVER_LABEL).getBeaconClient().runCommadsOnAgent(agentToQuery, "help"));
    System.out
        .println(testAnimas.get(SERVER_LABEL).getBeaconClient().runCommadsOnAgent(agentToQuery, "get-threads-info"));
    Thread.sleep(1000 * 60 * 1);
  }

}
