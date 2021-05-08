package org.ar4k.agent.watson;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione Watson AI.
 *
 */
public class WatsonConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -1925516615711040046L;
	@Parameter(names = "--apiKey", description = "Watson API Key (see https://cloud.ibm.com/apidocs/assistant/assistant-v2?code=java)")
	public String apiKey = null;
	@Parameter(names = "--watsonVersion", description = "Watson API version")
	public String watsonVersion = "2020-09-24";
	@Parameter(names = "--url", description = "Watson API url")
	public String url = "https://api.eu-de.assistant.watson.cloud.ibm.com";
	@Parameter(names = "--assitantId", description = "Watson assitantId")
	public String assitantId = null;
	@Parameter(names = "--channelInput", description = "Query to find the input channel")
	public String channelInput = null;
	@Parameter(names = "--channelOutput", description = "Query to find the output channel")
	public String channelOutput = null;

	@Override
	public EdgeComponent instantiate() {
		final WatsonService ss = new WatsonService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 35;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

}
