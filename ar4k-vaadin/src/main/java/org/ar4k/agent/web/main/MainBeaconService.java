package org.ar4k.agent.web.main;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.IBeaconClientScadaWrapper;
import org.ar4k.agent.core.interfaces.IBeaconProvisioningAuthorization;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainBeaconService implements AutoCloseable {

	@Autowired
	Homunculus homunculus;

	private final static int SCAN_DELAY = 20000;
	private final Timer timer = new Timer();
	private final TimerTask refreshServer;

	private boolean localAgentConfigured = false;

	public MainBeaconService() {
		refreshServer = createTimerTask();
		timer.schedule(refreshServer, SCAN_DELAY, SCAN_DELAY);
	}

	private TimerTask createTimerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				try {
					try {
						if (!localAgentConfigured && homunculus != null) {
							createBaseBeaconClient();
							localAgentConfigured = true;
						}
					} catch (Exception exception) {
						logger.logException(exception);
					}
					refreshAgentsFromBeacon();
					refreshProvisioningRequestsFromBeacon();
				} catch (final Exception a) {
					logger.logException(a);
					logger.info("exception when refresh Beacon client");
				}
			}
		};
	}

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(MainBeaconService.class);

	private List<IBeaconClientScadaWrapper> beaconServers = new ArrayList<>();

	private Map<String, IBeaconProvisioningAuthorization> provisioningRequests = new HashMap<>();
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
				final List<Agent> listAgentsConnectedToBeacon = c.getBeaconClient().listAgentsConnectedToBeacon();
				logger.trace("listAgentsConnectedToBeacon -> " + listAgentsConnectedToBeacon);
				if (c.getBeaconClient() != null && listAgentsConnectedToBeacon != null
						&& !listAgentsConnectedToBeacon.isEmpty())
					for (final Agent a : listAgentsConnectedToBeacon) {
						if (agents.keySet().isEmpty() || !agents.keySet().contains(a.getAgentUniqueName())) {
							agents.put(a.getAgentUniqueName(), new MainAgentWrapper(c.getBeaconClient(), a));
						}
					}
			}
	}

	private void refreshProvisioningRequestsFromBeacon() {
		if (!beaconServers.isEmpty())
			for (final IBeaconClientScadaWrapper c : beaconServers) {
				final List<AgentRequest> listProvisioningRequests = c.getBeaconClient().listProvisioningRequests();
				logger.trace("listProvisioningRequests -> " + listProvisioningRequests);
				if (listProvisioningRequests != null && !listProvisioningRequests.isEmpty())
					for (final AgentRequest a : listProvisioningRequests) {
						if (provisioningRequests.keySet().isEmpty()
								|| !provisioningRequests.keySet().contains(a.getIdRequest())) {
							provisioningRequests.put(a.getIdRequest(), new BeaconProvisioningAuthorization(a));
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

	public void createBaseBeaconClient() throws MalformedURLException {
		final IBeaconClientScadaWrapper s = new BeaconClientWrapper();
		s.setHomunculusClient(true);
		s.setDiscoveryPort(homunculus.getBeaconClient().getDiscoveryPort());
		s.setDiscoveryFilter(homunculus.getBeaconClient().getDiscoveryFilter());
		s.setContext("Local Beacon Client");
		s.setAliasBeaconClientInKeystore(homunculus.getBeaconClient().getAliasBeaconClientInKeystore());
		s.setBeaconCaChainPem(homunculus.getBeaconClient().getCertChainAuthority());
		s.setCertChainFile(homunculus.getBeaconClient().getCertChainFile());
		s.setCertFile(homunculus.getBeaconClient().getCertFile());
		s.setCompany("NOT KNOW");
		s.setHost(homunculus.getBeaconClient().getHostTarget());
		s.setPort(homunculus.getBeaconClient().getPort());
		s.setPrivateFile(homunculus.getBeaconClient().getPrivateFile());
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

	public Collection<IBeaconProvisioningAuthorization> getProvisioningAuthorizationList(String filter) {
		if (filter == null || filter.isEmpty()) {
			return provisioningRequests.values();
		} else {
			final Collection<IBeaconProvisioningAuthorization> data = new ArrayList<>();
			for (final IBeaconProvisioningAuthorization s : provisioningRequests.values()) {
				if (s.isFoundBy(filter)) {
					data.add(s);
				}
			}
			return data;
		}
	}

	public void approveRequestProvisioning(IBeaconProvisioningAuthorization beaconProvisioning) {
		for (final IBeaconClientScadaWrapper c : beaconServers) {
			final Status status = c.getBeaconClient().approveRemoteAgent(beaconProvisioning.getIdRequest(), "AUTO",
					null);
			logger.info("status provisioning request on " + c + " -> " + status);
		}
	}

}
