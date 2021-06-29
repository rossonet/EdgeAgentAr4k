package org.ar4k.agent.farm.bootpService;

import java.io.IOException;

import org.anarres.tftp.server.netty.TftpServer;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public abstract class BootpComponent implements EdgeComponent {

	protected static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BootpComponent.class);

	private Homunculus homunculus = null;

	private DataAddress dataspace = null;

	private TftpServer tftpServer; // TODO implementare server sftp

	public BootpComponent(BootpConfig farmConfig) {
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
