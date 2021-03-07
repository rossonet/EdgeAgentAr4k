package org.ar4k.agent.farm.recipes.activemq;

import org.ar4k.agent.farm.docker.DockerVirtualApplication;
import org.ar4k.agent.farm.kubernetes.KubernetesVirtualApplication;
import org.ar4k.agent.farm.local.LocalAccountVirtualApplication;
import org.ar4k.agent.farm.recipes.GeneralApplication;

//TODO implementare cluster activemq su openshift
//https://access.redhat.com/documentation/en-us/red_hat_amq/7.5/html-single/deploying_amq_broker_on_openshift/index#clustered_broker-ssl-broker-ocp

public class ActiveMqApplication extends GeneralApplication
		implements LocalAccountVirtualApplication, DockerVirtualApplication, KubernetesVirtualApplication {

	@Override
	public String getLocalInstallScript() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalStartCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalStopCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalCheckCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalPreSaveCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalPostSaveCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalPreLoadCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalPostLoadCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLambdaCommand() {
		// TODO Auto-generated method stub
		return null;
	}

}
