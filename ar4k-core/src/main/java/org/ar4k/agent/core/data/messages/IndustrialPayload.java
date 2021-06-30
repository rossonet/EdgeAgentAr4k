package org.ar4k.agent.core.data.messages;

import java.io.Serializable;
import java.util.Date;

public class IndustrialPayload implements Serializable {

	private static final long serialVersionUID = -9045737964796741480L;

	public enum Quality {
		BAD, GOOD, UNKNOW, OVERFLOW, SECURITY_ERROR
	}

	public enum DataType {
		UNKNOW, String, Integer, Boolean, Double, Float, UUID, Short, Long, Byte, UByte, ByteString, DateTime, UShort,
		UInteger, ULong, XmlElement
	}
	private String channel = null;
	private String value = null;
	private Quality quality = Quality.UNKNOW;
	private long receivedTime = new Date().getTime();
	private long productionTime = 0;
	private DataType dataType = DataType.UNKNOW;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public long getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(long receivedTime) {
		this.receivedTime = receivedTime;
	}

	public long getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(long productionTime) {
		this.productionTime = productionTime;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndustrialPayload [");
		if (channel != null) {
			builder.append("channel=");
			builder.append(channel);
			builder.append(", ");
		}
		if (value != null) {
			builder.append("value=");
			builder.append(value);
			builder.append(", ");
		}
		if (quality != null) {
			builder.append("quality=");
			builder.append(quality);
			builder.append(", ");
		}
		builder.append("receivedTime=");
		builder.append(receivedTime);
		builder.append(", productionTime=");
		builder.append(productionTime);
		builder.append(", ");
		if (dataType != null) {
			builder.append("dataType=");
			builder.append(dataType);
		}
		builder.append("]");
		return builder.toString();
	}

}
