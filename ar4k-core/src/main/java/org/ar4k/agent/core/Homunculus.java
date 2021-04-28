/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.data.DataAddressHomunculus;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.IBeaconClient;
import org.ar4k.agent.core.interfaces.IBeaconServer;
import org.ar4k.agent.core.interfaces.ServiceComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.core.services.HomunculusService;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.mattermost.MatterMostClientAr4k;
import org.ar4k.agent.mattermost.MatterMostRpcManager;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.process.ScriptEngineManagerProcess;
import org.ar4k.agent.spring.EdgeUserDetails;
import org.ar4k.agent.spring.autoconfig.EdgeStarterProperties;
import org.ar4k.agent.tunnels.http2.beacon.BeaconService;
import org.ar4k.agent.tunnels.http2.beacon.client.BeaconClient;
import org.ar4k.agent.tunnels.http2.beacon.client.BeaconClientBuilder;
import org.bouncycastle.cms.CMSException;
import org.joda.time.Instant;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.xbill.DNS.TextParseException;

import io.grpc.ConnectivityState;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

/**
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Classe principale singleton Ar4k Edge Agent. Gestisce la macchina a
 *         stati dei servizi e funge da Bean principale per l'uso delle API Ar4k
 */
@Component("homunculus")
@Scope("singleton")
@EnableJms
@EnableConfigurationProperties(EdgeStarterProperties.class)
public class Homunculus
		implements ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware, AutoCloseable {

	public enum HomunculusEvents {
		BOOTSTRAP, SETCONF, START, STOP, PAUSE, HIBERNATION, EXCEPTION, RESTART, COMPLETE_RELOAD
	}

	// tipi di router interno supportato per gestire lo scambio dei messagi tra gli
	// agenti, per la definizione della policy security sul routing public
	public enum HomunculusRouterType {
		NONE, PRODUCTION, DEVELOP, ROAD
	}

	public enum HomunculusStates {
		INIT, STAMINAL, CONFIGURED, RUNNING, KILLED, FAULTED, STASIS
	}

	public static final String THREAD_ID = "ar4k-" + (Math.round((new Random().nextDouble() * 9999)));

	public static final String DEFAULT_KS_PATH = "default-new.ks";

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(Homunculus.class.toString());
	private static final String REGISTRATION_PIN = ConfigHelper.createRandomRegistryId();

	// assegnato da Spring tramite setter al boot
	private static ApplicationContext applicationContext;

	private static final String DB_DATASTORE_NAME = "datastore";

	private static final int VALIDITY_CERT_DAYS = 365 * 3;

	private String agentUniqueName = null;

	private Timer timerScheduler = null;

	@Autowired
	private StateMachine<HomunculusStates, HomunculusEvents> homunculusStateMachine;

	@Autowired
	private HomunculusSession homunculusSession;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EdgeStarterProperties starterProperties;
	// gestione configurazioni
	// attuale configurazione in runtime
	private EdgeConfig runtimeConfig = null;
	// configurazione obbiettivo durante le transizioni
	private EdgeConfig targetConfig = null;
	// configurazione iniziale di bootStrap derivata dalle variabili Spring
	private EdgeConfig bootstrapConfig = null;
	// configurazione per il reload
	private EdgeConfig reloadConfig = null;
	private HomunculusStates stateTarget = HomunculusStates.RUNNING;

	private Map<Instant, HomunculusStates> statesBefore = new HashMap<>();

	private Set<ServiceComponent<EdgeComponent>> components = new HashSet<>();

	private Map<String, Object> dataStore = null;

	private Collection<EdgeUserDetails> localUsers = new HashSet<>();

	private RecordManager recMan = null;

	private String beanName = "homunculus";

	private MatterMostClientAr4k mattermostClient = null;

	private MatterMostRpcManager matterMostRpcManager = null;

	private BeaconClient beaconClient = null;

	private DataAddressHomunculus dataAddress = new DataAddressHomunculus(this);
	private KeystoreConfig myIdentityKeystore = null;

	private String myAliasCertInKeystore = "agent";

	private boolean onApplicationEventFlag = false;

	private boolean afterSpringInitFlag = false;

	private boolean firstStateFired = false;

	private Comparator<ServiceConfig> comparatorOrderPots = new Comparator<ServiceConfig>() {
		@Override
		public int compare(ServiceConfig o1, ServiceConfig o2) {
			return Integer.compare(o1.getPriority(), o2.getPriority());
		}
	};

	private int indexBeaconClient = -1;

	public void clearDataStore() {
		if (dataStore != null)
			dataStore.clear();
	}

	@Override
	public void close() {
		finalizeAgent();
		if (homunculusStateMachine != null) {
			homunculusStateMachine.sendEvent(HomunculusEvents.STOP);
			homunculusStateMachine.stop();
			homunculusStateMachine = null;
		}
		if (homunculusSession != null) {
			try {
				homunculusSession.close();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public BeaconClient connectToBeaconService(String urlBeacon, String beaconCaChainPem, int discoveryPort,
			String discoveryFilter, boolean force) {
		URL urlTarget = null;
		BeaconClient beaconClientTarget = null;
		{
			try {
				if (urlBeacon != null)
					urlTarget = new URL(urlBeacon);
				final String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + urlBeacon;
				homunculusSession.registerNewSession(sessionId, sessionId);
				final RpcConversation rpc = homunculusSession.getRpc(sessionId);
				beaconClientTarget = new BeaconClientBuilder()
						.setAliasBeaconClientInKeystore(starterProperties.getKeystoreBeaconAlias())
						.setUniqueName(getAgentUniqueName()).setHomunculus(this).setBeaconCaChainPem(beaconCaChainPem)
						.setDiscoveryFilter(discoveryFilter).setDiscoveryPort(discoveryPort)
						.setPort(urlTarget.getPort()).setRpcConversation(rpc).setHost(urlTarget.getHost()).build();
				if (beaconClientTarget != null
						&& beaconClientTarget.getStateConnection().equals(ConnectivityState.READY)) {
					logger.info("found Beacon endpoint: " + urlBeacon);
					if (!getAgentUniqueName().equals(beaconClientTarget.getAgentUniqueName())) {
						logger.info("the unique name is changed in " + getAgentUniqueName());
					}
				} else {
					logger.info(
							"the Beacon endpoint " + urlBeacon + " return " + beaconClientTarget.getStateConnection());
				}
			} catch (final IOException e) {
				logger.info("the url " + urlBeacon + " is malformed or unreachable [" + e.getCause() + "]");
			} catch (final UnrecoverableKeyException e) {
				logger.warn(e.getMessage());
			} catch (final Exception e) {
				logger.info(EdgeLogger.stackTraceToString(e, 6));
			}
			return beaconClientTarget;
		}
	}

	public boolean dataStoreExists() {
		return (dataStore != null);
	}

	public final void elaborateNewConfig(EdgeConfig newTargetConfig) {
		if (newTargetConfig != null && newTargetConfig.isMoreUpToDateThan(getRuntimeConfig())) {
			logger.warn("Found new config {}", newTargetConfig);
			reloadConfig = newTargetConfig;
			runtimeConfig = newTargetConfig;
			if (newTargetConfig.nextConfigReload == null || !newTargetConfig.nextConfigReload) {
				sendEvent(HomunculusEvents.RESTART);
			} else {
				sendEvent(HomunculusEvents.COMPLETE_RELOAD);
			}
		}
	}

	public String getAgentUniqueName() {
		return agentUniqueName;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public IBeaconClient getBeaconClient() {
		return beaconClient;
	}

	public IBeaconServer getBeaconServerIfExists() {
		for (ServiceComponent<EdgeComponent> singlePot : components) {
			if (singlePot.getPot() instanceof BeaconService) {
				return ((BeaconService) singlePot.getPot()).getBeaconServer();
			}
		}
		return null;
	}

	public Set<ServiceComponent<EdgeComponent>> getComponents() {
		return components;
	}

	public Object getContextData(String index) {
		if (dataStore != null)
			return dataStore.get(index);
		else
			return null;
	}

	public DataAddressHomunculus getDataAddress() {
		return dataAddress;
	}

	public Map<String, Object> getDataStore() {
		return dataStore;
	}

	public String getDbDataStoreName() {
		return DB_DATASTORE_NAME;
	}

	// adminPassword è escluso
	public Map<String, String> getEnvironmentVariables() {
		final Map<String, String> ritorno = new HashMap<>();
		ritorno.put("ar4k.fileKeystore", starterProperties.getFileKeystore());
		ritorno.put("ar4k.webKeystore", starterProperties.getWebKeystore());
		ritorno.put("ar4k.dnsKeystore", starterProperties.getDnsKeystore());
		ritorno.put("ar4k.keystorePassword", starterProperties.getKeystorePassword());
		ritorno.put("ar4k.keystoreMainAlias", starterProperties.getKeystoreMainAlias());
		ritorno.put("ar4k.keystoreBeaconAlias", starterProperties.getKeystoreBeaconAlias());
		ritorno.put("ar4k.confPath", starterProperties.getConfPath());
		ritorno.put("ar4k.fileConfig", starterProperties.getFileConfig());
		ritorno.put("ar4k.webConfig", starterProperties.getWebConfig());
		ritorno.put("ar4k.dnsConfig", starterProperties.getDnsConfig());
		ritorno.put("ar4k.baseConfig", starterProperties.getBaseConfig());
		ritorno.put("ar4k.webRegistrationEndpoint", starterProperties.getWebRegistrationEndpoint());
		ritorno.put("ar4k.dnsRegistrationEndpoint", starterProperties.getDnsRegistrationEndpoint());
		ritorno.put("ar4k.beaconDiscoveryPort", String.valueOf(starterProperties.getBeaconDiscoveryPort()));
		ritorno.put("ar4k.beaconDiscoveryFilterString", starterProperties.getBeaconDiscoveryFilterString());
		ritorno.put("ar4k.beaconCaChainPem", starterProperties.getBeaconCaChainPem());
		ritorno.put("ar4k.fileConfigOrder", String.valueOf(starterProperties.getFileConfigOrder()));
		ritorno.put("ar4k.webConfigOrder", String.valueOf(starterProperties.getWebConfigOrder()));
		ritorno.put("ar4k.dnsConfigOrder", String.valueOf(starterProperties.getDnsConfigOrder()));
		ritorno.put("ar4k.baseConfigOrder", String.valueOf(starterProperties.getBaseConfigOrder()));
		ritorno.put("ar4k.threadSleep", String.valueOf(starterProperties.getThreadSleep()));
		ritorno.put("ar4k.consoleOnly", String.valueOf(starterProperties.isConsoleOnly()));
		ritorno.put("ar4k.logoUrl", String.valueOf(starterProperties.getLogoUrl()));
		return ritorno;
	}

	public String getEnvironmentVariablesAsString() {
		return starterProperties.toString();
	}

	public String getFileKeystore() {
		return ConfigHelper.resolveWorkingString(starterProperties.getFileKeystore(), true);
	}

	public StateMachine<HomunculusStates, HomunculusEvents> getHomunculusStateMachine() {
		return homunculusStateMachine;
	}

	public Collection<EdgeUserDetails> getLocalUsers() {
		return localUsers;
	}

	public String getLogoUrl() {
		String logo = "/static/img/ar4k.png";
		if (starterProperties.getLogoUrl() != null && !starterProperties.getLogoUrl().isEmpty()) {
			logo = starterProperties.getLogoUrl();
		}
		if (runtimeConfig != null && runtimeConfig.logoUrl != null) {
			logo = runtimeConfig.logoUrl;
		}
		return logo;
	}

	public String getMyAliasCertInKeystore() {
		return myAliasCertInKeystore;
	}

	public KeystoreConfig getMyIdentityKeystore() {
		return myIdentityKeystore;
	}

	public RpcExecutor getRpc(String sessionId) {
		return homunculusSession.getRpc(sessionId);
	}

	public EdgeConfig getRuntimeConfig() {
		return runtimeConfig;
	}

	public Collection<String> getRuntimeProvides() {
		final Collection<String> result = new ArrayList<String>();
		for (final ServiceComponent<EdgeComponent> c : components) {
			if (c.isRunning() && c.getPot() != null && c.getPot().getConfiguration() != null
					&& c.getPot().getConfiguration().getProvides() != null) {
				result.addAll(c.getPot().getConfiguration().getProvides());
			}

		}
		return result;
	}

	public Collection<String> getRuntimeRequired() {
		final Collection<String> result = new ArrayList<String>();
		for (final ServiceComponent<EdgeComponent> c : components) {
			if (c.getPot() != null && c.getPot().getConfiguration() != null
					&& c.getPot().getConfiguration().getRequired() != null) {
				result.addAll(c.getPot().getConfiguration().getRequired());
			}
		}
		return result;
	}

	public Session getSession(String sessionId) {
		return (Session) homunculusSession.getSessionInformation(sessionId);
	}

	public EdgeStarterProperties getStarterProperties() {
		return starterProperties;
	}

	@ManagedOperation
	public HomunculusStates getState() {
		return (homunculusStateMachine != null && homunculusStateMachine.getState() != null)
				? homunculusStateMachine.getState().getId()
				: HomunculusStates.INIT;
	}

	public Class<Homunculus> getStaticClass() {
		return Homunculus.class;
	}

	public List<String> getTags() {
		final List<String> result = new ArrayList<>();
		if (getRuntimeConfig() != null) {
			result.addAll(getRuntimeConfig().getTags());
			result.add(getRuntimeConfig().dataCenter);
			result.add(getRuntimeConfig().author);
			result.add(String.valueOf(getRuntimeConfig().subVersion));
			result.add(getRuntimeConfig().tagVersion);
			result.add(String.valueOf(getRuntimeConfig().version));
		}
		result.add(getAgentUniqueName());
		return result;
	}

	public HomunculusStates getTargetState() {
		return stateTarget;
	}

	public boolean isRunning() {
		return HomunculusStates.RUNNING.equals(getState());
	}

	public boolean isSessionValid(String sessionId) {
		return homunculusSession.getSessionInformation(sessionId) != null;
	}

	public String loginAgent(String username, String password, String sessionId) {
		final UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
		final Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
		if (sessionId == null || sessionId.isEmpty()) {
			if (homunculusSession.getAllSessions(result, false).isEmpty()) {
				sessionId = UUID.randomUUID().toString().replace("-", "");
				homunculusSession.registerNewSession(sessionId, result);
			} else {
				sessionId = homunculusSession.getAllSessions(result, false).get(0).getSessionId();
			}
		} else {
			if (homunculusSession.getSessionInformation(sessionId) == null
					|| homunculusSession.getSessionInformation(sessionId).isExpired()) {
				homunculusSession.registerNewSession(sessionId, result);
			}
		}
		return sessionId;
	}

	public void logoutFromAgent() {
		SecurityContextHolder.clearContext();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		logger.debug(" event: {}", event);
		if (event instanceof ContextRefreshedEvent) {
			// avvio a contesto spring caricato
			onApplicationEventFlag = true;
			checkDualStart();
		}
	}

	@ManagedOperation
	public void sendEvent(HomunculusEvents event) {
		homunculusStateMachine.sendEvent(event);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		updateApplicationContext(applicationContext);
	}

	@Override
	public void setBeanName(String name) {
		beanName = name;
	}

	public void setContextData(String index, Object data) {
		if (dataStore != null)
			dataStore.put(index, data);
	}

	public void setDataAddress(DataAddressHomunculus dataAddress) {
		this.dataAddress = dataAddress;
	}

	public void setMyAliasCertInKeystore(String myAliasCertInKeystore) {
		this.myAliasCertInKeystore = myAliasCertInKeystore;
	}

	public void setMyIdentityKeystore(KeystoreConfig myIdentityKeystore) {
		this.myIdentityKeystore = myIdentityKeystore;
	}

	public void setTargetConfig(EdgeConfig config) {
		targetConfig = config;
	}

	public void terminateSession(String sessionId) {
		homunculusSession.removeSessionInformation(sessionId);
		logoutFromAgent();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Homunculus [");
		if (agentUniqueName != null)
			builder.append("agentUniqueName=").append(agentUniqueName).append(", ");
		if (homunculusStateMachine != null)
			builder.append("stateMachine=").append(homunculusStateMachine).append(", ");
		if (homunculusSession != null)
			builder.append("homunculusSession=").append(homunculusSession).append(", ");
		if (starterProperties != null)
			builder.append("starterProperties=").append(starterProperties).append(", ");
		if (runtimeConfig != null)
			builder.append("runtimeConfig=").append(runtimeConfig).append(", ");
		if (targetConfig != null)
			builder.append("targetConfig=").append(targetConfig).append(", ");
		if (bootstrapConfig != null)
			builder.append("bootstrapConfig=").append(bootstrapConfig).append(", ");
		if (reloadConfig != null)
			builder.append("reloadConfig=").append(reloadConfig).append(", ");
		if (stateTarget != null)
			builder.append("stateTarget=").append(stateTarget).append(", ");
		if (statesBefore != null)
			builder.append("statesBefore=").append(statesBefore).append(", ");
		if (components != null)
			builder.append("components=").append(components).append(", ");
		if (dataStore != null)
			builder.append("dataStore=").append(dataStore).append(", ");
		if (localUsers != null)
			builder.append("localUsers=").append(localUsers).append(", ");
		if (beanName != null)
			builder.append("beanName=").append(beanName).append(", ");
		if (beaconClient != null)
			builder.append("beaconClient=").append(beaconClient).append(", ");
		if (dataAddress != null)
			builder.append("dataAddress=").append(dataAddress).append(", ");
		if (myIdentityKeystore != null)
			builder.append("myIdentityKeystore=").append(myIdentityKeystore).append(", ");
		if (myAliasCertInKeystore != null)
			builder.append("myAliasCertInKeystore=").append(myAliasCertInKeystore).append(", ");
		builder.append("firstStateFired=").append(firstStateFired).append("]");
		return builder.toString();
	}

	@PostConstruct
	void afterSpringInit() throws Exception {
		afterSpringInitFlag = true;
		checkDualStart();
	}

	// workaround Spring State Machine
	// @OnStateChanged(target = "CONFIGURED")
	synchronized void configureAgent() {
		if (runtimeConfig == null) {
			logger.warn("Required running state without conf");
			homunculusStateMachine.sendEvent(HomunculusEvents.EXCEPTION);
		}
		if (stateTarget == null && runtimeConfig != null) {
			stateTarget = runtimeConfig.targetRunLevel;
		}
		if (runtimeConfig != null && runtimeConfig.updateFileConfig == true) {
			updateFileConfig(runtimeConfig);
		}
		if (stateTarget != null && stateTarget.equals(HomunculusStates.RUNNING)) {
			timerScheduler = new Timer("t-" + (Math.round((new Random().nextDouble() * 99))) + "-" + THREAD_ID);
			homunculusStateMachine.sendEvent(HomunculusEvents.START);
		} else {
			logger.warn("stateTarget is null in runtime config");
		}
	}

	// workaround Spring State Machine
	// @OnStateChanged(target = "KILLED")
	synchronized void finalizeAgent() {
		try {
			if (recMan != null) {
				closeDataMap();
				recMan = null;
			}
			for (final ServiceComponent<EdgeComponent> targetService : components) {
				targetService.stop();
				targetService.close();
			}
			components.clear();
			if (beaconClient != null) {
				beaconClient.close();
			}
			if (timerScheduler != null) {
				timerScheduler.cancel();
				timerScheduler.purge();
				timerScheduler = null;
			}
			if (dataAddress != null) {
				dataAddress.close();
			}
		} catch (final Exception aa) {
			logger.logException("error during finalize phase", aa);
		}
	}

	// workaround Spring State Machine
	// @OnStateMachineStart
	synchronized void initAgent() {
		if (starterProperties.isConsoleOnly() != null && !(starterProperties.isConsoleOnly().equals("true")
				|| starterProperties.isConsoleOnly().equals("yes"))) {
			homunculusStateMachine.sendEvent(HomunculusEvents.BOOTSTRAP);
		} else {
			logger.warn("console only true, run just the command line");
		}
		if (starterProperties.isShowRegistrationCode().equals("true")
				|| starterProperties.isShowRegistrationCode().equals("yes")) {
			System.out.println("__________________________________________________");
			System.out.println("       REGISTRATION CODE: " + REGISTRATION_PIN);
			System.out.println("__________________________________________________\n");
		}
		if (starterProperties.getRossonetChatServer() != null && !starterProperties.getRossonetChatServer().isEmpty()) {
			try {
				final String sessionId = UUID.randomUUID().toString().replace("-", "") + "_"
						+ starterProperties.getRossonetChatServer();
				homunculusSession.registerNewSession(sessionId, sessionId);
				final RpcConversation rpc = homunculusSession.getRpc(sessionId);
				matterMostRpcManager = new MatterMostRpcManager(rpc);
				mattermostClient = new MatterMostClientAr4k(starterProperties.getRossonetChatServer(),
						starterProperties.getRossonetChatUser(), starterProperties.getRossonetChatPassword(),
						starterProperties.getRossonetChatToken(), matterMostRpcManager.getCallBack());
				matterMostRpcManager.setMattermostClient(mattermostClient);
				mattermostClient.reportStatusInLog();
			} catch (Exception a) {
				logger.logException("during connection to " + starterProperties.getRossonetChatServer(), a);
			}
		}
	}

	void prepareAgentStasis() {
		logger.warn("PUTTING AGENT IN STASIS STATE...");
		finalizeAgent();
	}

	void prepareRestart() {
		logger.warn("RESTARTING AGENT...");
		finalizeAgent();
	}

	void reloadAgent() {
		logger.warn("RELOAD AGENT...");
		resetAgent();
	}

	// workaround Spring State Machine
	// @OnStateChanged(target = "KILLED")
	synchronized void resetAgent() {
		finalizeAgent();
		try {
			bootstrapConfig = null;
			targetConfig = null;
			runtimeConfig = null;
			dataAddress = new DataAddressHomunculus(this);
		} catch (final Exception aa) {
			logger.logException("error during reloading phase", aa);
		}
	}

	void runPostScript() {
		if (runtimeConfig.postScript != null && runtimeConfig.postScriptLanguage != null
				&& !runtimeConfig.postScript.isEmpty() && !runtimeConfig.postScriptLanguage.isEmpty()) {
			logger.info("run post script in language {}", runtimeConfig.postScriptLanguage);
			final String scriptLabel = "postscript";
			try {
				scriptRunner(scriptLabel, runtimeConfig.postScriptLanguage, runtimeConfig.postScript);
			} catch (final Exception a) {
				logger.logException("error running " + scriptLabel, a);
			}
		}
	}

	void runPreScript() {
		if (runtimeConfig.preScript != null && runtimeConfig.preScriptLanguage != null
				&& !runtimeConfig.preScript.isEmpty() && !runtimeConfig.preScriptLanguage.isEmpty()) {
			logger.info("run pre script in language {}", runtimeConfig.preScriptLanguage);
			final String scriptLabel = "prescript";
			try {
				scriptRunner(scriptLabel, runtimeConfig.preScriptLanguage, runtimeConfig.preScript);
			} catch (final Exception a) {
				logger.logException("error running " + scriptLabel, a);
			}
		}
	}

	// workaround Spring State Machine
	// @OnStateChanged(target = "RUNNING")
	// avvia i servizi
	synchronized void runServices() {
		final List<ServiceConfig> sortedList = new ArrayList<>(runtimeConfig.pots);
		Collections.sort(sortedList, comparatorOrderPots);
		for (final ServiceConfig confServizio : sortedList) {
			if (confServizio instanceof ServiceConfig) {
				logger.info("run {} as service", confServizio);
				runSeedService(confServizio);
			}
		}
	}

	@SuppressWarnings("unchecked")
	void setInitialAuth() {
		if (starterProperties.getAdminPassword() != null && !starterProperties.getAdminPassword().isEmpty()) {
			final EdgeUserDetails admin = new EdgeUserDetails();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode(starterProperties.getAdminPassword()));
			final GrantedAuthority grantedAuthorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
			final GrantedAuthority grantedAuthorityUser = new SimpleGrantedAuthority("ROLE_USER");
			((Set<GrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityAdmin);
			((Set<GrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityUser);
			boolean free = true;
			for (final EdgeUserDetails q : localUsers) {
				if (q.getUsername().equals("admin")) {
					free = false;
					break;
				}
			}
			if (free) {
				localUsers.add(admin);
				logger.warn("created user " + admin.getUsername());
			}
		}
	}

	void startCheckingNextConfig() {
		reloadConfig = null;
		if (runtimeConfig != null
				&& (runtimeConfig.nextConfigFile != null || runtimeConfig.nextConfigDns != null
						|| runtimeConfig.nextConfigWeb != null)
				&& runtimeConfig.configCheckPeriod != null && runtimeConfig.configCheckPeriod > 0) {
			if (runtimeConfig.nextConfigFile != null) {
				timerScheduler.schedule(checkFileConfigUpdate(runtimeConfig.nextConfigFile),
						runtimeConfig.configCheckPeriod, runtimeConfig.configCheckPeriod);
				logger.warn("scheduled periodically configuration checking on file {} with rate time of {} ms",
						runtimeConfig.nextConfigWeb, runtimeConfig.configCheckPeriod);
			}
			if (runtimeConfig.nextConfigDns != null) {
				timerScheduler.schedule(checkDnsConfigUpdate(runtimeConfig.nextConfigDns),
						runtimeConfig.configCheckPeriod, runtimeConfig.configCheckPeriod);
				logger.warn("scheduled periodically configuration checking on dns {} with rate time of {} ms",
						runtimeConfig.nextConfigWeb, runtimeConfig.configCheckPeriod);
			}
			if (runtimeConfig.nextConfigWeb != null) {
				timerScheduler.schedule(checkWebConfigUpdate(runtimeConfig.nextConfigWeb),
						runtimeConfig.configCheckPeriod, runtimeConfig.configCheckPeriod);
				logger.warn("scheduled periodically configuration checking on url {} with rate time of {} ms",
						runtimeConfig.nextConfigWeb, runtimeConfig.configCheckPeriod);
			}
		}
	}

	// workaround Spring State Machine
	// @OnStateChanged(target = "STAMINAL")
	synchronized void startingAgent() {
		new File(ConfigHelper.resolveWorkingString(starterProperties.getConfPath(), true)).mkdirs();
		try {
			if (dataStore == null) {
				recMan = RecordManagerFactory
						.createRecordManager(ConfigHelper.resolveWorkingString(starterProperties.getConfPath(), true)
								+ "/" + starterProperties.getHomunculusDatastoreFileName());
				dataStore = recMan.treeMap(DB_DATASTORE_NAME);
				logger.info("datastore on Homunculus started");
			}
		} catch (final IOException e) {
			logger.logException("datastore on Homunculus problem", e);
		}
		setMasterKeystore();
		bootstrapConfig = resolveBootstrapConfig();
		popolateAddressSpace();
		setInitialAuth();
		try {
			checkBeaconClient();
		} catch (final Exception a) {
			logger.warn("Error connecting beacon client -> " + EdgeLogger.stackTraceToString(a, 4));
		}
		if (runtimeConfig == null && targetConfig == null && bootstrapConfig != null) {
			targetConfig = bootstrapConfig;
		}
		if (runtimeConfig != null || (runtimeConfig == null && targetConfig != null)) {
			if (targetConfig != null)
				runtimeConfig = targetConfig;
		}
		if (runtimeConfig != null && runtimeConfig.name != null && !runtimeConfig.name.isEmpty()) {
			logger.info("Starting with config: " + runtimeConfig.toString());
			homunculusStateMachine.sendEvent(HomunculusEvents.SETCONF);
		}
	}

	// workaround Spring State Machine
	// @OnStateChanged()
	synchronized void stateChanged() {
		logger.info("State change in Homunculus to " + getState());
		statesBefore.put(new Instant(), getState());
	}

	protected String getBeanName() {
		return beanName;
	}

	private void checkBeaconClient() {
		if (runtimeConfig != null && runtimeConfig.beaconServer != null && !runtimeConfig.beaconServer.isEmpty()) {
			try {
				indexBeaconClient++;
				if (indexBeaconClient > (ConfigHelper
						.countWorkingStringSplittedByComma(filterBeaconUrl(runtimeConfig.beaconServer), false) - 1)) {
					indexBeaconClient = 0;
				}
				logger.info("TRY CONNECTION TO BEACON IN CONFIG RUNTIME AT "
						+ ConfigHelper.resolveWorkingStringSplittedByComma(filterBeaconUrl(runtimeConfig.beaconServer),
								false, indexBeaconClient));
				beaconClient = connectToBeaconService(
						ConfigHelper.resolveWorkingStringSplittedByComma(filterBeaconUrl(runtimeConfig.beaconServer),
								false, indexBeaconClient),
						runtimeConfig.beaconServerCertChain, Integer.valueOf(runtimeConfig.beaconDiscoveryPort),
						runtimeConfig.beaconDiscoveryFilterString, true);
			} catch (final Exception e) {
				logger.warn("Beacon connection in config not ok: " + e.getMessage());
				logger.info(EdgeLogger.stackTraceToString(e, 40));
			}
		} else {
			if ((starterProperties.getWebRegistrationEndpoint() != null
					&& !starterProperties.getWebRegistrationEndpoint().isEmpty())
					|| Integer.valueOf(starterProperties.getBeaconDiscoveryPort()) != 0) {
				try {
					indexBeaconClient++;
					if (indexBeaconClient > (ConfigHelper.countWorkingStringSplittedByComma(
							filterBeaconUrl(filterBeaconUrl(starterProperties.getWebRegistrationEndpoint())), false)
							- 1)) {
						indexBeaconClient = 0;
					}
					if (starterProperties.getWebRegistrationEndpoint() == null
							|| starterProperties.getWebRegistrationEndpoint().isEmpty()) {
						logger.warn("Beacon connection is not configured");
					}
					logger.info("TRY CONNECTION TO BEACON AT " + ConfigHelper.resolveWorkingStringSplittedByComma(
							filterBeaconUrl(starterProperties.getWebRegistrationEndpoint()), false, indexBeaconClient));
					beaconClient = connectToBeaconService(
							ConfigHelper.resolveWorkingStringSplittedByComma(
									filterBeaconUrl(starterProperties.getWebRegistrationEndpoint()), false,
									indexBeaconClient),
							starterProperties.getBeaconCaChainPem(),
							Integer.valueOf(starterProperties.getBeaconDiscoveryPort()),
							starterProperties.getBeaconDiscoveryFilterString(), false);
				} catch (final Exception e) {
					logger.warn("Beacon connection not ok: " + e.getMessage());
					logger.info(EdgeLogger.stackTraceToString(e, 6));
				}
			}
		}
	}

	private TimerTask checkDnsConfigUpdate(String nextConfigDns) {
		return new TimerTask() {
			@Override
			public void run() {
				final EdgeConfig newTargetConfig = dnsConfigDownload(nextConfigDns,
						starterProperties.getKeystoreConfigAlias());
				elaborateNewConfig(newTargetConfig);
			}
		};
	}

	private synchronized void checkDualStart() {
		if (onApplicationEventFlag && afterSpringInitFlag && !firstStateFired) {
			firstStateFired = true;
			if (starterProperties != null) {
				agentUniqueName = ConfigHelper.generateNewUniqueName(starterProperties.getUniqueName(),
						starterProperties.getUniqueNameFile());
			}
			logger.info("STARTING AGENT...");
			Thread.currentThread().setName(THREAD_ID);
			homunculusStateMachine.start();
		}
	}

	private TimerTask checkFileConfigUpdate(String nextConfigFile) {
		return new TimerTask() {
			@Override
			public void run() {
				final EdgeConfig newTargetConfig = loadConfigFromFile(nextConfigFile,
						starterProperties.getKeystoreConfigAlias());
				elaborateNewConfig(newTargetConfig);
			}
		};
	}

	private TimerTask checkWebConfigUpdate(String nextConfigWeb) {
		return new TimerTask() {
			@Override
			public void run() {
				final EdgeConfig newTargetConfig = webConfigDownload(nextConfigWeb,
						starterProperties.getKeystoreConfigAlias());
				elaborateNewConfig(newTargetConfig);
			}
		};
	}

	private void closeDataMap() {
		try {
			recMan.close();
		} catch (final IOException e) {
			logger.info("IOException closing file data map of Homunculus");
		}
	}

	private EdgeConfig dnsConfigDownload(String dnsTarget, String cryptoAlias) {
		logger.debug("try dns config {}", dnsTarget);
		final String hostPart = dnsTarget.split("\\.")[0];
		final String domainPart = dnsTarget.replaceAll("^" + hostPart, "");
		try {
			final String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart, 3);
			try {
				if (cryptoAlias != null && !cryptoAlias.isEmpty()) {
					return (EdgeConfig) ((payloadString != null && payloadString.length() > 0)
							? ConfigHelper.fromBase64Crypto(payloadString, cryptoAlias)
							: null);
				} else {
					return (EdgeConfig) ((payloadString != null && payloadString.length() > 0)
							? ConfigHelper.fromBase64(payloadString)
							: null);
				}
			} catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | NoSuchPaddingException
					| CMSException e) {
				logger.warn("error in dns download using H:" + hostPart + " D:" + domainPart + " -> "
						+ EdgeLogger.stackTraceToString(e, 4));
				return null;
			}
		} catch (UnknownHostException | TextParseException e) {
			logger.warn("error in dns download using H:" + hostPart + " D:" + domainPart + " -> "
					+ EdgeLogger.stackTraceToString(e, 4));
			return null;
		}
	}

	private String dnsKeystoreResolvedString() {
		return ConfigHelper.resolveWorkingString(starterProperties.getDnsKeystore(), false);
	}

	private String filterBeaconUrl(String beaconServer) {
		return beaconServer;
	}

	private EdgeConfig loadConfigFromFile(String pathConfig, String cryptoAlias) {
		logger.debug("try file config {}", pathConfig);
		EdgeConfig resultConfig = null;
		try (final FileReader fileReader = new FileReader(ConfigHelper.resolveWorkingString(pathConfig, true));
				final BufferedReader bufferedReader = new BufferedReader(fileReader);) {
			final StringBuilder config = new StringBuilder();

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				config.append(line);
			}
			if (cryptoAlias != null && !cryptoAlias.isEmpty()) {
				resultConfig = (EdgeConfig) ConfigHelper.fromBase64Crypto(config.toString(), cryptoAlias);
			} else {
				resultConfig = (EdgeConfig) ConfigHelper.fromBase64(config.toString());
			}
			logger.trace("resultConfig\n{}", resultConfig);
			return resultConfig;

		} catch (final FileNotFoundException ff) {
			logger.debug("config file not found " + pathConfig);
			return null;
		} catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException
				| CMSException e) {
			logger.warn("error in config file -> " + EdgeLogger.stackTraceToString(e, 4));
			return null;
		}
	}

	private void popolateAddressSpace() {
		dataAddress.firstStart(this);
	}

	// trova la configurazione appropriata per il bootstrap in funzione dei
	// parametri di configurazione
	private EdgeConfig resolveBootstrapConfig() {
		EdgeConfig targetConfig = null;
		if (reloadConfig == null) {
			int maxConfig = 0;
			try {
				maxConfig = Integer.max(maxConfig,
						starterProperties.getWebConfigOrder() != null
								? Integer.valueOf(starterProperties.getWebConfigOrder())
								: 6);
			} catch (final Exception a) {
				logger.warn("webConfigOrder -> " + starterProperties.getWebConfigOrder());
				maxConfig = 6;
			}
			try {
				maxConfig = Integer.max(maxConfig,
						starterProperties.getDnsConfigOrder() != null
								? Integer.valueOf(starterProperties.getDnsConfigOrder())
								: 6);
			} catch (final Exception a) {
				logger.warn("dnsConfigOrder -> " + starterProperties.getDnsConfigOrder());
				maxConfig = 6;
			}
			try {
				maxConfig = Integer.max(maxConfig,
						starterProperties.getBaseConfigOrder() != null
								? Integer.valueOf(starterProperties.getBaseConfigOrder())
								: 6);
			} catch (final Exception a) {
				logger.warn("base64ConfigOrder -> " + starterProperties.getBaseConfigOrder());
				maxConfig = 5;
			}
			try {
				maxConfig = Integer.max(maxConfig,
						starterProperties.getFileConfigOrder() != null
								? Integer.valueOf(starterProperties.getFileConfigOrder())
								: 6);
			} catch (final Exception a) {
				logger.warn("fileConfigOrder -> " + starterProperties.getFileConfigOrder());
				maxConfig = 4;
			}
			for (int liv = 0; liv <= maxConfig; liv++) {
				logger.info(String.valueOf(liv) + "/" + maxConfig + " searching config...");
				if (liv == Integer.valueOf(starterProperties.getWebConfigOrder()) && targetConfig == null
						&& starterProperties.getWebConfig() != null && !starterProperties.getWebConfig().isEmpty()) {
					try {
						logger.info("try webConfig");
						targetConfig = webConfigDownload(
								ConfigHelper.resolveWorkingString(starterProperties.getWebConfig(), false),
								starterProperties.getKeystoreConfigAlias());
						if (targetConfig != null) {
							logger.info("found webConfig");
							break;
						}
					} catch (final Exception e) {
						logger.warn("error in webconfig download" + EdgeLogger.stackTraceToString(e, 4));
					}
				}
				if (liv == Integer.valueOf(starterProperties.getDnsConfigOrder()) && targetConfig == null
						&& starterProperties.getDnsConfig() != null && !starterProperties.getDnsConfig().isEmpty()) {
					try {
						logger.info("try dnsConfig");
						targetConfig = dnsConfigDownload(
								ConfigHelper.resolveWorkingString(starterProperties.getDnsConfig(), false),
								starterProperties.getKeystoreConfigAlias());
						if (targetConfig != null) {
							logger.info("found dnsConfig "
									+ ConfigHelper.resolveWorkingString(starterProperties.getDnsConfig(), false));
							break;
						}
					} catch (final Exception e) {
						logger.warn("error in dns config download" + EdgeLogger.stackTraceToString(e, 4));
					}
				}
				if (liv == Integer.valueOf(starterProperties.getBaseConfigOrder()) && targetConfig == null
						&& starterProperties.getBaseConfig() != null && !starterProperties.getBaseConfig().isEmpty()) {
					try {
						logger.info("try base64Config");
						targetConfig = (EdgeConfig) ConfigHelper.fromBase64(starterProperties.getBaseConfig());
						if (targetConfig != null) {
							logger.info("found base64Config");
							break;
						}
					} catch (final Exception e) {
						logger.warn("error in baseconfig" + EdgeLogger.stackTraceToString(e, 4));
					}
				}
				if (liv == Integer.valueOf(starterProperties.getFileConfigOrder()) && targetConfig == null
						&& starterProperties.getFileConfig() != null && !starterProperties.getFileConfig().isEmpty()) {
					try {
						logger.info("try fileConfig");
						targetConfig = loadConfigFromFile(
								ConfigHelper.resolveWorkingString(starterProperties.getFileConfig(), true),
								starterProperties.getKeystoreConfigAlias());
						if (targetConfig != null) {
							logger.info("found fileConfig {}",
									ConfigHelper.resolveWorkingString(starterProperties.getFileConfig(), true));
							break;
						}
					} catch (final Exception e) {
						logger.warn("error in fileconfig" + EdgeLogger.stackTraceToString(e, 4));
					}
				}
			}
		} else {
			targetConfig = reloadConfig;
		}
		return targetConfig;
	}

	private void runSeedService(ServiceConfig confServizio) {
		final HomunculusService service = new HomunculusService(this, confServizio, timerScheduler);
		components.add(service);
		service.start();
	}

	private void scriptRunner(String scriptLabel, String scriptLanguage, String script) {
		final ScriptEngineManagerProcess p = new ScriptEngineManagerProcess();
		p.setLabel(scriptLabel);
		p.setEngine(scriptLanguage);
		p.eval(script);
		final String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + scriptLabel;
		homunculusSession.registerNewSession(sessionId, sessionId);
		final RpcConversation rpc = homunculusSession.getRpc(sessionId);
		rpc.getScriptSessions().put(scriptLabel, p);
	}

	private void setMasterKeystore() {
		if (myIdentityKeystore == null) {
			final KeystoreConfig ks = new KeystoreConfig();
			boolean foundFile = false;
			boolean foundWeb = false;
			boolean foundDns = false;
			if (starterProperties.getKeystorePassword() != null && !starterProperties.getKeystorePassword().isEmpty()) {
				ks.keystorePassword = starterProperties.getKeystorePassword();
			}
			if (starterProperties.getKeystoreMainAlias() != null
					&& !starterProperties.getKeystoreMainAlias().isEmpty()) {
				ks.keyStoreAlias = starterProperties.getKeystoreMainAlias();
			}
			ks.filePathPre = starterProperties.getFileKeystore() != null
					? ConfigHelper.resolveWorkingString(starterProperties.getFileKeystore(), true)
					: DEFAULT_KS_PATH;
			if (starterProperties.getFileKeystore() != null && !starterProperties.getFileKeystore().isEmpty()) {
				try {
					if (new File(ks.filePathPre).exists()) {
						if (ks.check()) {
							foundFile = true;
							logger.info("use keystore file {}", ks);
						} else {
							logger.info("keystore not works");
						}
					} else {
						logger.warn("keystore file not found ( {} )", ks);
					}
				} catch (final Exception a) {
					logger.warn("keystore error -> {}", a.getMessage());
				}
			} else {
				logger.info("value of fileKeystore is null, use: {}", ks);
			}
			final String errorDeletingKeyStoreLabel = "error deleting wrong keystore";
			if (!foundFile) {
				try {
					Files.deleteIfExists(Paths.get(ks.filePathPre));
				} catch (final IOException e) {
					logger.logException(errorDeletingKeyStoreLabel, e);
				}
				if (starterProperties.getWebKeystore() != null && !starterProperties.getWebKeystore().isEmpty()) {
					try {
						logger.info("try keystore from web url: {}", webKeystoreResolvedString());
						HardwareHelper.downloadFileFromUrl(ks.filePathPre, webKeystoreResolvedString());
					} catch (final Exception e) {
						foundWeb = false;
						logger.warn("webKeystore " + webKeystoreResolvedString() + " not found");
					}
					try {
						if (new File(ks.filePathPre).exists() && ks.check()) {
							foundWeb = true;
							logger.info("found web keystore {}", webKeystoreResolvedString());
						} else {
							logger.info("web keystore not works");
						}
					} catch (final Exception a) {
						logger.info("keystore error -> " + a.getMessage());
					}

				}
				if (!foundWeb && !foundFile) {
					try {
						Files.deleteIfExists(Paths.get(ks.filePathPre));
					} catch (final IOException e) {
						logger.logException(errorDeletingKeyStoreLabel, e);
					}
					if (starterProperties.getDnsKeystore() != null && !starterProperties.getDnsKeystore().isEmpty()) {
						try {
							logger.info("try keystore from dns: {}", dnsKeystoreResolvedString());
							final String hostPart = dnsKeystoreResolvedString().split("\\.")[0];
							final String domainPart = dnsKeystoreResolvedString().replaceAll("^" + hostPart, "");
							final String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart, 5);
							final byte[] data = Base64.getDecoder().decode(payloadString);
							final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
							final byte[] returnData = (byte[]) ois.readObject();
							ois.close();
							if (payloadString != null && payloadString.length() > 0) {
								FileUtils.writeByteArrayToFile(new File(ks.filePathPre), returnData);
							}
							try {
								if (new File(ks.filePathPre).exists() && ks.check()) {
									foundDns = true;
									logger.info("found dns keystore {}", dnsKeystoreResolvedString());
								} else {
									logger.info("dns keystore not works");
								}
							} catch (final Exception a) {
								logger.info("keystore error -> " + a.getMessage());
							}
						} catch (final Exception e) {
							logger.warn("dnsKeystore " + dnsKeystoreResolvedString() + " not found");
						}
					}
				}
			}
			// se alla fine non è stato trovato un keystore, lo creo
			if (!foundWeb && !foundFile && !foundDns) {
				try {
					Files.deleteIfExists(Paths.get(ks.filePathPre));
				} catch (final IOException e) {
					logger.logException(errorDeletingKeyStoreLabel, e);
				}
				logger.warn("new keystore: {}", ks);
				ks.createSelfSignedCert(getAgentUniqueName() + "-master", ConfigHelper.organization, ConfigHelper.unit,
						ConfigHelper.locality, ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri,
						ConfigHelper.dns, ConfigHelper.ip, ks.keyStoreAlias, true, VALIDITY_CERT_DAYS);
			}
			setMyIdentityKeystore(ks);
			setMyAliasCertInKeystore(ks.keyStoreAlias);
			logger.info(
					"Certificate for Homunculus: " + ks.getClientCertificate(ks.keyStoreAlias).getSubjectX500Principal()
							+ " - alias " + ks.keyStoreAlias);
		} else {
			logger.info("Use keystore " + myIdentityKeystore.toString());
		}
	}

	private void updateFileConfig(EdgeConfig config) {
		final String fileTarget = ConfigHelper.resolveWorkingString(starterProperties.getFileConfig(), true);
		if (starterProperties.getKeystoreConfigAlias() != null
				&& !starterProperties.getKeystoreConfigAlias().isEmpty()) {
			try {
				Files.write(Paths.get(fileTarget),
						ConfigHelper.toBase64Crypto(config, starterProperties.getKeystoreConfigAlias()).getBytes(),
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				logger.info("crypto configuration in {} updated with runtime config", fileTarget);
			} catch (final Exception e) {
				logger.logException("error saving crypto configuration in runtime to " + fileTarget, e);
			}
		} else {
			try {
				Files.write(Paths.get(fileTarget), ConfigHelper.toBase64(config).getBytes(), StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);
				logger.info("configuration in {} updated with runtime config", fileTarget);
			} catch (final Exception e) {
				logger.logException("error saving configuration in runtime to " + fileTarget, e);
			}
		}
	}

	private EdgeConfig webConfigDownload(String webConfigTarget, String cryptoAlias) {
		logger.info("try web config {}", webConfigTarget);
		final String temporaryFile = agentUniqueName + ".ar4k.conf"
				+ ((cryptoAlias != null && !cryptoAlias.isEmpty()) ? ".crypto" : "");
		try (BufferedInputStream in = new BufferedInputStream(new URL(webConfigTarget).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
			final byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			return loadConfigFromFile(temporaryFile, cryptoAlias);
		} catch (final ConnectException c) {
			logger.info(webConfigTarget + " is unreachable");
			return null;
		} catch (final UnknownHostException u) {
			logger.info(webConfigTarget + " is not a valid host name");
			return null;
		} catch (final Exception e) {
			logger.warn("url for web config failed: " + webConfigTarget);
			logger.warn("Error downloading web config -> " + EdgeLogger.stackTraceToString(e, 4));
			return null;
		}

	}

	private String webKeystoreResolvedString() {
		return ConfigHelper.resolveWorkingString(starterProperties.getWebKeystore(), false);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static final ContextCreationHelper getNewHomunculusInNewContext(Class<?> springMasterClass,
			ExecutorService executor, String loggerFile, String keyStore, int webPort, List<String> args,
			EdgeConfig homunculusConfig, String mainAliasInKeystore, String keystoreBeaconAlias,
			String webRegistrationEndpoint) {
		return new ContextCreationHelper(springMasterClass, executor, loggerFile, keyStore, webPort, args,
				homunculusConfig, mainAliasInKeystore, keystoreBeaconAlias, webRegistrationEndpoint);
	}

	public static String getRegistrationPin() {
		return REGISTRATION_PIN;
	}

	static synchronized void updateApplicationContext(ApplicationContext applicationContext) {
		Homunculus.applicationContext = applicationContext;
	}

	public MatterMostClientAr4k getMattermostClient() {
		return mattermostClient;
	}

}
