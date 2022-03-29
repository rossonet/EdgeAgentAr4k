package org.ar4k.agent.farm.kubernetes.operator;

import java.util.Collection;

import io.fabric8.kubernetes.client.KubernetesClient;

public class KubernetesOperator implements AutoCloseable {

	public KubernetesOperator(KubernetesClient client) {
		// TO______DO Auto-generated constructor stub
	}

	@Override
	public void close() throws Exception {
		// TO______DO Auto-generated method stub

	}

	public Collection<ResourceController> getControllers() {
		// TO______DO Auto-generated method stub
		return null;
	}

	public void register(ResourceController istantiateController) {
		// TO______DO Auto-generated method stub

	}

	public void remove(String className) {
		// TO______DO Auto-generated method stub

	}

}
