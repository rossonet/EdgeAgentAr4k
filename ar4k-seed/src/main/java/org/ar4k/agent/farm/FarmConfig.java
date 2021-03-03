package org.ar4k.agent.farm;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;

import com.beust.jcommander.Parameter;

public abstract class FarmConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -2924869182396567535L;

	@Parameter(names = "--applications", description = "applications in farm")
	public final List<ManagedVirtualApplication> applications = new ArrayList<>();

}
