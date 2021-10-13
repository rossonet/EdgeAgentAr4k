package org.ar4k.agent.farm.kubernetes;

import java.io.IOException;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per operatori k8s
 *
 **/
public class KubernetesService implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(KubernetesService.class);

	// iniettata vedi set/get
	private KubernetesConfig configuration = null;

	private Homunculus homunculusBase = null;

	private DataAddress dataspace = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private EdgeChannel statusChannel = null;

	@Override
	public KubernetesConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((KubernetesConfig) configuration);
	}

	@Override
	public synchronized void init() {

	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void kill() {
		serviceStatus = ServiceStatus.KILLED;
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		final StringMessage message = new StringMessage();
		message.setPayload(serviceStatus.toString());
		statusChannel.getChannel().send(message);
		return serviceStatus;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculusBase;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public void setDataAddress(DataAddress dataAddressBase) {
		dataspace = dataAddressBase;
	}

	@Override
	public void setHomunculus(Homunculus homunculusBase) {
		this.homunculusBase = homunculusBase;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
