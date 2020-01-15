package org.ar4k.agent.full;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.ContextCreationTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class BeaconClientServerTests {

  private static final String CLIENT1_LABEL = "client1";
  private static final String CLIENT2_LABEL = "client2";
  private static final String SERVER_LABEL = "server";
  private ExecutorService executor = Executors.newCachedThreadPool();
  private Map<String, Anima> testAnimas = new HashMap<>();

  @After
  public void tearDown() throws Exception {
    System.err.println("\n\nEND TESTS\n\n");
    for (Anima a : testAnimas.values()) {
      a.close();
    }
    Files.deleteIfExists(Paths.get("./tmp/test.config.base64.ar4k"));
  }

  @Test
  public void base3Animas() throws Exception {
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
    baseArgs.add("--ar4k.keystorePassword=secret");
    baseArgs.add("--ar4k.beaconCaChainPem=a4c8ff551a");
    baseArgs.add("--ar4k.adminPassword=password");
//    addArgs.add("--ar4k.webRegistrationEndpoint=");
//    addArgs.add("--ar4k.dnsRegistrationEndpoint=");
    baseArgs.add("--ar4k.beaconDiscoveryFilterString=AR4K");
    baseArgs.add("--ar4k.beaconDiscoveryPort=33666");
    baseArgs.add("--ar4k.fileConfigOrder=1");
    baseArgs.add("--ar4k.webConfigOrder=2");
    baseArgs.add("--ar4k.dnsConfigOrder=3");
    baseArgs.add("--ar4k.baseConfigOrder=0");
    baseArgs.add("--ar4k.threadSleep=1000");
    baseArgs.add("--ar4k.logoUrl=/static/img/ar4k.png");
    Ar4kConfig serverConfig = null; // TODO
    Ar4kConfig config2 = null; // TODO
    Ar4kConfig config1 = null; // TODO
    String mainAliasInKeystore1 = "cert1";
    String mainAliasInKeystore2 = "cert2";
    String mainAliasInKeystore3 = "cert3";
    testAnimas.put(SERVER_LABEL, executor.submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "a.log",
        "a.ks", 1124, baseArgs, serverConfig, mainAliasInKeystore1)).get());
    testAnimas.put(CLIENT2_LABEL, executor.submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "b.log",
        "b.ks", 1125, baseArgs, config2, mainAliasInKeystore2)).get());
    testAnimas.put(CLIENT1_LABEL, executor.submit(new ContextCreationTestUtils(Ar4kAgent.class, executor, "c.log",
        "c.ks", 1126, baseArgs, config1, mainAliasInKeystore3)).get());
    Thread.sleep(20000);
    for (Anima a : testAnimas.values()) {
      Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
    }
    Thread.sleep(20000);
  }

}
