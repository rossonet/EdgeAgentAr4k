package org.ar4k.agent.docker;

import java.io.Serializable;

import org.ar4k.agent.farm.ManagedNetworkInterface;

public class DockerNetworkPort implements Serializable, ManagedNetworkInterface {

	private static final long serialVersionUID = 3343978329721092122L;

	@Override
	public int getPortOnHost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPortOnContainer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TrafficType getTrafficType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReferenceString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLog() {
		// TODO Auto-generated method stub
		return null;
	}

}
