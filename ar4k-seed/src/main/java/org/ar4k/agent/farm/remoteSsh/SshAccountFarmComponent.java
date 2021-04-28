package org.ar4k.agent.farm.remoteSsh;

import org.ar4k.agent.farm.FarmConfig;
import org.ar4k.agent.farm.local.LocalAccountFarmComponent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         farm ssh LocalAccount
 */
public class SshAccountFarmComponent extends LocalAccountFarmComponent {

	public SshAccountFarmComponent(FarmConfig farmConfig) {
		super(farmConfig);
	}

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SshAccountFarmComponent.class.toString());

}
