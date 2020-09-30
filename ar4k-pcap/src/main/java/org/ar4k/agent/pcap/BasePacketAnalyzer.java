package org.ar4k.agent.pcap;

import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

@Ar4kPcapAnalyzer
public class BasePacketAnalyzer implements PackerAnalyzer {

  @Override
  public void elaboratePacket(Packet packet) throws IOException {
    if (packet != null) {
      if (packet.contains(IpPacket.class)) {
        IpPacket ipp = packet.get(IpPacket.class);
        System.out.println("IP srcAddress: " + ipp.getHeader().getSrcAddr().getHostAddress() + " dstAddress: "
            + ipp.getHeader().getDstAddr().getHostAddress());
      }
      if (packet.contains(UdpPacket.class)) {
        UdpPacket udpp = packet.get(UdpPacket.class);
        System.out.println("UDP srcPort: " + udpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
            + udpp.getHeader().getDstPort().valueAsInt());
      }
      if (packet.contains(TcpPacket.class)) {
        TcpPacket tcpp = packet.get(TcpPacket.class);
        System.out.println("TCP srcPort: " + tcpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
            + tcpp.getHeader().getDstPort().valueAsInt());
      }
      if (packet.contains(IcmpV4CommonPacket.class)) {
        IcmpV4CommonPacket icpp = packet.get(IcmpV4CommonPacket.class);
        System.out.println("ICMP name: " + icpp.getHeader().getCode().name() + " value: "
            + icpp.getHeader().getCode().valueAsString());
      }
      System.out.println("Payload: " + Hex.encodeHexString(packet.getPayload().getRawData()) + "\n");
    }
  }

}
