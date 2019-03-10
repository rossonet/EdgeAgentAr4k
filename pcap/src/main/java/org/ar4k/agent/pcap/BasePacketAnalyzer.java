package org.ar4k.agent.pcap;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Option;
import org.pcap4j.packet.Packet;

public class BasePacketAnalyzer implements PackerAnalyzer {

  @Override
  public void elaboratePacket(Packet packet) throws IOException {
    IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
    Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
    List<IpV4Option> options = ipV4Packet.getHeader().getOptions();
    System.out.println("src host: " + srcAddr);
    System.out.println(Hex.encodeHexString(ipV4Packet.getHeader().getRawData()));
    for (IpV4Option o : options) {
      System.out.println("option " + o.getType() + " -> " + o.length());
    }

  }

}
