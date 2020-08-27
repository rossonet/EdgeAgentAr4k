package org.ar4k.agent.pcap;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.EdgeComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
// TODO implementare servizio di sniffing PCAP verso dataspace
public class PcapSnifferService implements EdgeComponent {

	private ServiceConfig configuration;
	private Anima anima;
	private DataAddress dataspace;

	@Override
	public void init() {
		// init method of PCAP service

	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = configuration;
	}

	@Override
	public void close() throws Exception {
		// metodo close di PCAP service
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// metodo close di PCAP service
		return null;
	}

	@Override
	public void kill() {
		try {
			close();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Anima getAnima() {
		return anima;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		this.dataspace = dataAddress;
	}

	@Override
	public void setAnima(Anima anima) {
		this.anima = anima;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public JSONObject getDescriptionJson() {
		final Gson gson = new GsonBuilder().create();
		return new JSONObject(gson.toJsonTree(configuration).getAsString());
	}

}
