package org.ar4k.agent.farm.kubernetes;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.services.EdgeComponent;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio operator K8s.
 *
 **/
public class KubernetesConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -1213678092843330387L;

	@Override
	public EdgeComponent instantiate() {
		final KubernetesService ss = new KubernetesService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 150;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

}
