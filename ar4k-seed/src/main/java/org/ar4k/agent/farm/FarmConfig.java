package org.ar4k.agent.farm;

import org.ar4k.agent.config.AbstractServiceConfig;

import com.beust.jcommander.Parameter;

public abstract class FarmConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -2924869182396567535L;

	@Parameter(names = "--farmName", description = "the farm unique name")
	private String farmName;

	@Parameter(names = "--farmType", description = "class name for the farm operator")
	private String farmType;

	@Override
	public String getUniqueId() {
		return farmName;
	}

	@Override
	public int getPriority() {
		return 80;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

	@Override
	public String toString() {
		return "FarmConfig [farmName=" + farmName + ", farmType=" + farmType + "]";
	}

}
