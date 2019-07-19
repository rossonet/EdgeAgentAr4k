import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.ar4k.agent.config.tribe.TribeConfig;
import org.ar4k.agent.tribe.AtomixTribeComponent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.atomix.core.Atomix;
import io.atomix.utils.time.Versioned;

import static org.junit.Assert.assertTrue;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class AtomixClusterTest {

  String host = null;

  TribeConfig tc1 = new TribeConfig();
  TribeConfig tc2 = new TribeConfig();
  TribeConfig tc3 = new TribeConfig();
  AtomixTribeComponent n1 = null;
  AtomixTribeComponent n2 = null;
  AtomixTribeComponent n3 = null;

  @Before
  public void setUp() throws Exception {
    host = getLocalAddress().getHostAddress();
    Set<String> setClients = new HashSet<String>();
    setClients.add("test1");
    setClients.add("test2");
    setClients.add("test3");
    // setClients.add("test3");
    tc1.name = "test1";
    tc2.name = "test2";
    tc3.name = "test3";
    tc1.hostBind = host;
    tc2.hostBind = host;
    tc3.hostBind = host;
    tc1.port = 10001;
    tc2.port = 10002;
    tc3.port = 10003;
    tc1.joinLinks = setClients;
    tc2.joinLinks = setClients;
    tc3.joinLinks = setClients;
  }

  @After
  public void tearDown() throws Exception {
    n1.kill();
    n2.kill();
    n3.kill();
  }

  @Test
  public void testCreateThreeNodes() throws InterruptedException {
    createNodes();
    Thread.sleep(1000 * 20);
    System.out.println(n1.listAtomixNodes());
    System.out.println(n2.listAtomixNodes());
    System.out.println(n3.listAtomixNodes());
    System.out.println(n1.getStatusString());
    System.out.println(n2.getStatusString());
    System.out.println(n3.getStatusString());
    Atomix a = n1.getAtomix();
    System.out.println(a.getPrimitives());
    Thread.sleep(1000 * 10);
    String exampleValue = UUID.randomUUID().toString();
    n1.getAtomixMap().put("exampleData", exampleValue);
    System.out.println("list on node 2 ->");
    for (String line : n2.getAtomixMap().keySet()) {
      System.out.println(line);
    }
    assertTrue(n2.getAtomixMap().keySet().contains("exampleData"));
    Versioned<Object> vOn3 = n3.getAtomixMap().get("exampleData");
    System.out.println("value on node 3 -> " + vOn3.value().toString());
    assertTrue(vOn3.value().toString().equals(exampleValue));
    Thread.sleep(1000 * 30);
  }

  @Test
  public void testChat() throws InterruptedException {
    createNodes();
    Thread.sleep(1000 * 20);
    System.out.println(n1.listAtomixNodes());
    System.out.println(n2.listAtomixNodes());
    System.out.println(n3.listAtomixNodes());
    System.out.println(n1.getStatusString());
    System.out.println(n2.getStatusString());
    System.out.println(n3.getStatusString());
    Atomix a = n1.getAtomix();
    System.out.println(a.getPrimitives());
    Thread.sleep(1000 * 10);
    n1.sendChatMessage("This is a chat message");
    Thread.sleep(1000 * 30);
  }

  private void createNodes() {
    System.out.println(tc1.name);
    System.out.println(tc2.name);
    System.out.println(tc3.name);
    n1 = (AtomixTribeComponent) tc1.instantiate();
    n2 = (AtomixTribeComponent) tc2.instantiate();
    n3 = (AtomixTribeComponent) tc3.instantiate();
    n1.init();
    n2.init();
    n3.init();
  }

  private static InetAddress getLocalAddress() throws SocketException {
    Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
    while (ifaces.hasMoreElements()) {
      NetworkInterface iface = ifaces.nextElement();
      Enumeration<InetAddress> addresses = iface.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress addr = addresses.nextElement();
        if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
          return addr;
        }
      }
    }
    return null;
  }

}
