package org.ar4k.agent.pcap;

import java.io.IOException;

import org.ar4k.agent.pcap.message.PcapMessage;
import org.pcap4j.packet.Packet;

public interface PackerServiceAnalyzer {

  public PcapMessage elaboratePacket(Packet packet) throws IOException;

}
