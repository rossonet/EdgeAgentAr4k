package org.ar4k.agent.core;

import java.util.Collection;
import java.util.Set;

import org.ar4k.agent.core.data.DataAddressSystem;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceComponent;
import org.ar4k.agent.keystore.IKeystoreConfig;
import org.ar4k.agent.rpc.IHomunculusRpc;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.spring.EdgeUserDetails;
import org.ar4k.agent.spring.autoconfig.EdgeStarterProperties;
import org.ar4k.agent.tunnels.http2.beacon.IBeaconClient;
import org.ar4k.agent.tunnels.http2.beacon.IBeaconServer;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AuthenticationManager;

public interface Homunculus extends AutoCloseable, DataServiceOwner, ApplicationContextAware,
		ApplicationListener<ApplicationEvent>, BeanNameAware {

	public enum HomunculusEvents {
		BOOTSTRAP, COMPLETE_RELOAD, EXCEPTION, HIBERNATION, PAUSE, RESTART, SETCONF, START, STOP
	}

	// tipi di router interno supportato per gestire lo scambio dei messagi tra gli
	// agenti, per la definizione della policy security sul routing public
	public enum HomunculusRouterType {
		DEVELOP, NONE, PRODUCTION, ROAD
	}

	public enum HomunculusStates {
		CONFIGURED, FAULTED, INIT, KILLED, RUNNING, STAMINAL, STASIS
	}

	AuthenticationManager getAuthenticationManager();

	IKeystoreConfig getMyIdentityKeystore();

	EdgeStarterProperties getStarterProperties();

	IBeaconClient getBeaconClient();

	DataAddressSystem getDataAddress();

	String getAgentUniqueName();

	IHomunculusRpc getHomunculusSession();

	RpcExecutor getRpc(String sessionId);

	HomunculusStates getState();

	Collection<EdgeUserDetails> getLocalUsers();

	boolean isRunning();

	String loginAgent(String username, String password, String sessionId);

	void logoutFromAgent();

	ConfigSeed getRuntimeConfig();

	void terminateSession(String sessionId);

	void setTargetConfig(ConfigSeed config);

	void sendEvent(HomunculusEvents event);

	Set<ServiceComponent<EdgeComponent>> getComponents();

	String getEnvironmentVariablesAsString();

	IBeaconClient connectToBeaconService(String urlBeacon, String beaconCaChainPem, int discoveryPort,
			String discoveryFilter, boolean force);

	ApplicationContext getApplicationContext();

	boolean isSessionValid(String sessionId);

	Collection<String> getTags();

	String getMyAliasCertInKeystore();

	Collection<String> getRuntimeProvides();

	IBeaconServer getBeaconServerIfExists();

}
