package org.ar4k.agent.full;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.tunnels.http.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeaconClientServerComandTests {

  private static final String SERVER_LABEL = "server";
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private final Map<String, Homunculus> testAnimas = new HashMap<>();
  private final File keyStoreServer = new File("./tmp/server.ks");
  private final File keyStoreClient1 = new File("./tmp/client1.ks");
  private final File keyStoreClient2 = new File("./tmp/client2.ks");
  private final String serverAliasInKeystore = "server";
  private final String client2AliasInKeystore = "client2";
  private final String client1AliasInKeystore = "client1";
  private final String passwordKs = "password";

  @Before
  public void before() throws Exception {
    deleteDir(new File("./tmp"));
    deleteDir(new File("./tmp1"));
    deleteDir(new File("./tmp2"));
    deleteDir(new File("./tmp3"));
    deleteDir(new File("~/.ar4k"));

    Files.createDirectories(Paths.get("./tmp"));
    KeystoreLoader.create(serverAliasInKeystore, keyStoreServer.getAbsolutePath(), passwordKs);
    KeystoreLoader.create(client2AliasInKeystore, keyStoreClient1.getAbsolutePath(), passwordKs);
    KeystoreLoader.create(client1AliasInKeystore, keyStoreClient2.getAbsolutePath(), passwordKs);
    /*
     * try { File keyStoreMaster =
     * ResourceUtils.getFile("classpath:test-beacon.tkeystore");
     * FileUtils.copyFile(keyStoreMaster, keyStoreServer);
     * FileUtils.copyFile(keyStoreMaster, keyStoreClient1);
     * FileUtils.copyFile(keyStoreMaster, keyStoreClient2); } catch
     * (FileNotFoundException e) { e.printStackTrace(); }
     */
  }

  private void deleteDir(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (final File file : files) {
        deleteDir(file);
      }
    }
    dir.delete();
  }

  @After
  public void tearDown() throws Exception {
    System.err.println("\n\nEND TESTS\n\n");
    for (Homunculus a : testAnimas.values()) {
      a.close();
    }
    Files.deleteIfExists(Paths.get("./tmp/test.config.base64.ar4k"));
    Files.deleteIfExists(keyStoreServer.toPath());
    Files.deleteIfExists(keyStoreClient1.toPath());
    Files.deleteIfExists(keyStoreClient2.toPath());
    if (executor != null) {
      executor.shutdownNow();
      executor.awaitTermination(1, TimeUnit.MINUTES);
    }
    deleteDir(new File("./tmp"));
    deleteDir(new File("./tmp1"));
    deleteDir(new File("./tmp2"));
    deleteDir(new File("./tmp3"));
    deleteDir(new File("~/.ar4k"));
  }

  @Test
  public void oneServerAsClientSocketTestLeftNoSsl() throws Exception {
    oneServerAsClientSocketTestLeft(false);
  }

  private void oneServerAsClientSocketTestLeft(boolean ssl) throws Exception {
    List<String> baseArgs = new ArrayList<>();
    if (ssl) {
      baseArgs.add("--ar4k.beaconClearText=false");
    }
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
    baseArgs.add("--ar4k.keystorePassword=" + passwordKs);
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
    EdgeConfig serverConfig = new EdgeConfig();
    serverConfig.name = "server-beacon";
    serverConfig.beaconServer = null;
    serverConfig.beaconDiscoveryPort = 0;
    BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
    beaconServiceConfig.discoveryPort = 33667;
    beaconServiceConfig.port = 22116;
    beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
    beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
    serverConfig.pots.add(beaconServiceConfig);

    testAnimas.put(SERVER_LABEL,
        executor
            .submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log", keyStoreServer.getAbsolutePath(),
                1124, baseArgs, serverConfig, serverAliasInKeystore, serverAliasInKeystore, "https://127.0.0.1:22116"))
            .get());
    Thread.sleep(15000);
    for (Homunculus a : testAnimas.values()) {
      String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
      if (animaName.equals("server-beacon")) {
        Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
      } else {
        Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.STAMINAL);
      }
    }
    Thread.sleep(5000);
    List<Agent> agents = testAnimas.get(SERVER_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
    String agentToQuery = null;
    for (Agent a : agents) {
      System.out.println("agent found by test -> " + a.getAgentUniqueName());
      agentToQuery = a.getAgentUniqueName();
      System.out.println(a);
    }
  }

}
