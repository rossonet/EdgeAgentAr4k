package org.ar4k.agent.full;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class MultiContextTests {

  private ExecutorService executor = Executors.newCachedThreadPool();
  private Set<Anima> testAnimas = new HashSet<>();

  @After
  public void tearDown() throws Exception {
    System.err.println("\n\nEND TESTS\n\n");
    for (Anima a : testAnimas) {
      a.close();
      a = null;
    }
  }

  @Test
  public void base3AgentsTest() throws Exception {
    List<String> addArgs = new ArrayList<>();
    addArgs.add("--spring.shell.command.quit.enabled=false");
    addArgs.add("--logging.level.root=INFO");
    addArgs.add("--ar4k.consoleOnly=false");
    addArgs.add("--ar4k.test=true");
    addArgs.add("--ar4k.confPath=~/.ar4k");
    addArgs.add("--ar4k.fileConfig=~/.ar4k/defaultBoot.config.base64.ar4k");
    addArgs.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    addArgs.add("--ar4k.dnsConfig=demo1.rossonet.name");
//    addArgs.add("--ar4k.baseConfig=");
    addArgs.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    addArgs.add("--ar4k.dnsKeystore=ks1.rossonet.name");
    addArgs.add("--ar4k.keystoreMainAlias=\"ca\"");
    addArgs.add("--ar4k.keystorePassword=\"secA4.rk!8\"");
    addArgs.add("--ar4k.beaconCaChainPem=a4c8ff551a");
    addArgs.add("--ar4k.adminPassword=a4c8ff551a");
//    addArgs.add("--ar4k.webRegistrationEndpoint=");
//    addArgs.add("--ar4k.dnsRegistrationEndpoint=");
    addArgs.add("--ar4k.beaconDiscoveryFilterString=AR4K");
    addArgs.add("--ar4k.beaconDiscoveryPort=33666");
    addArgs.add("--ar4k.fileConfigOrder=1");
    addArgs.add("--ar4k.webConfigOrder=2");
    addArgs.add("--ar4k.dnsConfigOrder=0");
    addArgs.add("--ar4k.baseConfigOrder=3");
    addArgs.add("--ar4k.threadSleep=500");
    addArgs.add("--ar4k.logoUrl=/static/img/ar4k.png");
    testAnimas.add(
        executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log", "a.ks", 1124, addArgs)).get());
    testAnimas.add(
        executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log", "b.ks", 1125, addArgs)).get());
    testAnimas.add(
        executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log", "c.ks", 1126, addArgs)).get());
    Thread.sleep(20000);
    for (Anima a : testAnimas) {
      Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
    }
    Thread.sleep(20000);
  }

}
