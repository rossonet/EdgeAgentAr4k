package org.ar4k.agent.pcap.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.PcapMessage;
import org.ar4k.agent.core.data.messages.PcapPayload;
import org.ar4k.agent.core.data.messages.PcapPayload.PacketType;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class PcapSnifferService implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(PcapSnifferService.class);

	private PcapSnifferConfig configuration;
	private Homunculus homunculus;
	private DataAddress dataspace;

	private Map<PcapReader, IPublishSubscribeChannel> readerChannels = new HashMap<>();

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	@Override
	public void init() {
		for (PcapReader reader : configuration.readers) {
			createChannelReader(reader);
		}
		serviceStatus = ServiceStatus.RUNNING;
	}

	private void createChannelReader(PcapReader reader) {
		IPublishSubscribeChannel readerChannel = (IPublishSubscribeChannel) (getDataAddress().createOrGetDataChannel(
				reader.channel, IPublishSubscribeChannel.class,
				"pcap reader on " + reader.interfaceName + " with " + reader.pcapFilter, (String) null, null,
				ConfigHelper.mergeTags(Arrays.asList("pcap", reader.interfaceName.replace("/", "-")),
						getConfiguration().getTags()),
				this));
		readerChannels.put(reader, readerChannel);
		String runningFilter = reader.pcapFilter;
		if (runningFilter == null)
			runningFilter = "";
		PcapHandle handle = null;
		try {
			PacketListener listener = new PacketListener() {
				@Override
				public void gotPacket(Packet packet) {
					try {
						readerChannel.send(createMessage(packet));
					} catch (Exception e) {
						logger.logException(e);
					}
				}

			};
			PcapNetworkInterface nif = Pcaps.getDevByName(reader.interfaceName);
			int snapLen = 65536;
			PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
			int timeout = 10;
			handle = nif.openLive(snapLen, mode, timeout);
			handle.setFilter(runningFilter, BpfCompileMode.OPTIMIZE);
			handle.loop(-1, listener);
			handle.close();
		} catch (Exception e) {
			if (handle != null)
				handle.close();
			logger.logException(e);
		}
	}

	private PcapMessage createMessage(Packet packet) {
		final PcapPayload payload = new PcapPayload();
		if (packet instanceof IpV4Packet) {
			IpV4Packet ipPacket = (IpV4Packet) packet;
			payload.setDstIp(ipPacket.getHeader().getDstAddr().getHostAddress());
			payload.setSrcIp(ipPacket.getHeader().getSrcAddr().getHostAddress());
			if (packet instanceof TcpPacket) {
				TcpPacket tcpPacket = (TcpPacket) packet;
				payload.setPacketType(PacketType.TCP);
				payload.setDstPort(tcpPacket.getHeader().getDstPort().value());
				payload.setSrcPort(tcpPacket.getHeader().getSrcPort().value());
			} else if (packet instanceof UdpPacket) {
				UdpPacket udpPacket = (UdpPacket) packet;
				payload.setPacketType(PacketType.UDP);
				payload.setDstPort(udpPacket.getHeader().getDstPort().value());
				payload.setSrcPort(udpPacket.getHeader().getSrcPort().value());
			} else if (packet instanceof IcmpV4CommonPacket) {
				IcmpV4CommonPacket icmpPacket = (IcmpV4CommonPacket) packet;
				payload.setPacketType(PacketType.ICMP);
				payload.setIcmpCode(icmpPacket.getHeader().getCode().toString());
			}
		} else if (packet instanceof ArpPacket) {
			ArpPacket arpPacket = (ArpPacket) packet;
			payload.setPacketType(PacketType.ARP);
			payload.setSrcMacAddress(arpPacket.getHeader().getSrcHardwareAddr().toString());
			payload.setDstMacAddress(arpPacket.getHeader().getDstHardwareAddr().toString());
			payload.setDstIp(arpPacket.getHeader().getDstProtocolAddr().toString());
			payload.setSrcIp(arpPacket.getHeader().getSrcProtocolAddr().toString());
			payload.setArpOperation(arpPacket.getHeader().getOperation().toString());
		}
		payload.setBytesDataHeader(packet.getHeader().getRawData());
		payload.setBytesDataPayload(packet.getPayload().getRawData());
		final PcapMessage message = new PcapMessage();
		message.setPayload(payload);
		return message;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (PcapSnifferConfig) configuration;
	}

	@Override
	public void close() throws Exception {
		for (IPublishSubscribeChannel rc : readerChannels.values()) {
			rc.close();
		}
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return serviceStatus;
	}

	@Override
	public void kill() {
		try {
			close();
		} catch (final Exception e) {
			logger.logException(e);
		}
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		this.dataspace = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PcapSnifferService [");
		if (configuration != null) {
			builder.append("configuration=");
			builder.append(configuration);
			builder.append(", ");
		}
		if (homunculus != null) {
			builder.append("homunculus=");
			builder.append(homunculus);
			builder.append(", ");
		}
		if (dataspace != null) {
			builder.append("dataspace=");
			builder.append(dataspace);
			builder.append(", ");
		}
		if (readerChannels != null) {
			builder.append("readerChannels=");
			builder.append(readerChannels);
			builder.append(", ");
		}
		if (serviceStatus != null) {
			builder.append("serviceStatus=");
			builder.append(serviceStatus);
		}
		builder.append("]");
		return builder.toString();
	}

}
