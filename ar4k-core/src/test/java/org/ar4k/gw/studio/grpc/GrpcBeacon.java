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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.tunnels.http.grpc.BeaconClient;
import org.ar4k.agent.tunnels.http.grpc.BeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class GrpcBeacon {

  BeaconServer server = null;
  BeaconClient client = null;
  RpcConversation rpcConversation = null;
  int port = 2569;
  Anima anima = new Anima();

  @Before
  public void setUp() throws Exception {
    anima.setAgentUniqueName(UUID.randomUUID().toString().replaceAll("-", ""));
    KeystoreConfig ks = new KeystoreConfig();
    ks.create(anima.getAgentUniqueName(), Anima.organization, Anima.unit, Anima.locality, Anima.state, Anima.country,
        Anima.uri, Anima.dns, Anima.ip);
    anima.setMyIdentityKeystore(ks);
    server = new BeaconServer(anima, port, 33666, "255.255.255.255", false,
        "AR4K-BEACON-" + UUID.randomUUID().toString());
    server.start();
    Thread.sleep(3000L);
    // client = new BeaconClient(rpcConversation, "127.0.0.1", port);
  }

  @After
  public void tearDown() throws Exception {
    client.shutdown();
    server.stop();
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void implementTestClass() throws InterruptedException, IOException {
    client = new BeaconClient(anima, rpcConversation, "127.0.0.1", port, 0, null, null);
    Thread.sleep(6000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
  }

  @Test
  public void testRegistration() throws InterruptedException, IOException, ParseException {
    client = new BeaconClient(anima, rpcConversation, "127.0.0.1", port, 0, null, null);
    Thread.sleep(6000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
    // String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out
        .println("REGISTER STATUS: " + client.getStateConnection() + " [register code] " + client.getAgentUniqueName());
    assertEquals("READY", client.getStateConnection().toString());
  }

  @Test
  public void checkRemoteList() throws InterruptedException, IOException, ParseException {
    client = new BeaconClient(anima, rpcConversation, "127.0.0.1", port, 0, null, null);
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
    client = new BeaconClient(anima, rpcConversation, "127.0.0.1", 0, 33666, "AR4K", UUID.randomUUID().toString());
    Thread.sleep(12000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
  }

}
