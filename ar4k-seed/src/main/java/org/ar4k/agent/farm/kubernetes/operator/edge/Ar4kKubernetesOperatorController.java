package org.ar4k.agent.farm.kubernetes.operator.edge;

import org.ar4k.agent.farm.kubernetes.operator.OperatorController;
import org.ar4k.agent.farm.kubernetes.operator.ResourceController;

import io.fabric8.kubernetes.client.KubernetesClient;

@OperatorController
public class Ar4kKubernetesOperatorController implements ResourceController  {

	private final KubernetesClient client;

	public Ar4kKubernetesOperatorController(KubernetesClient client) {
		this.client = client;
	}

	public KubernetesClient getClient() {
		return client;
	}


}
