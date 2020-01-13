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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.crypto.NoSuchPaddingException;

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
import org.ar4k.agent.spring.autoconfig.Ar4kStarterProperties;
import org.ar4k.agent.tunnels.http.beacon.BeaconClient;
import org.bouncycastle.cms.CMSException;
import org.joda.time.Instant;
import org.springframework.beans.BeansException;
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
//@ManagedResource(objectName = "bean:name=anima", description = "Gestore principale agente", log = true, logFile = "anima.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "anima")
@Component("anima")
//@EnableMBeanExport
@Scope("singleton")
@EnableJms
@EnableConfigurationProperties(Ar4kStarterProperties.class)
public class Anima
    implements ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware, AutoCloseable {

  private static final String TILDE = "~";

  public static final String USER_HOME = System.getProperty("user.home");

  public static final String DEFAULT_KS_PATH = "default-new.ks";

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private static transient final String registrationPin = ConfigHelper.createRandomRegistryId();
  // assegnato da Spring tramite setter al boot
  private static ApplicationContext applicationContext;

  private final String agentUniqueName = ConfigHelper.generateNewUniqueName();

  private final String dbDataStorePath = USER_HOME + "/.ar4k/anima_datastore_" + agentUniqueName;
  private final String dbDataStoreName = "datastore";

  private Timer timerScheduler = null;

  @Autowired
  private StateMachine<AnimaStates, AnimaEvents> animaStateMachine;

  @Autowired
  private AnimaHomunculus animaHomunculus;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private Ar4kStarterProperties starterProperties;

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
        : AnimaStates.INIT;
  }

  public boolean isRunning() {
    return AnimaStates.RUNNING.equals(getState());
  }

  @Override
  public void close() {
    finalizeAgent();
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
    if (animaHomunculus != null) {
      try {
        animaHomunculus.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
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
    if (starterProperties.getAdminPassword() != null && !starterProperties.getAdminPassword().isEmpty()) {
      // logger.warn("create admin user with config password");
      Ar4kUserDetails admin = new Ar4kUserDetails();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode(starterProperties.getAdminPassword()));
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
        logger.warn("created user " + admin.getUsername());
      }
    }
  }

  private void setMasterKeystore() {
    if (myIdentityKeystore == null) {
      KeystoreConfig ks = new KeystoreConfig();
      boolean foundFile = false;
      boolean foundWeb = false;
      ks.keyStoreAlias = starterProperties.getKeystoreMainAlias();
      ks.keystorePassword = starterProperties.getKeystorePassword();
      ks.filePathPre = starterProperties.getFileKeystore() != null
          ? starterProperties.getFileKeystore().replace(TILDE, USER_HOME)
          : DEFAULT_KS_PATH;
      if (starterProperties.getFileKeystore() != null && !starterProperties.getFileKeystore().isEmpty()) {
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
        if (starterProperties.getWebKeystore() != null && !starterProperties.getWebKeystore().isEmpty()) {
          try {
            logger.info("try keystore from url: " + starterProperties.getWebKeystore());
            HardwareHelper.downloadFileFromUrl(ks.filePathPre, starterProperties.getWebKeystore());
          } catch (Exception e) {
            foundWeb = false;
            // logger.logException(e);
          }
          if (new File(ks.filePathPre).exists()) {
            foundWeb = true;
          }
        }
        if (!foundWeb && !foundFile) {
          if (starterProperties.getDnsKeystore() != null && !starterProperties.getDnsKeystore().isEmpty()) {
            logger.info("try keystore from dns: " + starterProperties.getDnsKeystore());
            try {
              String hostPart = starterProperties.getDnsKeystore().split("\\.")[0];
              String domainPart = starterProperties.getDnsKeystore().replaceAll("^" + hostPart, "");
              System.out.println("Using H:" + hostPart + " D:" + domainPart);
              String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart);
              if (payloadString != null && payloadString.length() > 0) {
                FileUtils.writeByteArrayToFile(new File(ks.filePathPre), Base64.getDecoder().decode(payloadString));
              }
            } catch (Exception e) {
              logger.warn("dnsKeystore -> " + starterProperties.getDnsKeystore());
              logger.logException(e);
            }
          }
        }
      }
      if (starterProperties.getKeystorePassword() != null && !starterProperties.getKeystorePassword().isEmpty()) {
        ks.keystorePassword = starterProperties.getKeystorePassword();
      }
      if (starterProperties.getKeystoreMainAlias() != null && !starterProperties.getKeystoreMainAlias().isEmpty()) {
        ks.keyStoreAlias = starterProperties.getKeystoreMainAlias();
      }
      // se alla fine non è stato trovato un keystore, lo creo
      if (!new File(starterProperties.getFileKeystore().replace(TILDE, USER_HOME)).exists()) {
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
    if (!Boolean.valueOf(starterProperties.isConsoleOnly())) {
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
    new File(starterProperties.getConfPath().replace(TILDE, USER_HOME)).mkdirs();
    try {
      if (dataStore == null) {
        recMan = RecordManagerFactory.createRecordManager(dbDataStorePath.replace(TILDE, USER_HOME));
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
    if ((starterProperties.getWebRegistrationEndpoint() != null
        && !starterProperties.getWebRegistrationEndpoint().isEmpty())
        || Integer.valueOf(starterProperties.getBeaconDiscoveryPort()) != 0) {
      try {
        if (starterProperties.getWebRegistrationEndpoint() == null
            || starterProperties.getWebRegistrationEndpoint().isEmpty()) {
          throw new UnknownHostException();
        }
        logger.info("TRY CONECTION TO BEACON AT " + starterProperties.getWebRegistrationEndpoint());
        connectToBeaconService(starterProperties.getWebRegistrationEndpoint(), starterProperties.getBeaconCaChainPem(),
            Integer.valueOf(starterProperties.getBeaconDiscoveryPort()),
            starterProperties.getBeaconDiscoveryFilterString(), false);
      } catch (Exception e) {
        logger.warn("Beacon connection not ok: " + e.getMessage());
        // logger.logException(e);
      }
    }
  }

  public BeaconClient connectToBeaconService(String urlBeacon, String beaconCaChainPem, int discoveryPort,
      String discoveryFilter, boolean force) {
    URL urlTarget = null;
    if (beaconClient != null) {
      logger.info("This agent is connected to another Beacon service");
      return null;
    } else {
      try {
        if (urlBeacon != null)
          urlTarget = new URL(urlBeacon);
        String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + urlBeacon;
        animaHomunculus.registerNewSession(sessionId, sessionId);
        RpcConversation rpc = animaHomunculus.getRpc(sessionId);
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
  }

  // trova la configurazione appropriata per il bootstrap in funzione dei
  // parametri di configurazione
  private Ar4kConfig resolveBootstrapConfig() {
    Ar4kConfig targetConfig = null;
    int maxConfig = 0;
    try {
      maxConfig = Integer.max(maxConfig,
          starterProperties.getWebConfigOrder() != null ? Integer.valueOf(starterProperties.getWebConfigOrder()) : 6);
    } catch (Exception a) {
      logger.warn("webConfigOrder -> " + starterProperties.getWebConfigOrder());
      maxConfig = 6;
    }
    try {
      maxConfig = Integer.max(maxConfig,
          starterProperties.getDnsConfigOrder() != null ? Integer.valueOf(starterProperties.getDnsConfigOrder()) : 6);
    } catch (Exception a) {
      logger.warn("dnsConfigOrder -> " + starterProperties.getDnsConfigOrder());
      maxConfig = 6;
    }
    try {
      maxConfig = Integer.max(maxConfig,
          starterProperties.getBaseConfigOrder() != null ? Integer.valueOf(starterProperties.getBaseConfigOrder()) : 6);
    } catch (Exception a) {
      logger.warn("base64ConfigOrder -> " + starterProperties.getBaseConfigOrder());
      maxConfig = 5;
    }
    try {
      maxConfig = Integer.max(maxConfig,
          starterProperties.getFileConfigOrder() != null ? Integer.valueOf(starterProperties.getFileConfigOrder()) : 6);
    } catch (Exception a) {
      logger.warn("fileConfigOrder -> " + starterProperties.getFileConfigOrder());
      maxConfig = 4;
    }
    for (int liv = 0; liv <= maxConfig; liv++) {
      if (liv == Integer.valueOf(starterProperties.getWebConfigOrder()) && targetConfig == null
          && starterProperties.getWebConfig() != null && !starterProperties.getWebConfig().isEmpty()) {
        try {
          logger.info("try webConfigDownload");
          targetConfig = webConfigDownload(starterProperties.getWebConfig(),
              starterProperties.getKeystoreConfigAlias());
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == Integer.valueOf(starterProperties.getDnsConfigOrder()) && targetConfig == null
          && starterProperties.getDnsConfig() != null && !starterProperties.getDnsConfig().isEmpty()) {
        try {
          logger.info("try dnsConfigDownload");
          targetConfig = dnsConfigDownload(starterProperties.getDnsConfig(),
              starterProperties.getKeystoreConfigAlias());
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == Integer.valueOf(starterProperties.getBaseConfigOrder()) && targetConfig == null
          && starterProperties.getBaseConfig() != null && !starterProperties.getBaseConfig().isEmpty()) {
        try {
          logger.info("try fromBase64");
          targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(starterProperties.getBaseConfig());
        } catch (Exception e) {
          logger.logException(e);
        }
        break;
      }
      if (liv == Integer.valueOf(starterProperties.getFileConfigOrder()) && targetConfig == null
          && starterProperties.getFileConfig() != null && !starterProperties.getFileConfig().isEmpty()) {
        try {
          logger.info("try fileConfig");
          targetConfig = loadConfigFromFile(starterProperties.getFileConfig().replace(TILDE, USER_HOME),
              starterProperties.getKeystoreConfigAlias());
        } catch (Exception e) {
          logger.logException(e);
        }
      }
    }
    return targetConfig;
  }

  private Ar4kConfig dnsConfigDownload(String dnsTarget, String cryptoAlias) {
    String hostPart = dnsTarget.split("\\.")[0];
    String domainPart = dnsTarget.replaceAll("^" + hostPart, "");
    System.out.println("Using H:" + hostPart + " D:" + domainPart);
    try {
      String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart);
      try {
        if (cryptoAlias != null && !cryptoAlias.isEmpty()) {
          return (Ar4kConfig) ((payloadString != null && payloadString.length() > 0)
              ? ConfigHelper.fromBase64Crypto(payloadString.toString(), cryptoAlias)
              : null);
        } else {
          return (Ar4kConfig) ((payloadString != null && payloadString.length() > 0)
              ? ConfigHelper.fromBase64(payloadString.toString())
              : null);
        }
      } catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | NoSuchPaddingException
          | CMSException e) {
        logger.logException(e);
        return null;
      }
    } catch (UnknownHostException | TextParseException e) {
      logger.logException(e);
      return null;
    }
  }

  public Ar4kConfig loadConfigFromFile(String pathConfig, String cryptoAlias) {
    Ar4kConfig resultConfig = null;
    try {
      String config = "";
      FileReader fileReader = new FileReader(pathConfig.replace(TILDE, USER_HOME));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        config = config + line;
      }
      bufferedReader.close();
      if (cryptoAlias != null && !cryptoAlias.isEmpty()) {
        resultConfig = (Ar4kConfig) ConfigHelper.fromBase64Crypto(config, cryptoAlias);
      } else {
        resultConfig = (Ar4kConfig) ConfigHelper.fromBase64(config);
      }
      return resultConfig;
    } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException
        | CMSException e) {
      if (logger != null)
        logger.logException(e);
      return null;
    }
  }

  public Ar4kConfig webConfigDownload(String webConfigTarget, String cryptoAlias) {
    String temporaryFile = UUID.randomUUID().toString() + ".ar4k.conf"
        + ((cryptoAlias != null && !cryptoAlias.isEmpty()) ? ".crypto" : "");
    try (BufferedInputStream in = new BufferedInputStream(new URL(webConfigTarget).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
      byte dataBuffer[] = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
      return loadConfigFromFile(temporaryFile, cryptoAlias);
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

  private Comparator<PotConfig> comparatorOrderPots = new Comparator<PotConfig>() {
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
    ritorno.put("ar4k.fileKeystore", starterProperties.getFileKeystore());
    ritorno.put("ar4k.webKeystore", starterProperties.getWebKeystore());
    ritorno.put("ar4k.dnsKeystore", starterProperties.getDnsKeystore());
    ritorno.put("ar4k.keystorePassword", starterProperties.getKeystorePassword());
    ritorno.put("ar4k.keystoreMainAlias", starterProperties.getKeystoreMainAlias());
    ritorno.put("ar4k.confPath", starterProperties.getConfPath());
    ritorno.put("ar4k.fileConfig", starterProperties.getFileConfig());
    ritorno.put("ar4k.webConfig", starterProperties.getWebConfig());
    ritorno.put("ar4k.dnsConfig", starterProperties.getDnsConfig());
    ritorno.put("ar4k.baseConfig", starterProperties.getBaseConfig());
    ritorno.put("ar4k.beaconCaChainPem", starterProperties.getBeaconCaChainPem());
    ritorno.put("ar4k.webRegistrationEndpoint", starterProperties.getWebRegistrationEndpoint());
    ritorno.put("ar4k.dnsRegistrationEndpoint", starterProperties.getDnsRegistrationEndpoint());
    ritorno.put("ar4k.beaconDiscoveryPort", String.valueOf(starterProperties.getBeaconDiscoveryPort()));
    ritorno.put("ar4k.beaconDiscoveryFilterString", starterProperties.getBeaconDiscoveryFilterString());
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

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Anima.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() throws BeansException {
    return applicationContext;
  }

  public Collection<ServiceComponent> getServicesOnly() {
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
      animaStateMachine.start();
    }
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
        Ar4kConfig newTargetConfig = webConfigDownload(nextConfigWeb, starterProperties.getKeystoreConfigAlias());
        checkPolledConfig(newTargetConfig);
      }
    };
  }

  private TimerTask checkDnsConfigUpdate(String nextConfigDns) {
    return new TimerTask() {
      @Override
      public void run() {
        Ar4kConfig newTargetConfig = dnsConfigDownload(nextConfigDns, starterProperties.getKeystoreConfigAlias());
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

  public StateMachine<AnimaStates, AnimaEvents> getAnimaStateMachine() {
    return animaStateMachine;
  }

  @Override
  public String toString() {
    return "Anima [agentUniqueName=" + agentUniqueName + ", dbDataStorePath=" + dbDataStorePath + ", dbDataStoreName="
        + dbDataStoreName + ", timerScheduler=" + timerScheduler + ", animaStateMachine=" + animaStateMachine
        + ", animaHomunculus=" + animaHomunculus + ", starterProperties=" + starterProperties + ", runtimeConfig="
        + runtimeConfig + ", targetConfig=" + targetConfig + ", bootstrapConfig=" + bootstrapConfig + ", stateTarget="
        + stateTarget + ", statesBefore=" + statesBefore + ", components=" + components + ", dataStore=" + dataStore
        + ", localUsers=" + localUsers + ", beanName=" + beanName + ", beaconClient=" + beaconClient + ", dataAddress="
        + dataAddress + ", myIdentityKeystore=" + myIdentityKeystore + ", myAliasCertInKeystore="
        + myAliasCertInKeystore + "]";
  }

  public String getFileKeystore() {
    return starterProperties.getFileKeystore();
  }

  public String getDbDataStorePath() {
    return dbDataStorePath;
  }

}
