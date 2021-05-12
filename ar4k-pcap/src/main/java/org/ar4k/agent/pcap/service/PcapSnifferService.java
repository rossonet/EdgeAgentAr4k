package org.ar4k.agent.pcap.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.pcap.message.PcapMessage;
import org.ar4k.agent.pcap.message.PcapPayload;
import org.ar4k.agent.pcap.message.PcapPayload.PacketType;
import org.json.JSONObject;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class PcapSnifferService implements EdgeComponent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(PcapSnifferService.class.toString());

	private PcapSnifferConfig configuration;
	private Homunculus homunculus;
	private DataAddress dataspace;

	private Map<PcapReader, IPublishSubscribeChannel> readerChannels = new HashMap<>();
	private Map<PcapWriter, IPublishSubscribeChannel> writeChannels = new HashMap<>();

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	@Override
	public void init() {
		for (PcapWriter writer : configuration.writers) {
			createChannelwriter(writer);
		}
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
		} catch (PcapNativeException | InterruptedException | NotOpenException e) {
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

	private void createChannelwriter(PcapWriter writer) {
		IPublishSubscribeChannel writeChannel = (IPublishSubscribeChannel) (getDataAddress().createOrGetDataChannel(
				writer.channel, IPublishSubscribeChannel.class, "pcap writer on " + writer.interfaceName, (String) null,
				null, ConfigHelper.mergeTags(Arrays.asList("pcap", writer.interfaceName.replace("/", "-")),
						getConfiguration().getTags()),
				this));
		try {
			final PcapNetworkInterface nif = Pcaps.getDevByName(writer.interfaceName);
			final int timeout = 10000;
			final int snapLen = 65536;
			final PromiscuousMode mode = PromiscuousMode.NONPROMISCUOUS;
			writeChannels.put(writer, writeChannel);
			final MessageHandler writeHandler = new MessageHandler() {
				@Override
				public void handleMessage(Message<?> message) throws MessagingException {
					if (message instanceof PcapMessage) {
						try {
							sendByInterface((PcapMessage) message, nif.openLive(snapLen, mode, timeout));
						} catch (Exception exception) {
							logger.logException(exception);
						}
					}
				}

			};
			writeChannel.subscribe(writeHandler);
		} catch (PcapNativeException ee) {
			logger.logException(ee);
		}
	}

	private void sendByInterface(PcapMessage message, PcapHandle handleSender)
			throws PcapNativeException, NotOpenException {
		Packet packet = null;// TODO
		handleSender.sendPacket(packet);
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
		for (IPublishSubscribeChannel wc : writeChannels.values()) {
			wc.close();
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
	public JSONObject getDescriptionJson() {
		final Gson gson = new GsonBuilder().create();
		return new JSONObject(gson.toJsonTree(configuration).getAsString());
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
