package org.ar4k.agent.bootstrap.recipes;

import org.ar4k.agent.bootstrap.BootstrapRecipe;

public class BootstrapViaLocalConsole extends BootstrapRecipe {

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUp() {
		copyTemplateToLocalStorage();
		copyMasterKeyToLocalStorage();
		generateAgentJar();
		generateBeaconServerConfig();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAuthRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String descriptionAuthenticationRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEndPointRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String descriptionEndPointRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSetupRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return false;
	}

}
