package org.ar4k.agent.core.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio simulatore dati
 *
 */
public class DataGeneratorConfig extends AbstractServiceConfig {

	@Parameter(names = "--datas", description = "array of simulated datas")
	public List<SingleDataGeneratorPointConfig> datas = new ArrayList<>();

	private static final long serialVersionUID = 6301077946480730173L;

	@Override
	public EdgeComponent instantiate() {
		final DataGeneratorService ss = new DataGeneratorService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 90;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

}
