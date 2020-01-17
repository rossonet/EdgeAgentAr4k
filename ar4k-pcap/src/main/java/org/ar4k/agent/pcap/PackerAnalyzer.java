package org.ar4k.agent.pcap;

import java.io.IOException;

import org.pcap4j.packet.Packet;

public interface PackerAnalyzer {

  public void elaboratePacket(Packet packet) throws IOException;

}
