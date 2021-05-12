package org.ar4k.agent.pcap.service;

import java.util.UUID;

import com.beust.jcommander.Parameter;

public class PcapReader {

	@Parameter(names = "--interfaceName", description = "network interface to sniff")
	public String interfaceName = null;

	@Parameter(names = "--pcapFilter", description = "pcap filter -https://wiki.wireshark.org/CaptureFilters-")
	public String pcapFilter = null;

	@Parameter(names = "--dataQueue", description = "channel for output data")
	public String channel = null;

	public String uuid = UUID.randomUUID().toString();

}