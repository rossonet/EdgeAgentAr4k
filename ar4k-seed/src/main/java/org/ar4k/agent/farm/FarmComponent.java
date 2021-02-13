package org.ar4k.agent.farm;

import java.io.IOException;
import java.util.List;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public abstract class FarmComponent implements EdgeComponent {
	public enum ConnectionState {
		CONNECTED, DISCONNECTED, UNKNOW
	}

	protected static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(FarmComponent.class.toString());

	private Homunculus homunculus = null;

	private DataAddress dataspace = null;

	public abstract ConnectionState getConnectionState();

	public abstract List<ManagedArchives> getManagedArchives();

	public abstract List<ManagedHost> getManagedHosts();

	public abstract void pruneSystem();

	public FarmComponent(FarmConfig farmConfig) {
		homunculus = farmConfig.homunculus;
	}

	@Override
	public void close() throws IOException {
		kill();
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
		dataspace = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

}
