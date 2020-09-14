package org.ar4k.agent.farm;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

public abstract class FarmConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -2924869182396567535L;
	private String uniqueName;

	@Parameter(names = "--uniqueName", description = "the farm uniqueName")

	@Override
	public String getName() {
		return name;
	}

	@Override
	public EdgeComponent instantiate() {
		return new FarmComponent(this);
	}

	@Override
	public String getUniqueId() {
		return uniqueName;
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
		final StringBuilder builder = new StringBuilder();
		builder.append("FarmConfig [");
		if (uniqueName != null)
			builder.append("uniqueName=").append(uniqueName);
		builder.append("]");
		return builder.toString();
	}

}
