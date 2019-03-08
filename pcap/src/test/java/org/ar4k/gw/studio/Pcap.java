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

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.codec.binary.Hex;
import org.ar4k.agent.pcap.ice.AddModifyOrderMessage;
import org.ar4k.agent.pcap.ice.DeleteOrderMessage;
import org.ar4k.agent.pcap.ice.Helper;
import org.ar4k.agent.pcap.ice.MarketSnapshotMessage;
import org.ar4k.agent.pcap.ice.MarketStatisticsMessage;
import org.ar4k.agent.pcap.ice.MessageBundleMarker;
import org.ar4k.agent.pcap.ice.OpenPriceMessage;
import org.ar4k.agent.pcap.ice.TradeMessage;
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
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Option;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.UdpPort;

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
  public void filePcap() {
    PcapHandle handle;
    try {
      handle = Pcaps.openOffline(
          "/home/andrea/git/EdgeAgentAr4k/pcap/example/PCAP_20190204.080000.090000.ICE_ENDEX.FOD_LU.208_101.A.02.pcap.00014");
      Packet packet = null;
      boolean continua = true;
      while (continua) {
        try {
          packet = handle.getNextPacketEx();
        } catch (Exception a) {
          continua = false;
        }
        IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
        UdpPacket udpPacket = packet.get(UdpPacket.class);
        Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
        UdpPort srcPort = udpPacket.getHeader().getSrcPort();
        List<IpV4Option> options = ipV4Packet.getHeader().getOptions();
        InputStream targetStream = new ByteArrayInputStream(udpPacket.getPayload().getRawData());
        Short session = Helper.popShort(targetStream);
        Integer sequence = Helper.popInteger(targetStream);
        Short count = Helper.popShort(targetStream);
        Long timestamp = Helper.popLong(targetStream);
        System.out.println(srcAddr + ":" + srcPort.valueAsString() + " -> " + session + " | " + sequence + " | " + count
            + " | " + timestamp);
        for (int m = 0; m < count; m++) {
          char messageType = Helper.popChar(targetStream);
          Short messageBodyLength = Helper.popShort(targetStream);
          byte[] payload = new byte[messageBodyLength];
          targetStream.read(payload, 0, messageBodyLength);
          System.out.println(" - " + m + " -> " + messageType + " | " + messageBodyLength);
          if (messageType == 'T') {
            MessageBundleMarker messageBundleMarker = new MessageBundleMarker();
            messageBundleMarker.valueOf(payload);
            System.out.println(messageBundleMarker);
          } else if (messageType == 'E') {
            AddModifyOrderMessage messageAddModifyOrderMessage = new AddModifyOrderMessage();
            messageAddModifyOrderMessage.valueOf(payload);
            System.out.println(messageAddModifyOrderMessage);
          } else if (messageType == 'F') {
            DeleteOrderMessage messageDeleteOrderMessage = new DeleteOrderMessage();
            messageDeleteOrderMessage.valueOf(payload);
            System.out.println(messageDeleteOrderMessage);
          } else if (messageType == 'G') {
            TradeMessage messageTradeMessage = new TradeMessage();
            messageTradeMessage.valueOf(payload);
            System.out.println(messageTradeMessage);
          } else if (messageType == 'J') {
            MarketStatisticsMessage messageMarketStatisticsMessage = new MarketStatisticsMessage();
            messageMarketStatisticsMessage.valueOf(payload);
            System.out.println(messageMarketStatisticsMessage);
          } else if (messageType == 'N') {
            OpenPriceMessage messageOpenPriceMessage = new OpenPriceMessage();
            messageOpenPriceMessage.valueOf(payload);
            System.out.println(messageOpenPriceMessage);
          } else if (messageType == 'C') {
            MarketSnapshotMessage messageMarketSnapshotMessage = new MarketSnapshotMessage();
            messageMarketSnapshotMessage.valueOf(payload);
            System.out.println(messageMarketSnapshotMessage);
          } else {
            System.out.println("Pacchetto sconosciuto!!");
            System.out.println(Hex.encodeHexString(payload));
            System.out.println("\n");
            assertFalse(true);
          }
        }
        // System.out.println(Hex.encodeHexString(udpPacket.getPayload().getRawData()));
        for (IpV4Option o : options) {
          System.out.println("option " + o.getType() + " -> " + o.length());
        }
      }
      handle.close();
    } catch (PcapNativeException | IOException e) {
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
