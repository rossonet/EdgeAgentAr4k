package org.ar4k.agent.bootstrap;

public abstract class BootstrapRecipe implements AutoCloseable {

	protected abstract boolean isAuthRequired();

	protected abstract String descriptionAuthenticationRequired();

	protected abstract boolean isEndPointRequired();

	protected abstract String descriptionEndPointRequired();

	public boolean isMasterKeystoreRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public String descriptionMasterKeystoreRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setShellInterface(BootstrapShellInterface bean) {
		// TODO Auto-generated method stub

	}

	protected abstract void cleanAll();

	public boolean isRunningArchiveRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public String descriptionRunningArchiveRequired() {
		// TODO Auto-generated method stub
		return null;
	}

}
