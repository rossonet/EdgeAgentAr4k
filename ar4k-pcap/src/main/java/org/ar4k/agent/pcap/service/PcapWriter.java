package org.ar4k.agent.pcap.service;

import java.util.UUID;

import com.beust.jcommander.Parameter;

public class PcapWriter {

	@Parameter(names = "--interfaceName", description = "network interface to write to")
	public String interfaceName = null;

	@Parameter(names = "--dataQueue", description = "channel for input data")
	public String channel = null;

	public String uuid = UUID.randomUUID().toString();

}
