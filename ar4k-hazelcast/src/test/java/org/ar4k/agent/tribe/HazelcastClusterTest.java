package org.ar4k.agent.tribe;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.ar4k.agent.hazelcast.HazelcastComponent;
import org.ar4k.agent.hazelcast.HazelcastConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HazelcastClusterTest {

  String host = null;

  HazelcastConfig tc1 = new HazelcastConfig();
  HazelcastConfig tc2 = new HazelcastConfig();
  HazelcastConfig tc3 = new HazelcastConfig();
  HazelcastComponent n1 = null;
  HazelcastComponent n2 = null;
  HazelcastComponent n3 = null;

  @Before
  public void setUp() throws Exception {
    tc1.setBeanName("hz-bean1");
    tc2.setBeanName("hz-bean2");
    tc3.setBeanName("hz-bean3");
    tc1.setMultiCastEnable(true);
    tc2.setMultiCastEnable(true);
    tc3.setMultiCastEnable(true);
  }

  @After
  public void tearDown() throws Exception {
    n1.kill();
    n2.kill();
    n3.kill();
  }

  private void createNodes() {
    System.out.println(tc1.name);
    System.out.println(tc2.name);
    System.out.println(tc3.name);
    n1 = (HazelcastComponent) tc1.instantiate();
    n2 = (HazelcastComponent) tc2.instantiate();
    n3 = (HazelcastComponent) tc3.instantiate();
    n1.init();
    n2.init();
    n3.init();
  }

  @Test
  public void testCreateThreeNodes() throws InterruptedException {
    createNodes();
    Thread.sleep(1000 * 20);

    Thread.sleep(1000 * 30);
  }

  @SuppressWarnings("unused")
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
