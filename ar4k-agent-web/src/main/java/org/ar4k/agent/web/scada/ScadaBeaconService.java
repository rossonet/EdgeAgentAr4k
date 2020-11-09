package org.ar4k.agent.web.scada;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.core.interfaces.IBeaconClientScadaWrapper;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
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

	private List<IBeaconClientScadaWrapper> beaconServers = new ArrayList<>();
	private Map<String, IScadaAgent> agents = new HashMap<>();

	public Collection<IScadaAgent> getClients(String filter) {
		if (filter == null || filter.isEmpty()) {
			return agents.values();
		} else {
			final Collection<IScadaAgent> data = new ArrayList<>();
			for (final IScadaAgent s : agents.values()) {
				if (s.isFoundBy(filter)) {
					data.add(s);
				}
			}
			return data;
		}

	}

	private void refreshAgentsFromBeacon() {
		if (!beaconServers.isEmpty())
			for (final IBeaconClientScadaWrapper c : beaconServers) {
				if (c.getBeaconClient() != null && c.getBeaconClient().listAgentsConnectedToBeacon() != null
						&& !c.getBeaconClient().listAgentsConnectedToBeacon().isEmpty())
					for (final Agent a : c.getBeaconClient().listAgentsConnectedToBeacon()) {
						if (agents.keySet().isEmpty() || !agents.keySet().contains(a.getAgentUniqueName())) {
							agents.put(a.getAgentUniqueName(), new ScadaAgentWrapper(c.getBeaconClient(), a));
						}
					}
			}
	}

	public Collection<IBeaconClientScadaWrapper> getBeaconServersList() {
		return beaconServers;
	}

	public Collection<IBeaconClientScadaWrapper> getBeaconServersList(String filter) {
		if (filter == null || filter.isEmpty()) {
			return beaconServers;
		} else {
			final Collection<IBeaconClientScadaWrapper> data = new ArrayList<IBeaconClientScadaWrapper>();
			for (final IBeaconClientScadaWrapper s : getBeaconServersList()) {
				if (s.isFoundBy(filter)) {
					data.add(s);
				}
			}
			return data;
		}
	}

	public void createDemoData() {
		final IBeaconClientScadaWrapper s = new BeaconClientWrapper();
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

	public void addClientServer(IBeaconClientScadaWrapper bc) {
		beaconServers.add(bc);
	}

	@Override
	public void close() throws Exception {
		if (timer != null) {
			timer.cancel();
		}
	}

}
