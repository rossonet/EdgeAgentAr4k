package org.ar4k.agent.farm.kubernetes.operator;

import java.util.Collection;

import io.fabric8.kubernetes.client.KubernetesClient;

public class KubernetesOperator implements AutoCloseable {

	public KubernetesOperator(KubernetesClient client) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public Collection<ResourceController> getControllers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void register(ResourceController istantiateController) {
		// TODO Auto-generated method stub

	}

	public void remove(String className) {
		// TODO Auto-generated method stub

	}

}
