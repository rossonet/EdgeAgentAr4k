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
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.Channel;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.exception.Ar4kException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.ar4k.agent.spring.HealthMessage;
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
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.Shell;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

  private static transient String registrationPin = ConfigHelper.createRandomRegistryId();
  private String agentUniqueName = ConfigHelper.generateNewUniqueName();

  @Autowired
  private Shell shell;

  @Autowired
  private StateMachine<AnimaStates, AnimaEvents> animaStateMachine;

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

  private KeystoreConfig myIdentityKeystore = null;
  private String myAliasCertInKeystore = "agent";

  private transient boolean onApplicationEventFlag = false;

  private transient boolean afterSpringInitFlag = false;

  public static enum AnimaStates {
    INIT, STAMINAL, CONFIGURED, RUNNING, KILLED, FAULTED, STASIS
  }

  // tipi di router interno supportato per gestire lo scambio dei messagi tra gli
  // agenti, per la definizione della secutrity sul routing
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

  private long delay = 35000L;
  private long period = 15000L;

  private Timer timer = new Timer("TimerHealth");

  // task per health
  private TimerTask repeatedTask = new TimerTask() {

    private Anima anima = null;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void run() {
      try {
        sendEvent(HardwareHelper.getSystemInfo().getHealthIndicator());
      } catch (Exception e) {
        logger.logException(e);
      }
    }

    private void sendEvent(Map<String, Object> healthMessage) {
      if (anima == null && Anima.getApplicationContext() != null
          && Anima.getApplicationContext().getBean(Anima.class) != null
          && ((Anima) Anima.getApplicationContext().getBean(Anima.class)).getDataAddress() != null) {
        anima = (Anima) Anima.getApplicationContext().getBean(Anima.class);
      }
      HealthMessage<String> messageObject = new HealthMessage<>();
      messageObject.setPayload(gson.toJson(healthMessage));
      anima.getDataAddress().getChannel("health").send(messageObject);
    }
  };
  // task per health

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
      // logger.warn("create admin user with config password");
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

  private void setMasterKeystore() {
    KeystoreConfig ks = new KeystoreConfig();
    boolean foundFile = false;
    boolean foundWeb = false;
    if (fileKeystore != null && !fileKeystore.isEmpty()) {
      ks.filePathPre = fileKeystore;
      if (new File(ks.filePathPre).exists()) {
        foundFile = true;
        logger.info("use keystore " + ks.toString());
      } else {
        logger.info("keystore file not found, using parameters " + ks.toString());
      }
    } else {
      logger.info("value of fileKeystore is null, use: " + ks.toString());
    }
    if (!foundFile) {
      if (webKeystore != null && !webKeystore.isEmpty()) {
        try {
          logger.info("try keystore from url: " + webKeystore);
          HardwareHelper.downloadFileFromUrl(ks.filePathPre, webKeystore);
        } catch (Exception e) {
          foundWeb = false;
          // logger.logException(e);
        }
        if (new File(ks.filePathPre).exists()) {
          foundWeb = true;
        }
      }
      if (!foundWeb && dnsKeystore != null && !dnsKeystore.isEmpty()) {
        logger.info("try keystore from dns: " + dnsKeystore);
        // TODO: implementare scaricamento keystore da DNS
        // logger.warn("use keystore from DNS, domain: " + webKeystore);
      }
    }
    if (keystorePassword != null && !keystorePassword.isEmpty()) {
      ks.keystorePassword = keystorePassword;
    }
    if (keystoreCaAlias != null && !keystoreCaAlias.isEmpty()) {
      ks.keyStoreAlias = keystoreCaAlias;
    }
    if (!new File(fileKeystore).exists()) {
      logger.warn("new keystore: " + ks.toString());
      ks.filePathPre = fileKeystore;
      ks.keyStoreAlias = keystoreCaAlias;
      ks.keystorePassword = keystorePassword;
      ks.create(agentUniqueName, ConfigHelper.organization, ConfigHelper.unit, ConfigHelper.locality,
          ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri, ConfigHelper.dns, ConfigHelper.ip);
      logger.debug("keystore created");
    }
    addKeyStores(ks);
    setMyIdentityKeystore(ks);
    setMyAliasCertInKeystore(ks.keyStoreAlias);
    logger.info("Certificate for anima created: "
        + getMyIdentityKeystore().getClientCertificate(getMyAliasCertInKeystore()).getSubjectX500Principal().toString()
        + " - alias " + getMyAliasCertInKeystore());
  }

  @PostConstruct
  public void afterSpringInit() throws Exception {
    afterSpringInitFlag = true;
    // animaStateMachine.start();
    checkDualStart();
  }

  // workaround Spring State Machine
  // @OnStateMachineStart
  public synchronized void initAgent() {
    // System.out.println("First state " + consoleOnly);
    if (!consoleOnly) {
      animaStateMachine.sendEvent(AnimaEvents.BOOTSTRAP);
    } else {
      logger.warn("console only true, run just the command line");
    }
    System.out.println("__________________________________________________");
    System.out.println("       REGISTRATION CODE: " + registrationPin);
    System.out.println("__________________________________________________\n");
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
      // logger.warn(e.getMessage());
      logger.logException(e);
    }
    bootStrapConfig = resolveBootstrapConfig();
    popolateAddressSpace();
    setInitialAuth();
    setMasterKeystore();
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

  private void popolateAddressSpace() {
    String scope = "ar4k-system";
    Channel systemChannel = new INoDataChannel();
    systemChannel.setNodeId("system");
    systemChannel.setDescription("local JVM system");
    Channel loggerChannel = new IPublishSubscribeChannel();
    loggerChannel.setNodeId("logger");
    loggerChannel.setDescription("logger queue");
    loggerChannel.setFatherOfScope(scope, systemChannel);
    dataAddress.addDataChannel(loggerChannel);
    Channel healthChannel = new IPublishSubscribeChannel();
    healthChannel.setNodeId("health");
    healthChannel.setDescription("local machine hardware and software stats");
    healthChannel.setFatherOfScope(scope, systemChannel);
    dataAddress.addDataChannel(healthChannel);
    Channel cmdChannel = new IQueueChannel();
    cmdChannel.setNodeId("command");
    cmdChannel.setDescription("RPC interface");
    cmdChannel.setFatherOfScope(scope, systemChannel);
    dataAddress.addDataChannel(cmdChannel);
    timer.scheduleAtFixedRate(repeatedTask, delay, period);
  }

  private void checkBeaconClient() {
    if (webRegistrationEndpoint != null && !webRegistrationEndpoint.isEmpty()) {
      try {
        connectToBeaconService(webRegistrationEndpoint, beaconDiscoveryPort, beaconDiscoveryFilterString);
      } catch (Exception e) {
        // logger.warn("Beacon connection not ok: " + e.getMessage());
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
      beaconClient = new BeaconClient(this, rpc, urlTarget.getHost(), urlTarget.getPort(), discoveryPort,
          discoveryFilter, getAgentUniqueName());
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
    Ar4kConfig targetConfig = null;
    int maxConfig = 0;
    maxConfig = Integer.max(maxConfig, webConfigOrder);
    maxConfig = Integer.max(maxConfig, dnsConfigOrder);
    maxConfig = Integer.max(maxConfig, base64ConfigOrder);
    maxConfig = Integer.max(maxConfig, fileConfigOrder);
    for (int liv = 0; liv <= maxConfig; liv++) {
      if (liv == webConfigOrder && targetConfig == null && webConfig != null && !webConfig.isEmpty()) {
        try {
          logger.info("try webConfigDownload");
          targetConfig = webConfigDownload();
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == dnsConfigOrder && targetConfig == null && dnsConfig != null && !dnsConfig.isEmpty()) {
        try {
          logger.info("try dnsConfigOrder");
          targetConfig = dnsConfigDownload();
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == base64ConfigOrder && targetConfig == null && baseConfig != null && !baseConfig.isEmpty()) {
        try {
          logger.info("try fromBase64");
          targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(baseConfig);
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == fileConfigOrder && targetConfig == null && fileConfig != null && !fileConfig.isEmpty()) {
        try {
          logger.info("try fileConfig");
          targetConfig = loadConfigFromFile(fileConfig);
        } catch (Exception e) {
          logger.logException(e);
        }
      }
    }
    return targetConfig;
  }

  private Ar4kConfig dnsConfigDownload() {
    StringBuilder resultString = new StringBuilder();
    String hostPart = dnsConfig.split("\\.")[0];
    String domainPart = dnsConfig.replaceAll("^" + hostPart, "");
    System.out.println("Using H:" + hostPart + " D:" + domainPart);
    Set<String> errors = new HashSet<>();
    try {
      Lookup l = new Lookup(hostPart + "-max" + domainPart, Type.TXT, DClass.IN);
      l.setResolver(new SimpleResolver());
      l.run();
      if (l.getResult() == Lookup.SUCCESSFUL) {
        int chunkSize = Integer.valueOf(l.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
        if (chunkSize > 0) {
          for (int c = 0; c < chunkSize; c++) {
            Lookup cl = new Lookup(hostPart + "-" + String.valueOf(c) + domainPart, Type.TXT, DClass.IN);
            cl.setResolver(new SimpleResolver());
            cl.run();
            if (cl.getResult() == Lookup.SUCCESSFUL) {
              resultString.append(cl.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
            } else {
              errors.add(
                  "error in chunk " + hostPart + "-" + String.valueOf(c) + domainPart + " -> " + cl.getErrorString());
            }
          }
        } else {
          errors.add("error, size of data is " + l.getAnswers()[0].rdataToString());
        }
      } else {
        errors.add("no " + hostPart + "-max" + domainPart + " record found -> " + l.getErrorString());
      }
      if (!errors.isEmpty()) {
        logger.error(errors.toString());
      }
    } catch (UnknownHostException | TextParseException e) {
      logger.logException(e);
    }
    try {
      return (Ar4kConfig) ((errors.isEmpty() && resultString.length() > 0)
          ? ConfigHelper.fromBase64(resultString.toString())
          : null);
    } catch (ClassNotFoundException | IOException e) {
      logger.logException(e);
      return null;
    }
  }

  public Ar4kConfig loadConfigFromFile(String pathConfig) {
    Ar4kConfig resultConfig = null;
    try {
      String config = "";
      FileReader fileReader = new FileReader(pathConfig.replaceFirst("^~", System.getProperty("user.home")));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        config = config + line;
      }
      bufferedReader.close();
      resultConfig = (Ar4kConfig) ConfigHelper.fromBase64(config);
    } catch (IOException | ClassNotFoundException e) {
      if (logger != null)
        logger.logException(e);
    }
    return resultConfig;
  }

  public Ar4kConfig webConfigDownload() {
    String temporaryFile = UUID.randomUUID().toString() + ".ar4k.conf";
    try (BufferedInputStream in = new BufferedInputStream(new URL(webConfig).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
      byte dataBuffer[] = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
    } catch (IOException e) {
      if (logger != null)
        logger.logException(e);
    }
    return loadConfigFromFile(temporaryFile);
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
    logger.info(" event: " + event.toString());
    if (event instanceof ContextRefreshedEvent) {
      // avvio a contesto spring caricato
      onApplicationEventFlag = true;
      checkDualStart();
    }
  }

  private synchronized void checkDualStart() {
    logger.info("STARTING AGENT...");
    if (onApplicationEventFlag && afterSpringInitFlag) {
      animaStateMachine.start();
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

  public KeystoreConfig getMyIdentityKeystore() {
    return myIdentityKeystore;
  }

  public void setMyIdentityKeystore(KeystoreConfig myIdentityKeystore) {
    this.myIdentityKeystore = myIdentityKeystore;
  }

  public void setMyAliasCertInKeystore(String myAliasCertInKeystore) {
    this.myAliasCertInKeystore = myAliasCertInKeystore;
  }

  public String getMyAliasCertInKeystore() {
    return myAliasCertInKeystore;
  }

  public static String getRegistrationPin() {
    return registrationPin;
  }

}
