package org.ar4k.agent.core.data;

import org.ar4k.agent.core.data.channels.EdgeChannel;

public interface IDataChannelFilter {

	public enum Operator {
		OR, AND, AND_NOT, OR_NOT
	}

	public enum Label {
		TAG, DOMAIN, NAME_SPACE, STATUS, SERVICE_NAME, SERVICE_CLASS, BASE_NAME
	}

	public boolean filtersMatch(EdgeChannel singleChannel);

}