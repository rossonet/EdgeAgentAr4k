package org.ar4k.agent.farm.recipes;

import org.ar4k.agent.farm.ManagedVirtualApplication;

import com.beust.jcommander.Parameter;

public abstract class GeneralApplication implements ManagedVirtualApplication {

	@Parameter(names = "--applicationName", description = "unique name for application")
	public String applicationName = "application-name";

	@Parameter(names = "--farmName", description = "farm in where the application is implemented")
	public String farmName = null;

	@Parameter(names = "--priority", description = "priority for the application in the system")
	public int priority = 0;

	@Parameter(names = "--startOnBoot", description = "start application on boot (true/false)", arity = 1)
	public boolean startOnBoot = true;

	@Parameter(names = "--autoRestart", description = "autorestart app when fails (true/false)", arity = 1)
	public boolean autoRestart = true;

	@Parameter(names = "--lambdaFunction", description = "run as lambda function (true/false)", arity = 1)
	public boolean lambdaFunction = true;

	public boolean isLambdaFunction() {
		return lambdaFunction;
	}

	@Override
	public boolean autoRestart() {
		return autoRestart;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getFarmName() {
		return farmName;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean startOnBoot() {
		return startOnBoot;
	}

}
