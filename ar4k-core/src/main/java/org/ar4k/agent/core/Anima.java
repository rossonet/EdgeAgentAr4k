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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.Ar4kException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.ar4k.agent.tunnels.http.beacon.BeaconClient;
import org.joda.time.Instant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
//@ManagedResource(objectName = "bean:name=anima", description = "Gestore principale agente", log = true, logFile = "anima.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "anima")
@Component("anima")
//@EnableMBeanExport
@Scope("singleton")
@EnableJms
public class Anima
    implements ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware, AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private static transient final String registrationPin = ConfigHelper.createRandomRegistryId();
  private final String agentUniqueName = ConfigHelper.generateNewUniqueName();

  private final String dbDataStorePath = "~/.ar4k/anima_datastore_" + agentUniqueName;
  private final String dbDataStoreName = "datastore";

  private Timer timerScheduler = null;

  /*
   * @Autowired private StateMachineFactory<AnimaStates, AnimaEvents>
   * factoryStateMachine;
   */
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
  @Value("${ar4k.keystoreMainAlias}")
  private String keystoreMainAlias;
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
  @Value("${ar4k.beaconCaChainPem}")
  private String beaconCaChainPem;
  @Value("${ar4k.adminPassword}")
  private String adminPassword;
  @Value("${ar4k.webRegistrationEndpoint}")
  private String webRegistrationEndpoint;
  @Value("${ar4k.dnsRegistrationEndpoint}")
  private String dnsRegistrationEndpoint;
  @Value("${ar4k.beaconDiscoveryFilterString}")
  private String beaconDiscoveryFilterString;
  @Value("${ar4k.beaconDiscoveryPort}")
  private String beaconDiscoveryPort;
  @Value("${ar4k.fileConfigOrder}")
  private String fileConfigOrder;
  @Value("${ar4k.webConfigOrder}")
  private String webConfigOrder;
  @Value("${ar4k.dnsConfigOrder}")
  private String dnsConfigOrder;
  @Value("${ar4k.baseConfigOrder}")
  private String base64ConfigOrder;
  @Value("${ar4k.threadSleep}")
  private String threadSleep;
  @Value("${ar4k.consoleOnly}")
  private String consoleOnly;
  @Value("${ar4k.logoUrl}")
  private String logoUrl;

  // gestione configurazioni
  // attuale configurazione in runtime
  private Ar4kConfig runtimeConfig = null;
  // configurazione obbiettivo durante le transizioni
  private Ar4kConfig targetConfig = null;
  // configurazione iniziale di bootStrap derivata dalle variabili Spring
  private Ar4kConfig bootstrapConfig = null;
  private AnimaStates stateTarget = AnimaStates.RUNNING;
  private Map<Instant, AnimaStates> statesBefore = new HashMap<>();

  // TODO implementare l'esecuzione dei pre e post script

  // assegnato da Spring tramite setter al boot
  private static ApplicationContext applicationContext;

  private Set<Ar4kComponent> components = new HashSet<>();

  private Map<String, Object> dataStore = null;

  private Collection<Ar4kUserDetails> localUsers = new HashSet<>();

  private transient RecordManager recMan = null;

  private String beanName = "anima";

  private BeaconClient beaconClient = null;

  private DataAddress dataAddress = new DataAddress(this);

  private KeystoreConfig myIdentityKeystore = null;
  private String myAliasCertInKeystore = "agent";

  private transient boolean onApplicationEventFlag = false;

  private transient boolean afterSpringInitFlag = false;

  public static enum AnimaStates {
    INIT, STAMINAL, CONFIGURED, RUNNING, KILLED, FAULTED, STASIS
  }

  // tipi di router interno supportato per gestire lo scambio dei messagi tra gli
  // agenti, per la definizione della policy security sul routing public
  public static enum AnimaRouterType {
    NONE, PRODUCTION, DEVELOP, ROAD
  }

  public static enum AnimaEvents {
    BOOTSTRAP, SETCONF, START, STOP, PAUSE, HIBERNATION, EXCEPTION, RESTART
  }

  @ManagedOperation
  public void sendEvent(AnimaEvents event) {
    animaStateMachine.sendEvent(event);
  }

  @ManagedOperation
  public AnimaStates getState() {
    return (animaStateMachine != null && animaStateMachine.getState() != null) ? animaStateMachine.getState().getId()
        : null;
  }

  public boolean isRunning() {
    return AnimaStates.RUNNING.equals(getState());
  }

  @Override
  public void close() {
    if (animaStateMachine != null) {
      animaStateMachine.sendEvent(AnimaEvents.STOP);
      animaStateMachine.stop();
      animaStateMachine = null;
    }
    if (timerScheduler != null) {
      timerScheduler.cancel();
      timerScheduler = null;
    }
    if (recMan != null) {
      try {
        recMan.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      recMan = null;
    }
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
    try {
      for (Ar4kComponent targetService : components) {
        targetService.kill();
      }
      components.clear();
      if (timerScheduler != null) {
        timerScheduler.cancel();
        timerScheduler.purge();
        timerScheduler = null;
      }
    } catch (Exception aa) {
      logger.warn("error during finalize phase");
      logger.logException(aa);
    }
  }

  @SuppressWarnings("unchecked")
  private void setInitialAuth() {
    if (adminPassword != null && !adminPassword.isEmpty()) {
      // logger.warn("create admin user with config password");
      Ar4kUserDetails admin = new Ar4kUserDetails();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode(adminPassword));
      GrantedAuthority grantedAuthorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
      GrantedAuthority grantedAuthorityUser = new SimpleGrantedAuthority("ROLE_USER");
      ((Set<GrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityAdmin);
      ((Set<GrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityUser);
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
    if (myIdentityKeystore == null) {
      KeystoreConfig ks = new KeystoreConfig();
      boolean foundFile = false;
      boolean foundWeb = false;
      ks.keyStoreAlias = keystoreMainAlias;
      ks.keystorePassword = keystorePassword;
      ks.filePathPre = fileKeystore != null ? fileKeystore.replace("~", System.getProperty("user.home")) : null;
      if (fileKeystore != null && !fileKeystore.isEmpty()) {
        if (new File(ks.filePathPre).exists()) {
          foundFile = true;
          logger.info("use keystore " + ks.toString());
        } else {
          logger.info("keystore file name not found, using parameters " + ks.toString());
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
          try {
            String hostPart = dnsKeystore.split("\\.")[0];
            String domainPart = dnsKeystore.replaceAll("^" + hostPart, "");
            System.out.println("Using H:" + hostPart + " D:" + domainPart);
            String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart);
            if (payloadString != null && payloadString.length() > 0) {
              FileUtils.writeByteArrayToFile(new File(ks.filePathPre), Base64.getDecoder().decode(payloadString));
            }
          } catch (Exception e) {
            logger.warn("dnsKeystore -> " + dnsKeystore);
            logger.logException(e);
          }
        }
      }
      if (keystorePassword != null && !keystorePassword.isEmpty()) {
        ks.keystorePassword = keystorePassword;
      }
      if (keystoreMainAlias != null && !keystoreMainAlias.isEmpty()) {
        ks.keyStoreAlias = keystoreMainAlias;
      }
      // se alla fine non è stato trovato un keystore, lo creo
      if (!new File(fileKeystore.replace("~", System.getProperty("user.home"))).exists()) {
        logger.warn("new keystore: " + ks.toString());
        ks.createSelfSignedCert(agentUniqueName + "-master", ConfigHelper.organization, ConfigHelper.unit,
            ConfigHelper.locality, ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri, ConfigHelper.dns,
            ConfigHelper.ip, ks.keyStoreAlias, true);
        logger.debug("keystore created");
      }
      // addKeyStores(ks);
      setMyIdentityKeystore(ks);
      setMyAliasCertInKeystore(ks.keyStoreAlias);
      logger.info("Certificate for anima created: "
          + ks.getClientCertificate(ks.keyStoreAlias).getSubjectX500Principal() + " - alias " + ks.keyStoreAlias);
    } else {
      logger.info("Use keystore " + myIdentityKeystore.toString());
    }
  }

  @PostConstruct
  public void afterSpringInit() throws Exception {
    afterSpringInitFlag = true;
    checkDualStart();
  }

  // workaround Spring State Machine
  // @OnStateMachineStart
  public synchronized void initAgent() {
    if (!Boolean.valueOf(consoleOnly)) {
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
    new File(confPath.replace("~", System.getProperty("user.home"))).mkdirs();
    fileConfig = fileConfig.replace("~", System.getProperty("user.home"));
    try {
      if (dataStore == null) {
        recMan = RecordManagerFactory
            .createRecordManager(dbDataStorePath.replace("~", System.getProperty("user.home")));
        dataStore = recMan.treeMap(dbDataStoreName);
        logger.info("datastore on Anima started");
      }
    } catch (IOException e) {
      logger.logException(e);
    }
    bootstrapConfig = resolveBootstrapConfig();
    popolateAddressSpace();
    setInitialAuth();
    setMasterKeystore();
    checkBeaconClient();
    if (runtimeConfig == null && targetConfig == null && bootstrapConfig != null) {
      targetConfig = bootstrapConfig;
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
    dataAddress.firstStart();
  }

  private void checkBeaconClient() {
    if ((webRegistrationEndpoint != null && !webRegistrationEndpoint.isEmpty())
        || Integer.valueOf(beaconDiscoveryPort) != 0) {
      try {
        if (webRegistrationEndpoint == null || webRegistrationEndpoint.isEmpty()) {
          webRegistrationEndpoint = "http://localhost:0";
        }
        logger.info("TRY CONECTION TO BEACON AT " + webRegistrationEndpoint);
        connectToBeaconService(webRegistrationEndpoint, beaconCaChainPem, Integer.valueOf(beaconDiscoveryPort),
            beaconDiscoveryFilterString);
      } catch (Exception e) {
        logger.warn("Beacon connection not ok: " + e.getMessage());
        // logger.logException(e);
      }
    }
  }

  public BeaconClient connectToBeaconService(String urlBeacon, String beaconCaChainPem, int discoveryPort,
      String discoveryFilter) {
    URL urlTarget = null;
    if (beaconClient != null) {
      logger.info("This agent is connected to another Beacon service");
    }
    try {
      if (urlBeacon != null)
        urlTarget = new URL(urlBeacon);
      String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + urlBeacon;
      animaHomunculus.registerNewSession(sessionId, sessionId);
      RpcConversation rpc = animaHomunculus.getRpc(sessionId);
      // rpc.setShell(shell);
      beaconClient = new BeaconClient.Builder().setUniqueName(getAgentUniqueName()).setAnima(this)
          .setBeaconCaChainPem(beaconCaChainPem).setDiscoveryFilter(discoveryFilter).setDiscoveryPort(discoveryPort)
          .setPort(urlTarget.getPort()).setRpcConversation(rpc).setHost(urlTarget.getHost()).build();
      if (beaconClient != null && beaconClient.getStateConnection().equals(ConnectivityState.READY)) {
        logger.info("found Beacon endpoint: " + urlBeacon);
        if (!getAgentUniqueName().equals(beaconClient.getAgentUniqueName())) {
          logger.info("the unique name is changed in " + getAgentUniqueName());
        }
      } else {
        logger.info("the Beacon endpoint " + urlBeacon + " return " + beaconClient.getStateConnection());
      }
    } catch (IOException e) {
      logger.info("the url " + urlBeacon + " is malformed or unreachable [" + e.getCause() + "]");
    } catch (UnrecoverableKeyException e) {
      logger.warn(e.getMessage());
    }
    return beaconClient;
  }

  // trova la configurazione appropriata per il bootstrap in funzione dei
  // parametri di configurazione
  private Ar4kConfig resolveBootstrapConfig() {
    Ar4kConfig targetConfig = null;
    int maxConfig = 0;
    try {
      maxConfig = Integer.max(maxConfig, webConfigOrder != null ? Integer.valueOf(webConfigOrder) : 6);
    } catch (Exception a) {
      logger.warn("webConfigOrder -> " + webConfigOrder);
      webConfigOrder = "6";
    }
    try {
      maxConfig = Integer.max(maxConfig, dnsConfigOrder != null ? Integer.valueOf(dnsConfigOrder) : 6);
    } catch (Exception a) {
      logger.warn("dnsConfigOrder -> " + dnsConfigOrder);
      dnsConfigOrder = "6";
    }
    try {
      maxConfig = Integer.max(maxConfig, base64ConfigOrder != null ? Integer.valueOf(base64ConfigOrder) : 6);
    } catch (Exception a) {
      logger.warn("base64ConfigOrder -> " + base64ConfigOrder);
      base64ConfigOrder = "6";
    }
    try {
      maxConfig = Integer.max(maxConfig, fileConfigOrder != null ? Integer.valueOf(fileConfigOrder) : 6);
    } catch (Exception a) {
      logger.warn("fileConfigOrder -> " + fileConfigOrder);
      fileConfigOrder = "6";
    }
    for (int liv = 0; liv <= maxConfig; liv++) {
      if (liv == Integer.valueOf(webConfigOrder) && targetConfig == null && webConfig != null && !webConfig.isEmpty()) {
        try {
          logger.info("try webConfigDownload");
          targetConfig = webConfigDownload(webConfig);
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == Integer.valueOf(dnsConfigOrder) && targetConfig == null && dnsConfig != null && !dnsConfig.isEmpty()) {
        try {
          logger.info("try dnsConfigDownload");
          targetConfig = dnsConfigDownload(dnsConfig);
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == Integer.valueOf(base64ConfigOrder) && targetConfig == null && baseConfig != null
          && !baseConfig.isEmpty()) {
        try {
          logger.info("try fromBase64");
          targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(baseConfig);
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == Integer.valueOf(fileConfigOrder) && targetConfig == null && fileConfig != null
          && !fileConfig.isEmpty()) {
        try {
          logger.info("try fileConfig");
          targetConfig = loadConfigFromFile(fileConfig.replace("~", System.getProperty("user.home")));
        } catch (Exception e) {
          logger.logException(e);
        }
      }
    }
    return targetConfig;
  }

  private Ar4kConfig dnsConfigDownload(String dnsTarget) {
    String hostPart = dnsTarget.split("\\.")[0];
    String domainPart = dnsTarget.replaceAll("^" + hostPart, "");
    System.out.println("Using H:" + hostPart + " D:" + domainPart);
    try {
      String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart);
      try {
        return (Ar4kConfig) ((payloadString != null && payloadString.length() > 0)
            ? ConfigHelper.fromBase64(payloadString.toString())
            : null);
      } catch (ClassNotFoundException | IOException e) {
        logger.logException(e);
        return null;
      }
    } catch (UnknownHostException | TextParseException e) {
      logger.logException(e);
      return null;
    }
  }

  public Ar4kConfig loadConfigFromFile(String pathConfig) {
    Ar4kConfig resultConfig = null;
    try {
      String config = "";
      FileReader fileReader = new FileReader(pathConfig.replace("~", System.getProperty("user.home")));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        config = config + line;
      }
      bufferedReader.close();
      resultConfig = (Ar4kConfig) ConfigHelper.fromBase64(config);
      return resultConfig;
    } catch (IOException | ClassNotFoundException e) {
      if (logger != null)
        logger.logException(e);
      return null;
    }
  }

  public Ar4kConfig webConfigDownload(String webConfigTarget) {
    String temporaryFile = UUID.randomUUID().toString() + ".ar4k.conf";
    try (BufferedInputStream in = new BufferedInputStream(new URL(webConfigTarget).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
      byte dataBuffer[] = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
      return loadConfigFromFile(temporaryFile);
    } catch (ConnectException c) {
      logger.info(webConfigTarget + " is unreachable");
      return null;
    } catch (Exception e) {
      if (logger != null)
        logger.logException(e);
      return null;
    }

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
    if (stateTarget != null && stateTarget.equals(AnimaStates.RUNNING)) {
      timerScheduler = new Timer();
      animaStateMachine.sendEvent(AnimaEvents.START);
    } else {
      logger.warn("stateTarget is null in runtime config");
    }
  }

  Comparator<PotConfig> comparatorOrderPots = new Comparator<PotConfig>() {
    @Override
    public int compare(PotConfig o1, PotConfig o2) {
      return Integer.compare(o1.getPriority(), o2.getPriority());
    }
  };

  // workaround Spring State Machine
  // @OnStateChanged(target = "RUNNING")
  // avvia i servizi
  public synchronized void runServices() {
    List<PotConfig> sortedList = new ArrayList<>(runtimeConfig.pots);
    Collections.sort(sortedList, comparatorOrderPots);
    for (PotConfig confServizio : sortedList) {
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
  // inizializza pots semplici e servizi
  public synchronized void runPots() {
    List<PotConfig> sortedList = new ArrayList<>(runtimeConfig.pots);
    Collections.sort(sortedList, comparatorOrderPots);
    for (PotConfig confVaso : sortedList) {
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
    targetService.setConfiguration(potConfig);
    components.add(targetService);
    targetService.init();
  }

  private void runSeedService(ServiceConfig confServizio)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = confServizio.getClass().getMethod("instantiate");
    ServiceComponent targetService;
    targetService = (ServiceComponent) method.invoke(confServizio);
    targetService.setConfiguration(confServizio);
    targetService.setAnima(this);
    components.add(targetService);
    // targetService.init();
    targetService.start();
  }

  // adminPassword è escluso
  public Map<String, String> getEnvironmentVariables() {
    Map<String, String> ritorno = new HashMap<>();
    ritorno.put("ar4k.fileKeystore", fileKeystore);
    ritorno.put("ar4k.webKeystore", webKeystore);
    ritorno.put("ar4k.dnsKeystore", dnsKeystore);
    ritorno.put("ar4k.keystorePassword", keystorePassword);
    ritorno.put("ar4k.keystoreMainAlias", keystoreMainAlias);
    ritorno.put("ar4k.confPath", confPath);
    ritorno.put("ar4k.fileConfig", fileConfig);
    ritorno.put("ar4k.webConfig", webConfig);
    ritorno.put("ar4k.dnsConfig", dnsConfig);
    ritorno.put("ar4k.baseConfig", baseConfig);
    ritorno.put("ar4k.beaconCaChainPem", beaconCaChainPem);
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
    configTxt += "ar4k.keystoreMainAlias: " + keystoreMainAlias + "\n";
    configTxt += "ar4k.confPath: " + confPath + "\n";
    configTxt += "ar4k.fileConfig: " + fileConfig + "\n";
    configTxt += "ar4k.webConfig: " + webConfig + "\n";
    configTxt += "ar4k.dnsConfig: " + dnsConfig + "\n";
    configTxt += "ar4k.baseConfig: " + baseConfig + "\n";
    configTxt += "ar4k.beaconCaChainPem: " + beaconCaChainPem + "\n";
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
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Anima.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() throws BeansException {
    return applicationContext;
  }

  public Collection<ServiceComponent> getServices() {
    Set<ServiceComponent> target = new HashSet<>();
    for (Ar4kComponent bean : components) {
      if (bean instanceof ServiceComponent) {
        target.add((ServiceComponent) bean);
      }
    }
    return target;
  }

  public Collection<ServiceComponent> getPotsOnly() {
    Set<ServiceComponent> target = new HashSet<>();
    for (Ar4kComponent bean : components) {
      if (!(bean instanceof ServiceComponent)) {
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
    if (onApplicationEventFlag && afterSpringInitFlag) {
      logger.info("STARTING AGENT...");
      // animaStateMachine = factoryStateMachine.getStateMachine();
      animaStateMachine.start();
    }
  }

  public String getLogoUrl() {
    String logo = "/static/img/ar4k.png";
    if (logoUrl != null && !logoUrl.isEmpty()) {
      logo = logoUrl;
    }
    if (runtimeConfig != null && runtimeConfig.logoUrl != null) {
      logo = runtimeConfig.logoUrl;
    }
    return logo;
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

  public Map<String, Object> getDataStore() {
    return dataStore;
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

  public DataAddress getDataAddress() {
    return dataAddress;
  }

  public void setDataAddress(DataAddress dataAddress) {
    this.dataAddress = dataAddress;
  }

  public String getDbDataStoreName() {
    return dbDataStoreName;
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

  public void prepareAgentStasis() {
    logger.warn("PUTTING AGENT IN STASIS STATE...");
    finalizeAgent();
  }

  public void prepareRestart() {
    logger.warn("RESTARTING AGENT...");
    finalizeAgent();
  }

  public void startCheckingNextConfig() {
    if (runtimeConfig != null && (runtimeConfig.nextConfigDns != null || runtimeConfig.nextConfigWeb != null)
        && runtimeConfig.configCheckPeriod != null && runtimeConfig.configCheckPeriod > 0) {
      if (runtimeConfig.nextConfigDns != null) {
        timerScheduler.schedule(checkDnsConfigUpdate(runtimeConfig.nextConfigDns), runtimeConfig.configCheckPeriod,
            runtimeConfig.configCheckPeriod);
        logger.warn("scheduled periodically configuration checking on dns " + runtimeConfig.nextConfigDns
            + " with rate time of " + runtimeConfig.configCheckPeriod + " ms");
      }
      if (runtimeConfig.nextConfigWeb != null) {
        timerScheduler.schedule(checkWebConfigUpdate(runtimeConfig.nextConfigWeb), runtimeConfig.configCheckPeriod,
            runtimeConfig.configCheckPeriod);
        logger.warn("scheduled periodically configuration checking on url " + runtimeConfig.nextConfigWeb
            + " with rate time of " + runtimeConfig.configCheckPeriod + " ms");
      }
    }
  }

  private TimerTask checkWebConfigUpdate(String nextConfigWeb) {
    return new TimerTask() {
      @Override
      public void run() {
        Ar4kConfig newTargetConfig = webConfigDownload(nextConfigWeb);
        checkPolledConfig(newTargetConfig);
      }
    };
  }

  private TimerTask checkDnsConfigUpdate(String nextConfigDns) {
    return new TimerTask() {
      @Override
      public void run() {
        Ar4kConfig newTargetConfig = dnsConfigDownload(nextConfigDns);
        checkPolledConfig(newTargetConfig);
      }
    };
  }

  private final void checkPolledConfig(Ar4kConfig newTargetConfig) {
    if (newTargetConfig != null && newTargetConfig.isMoreUpToDateThan(getRuntimeConfig())) {
      logger.warn("Found new config " + newTargetConfig.toString());
      runtimeConfig = newTargetConfig;
      sendEvent(AnimaEvents.RESTART);
    }
  }

  @Override
  public String toString() {
    return "Anima [dbDataStorePath=" + dbDataStorePath + ", dbDataStoreName=" + dbDataStoreName + ", agentUniqueName="
        + agentUniqueName + ", animaHomunculus=" + animaHomunculus + ", fileKeystore=" + fileKeystore + ", webKeystore="
        + webKeystore + ", dnsKeystore=" + dnsKeystore + ", keystorePassword=" + keystorePassword
        + ", keystoreMainAlias=" + keystoreMainAlias + ", confPath=" + confPath + ", fileConfig=" + fileConfig
        + ", webConfig=" + webConfig + ", dnsConfig=" + dnsConfig + ", baseConfig=" + baseConfig + ", beaconCaChainPem="
        + beaconCaChainPem + ", adminPassword=" + adminPassword + ", webRegistrationEndpoint=" + webRegistrationEndpoint
        + ", dnsRegistrationEndpoint=" + dnsRegistrationEndpoint + ", beaconDiscoveryFilterString="
        + beaconDiscoveryFilterString + ", beaconDiscoveryPort=" + beaconDiscoveryPort + ", fileConfigOrder="
        + fileConfigOrder + ", webConfigOrder=" + webConfigOrder + ", dnsConfigOrder=" + dnsConfigOrder
        + ", base64ConfigOrder=" + base64ConfigOrder + ", threadSleep=" + threadSleep + ", consoleOnly=" + consoleOnly
        + ", logoUrl=" + logoUrl + ", runtimeConfig=" + runtimeConfig + ", targetConfig=" + targetConfig
        + ", bootstrapConfig=" + bootstrapConfig + ", stateTarget=" + stateTarget + ", statesBefore=" + statesBefore
        + ", components=" + components + ", dataStore=" + dataStore + ", localUsers=" + localUsers + ", beanName="
        + beanName + ", myIdentityKeystore=" + myIdentityKeystore + ", myAliasCertInKeystore=" + myAliasCertInKeystore
        + "]";
  }

  public StateMachine<AnimaStates, AnimaEvents> getAnimaStateMachine() {
    return animaStateMachine;
  }

  public String getFileKeystore() {
    return fileKeystore;
  }

  public String getDbDataStorePath() {
    return dbDataStorePath;
  }

}
