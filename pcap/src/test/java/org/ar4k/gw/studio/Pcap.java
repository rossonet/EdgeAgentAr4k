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
package org.ar4k.gw.studio;

import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.codec.binary.Hex;
import org.ar4k.agent.pcap.ice.IcePacketAnalyzer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;

public class Pcap {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void listDevices() {
    try {
      List<PcapNetworkInterface> lista = Pcaps.findAllDevs();
      for (PcapNetworkInterface a : lista) {
        System.out.println(a.getName());
      }

    } catch (PcapNativeException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void checkDecoder() {
    char[] files = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'l', 'm' };
    for (char test : files) {
      String nomeFile = "example/" + test + ".pcap";
      filePcap(nomeFile);
    }
  }

  @Test
  // necessita dei permessi di root
  public void checkSender() {
    char[] files = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'l', 'm' };
    for (char test : files) {
      String nomeFile = "example/" + test + ".pcap";
      sendDataFromPcapFile(nomeFile, "wlp1s0", 200);
    }
  }

  private void filePcap(String file) {
    PcapHandle handle;
    try {
      handle = Pcaps.openOffline(file);
      Packet packet = null;
      boolean continua = true;
      IcePacketAnalyzer pa = new IcePacketAnalyzer();
      while (continua) {
        try {
          packet = handle.getNextPacketEx();
        } catch (Exception a) {
          continua = false;
        }
        pa.elaboratePacket(packet);
      }
      handle.close();
    } catch (PcapNativeException | IOException e) {
      e.printStackTrace();
    }
  }

  // necessita dei permessi di root
  private void sendDataFromPcapFile(String file, String interfaceName, long waitTime) {
    PcapHandle handleFile;
    PcapHandle handleSender;
    try {
      handleFile = Pcaps.openOffline(file);
      int snapLen = 65536;
      PromiscuousMode mode = PromiscuousMode.NONPROMISCUOUS;
      PcapNetworkInterface nif = Pcaps.getDevByName(interfaceName);
      int timeout = 10000;
      handleSender = nif.openLive(snapLen, mode, timeout);
      Packet packet = null;
      boolean continua = true;
      while (continua) {
        try {
          packet = handleFile.getNextPacketEx();
        } catch (Exception a) {
          continua = false;
        }
        try {
          IpPacket ipp = packet.get(IpPacket.class);
          System.out.println("srcAddress: " + ipp.getHeader().getSrcAddr().getHostAddress() + " dstAddress: "
              + ipp.getHeader().getDstAddr().getHostAddress());
        } catch (Exception a) {
        }
        try {
          UdpPacket udpp = packet.get(UdpPacket.class);
          System.out.println("srcPort: " + udpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
              + udpp.getHeader().getDstPort().valueAsInt());
        } catch (Exception a) {
        }
        if (packet != null) {
          System.out.println(Hex.encodeHexString(packet.getRawData()));
        }
        handleSender.sendPacket(packet);
        Thread.sleep(waitTime);
      }
      handleFile.close();
    } catch (PcapNativeException | NotOpenException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  // necessita dei permessi di root
  public void basePcap() {
    InetAddress addr = null;
    try {
      addr = InetAddress.getByName("192.168.0.158");
      PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
      int snapLen = 65536;
      PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
      int timeout = 10;
      PcapHandle handle = nif.openLive(snapLen, mode, timeout);
      Packet packet = null;
      packet = handle.getNextPacketEx();
      while (packet != null) {
        packet = handle.getNextPacketEx();
        IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
        Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
        System.out.println(srcAddr);
      }
      handle.close();
    } catch (UnknownHostException | PcapNativeException | EOFException | TimeoutException | NotOpenException e) {
      e.printStackTrace();
    }
  }

}
