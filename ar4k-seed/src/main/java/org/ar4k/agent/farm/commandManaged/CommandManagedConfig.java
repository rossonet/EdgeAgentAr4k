package org.ar4k.agent.farm.commandManaged;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         command managed service configuration
 *
 */
//TODO completare command managed configuration
public class CommandManagedConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = 5180966181577560906L;

	@Parameter(names = "--installScript", description = "install script runned the first time the service is deployed")
	public String installScript = null;

	@Parameter(names = "--updateScript", description = "update script runned every time the agent start")
	public String updateScript = null;

	@Parameter(names = "--startScript", description = "script to start the service")
	public String startScript = null;

	@Parameter(names = "--stopScript", description = "script to stop the service")
	public String stopScript = null;

	@Parameter(names = "--pulseScript", description = "script to run a single job with the service")
	public String pulseScript = null;

	@Parameter(names = "--saveScript", description = "script to save the state of the service")
	public String saveScript = null;

	@Parameter(names = "--loadScript", description = "script to load the state from the service")
	public String loadScript = null;

	@Parameter(names = "--statusScript", description = "script to get the status of the service")
	public String statusScript = null;

	@Parameter(names = "--removeScript", description = "script to clean the agent from the service")
	public String removeScript = null;

	@Parameter(names = "--checkScript", description = "script called every delayCheckMs")
	public String checkScript = null;

	@Parameter(names = "--delayCheckMs", description = "delay time between the execution of the script checkScript")
	public long delayCheckMs = 60000L;
	
	@Parameter(names = "--fieldsOfTheService", description = "List of node to subscribe", variableArity = true)
	public List<ServiceField> fieldsOfTheService = new ArrayList<>();

	@Override
	public EdgeComponent instantiate() {
		final CommandManagedService ss = new CommandManagedService();
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
