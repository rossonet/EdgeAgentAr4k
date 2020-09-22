package org.ar4k.agent.farm;

public interface ManagedNetworkInterface {

	public enum TrafficType {
		UDP, TCP
	}

	public int getPortOnHost();

	public int getPortOnContainer();

	public TrafficType getTrafficType();

	public String getLabel();

	public String getReferenceString();

	public String getLog();
}
