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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.Ar4kException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.ar4k.agent.tunnels.http.grpc.BeaconClient;
//import org.ar4k.agent.tunnels.socket.ISocketFactoryComponent;
//import org.ar4k.agent.tribe.AtomixTribeComponent;
import org.joda.time.Instant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.Shell;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

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
@ManagedResource(objectName = "bean:name=anima", description = "Gestore principale agente", log = true, logFile = "anima.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "anima")
@Component("anima")
@EnableMBeanExport
@Scope("singleton")
@EnableJms
// Non funzionano le annotazioni sotto. Nel primo stati non chiamano il metodo. Bisogna mtterci un Thread.sleep
//@EnableWithStateMachine
//@WithStateMachine
public class Anima implements ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware, Closeable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private final String dbDataStorePath = "~/.ar4k/anima_datastore-" + UUID.randomUUID().toString();
  private final String dbDataStoreName = "datastore";

  public Anima() {
    agentUniqueName = generateNewUniqueName();
  }

  public static String generateNewUniqueName() {
    String result = null;
    try {
      result = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      logger.info("no hostname found...");
      result = "";
    }
    result = result + "_" + UUID.randomUUID().toString().replaceAll("-", "");
    return result;
  }

  private String agentUniqueName = null;

  @Autowired
  private Shell shell;

  @Autowired
  private StateMachine<AnimaStates, AnimaEvents> animaStateMachine;

  // @Autowired
  // private AnimaStateMachineConfig animaStateMachineConfig;

  @Autowired
  private AnimaHomunculus animaHomunculus;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // parametri importate da Spring Boot
  @Value("${ar4k.fileKeystore}")
  private String fileKeystore;
  @Value("${ar4k.webKeystore}")
  private String webKeystore;
  @Value("${ar4k.dnsKeystore}")
  private String dnsKeystore;
  @Value("${ar4k.keystorePassword}")
  private String keystorePassword;
  @Value("${ar4k.keystoreCaAlias}")
  private String keystoreCaAlias;
  @Value("${ar4k.confPath}")
  private String confPath;
  @Value("${ar4k.fileConfig}")
  private String fileConfig;
  @Value("${ar4k.webConfig}")
  private String webConfig;
  @Value("${ar4k.dnsConfig}")
  private String dnsConfig;
  @Value("${ar4k.baseConfig}")
  private String baseConfig;
  @Value("${ar4k.otpRegistrationSeed}")
  private String otpRegistrationSeed;
  @Value("${ar4k.adminPassword}")
  private String adminPassword;
  @Value("${ar4k.webRegistrationEndpoint}")
  private String webRegistrationEndpoint;
  @Value("${ar4k.dnsRegistrationEndpoint}")
  private String dnsRegistrationEndpoint;
  @Value("${ar4k.beaconDiscoveryFilterString}")
  private String beaconDiscoveryFilterString;
  @Value("${ar4k.beaconDiscoveryPort}")
  private Integer beaconDiscoveryPort;
  @Value("${ar4k.fileConfigOrder}")
  private Integer fileConfigOrder;
  @Value("${ar4k.webConfigOrder}")
  private Integer webConfigOrder;
  @Value("${ar4k.dnsConfigOrder}")
  private Integer dnsConfigOrder;
  @Value("${ar4k.baseConfigOrder}")
  private Integer base64ConfigOrder;
  @Value("${ar4k.threadSleep}")
  private Long threadSleep;
  @Value("${ar4k.consoleOnly}")
  private boolean consoleOnly;
  @Value("${ar4k.logoUrl}")
  private String logoUrl;

  // gestione configurazioni
  // attuale configurazione in runtime
  private Ar4kConfig runtimeConfig = null;
  // configurazione obbiettivo durante le transizioni
  private Ar4kConfig targetConfig = null;
  // configurazione iniziale di bootStrap derivata dalle variabili Spring
  private Ar4kConfig bootStrapConfig = null;
  private AnimaStates stateTarget = AnimaStates.RUNNING;
  private Map<Instant, AnimaStates> statesBefore = new HashMap<Instant, AnimaStates>();

  // TODO implementare l'esecuzione dei pre e post script

  // array keystore disponibili
  private Set<KeystoreConfig> keyStores = new HashSet<KeystoreConfig>();

  // assegnato da Spring tramite setter al boot
  private static ApplicationContext applicationContext;

  private Set<Ar4kComponent> components = new HashSet<Ar4kComponent>();

  private Map<String, Object> dataStore = null;

  private Collection<Ar4kUserDetails> localUsers = new HashSet<>();

  private transient RecordManager recMan = null;

  private String beanName = null;

  private BeaconClient beaconClient = null;

  private DataAddress dataAddress = new DataAddress();

  public static enum AnimaStates {
    INIT, STAMINAL, CONFIGURED, RUNNING, KILLED, FAULTED, STASIS
  }

  // tipi di router interno supportato per gestire lo scambio dei messagi tra gli
  // agenti
  public static enum AnimaRouterType {
    NONE, PRODUCTION, DEVELOP, ROAD
  }

  public static enum AnimaEvents {
    BOOTSTRAP, SETCONF, START, STOP, PAUSE, HIBERNATION, EXCEPTION
  }

  @ManagedOperation
  public void sendEvent(AnimaEvents event) {
    animaStateMachine.sendEvent(event);
  }

  @ManagedOperation
  public AnimaStates getState() {
    return animaStateMachine.getState().getId();
  }

  public boolean isRunning() {
    return AnimaStates.RUNNING.equals(getState());
  }

  @Override
  public void close() {
    animaStateMachine.sendEvent(AnimaEvents.STOP);
    animaStateMachine.stop();
    animaStateMachine = null;
  }

  // workaround Spring State Machine
  // @OnStateChanged()
  public synchronized void stateChanged() {
    logger.info("State change in ANIMA to " + getState());
    statesBefore.put(new Instant(), getState());
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "KILLED")
  public synchronized void finalizeAgent() {
    for (Ar4kComponent targetService : components) {
      targetService.kill();
    }
    components = null;
  }

  @SuppressWarnings("unchecked")
  private void setInitialAuth() {
    if (adminPassword != null && !adminPassword.isEmpty()) {
      Ar4kUserDetails admin = new Ar4kUserDetails();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode((CharSequence) adminPassword));
      SimpleGrantedAuthority grantedAuthorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
      SimpleGrantedAuthority grantedAuthorityUser = new SimpleGrantedAuthority("ROLE_USER");
      ((Set<SimpleGrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityAdmin);
      ((Set<SimpleGrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityUser);
      boolean free = true;
      for (Ar4kUserDetails q : localUsers) {
        if (q.getUsername().equals("admin")) {
          free = false;
          break;
        }
      }
      if (free == true) {
        localUsers.add(admin);
        logger.warn("create user " + admin.getUsername());
      }
    }
  }

  @PostConstruct
  public void afterSpringInit() throws Exception {
    animaStateMachine.start();
  }

  // workaround Spring State Machine
  // @OnStateMachineStart
  public synchronized void initAgent() {
    // System.out.println("First state " + consoleOnly);
    if (!consoleOnly) {
      animaStateMachine.sendEvent(AnimaEvents.BOOTSTRAP);
    }
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "STAMINAL")
  public synchronized void startingAgent() {
    new File(confPath.replaceFirst("^~", System.getProperty("user.home"))).mkdirs();
    fileConfig = fileConfig.replaceFirst("^~", System.getProperty("user.home"));
    try {
      if (dataStore == null) {
        recMan = RecordManagerFactory
            .createRecordManager(dbDataStorePath.replaceFirst("^~", System.getProperty("user.home")));
        dataStore = recMan.treeMap(dbDataStoreName);
        logger.info("datastore on Anima started");
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      logger.logException(e);
    }
    bootStrapConfig = resolveBootstrapConfig();
    setInitialAuth();
    checkBeaconClient();
    if (runtimeConfig == null && targetConfig == null && bootStrapConfig != null) {
      targetConfig = bootStrapConfig;
    }
    if (runtimeConfig != null || (runtimeConfig == null && targetConfig != null)) {
      if (targetConfig != null)
        runtimeConfig = targetConfig;
    }
    if (runtimeConfig != null && runtimeConfig.name != null && !runtimeConfig.name.isEmpty()) {
      logger.info("Starting with config: " + runtimeConfig.toString());
      animaStateMachine.sendEvent(AnimaEvents.SETCONF);
    }
  }

  private void checkBeaconClient() {
    if (webRegistrationEndpoint != null && !webRegistrationEndpoint.isEmpty()) {
      try {
        connectToBeaconService(webRegistrationEndpoint, beaconDiscoveryPort, beaconDiscoveryFilterString);
      } catch (Exception e) {
        logger.warn("Beacon connection not ok: " + e.getMessage());
        logger.logException(e);
      }
    }
  }

  public BeaconClient connectToBeaconService(String urlBeacon, int discoveryPort, String discoveryFilter) {
    URL urlTarget;
    if (beaconClient != null) {
      logger.info("This agent is connected to another Beacon service");
    }
    try {
      urlTarget = new URL(urlBeacon);
      String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + urlBeacon;
      animaHomunculus.registerNewSession(sessionId, sessionId);
      RpcConversation rpc = animaHomunculus.getRpc(sessionId);
      rpc.setShell(shell);
      beaconClient = new BeaconClient(rpc, urlTarget.getHost(), urlTarget.getPort(), discoveryPort, discoveryFilter,
          getAgentUniqueName());
      if (beaconClient != null && beaconClient.getStateConnection().equals(ConnectivityState.READY)) {
        logger.info("found Beacon endpoint: " + urlBeacon);
        if (!getAgentUniqueName().equals(beaconClient.getAgentUniqueName())) {
          setAgentUniqueName(beaconClient.getAgentUniqueName());
          logger.info("the unique name is changed in " + getAgentUniqueName());
        }
      } else {
        logger.info("the Beacon endpoint " + urlBeacon + " return " + beaconClient.getStateConnection());
      }
    } catch (IOException e) {
      logger.info("the url " + urlBeacon + " is malformed or unreachable [" + e.getCause() + "]");
      logger.logException(e);
    }
    return beaconClient;
  }

  // trova la configurazione appropriata per il bootstrap in funzione dei
  // parametri di configurazione
  private Ar4kConfig resolveBootstrapConfig() {
    // System.out.println("searching file " + fileConfig != null ? fileConfig :
    // "NaN");
    Ar4kConfig targetConfig = null;
    // se impostato il flag solo console esclude qualsiasi ragionamento
    if (consoleOnly == true) {
      if (logger != null)
        logger.warn("console only true, run just the command line");
    } else {
      // System.out.println("\nResolving bootstrap config...\n");
      int maxConfig = fileConfigOrder;
      if (maxConfig < webConfigOrder)
        maxConfig = webConfigOrder;
      if (maxConfig < dnsConfigOrder)
        maxConfig = dnsConfigOrder;
      if (maxConfig < base64ConfigOrder)
        maxConfig = base64ConfigOrder;
      for (int liv = 0; liv <= maxConfig; liv++) {
        if (liv == webConfigOrder && targetConfig == null) {
          // TODO: implementare caricamento da web
        }
        if (liv == dnsConfigOrder && targetConfig == null) {
          // TODO: implementare caricamento configurazione da DNS
        }
        if (liv == base64ConfigOrder && targetConfig == null && baseConfig != null && !baseConfig.equals("")) {
          try {
            targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(baseConfig);
          } catch (ClassNotFoundException | IOException e) {
            if (logger != null)
              logger.warn(e.getMessage());
            logger.logException(e);
          }
        }
        if (liv == fileConfigOrder && targetConfig == null && fileConfig != null && !fileConfig.equals("")) {
          try {
            String config = "";
            // System.out.println("config path " + fileConfig.replaceFirst("^~",
            // System.getProperty("user.home")));
            FileReader fileReader = new FileReader(fileConfig.replaceFirst("^~", System.getProperty("user.home")));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
              config = config + line;
            }
            bufferedReader.close();
            targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(config);
          } catch (IOException | ClassNotFoundException e) {
            if (logger != null)
              logger.logException(e);
            logger.warn(e.getMessage());
          }
        }
      }
    }
    return targetConfig;
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "CONFIGURED")
  public synchronized void configureAgent() {
    if (runtimeConfig == null) {
      logger.warn("Required running state without conf");
      animaStateMachine.sendEvent(AnimaEvents.EXCEPTION);
    }
    if (stateTarget == null && runtimeConfig != null) {
      stateTarget = runtimeConfig.targetRunLevel;
    }
    if (stateTarget.equals(AnimaStates.RUNNING))
      animaStateMachine.sendEvent(AnimaEvents.START);
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "RUNNING")
  public synchronized void runServices() {
    for (PotConfig confServizio : runtimeConfig.pots) {
      if (confServizio instanceof ServiceConfig) {
        try {
          runSeedService((ServiceConfig) confServizio);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException e) {
          throw new Ar4kException("problem trying to run service " + confServizio.getName(), e.getCause());
        }
      }
    }
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "RUNNING")
  public synchronized void runPots() {
    for (PotConfig confVaso : runtimeConfig.pots) {
      try {
        runSeedPot(confVaso);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        throw new Ar4kException("problem trying to run " + confVaso.getName(), e.getCause());
      }
    }
  }

  private void runSeedPot(PotConfig potConfig)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = potConfig.getClass().getMethod("instantiate");
    Ar4kComponent targetService;
    targetService = (Ar4kComponent) method.invoke(potConfig);
    targetService.setConfiguration((PotConfig) potConfig);
    components.add(targetService);
    targetService.init();
  }

  private void runSeedService(ServiceConfig confServizio)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = confServizio.getClass().getMethod("instantiate");
    ServiceComponent targetService;
    targetService = (ServiceComponent) method.invoke(confServizio);
    targetService.setConfiguration((ServiceConfig) confServizio);
    targetService.setAnima(this);
    components.add(targetService);
    targetService.start();
  }

  public Map<String, String> getEnvironmentVariables() {
    Map<String, String> ritorno = new HashMap<String, String>();
    ritorno.put("ar4k.fileKeystore", fileKeystore);
    ritorno.put("ar4k.webKeystore", webKeystore);
    ritorno.put("ar4k.dnsKeystore", dnsKeystore);
    ritorno.put("ar4k.keystorePassword", keystorePassword);
    ritorno.put("ar4k.keystoreCaAlias", keystoreCaAlias);
    ritorno.put("ar4k.confPath", confPath);
    ritorno.put("ar4k.fileConfig", fileConfig);
    ritorno.put("ar4k.webConfig", webConfig);
    ritorno.put("ar4k.dnsConfig", dnsConfig);
    ritorno.put("ar4k.baseConfig", baseConfig);
    ritorno.put("ar4k.otpRegistrationSeed", otpRegistrationSeed);
    ritorno.put("ar4k.webRegistrationEndpoint", webRegistrationEndpoint);
    ritorno.put("ar4k.dnsRegistrationEndpoint", dnsRegistrationEndpoint);
    ritorno.put("ar4k.beaconDiscoveryPort", String.valueOf(beaconDiscoveryPort));
    ritorno.put("ar4k.beaconDiscoveryFilterString", beaconDiscoveryFilterString);
    ritorno.put("ar4k.fileConfigOrder", String.valueOf(fileConfigOrder));
    ritorno.put("ar4k.webConfigOrder", String.valueOf(webConfigOrder));
    ritorno.put("ar4k.dnsConfigOrder", String.valueOf(dnsConfigOrder));
    ritorno.put("ar4k.baseConfigOrder", String.valueOf(base64ConfigOrder));
    ritorno.put("ar4k.threadSleep", String.valueOf(threadSleep));
    ritorno.put("ar4k.consoleOnly", String.valueOf(consoleOnly));
    ritorno.put("ar4k.logoUrl", String.valueOf(logoUrl));
    return ritorno;
  }

  public String getEnvironmentVariablesAsString() {
    String configTxt = "ENV found:\n---------------------------------------------------------------\n";
    configTxt += "ar4k.fileKeystore: " + fileKeystore + "\n";
    configTxt += "ar4k.webKeystore: " + webKeystore + "\n";
    configTxt += "ar4k.dnsKeystore: " + dnsKeystore + "\n";
    // configTxt += "ar4k.keystorePassword: " + keystorePassword + "\n";
    configTxt += "ar4k.keystorePassword: xxx\n";
    configTxt += "ar4k.keystoreCaAlias: " + keystoreCaAlias + "\n";
    configTxt += "ar4k.confPath: " + confPath + "\n";
    configTxt += "ar4k.fileConfig: " + fileConfig + "\n";
    configTxt += "ar4k.webConfig: " + webConfig + "\n";
    configTxt += "ar4k.dnsConfig: " + dnsConfig + "\n";
    configTxt += "ar4k.baseConfig: " + baseConfig + "\n";
    // configTxt += "ar4k.otpRegistrationSeed: " + otpRegistrationSeed + "\n";
    configTxt += "ar4k.otpRegistrationSeed: xxx\n";
    configTxt += "ar4k.webRegistrationEndpoint: " + webRegistrationEndpoint + "\n";
    configTxt += "ar4k.dnsRegistrationEndpoint: " + dnsRegistrationEndpoint + "\n";
    configTxt += "ar4k.beaconDiscoveryPort: " + beaconDiscoveryPort + "\n";
    configTxt += "ar4k.beaconDiscoveryFilterString: " + beaconDiscoveryFilterString + "\n";
    configTxt += "ar4k.fileConfigOrder: " + fileConfigOrder + "\n";
    configTxt += "ar4k.webConfigOrder: " + webConfigOrder + "\n";
    configTxt += "ar4k.dnsConfigOrder: " + dnsConfigOrder + "\n";
    configTxt += "ar4k.baseConfigOrder: " + base64ConfigOrder + "\n";
    configTxt += "ar4k.threadSleep: " + threadSleep + "\n";
    configTxt += "ar4k.logoUrl: " + logoUrl + "\n";
    configTxt += "ar4k.consoleOnly: " + consoleOnly
        + "\n---------------------------------------------------------------\n";
    return configTxt;
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    Anima.applicationContext = ac;
  }

  public static ApplicationContext getApplicationContext() throws BeansException {
    return applicationContext;
  }

  public Collection<ServiceComponent> getServices() {
    Set<ServiceComponent> target = new HashSet<ServiceComponent>();
    for (Ar4kComponent bean : components) {
      if (bean instanceof ServiceComponent) {
        target.add((ServiceComponent) bean);
      }
    }
    return target;
  }

  public Set<Ar4kComponent> getComponents() {
    return components;
  }

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if (logger != null) {
      logger.info(" event: " + event.toString());
    }
  }

  public String getLogoUrl() {
    String logo = "/static/img/ar4k.png";
    if (logoUrl != null && logoUrl != "") {
      logo = logoUrl;
    }
    if (runtimeConfig != null && runtimeConfig.logoUrl != null) {
      logo = runtimeConfig.logoUrl;
    }
    return logo;
  }

  public Set<KeystoreConfig> getKeyStores() {
    return keyStores;
  }

  public void addKeyStores(KeystoreConfig keyStore) {
    this.keyStores.add(keyStore);
  }

  public void delKeyStores(KeystoreConfig keyStore) {
    this.keyStores.remove(keyStore);
  }

  public void setTargetConfig(Ar4kConfig config) {
    targetConfig = config;
  }

  public AnimaStates getTargetState() {
    return stateTarget;
  }

  public Ar4kConfig getRuntimeConfig() {
    return runtimeConfig;
  }

  public Object getContextData(String index) {
    if (dataStore != null)
      return dataStore.get(index);
    else
      return null;
  }

  public void setContextData(String index, Object data) {
    if (dataStore != null)
      dataStore.put(index, data);
  }

  public void clearDataStore() {
    if (dataStore != null)
      dataStore.clear();
  }

  public boolean dataStoreExists() {
    return (dataStore != null);
  }

  // coda principale Anima (sottoscritto da Anima con IAnimaGateway
  @Bean
  public MessageChannel mainAnimaChannel() {
    return new PublishSubscribeChannel();
  }
  // TODO:implementare IAnimaGateway

  @Override
  public void setBeanName(String name) {
    beanName = name;
  }

  public Collection<Ar4kUserDetails> getLocalUsers() {
    return localUsers;
  }

  public String loginAgent(String username, String password, String sessionId) {
    UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
    Authentication result = authenticationManager.authenticate(request);
    SecurityContextHolder.getContext().setAuthentication(result);
    if (sessionId == null || sessionId.isEmpty()) {
      if (animaHomunculus.getAllSessions(result, false).isEmpty()) {
        sessionId = UUID.randomUUID().toString();
        animaHomunculus.registerNewSession(sessionId, result);
      } else {
        sessionId = animaHomunculus.getAllSessions(result, false).get(0).getSessionId();
      }
    } else {
      if (animaHomunculus.getSessionInformation(sessionId) == null
          || animaHomunculus.getSessionInformation(sessionId).isExpired()) {
        animaHomunculus.registerNewSession(sessionId, result);
      }
    }
    return sessionId;
  }

  public void terminateSession(String sessionId) {
    animaHomunculus.removeSessionInformation(sessionId);
    logoutFromAgent();
  }

  public void logoutFromAgent() {
    SecurityContextHolder.clearContext();
  }

  public AnimaSession getSession(String sessionId) {
    return (AnimaSession) animaHomunculus.getSessionInformation(sessionId);
  }

  public boolean isSessionValid(String sessionId) {
    return animaHomunculus.getSessionInformation(sessionId) != null;
  }

  public RpcExecutor getRpc(String sessionId) {
    return animaHomunculus.getRpc(sessionId);
  }

  protected String getBeanName() {
    return beanName;
  }

  public String getAgentUniqueName() {
    return agentUniqueName;
  }

  public void setAgentUniqueName(String agentUniqueName) {
    if (agentUniqueName != null && !agentUniqueName.isEmpty())
      this.agentUniqueName = agentUniqueName;
  }

  public DataAddress getDataAddress() {
    return dataAddress;
  }

  public void setDataAddress(DataAddress dataAddress) {
    this.dataAddress = dataAddress;
  }

  @Override
  public String toString() {
    return "Anima [dbDataStorePath=" + dbDataStorePath + ", dbDataStoreName=" + dbDataStoreName + ", agentUniqueName="
        + agentUniqueName + ", fileKeystore=" + fileKeystore + ", webKeystore=" + webKeystore + ", dnsKeystore="
        + dnsKeystore + ", keystorePassword=" + keystorePassword + ", keystoreCaAlias=" + keystoreCaAlias
        + ", confPath=" + confPath + ", fileConfig=" + fileConfig + ", webConfig=" + webConfig + ", dnsConfig="
        + dnsConfig + ", baseConfig=" + baseConfig + ", otpRegistrationSeed=" + otpRegistrationSeed
        + ", webRegistrationEndpoint=" + webRegistrationEndpoint + ", dnsRegistrationEndpoint="
        + dnsRegistrationEndpoint + ", beaconDiscoveryFilterString=" + beaconDiscoveryFilterString
        + ", beaconDiscoveryPort=" + beaconDiscoveryPort + ", fileConfigOrder=" + fileConfigOrder + ", webConfigOrder="
        + webConfigOrder + ", dnsConfigOrder=" + dnsConfigOrder + ", base64ConfigOrder=" + base64ConfigOrder
        + ", threadSleep=" + threadSleep + ", consoleOnly=" + consoleOnly + ", logoUrl=" + logoUrl + ", runtimeConfig="
        + runtimeConfig + ", targetConfig=" + targetConfig + ", bootStrapConfig=" + bootStrapConfig + ", stateTarget="
        + stateTarget + ", statesBefore=" + statesBefore + ", beanName=" + beanName + "]";
  }
}
