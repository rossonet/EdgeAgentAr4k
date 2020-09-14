/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.qa.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.interfaces.IBeaconClient;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgekuserDetailsService;
import org.ar4k.agent.tunnels.http.beacon.BeaconClient;
import org.ar4k.agent.tunnels.http.beacon.BeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.assertj.core.util.Files;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Homunculus.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class, EdgekuserDetailsService.class,
    EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GrpcBeaconTests {

  BeaconServer server = null;
  IBeaconClient client = null;
  RpcConversation rpcConversation = null;
  int port = 2569;

  @Autowired
  Homunculus homunculus;

  @Before
  public void setUp() throws Exception {
    System.out.println("Waiting anima");
    while (!homunculus.getState().equals(HomunculusStates.STAMINAL)) {
      System.out.println("waiting anima, actual state: " + homunculus.getState());
      Thread.sleep(1500L);
    }
    Thread.sleep(1500L);
    System.out.println("Beacon server starting");
    server = new BeaconServer.Builder().setHomunculus(homunculus).setPort(port)
        .setStringDiscovery("AR4K-BEACON-" + UUID.randomUUID().toString()).setBroadcastAddress("255.255.255.255")
        .setAcceptCerts(true).setAliasBeaconServerInKeystore(homunculus.getMyAliasCertInKeystore()).build();
    server.start();
    System.out.println("Beacon server started");
    Thread.sleep(3000L);
  }

  private void changeLogLevel(String gwlog) {
    for (ch.qos.logback.classic.Logger l : findAllLogger()) {
      l.setLevel(ch.qos.logback.classic.Level.toLevel(gwlog, ch.qos.logback.classic.Level.INFO));
    }
  }

  public Collection<ch.qos.logback.classic.Logger> findAllLogger() {
    return getLoggerContext().getLoggerList();
  }

  private LoggerContext getLoggerContext() {
    return ContextSelectorStaticBinder.getSingleton().getContextSelector().getLoggerContext();
  }

  @After
  public void tearDown() throws Exception {
    if (client != null)
      client.shutdown();
    if (server != null) {
      server.close();
    }
    Files.delete(new File("/home/andrea/.ar4k/default.keystore"));
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void implementTest() {
    try {
      Thread.sleep(6000L);
      System.out.println("looking for alias " + homunculus.getMyAliasCertInKeystore());
      System.out.println("in keystore certificates " + homunculus.getMyIdentityKeystore().listCertificate());
      System.out.println("Test completed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void implementTestClass() throws UnrecoverableKeyException {
    try {
      Thread.sleep(2000L);
      client = new BeaconClient.Builder().setHomunculus(homunculus).setPort(port).setRpcConversation(rpcConversation)
          .setHost("localhost").setDiscoveryPort(0).setAliasBeaconClientInKeystore(homunculus.getMyAliasCertInKeystore())
          .build();
      Thread.sleep(10000L);
      String ls = client.getStateConnection().name();
      System.out.println("LAST STATE: " + ls);
      assertEquals("READY", ls);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRegistration() throws UnrecoverableKeyException {
    try {
      changeLogLevel("DEBUG");
      Thread.sleep(2000L);
      client = new BeaconClient.Builder().setHomunculus(homunculus).setPort(port).setRpcConversation(rpcConversation)
          .setHost("localhost").setDiscoveryPort(0).setAliasBeaconClientInKeystore(homunculus.getMyAliasCertInKeystore())
          .build();
      Thread.sleep(2000L);
      String ls = client.getStateConnection().name();
      System.out.println("LAST STATE: " + ls);
      changeLogLevel("INFO");
      assertEquals("READY", ls);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void checkRemoteList() throws Exception {
    client = new BeaconClient.Builder().setHomunculus(homunculus).setPort(port).setRpcConversation(rpcConversation)
        .setHost("localhost").setDiscoveryPort(0).setAliasBeaconClientInKeystore(homunculus.getMyAliasCertInKeystore())
        .build();
    Thread.sleep(6000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    // assertEquals("READY", ls);
    // server.blockUntilShutdown();
    // String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out.println("REGISTER STATUS: [register code] " + client.getAgentUniqueName());
    // assertEquals("GOOD", status);
    Thread.sleep(6000L);
    List<Agent> list = client.listAgentsConnectedToBeacon();
    assertEquals(list.isEmpty(), false);
    System.out.println("I'm " + list.get(0).getAgentUniqueName());
    Thread.sleep(6000L);
  }

  @Test
  public void checkDiscoveryRegistration()
      throws InterruptedException, IOException, ParseException, UnrecoverableKeyException {
    client = new BeaconClient.Builder().setHomunculus(homunculus).setPort(port).setRpcConversation(rpcConversation)
        .setHost("localhost").setDiscoveryPort(0).setAliasBeaconClientInKeystore(homunculus.getMyAliasCertInKeystore())
        .build();
    Thread.sleep(12000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
  }

}
