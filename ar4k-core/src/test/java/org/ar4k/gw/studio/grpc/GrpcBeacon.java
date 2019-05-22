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

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.tunnels.http.grpc.BeaconClient;
import org.ar4k.agent.tunnels.http.grpc.BeaconServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class GrpcBeacon {

  BeaconServer server = null;
  BeaconClient client = null;
  int port = 2569;

  @Before
  public void setUp() throws Exception {
    server = new BeaconServer(port);
    server.start();
    Thread.sleep(3000L);
    client = new BeaconClient("127.0.0.1", port);
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
    Thread.sleep(6000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
  }

  @Test
  public void testRegistration() throws InterruptedException, IOException, ParseException {
    Thread.sleep(6000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
    String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out.println("REGISTER STATUS: " + status + " [register code] " + client.getAgentUniqueName());
    assertEquals("GOOD", status);
  }

}
