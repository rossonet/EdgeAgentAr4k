package org.ar4k.agent.core.data.router;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.services.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         router messages service configuration
 *
 */
//TO______DO completare router messaggi
public class MessagesRouterConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -1744577761935943003L;
	
	@Parameter(names = "--routers", description = "array of simulated datas")
	public final List<SpringIntegrationRouterConfig> routers = new ArrayList<>();

	@Override
	public EdgeComponent instantiate() {
		final MessagesRouterService ss = new MessagesRouterService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 130;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

}
