package org.ar4k.agent.web.scada;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.springframework.stereotype.Service;

@Service
public class ScadaBeaconService implements AutoCloseable {

	private final static int SCAN_DELAY = 20000;
	private final Timer timer = new Timer();
	private final TimerTask refreshServer;

	public ScadaBeaconService() {
		createDemoData();
		refreshServer = createTimerTask();
		timer.schedule(refreshServer, SCAN_DELAY, SCAN_DELAY);
	}

	private TimerTask createTimerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				try {
					refreshAgentsFromBeacon();
				} catch (final Exception a) {
					logger.info("exception when refresh Beacon client");
				}
			}
		};
	}

	@SuppressWarnings("unused")
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(ScadaBeaconService.class.toString());

	private List<BeaconClientWrapper> beaconServers = new ArrayList<>();
	private Map<String, ScadaAgentWrapper> agents = new HashMap<>();

	public Collection<ScadaAgentWrapper> getClients(String filter) {
		if (filter == null || filter.isEmpty()) {
			return agents.values();
		} else {
			final Collection<ScadaAgentWrapper> data = new ArrayList<>();
			for (final ScadaAgentWrapper s : agents.values()) {
				if (s.isFoundBy(filter)) {
					data.add(s);
				}
			}
			return data;
		}

	}

	private void refreshAgentsFromBeacon() {
		if (!beaconServers.isEmpty())
			for (final BeaconClientWrapper c : beaconServers) {
				if (c.getBeaconClient() != null && c.getBeaconClient().listAgentsConnectedToBeacon() != null
						&& !c.getBeaconClient().listAgentsConnectedToBeacon().isEmpty())
					for (final Agent a : c.getBeaconClient().listAgentsConnectedToBeacon()) {
						if (agents.keySet().isEmpty() || !agents.keySet().contains(a.getAgentUniqueName())) {
							agents.put(a.getAgentUniqueName(), new ScadaAgentWrapper(c.getBeaconClient(), a));
						}
					}
			}
	}

	public Collection<BeaconClientWrapper> getBeaconServersList() {
		return beaconServers;
	}

	public Collection<BeaconClientWrapper> getBeaconServersList(String filter) {
		if (filter == null || filter.isEmpty()) {
			return beaconServers;
		} else {
			final Collection<BeaconClientWrapper> data = new ArrayList<BeaconClientWrapper>();
			for (final BeaconClientWrapper s : getBeaconServersList()) {
				if (s.isFoundBy(filter)) {
					data.add(s);
				}
			}
			return data;
		}
	}

	public void createDemoData() {
		final BeaconClientWrapper s = new BeaconClientWrapper();
		s.setDiscoveryPort(0);
		s.setDiscoveryFilter("SCADA");
		s.setAliasBeaconClientInKeystore("scada-web");
		s.setBeaconCaChainPem("beaconCaChainPem");
		s.setCertChainFile("certChainFile");
		s.setCertFile("cert file");
		s.setPort(11231);
		s.setHost("127.0.0.1");
		s.setCompany("Rossonet");
		s.setPrivateFile("privateFile");
		s.setContext("Locale");
		beaconServers.add(s);
	}

	public void addClientServer(BeaconClientWrapper bc) {
		beaconServers.add(bc);
	}

	@Override
	public void close() throws Exception {
		if (timer != null) {
			timer.cancel();
		}
	}

}
