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
package org.ar4k.gw.studio.grpc;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.config.AnimaStateMachineConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.ar4k.agent.tunnels.http.grpc.BeaconClient;
import org.ar4k.agent.tunnels.http.grpc.BeaconServer;
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
import org.springframework.context.annotation.Configuration;
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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Configuration
@Import({
    // Core runtime
    SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class,
    // Various Resolvers
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class,
    // Built-In Commands
    StandardCommandsAutoConfiguration.class,
    // Sample Commands
    Commands.class, Ar4kuserDetailsService.class, Ar4kAuthenticationManager.class, FileValueProvider.class, Anima.class,
    AnimaStateMachineConfig.class, AnimaHomunculus.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GrpcBeaconTests {

  BeaconServer server = null;
  BeaconClient client = null;
  RpcConversation rpcConversation = null;
  int port = 2569;

  @Autowired
  Anima anima;

  @Before
  public void setUp() throws Exception {
    System.out.println("Waiting anima");
    while (!anima.getState().equals(AnimaStates.STAMINAL)) {
      System.out.println("waiting anima, actual state: " + anima.getState());
      Thread.sleep(1500L);
    }
    Thread.sleep(1500L);
    System.out.println("Beacon server starting");
    server = new BeaconServer(anima, port, 33666, "255.255.255.255", true,
        "AR4K-BEACON-" + UUID.randomUUID().toString(), null, null, null, null);
    server.start();
    System.out.println("Beacon server started");
    Thread.sleep(3000L);
    // client = new BeaconClient(rpcConversation, "127.0.0.1", port);
    // Gestione dei certificati
  }

  @After
  public void tearDown() throws Exception {
    if (client != null)
      client.shutdown();
    if (server != null)
      server.stop();
    Files.delete(new File("/home/andrea/.ar4k/default.keystore"));
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void implementTest() {
    try {
      Thread.sleep(6000L);
      System.out.println("looking for alias " + anima.getMyAliasCertInKeystore());
      System.out.println("in keystore certificates " + anima.getMyIdentityKeystore().listCertificate());
      System.out.println("Test completed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void implementTestClass() {
    try {
      Thread.sleep(2000L);
      client = new BeaconClient(anima, rpcConversation, "localhost", port, 0, null, null, null, null, null, null, null);
      Thread.sleep(2000L);
      String ls = client.getStateConnection().name();
      System.out.println("LAST STATE: " + ls);
      assertEquals("READY", ls);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRegistration() {
    try {
      Thread.sleep(2000L);
      client = new BeaconClient(anima, rpcConversation, "127.0.0.1", port, 0, null, null, null, null, null, null, null);
      Thread.sleep(2000L);
      String ls = client.getStateConnection().name();
      System.out.println("LAST STATE: " + ls);
      assertEquals("READY", ls);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void checkRemoteList() throws InterruptedException, IOException, ParseException {
    client = new BeaconClient(anima, rpcConversation, "127.0.0.1", port, 0, null, null, null, null, null, null, null);
    Thread.sleep(6000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
    // String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out.println("REGISTER STATUS: [register code] " + client.getAgentUniqueName());
    // assertEquals("GOOD", status);
    List<Agent> list = client.listAgentsConnectedToBeacon();
    assertEquals(list.isEmpty(), false);
    System.out.println("I'm " + list.get(0).getAgentUniqueName());
  }

  @Test
  public void checkDiscoveryRegistration() throws InterruptedException, IOException, ParseException {
    client = new BeaconClient(anima, rpcConversation, "127.0.0.1", 0, 33666, "AR4K", UUID.randomUUID().toString(), null,
        null, null, null, null);
    Thread.sleep(12000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
  }

}
