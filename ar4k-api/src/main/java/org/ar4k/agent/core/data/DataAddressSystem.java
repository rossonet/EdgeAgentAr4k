package org.ar4k.agent.core.data;

import java.util.Collection;

import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.services.EdgeComponent;

public interface DataAddressSystem extends DataAddress {

	EdgeChannel getSystemChannel();

	void registerSlave(EdgeComponent component);

	Collection<DataAddress> getSlaveDataAddress();
}