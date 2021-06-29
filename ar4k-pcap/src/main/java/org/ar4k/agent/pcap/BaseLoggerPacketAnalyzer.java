package org.ar4k.agent.pcap;

import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

@Ar4kPcapAnalyzer
public class BaseLoggerPacketAnalyzer implements PackerAnalyzer {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BaseLoggerPacketAnalyzer.class);

	@Override
	public void elaboratePacket(Packet packet) throws IOException {
		if (packet != null) {
			if (packet.contains(IpPacket.class)) {
				IpPacket ipp = packet.get(IpPacket.class);
				logger.info("IP srcAddress: " + ipp.getHeader().getSrcAddr().getHostAddress() + " dstAddress: "
						+ ipp.getHeader().getDstAddr().getHostAddress());
			}
			if (packet.contains(UdpPacket.class)) {
				UdpPacket udpp = packet.get(UdpPacket.class);
				logger.info("UDP srcPort: " + udpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
						+ udpp.getHeader().getDstPort().valueAsInt());
			}
			if (packet.contains(TcpPacket.class)) {
				TcpPacket tcpp = packet.get(TcpPacket.class);
				logger.info("TCP srcPort: " + tcpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
						+ tcpp.getHeader().getDstPort().valueAsInt());
			}
			if (packet.contains(IcmpV4CommonPacket.class)) {
				IcmpV4CommonPacket icpp = packet.get(IcmpV4CommonPacket.class);
				logger.info("ICMP name: " + icpp.getHeader().getCode().name() + " value: "
						+ icpp.getHeader().getCode().valueAsString());
			}
			logger.info("Payload: " + Hex.encodeHexString(packet.getPayload().getRawData()) + "\n");
		}
	}

}
