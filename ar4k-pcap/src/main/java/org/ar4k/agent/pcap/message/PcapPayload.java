package org.ar4k.agent.pcap.message;

public class PcapPayload {

	public enum PacketType {
		ARP, ICMP, TCP, UDP
	}

	private String arpOperation;

	private String dstIp;

	private String dstMacAddress;

	private int dstPort;

	private byte[] bytesDataHeader;

	private byte[] bytesDataPayload;

	private String icmpCode;

	private PacketType packetType;

	private String srcIp;

	private String srcMacAddress;

	private int srcPort;

	public String getArpOperation() {
		return arpOperation;
	}

	public String getDstIp() {
		return dstIp;
	}

	public String getDstMacAddress() {
		return dstMacAddress;
	}

	public int getDstPort() {
		return dstPort;
	}

	public byte[] getBytesDataHeader() {
		return bytesDataHeader;
	}

	public byte[] getBytesDataPayload() {
		return bytesDataPayload;
	}

	public String getIcmpCode() {
		return icmpCode;
	}

	public PacketType getPacketType() {
		return packetType;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public String getSrcMacAddress() {
		return srcMacAddress;
	}

	public int getSrcPort() {
		return srcPort;
	}

	public void setArpOperation(String arpOperation) {
		this.arpOperation = arpOperation;

	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public void setDstMacAddress(String dstMacAddress) {
		this.dstMacAddress = dstMacAddress;
	}

	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}

	public void setBytesDataHeader(byte[] bytesDataHeader) {
		this.bytesDataHeader = bytesDataHeader;
	}

	public void setBytesDataPayload(byte[] bytesDataPayload) {
		this.bytesDataPayload = bytesDataPayload;
	}

	public void setIcmpCode(String icmpCode) {
		this.icmpCode = icmpCode;
	}

	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public void setSrcMacAddress(String srcMacAddress) {
		this.srcMacAddress = srcMacAddress;
	}

	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PcapPayload [");
		if (arpOperation != null) {
			builder.append("arpOperation=");
			builder.append(arpOperation);
			builder.append(", ");
		}
		if (dstIp != null) {
			builder.append("dstIp=");
			builder.append(dstIp);
			builder.append(", ");
		}
		if (dstMacAddress != null) {
			builder.append("dstMacAddress=");
			builder.append(dstMacAddress);
			builder.append(", ");
		}
		builder.append("dstPort=");
		builder.append(dstPort);
		builder.append(", ");
		if (icmpCode != null) {
			builder.append("icmpCode=");
			builder.append(icmpCode);
			builder.append(", ");
		}
		if (packetType != null) {
			builder.append("packetType=");
			builder.append(packetType);
			builder.append(", ");
		}
		if (srcIp != null) {
			builder.append("srcIp=");
			builder.append(srcIp);
			builder.append(", ");
		}
		if (srcMacAddress != null) {
			builder.append("srcMacAddress=");
			builder.append(srcMacAddress);
			builder.append(", ");
		}
		builder.append("srcPort=");
		builder.append(srcPort);
		builder.append("]");
		return builder.toString();
	}

}
