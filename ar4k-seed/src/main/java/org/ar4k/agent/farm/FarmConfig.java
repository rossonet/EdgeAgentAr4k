package org.ar4k.agent.farm;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;

public abstract class FarmConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -2924869182396567535L;

	private final List<ManagedVirtualApplication> applications = new ArrayList<>();

	public List<ManagedVirtualApplication> getApplications() {
		return applications;
	}

}
