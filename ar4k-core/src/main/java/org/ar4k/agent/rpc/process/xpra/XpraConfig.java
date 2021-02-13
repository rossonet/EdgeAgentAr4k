package org.ar4k.agent.rpc.process.xpra;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

//TODO portare in seed
public class XpraConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -2924869182396567535L;

	@Parameter(names = "--xpraSessionName", description = "the unique name for tehe session")
	private String xpraSessionName;

	@Override
	public String getUniqueId() {
		return xpraSessionName;
	}

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

	@Override
	public EdgeComponent instantiate() {
		// TODO Auto-generated method stub
		return null;
	}

}
