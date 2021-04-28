package org.ar4k.agent.farm.kubernetes;

import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.farm.FarmComponent;
import org.ar4k.agent.farm.FarmConfig;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         farm Kubernetes
 */
public class KubernetesFarmComponent extends FarmComponent {

	public KubernetesFarmComponent(FarmConfig farmConfig) {
		super(farmConfig);
		// TODO Auto-generated constructor stub
	}

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(KubernetesFarmComponent.class.toString());

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws ServiceInitException {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServiceConfig getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getDescriptionJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
