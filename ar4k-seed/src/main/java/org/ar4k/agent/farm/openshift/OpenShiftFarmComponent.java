package org.ar4k.agent.farm.openshift;

import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.farm.kubernetes.KubernetesFarmComponent;
import org.json.JSONObject;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         farm OpenShift
 */
public class OpenShiftFarmComponent extends KubernetesFarmComponent {

	public OpenShiftFarmComponent(OpenShiftFarmConfig farmConfig) {
		super(farmConfig);
		// TODO Auto-generated constructor stub
	}

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
