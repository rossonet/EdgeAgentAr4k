package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.ar4k.agent.pcap.PackerAnalyzer;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.IpV4Packet.IpV4Option;
import org.pcap4j.packet.namednumber.UdpPort;

public class IcePacketAnalyzer implements PackerAnalyzer {

  @Override
  public void elaboratePacket(Packet packet) throws IOException {
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
      } else if (messageType == 'D') {
        MarketSnapshotOrderMessage messageMarketSnapshotOrderMessage = new MarketSnapshotOrderMessage();
        messageMarketSnapshotOrderMessage.valueOf(payload);
        System.out.println(messageMarketSnapshotOrderMessage);
      } else {
        System.out.println("Pacchetto sconosciuto!!");
        System.out.println(Hex.encodeHexString(payload));
        System.out.println("\n");
        break;
      }
    }
    // System.out.println(Hex.encodeHexString(udpPacket.getPayload().getRawData()));
    for (IpV4Option o : options) {
      System.out.println("option " + o.getType() + " -> " + o.length());
    }

  }

}
