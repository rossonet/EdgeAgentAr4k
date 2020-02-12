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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.DataAddressAnima;
import org.ar4k.agent.exception.Ar4kException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.process.ScriptEngineManagerProcess;
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

  public static ContextCreationHelper getNewAnimaInNewContext(Class<?> springMasterClass, ExecutorService executor,
      String loggerFile, String keyStore, int webPort, List<String> args, Ar4kConfig animaConfig,
      String mainAliasInKeystore, String keystoreBeaconAlias, String webRegistrationEndpoint) {
    return new ContextCreationHelper(springMasterClass, executor, loggerFile, keyStore, webPort, args, animaConfig,
        mainAliasInKeystore, keystoreBeaconAlias, webRegistrationEndpoint);
  }

  public Class<Anima> getStaticClass() {
    return Anima.class;
  }

  public static final String DEFAULT_KS_PATH = "default-new.ks";

  public static String addressSpacePrefix = null;

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private static transient final String registrationPin = ConfigHelper.createRandomRegistryId();
  // assegnato da Spring tramite setter al boot
  private static ApplicationContext applicationContext;

  private String agentUniqueName = null;

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
  // configurazione per il reload
  private transient Ar4kConfig reloadConfig = null;
  private AnimaStates stateTarget = AnimaStates.RUNNING;
  private Map<Instant, AnimaStates> statesBefore = new HashMap<>();

  private Set<ServiceComponent<Ar4kComponent>> components = new HashSet<>();

  private Map<String, Object> dataStore = null;

  private Collection<Ar4kUserDetails> localUsers = new HashSet<>();

  private transient RecordManager recMan = null;

  private String beanName = "anima";

  private BeaconClient beaconClient = null;

  private DataAddressAnima dataAddress = new DataAddressAnima(this, addressSpacePrefix);

  private KeystoreConfig myIdentityKeystore = null;
  private String myAliasCertInKeystore = "agent";

  private transient boolean onApplicationEventFlag = false;

  private transient boolean afterSpringInitFlag = false;

  private transient boolean firstStateFired = false;

  public static enum AnimaStates {
    INIT, STAMINAL, CONFIGURED, RUNNING, KILLED, FAULTED, STASIS
  }

  // tipi di router interno supportato per gestire lo scambio dei messagi tra gli
  // agenti, per la definizione della policy security sul routing public
  public static enum AnimaRouterType {
    NONE, PRODUCTION, DEVELOP, ROAD
  }

  public static enum AnimaEvents {
    BOOTSTRAP, SETCONF, START, STOP, PAUSE, HIBERNATION, EXCEPTION, RESTART, COMPLETE_RELOAD
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
    resetAgent();
    if (animaStateMachine != null) {
      animaStateMachine.sendEvent(AnimaEvents.STOP);
      animaStateMachine.stop();
      animaStateMachine = null;
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
  synchronized void stateChanged() {
    logger.info("State change in ANIMA to " + getState());
    statesBefore.put(new Instant(), getState());
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "KILLED")
  synchronized void finalizeAgent() {
    try {
      for (ServiceComponent<Ar4kComponent> targetService : components) {
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
    } catch (Exception aa) {
      logger.logException("error during finalize phase", aa);
    }
  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "KILLED")
  synchronized void resetAgent() {
    finalizeAgent();
    try {
      if (recMan != null) {
        try {
          recMan.close();
        } catch (IOException e) {
          logger.info("IOException closing file data map of anima");
        }
        recMan = null;
      }
      bootstrapConfig = null;
      targetConfig = null;
      runtimeConfig = null;
      if (dataAddress != null) {
        dataAddress.close();
      }
      dataAddress = new DataAddressAnima(this, addressSpacePrefix);
    } catch (Exception aa) {
      logger.logException("error during reloading phase", aa);
    }
  }

  @SuppressWarnings("unchecked")
  void setInitialAuth() {
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
      boolean foundDns = false;
      if (starterProperties.getKeystorePassword() != null && !starterProperties.getKeystorePassword().isEmpty()) {
        ks.keystorePassword = starterProperties.getKeystorePassword();
      }
      if (starterProperties.getKeystoreMainAlias() != null && !starterProperties.getKeystoreMainAlias().isEmpty()) {
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
              logger.info("use keystore file " + ks.toString());
            } else {
              logger.info("keystore not works");
            }
          } else {
            logger.warn("keystore file not found (" + ks.toString() + ")");
          }
        } catch (Exception a) {
          logger.warn("keystore error -> " + a.getMessage());
        }
      } else {
        logger.info("value of fileKeystore is null, use: " + ks.toString());
      }
      if (!foundFile) {
        try {
          Files.deleteIfExists(Paths.get(ks.filePathPre));
        } catch (IOException e) {
          logger.logException("error deleting wrong keystore", e);
        }
        if (starterProperties.getWebKeystore() != null && !starterProperties.getWebKeystore().isEmpty()) {
          try {
            logger.info("try keystore from web url: "
                + ConfigHelper.resolveWorkingString(starterProperties.getWebKeystore(), false));
            HardwareHelper.downloadFileFromUrl(ks.filePathPre,
                ConfigHelper.resolveWorkingString(starterProperties.getWebKeystore(), false));
          } catch (Exception e) {
            foundWeb = false;
            logger.warn("webKeystore " + ConfigHelper.resolveWorkingString(starterProperties.getWebKeystore(), false)
                + " not found");
            // logger.logExceptionDebug(e);
          }
          try {
            if (new File(ks.filePathPre).exists() && ks.check()) {
              foundWeb = true;
              logger.info(
                  "found web keystore " + ConfigHelper.resolveWorkingString(starterProperties.getWebKeystore(), false));
            } else {
              logger.info("web keystore not works");
            }
          } catch (Exception a) {
            logger.info("keystore error -> " + a.getMessage());
          }

        }
        if (!foundWeb && !foundFile) {
          try {
            Files.deleteIfExists(Paths.get(ks.filePathPre));
          } catch (IOException e) {
            logger.logException("error deleting wrong keystore", e);
          }
          if (starterProperties.getDnsKeystore() != null && !starterProperties.getDnsKeystore().isEmpty()) {
            try {
              logger.info("try keystore from dns: "
                  + ConfigHelper.resolveWorkingString(starterProperties.getDnsKeystore(), false));
              String hostPart = ConfigHelper.resolveWorkingString(starterProperties.getDnsKeystore(), false)
                  .split("\\.")[0];
              String domainPart = ConfigHelper.resolveWorkingString(starterProperties.getDnsKeystore(), false)
                  .replaceAll("^" + hostPart, "");
              String payloadString = HardwareHelper.resolveFileFromDns(hostPart, domainPart);
              byte[] data = Base64.getDecoder().decode(payloadString);
              ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
              byte[] returnData = (byte[]) ois.readObject();
              ois.close();
              if (payloadString != null && payloadString.length() > 0) {
                FileUtils.writeByteArrayToFile(new File(ks.filePathPre), returnData);
              }
              try {
                if (new File(ks.filePathPre).exists() && ks.check()) {
                  foundDns = true;
                  logger.info("found dns keystore "
                      + ConfigHelper.resolveWorkingString(starterProperties.getDnsKeystore(), false));
                } else {
                  logger.info("dns keystore not works");
                }
              } catch (Exception a) {
                logger.info("keystore error -> " + a.getMessage());
              }
            } catch (Exception e) {
              logger.warn("dnsKeystore " + ConfigHelper.resolveWorkingString(starterProperties.getDnsKeystore(), false)
                  + " not found");
              // logger.logExceptionDebug(e);
            }
          }
        }
      }
      // se alla fine non è stato trovato un keystore, lo creo
      if (!foundWeb && !foundFile && !foundDns) {
        try {
          Files.deleteIfExists(Paths.get(ks.filePathPre));
        } catch (IOException e) {
          logger.logException("error deleting wrong keystore", e);
        }
        logger.warn("new keystore: " + ks.toString());
        ks.createSelfSignedCert(getAgentUniqueName() + "-master", ConfigHelper.organization, ConfigHelper.unit,
            ConfigHelper.locality, ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri, ConfigHelper.dns,
            ConfigHelper.ip, ks.keyStoreAlias, true);
      }
      // addKeyStores(ks);
      setMyIdentityKeystore(ks);
      setMyAliasCertInKeystore(ks.keyStoreAlias);
      logger.info("Certificate for anima: " + ks.getClientCertificate(ks.keyStoreAlias).getSubjectX500Principal()
          + " - alias " + ks.keyStoreAlias);
    } else {
      logger.info("Use keystore " + myIdentityKeystore.toString());
    }
  }

  @PostConstruct
  void afterSpringInit() throws Exception {
    afterSpringInitFlag = true;
    checkDualStart();
  }

  // workaround Spring State Machine
  // @OnStateMachineStart
  synchronized void initAgent() {
    if (starterProperties.isConsoleOnly() != null
        && !(starterProperties.isConsoleOnly().equals("true") || starterProperties.isConsoleOnly().equals("yes"))) {
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
  synchronized void startingAgent() {
    new File(ConfigHelper.resolveWorkingString(starterProperties.getConfPath(), true)).mkdirs();
    try {
      if (dataStore == null) {
        recMan = RecordManagerFactory
            .createRecordManager(ConfigHelper.resolveWorkingString(starterProperties.getConfPath(), true) + "/"
                + starterProperties.getAnimaDatastoreFileName());
        dataStore = recMan.treeMap(dbDataStoreName);
        logger.info("datastore on Anima started");
      }
    } catch (IOException e) {
      logger.logException("datastore on Anima problem", e);
    }
    setMasterKeystore();
    bootstrapConfig = resolveBootstrapConfig();
    popolateAddressSpace();
    setInitialAuth();
    try {
      checkBeaconClient();
    } catch (Exception a) {
      logger.warn("Error connecting beacon client -> " + Ar4kLogger.stackTraceToString(a, 4));
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
      animaStateMachine.sendEvent(AnimaEvents.SETCONF);
    }
  }

  private void popolateAddressSpace() {
    dataAddress.firstStart();
  }

  private void checkBeaconClient() {
    if (runtimeConfig != null && runtimeConfig.beaconServer != null && !runtimeConfig.beaconServer.isEmpty()) {
      try {
        logger.info("TRY CONNECTION TO BEACON IN CONFIG RUNTIME AT "
            + ConfigHelper.resolveWorkingString(filterBeaconUrl(runtimeConfig.beaconServer), false));
        beaconClient = connectToBeaconService(
            ConfigHelper.resolveWorkingString(filterBeaconUrl(runtimeConfig.beaconServer), false),
            runtimeConfig.beaconServerCertChain, Integer.valueOf(runtimeConfig.beaconDiscoveryPort),
            runtimeConfig.beaconDiscoveryFilterString, true);
      } catch (Exception e) {
        logger.warn("Beacon connection in config not ok: " + e.getMessage());
        logger.info(Ar4kLogger.stackTraceToString(e, 40));
      }
    } else {
      if ((starterProperties.getWebRegistrationEndpoint() != null
          && !starterProperties.getWebRegistrationEndpoint().isEmpty())
          || Integer.valueOf(starterProperties.getBeaconDiscoveryPort()) != 0) {
        try {
          if (starterProperties.getWebRegistrationEndpoint() == null
              || starterProperties.getWebRegistrationEndpoint().isEmpty()) {
            throw new UnknownHostException();
          }
          logger.info("TRY CONNECTION TO BEACON AT "
              + ConfigHelper.resolveWorkingString(starterProperties.getWebRegistrationEndpoint(), false));
          beaconClient = connectToBeaconService(
              ConfigHelper.resolveWorkingString(starterProperties.getWebRegistrationEndpoint(), false),
              starterProperties.getBeaconCaChainPem(), Integer.valueOf(starterProperties.getBeaconDiscoveryPort()),
              starterProperties.getBeaconDiscoveryFilterString(), false);
        } catch (Exception e) {
          logger.warn("Beacon connection not ok: " + e.getMessage());
          logger.info(Ar4kLogger.stackTraceToString(e, 6));
        }
      }
    }
  }

  private String filterBeaconUrl(String beaconServer) {
    return beaconServer;
  }

  public BeaconClient connectToBeaconService(String urlBeacon, String beaconCaChainPem, int discoveryPort,
      String discoveryFilter, boolean force) {
    URL urlTarget = null;
    BeaconClient beaconClientTarget = null;
    {
      try {
        if (urlBeacon != null)
          urlTarget = new URL(urlBeacon);
        String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + urlBeacon;
        animaHomunculus.registerNewSession(sessionId, sessionId);
        RpcConversation rpc = animaHomunculus.getRpc(sessionId);
        beaconClientTarget = new BeaconClient.Builder()
            .setAliasBeaconClientInKeystore(starterProperties.getKeystoreBeaconAlias())
            .setUniqueName(getAgentUniqueName()).setAnima(this).setBeaconCaChainPem(beaconCaChainPem)
            .setDiscoveryFilter(discoveryFilter).setDiscoveryPort(discoveryPort).setPort(urlTarget.getPort())
            .setRpcConversation(rpc).setHost(urlTarget.getHost()).build();
        if (beaconClientTarget != null && beaconClientTarget.getStateConnection().equals(ConnectivityState.READY)) {
          logger.info("found Beacon endpoint: " + urlBeacon);
          if (!getAgentUniqueName().equals(beaconClientTarget.getAgentUniqueName())) {
            logger.info("the unique name is changed in " + getAgentUniqueName());
          }
        } else {
          logger.info("the Beacon endpoint " + urlBeacon + " return " + beaconClientTarget.getStateConnection());
        }
      } catch (IOException e) {
        logger.info("the url " + urlBeacon + " is malformed or unreachable [" + e.getCause() + "]");
      } catch (UnrecoverableKeyException e) {
        logger.warn(e.getMessage());
      } catch (Exception e) {
        logger.info(Ar4kLogger.stackTraceToString(e, 6));
      }
      return beaconClientTarget;
    }
  }

  // trova la configurazione appropriata per il bootstrap in funzione dei
  // parametri di configurazione
  private Ar4kConfig resolveBootstrapConfig() {
    Ar4kConfig targetConfig = null;
    if (reloadConfig == null) {
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
            starterProperties.getBaseConfigOrder() != null ? Integer.valueOf(starterProperties.getBaseConfigOrder())
                : 6);
      } catch (Exception a) {
        logger.warn("base64ConfigOrder -> " + starterProperties.getBaseConfigOrder());
        maxConfig = 5;
      }
      try {
        maxConfig = Integer.max(maxConfig,
            starterProperties.getFileConfigOrder() != null ? Integer.valueOf(starterProperties.getFileConfigOrder())
                : 6);
      } catch (Exception a) {
        logger.warn("fileConfigOrder -> " + starterProperties.getFileConfigOrder());
        maxConfig = 4;
      }
      for (int liv = 0; liv <= maxConfig; liv++) {
        logger.info(String.valueOf(liv) + "/" + maxConfig + " searching config...");
        if (liv == Integer.valueOf(starterProperties.getWebConfigOrder()) && targetConfig == null
            && starterProperties.getWebConfig() != null && !starterProperties.getWebConfig().isEmpty()) {
          try {
            logger.info("try webConfig");
            targetConfig = webConfigDownload(ConfigHelper.resolveWorkingString(starterProperties.getWebConfig(), false),
                starterProperties.getKeystoreConfigAlias());
            if (targetConfig != null) {
              logger.info("found webConfig");
              break;
            }
          } catch (Exception e) {
            logger.warn("error in webconfig download" + Ar4kLogger.stackTraceToString(e, 4));
          }
        }
        if (liv == Integer.valueOf(starterProperties.getDnsConfigOrder()) && targetConfig == null
            && starterProperties.getDnsConfig() != null && !starterProperties.getDnsConfig().isEmpty()) {
          try {
            logger.info("try dnsConfig");
            targetConfig = dnsConfigDownload(ConfigHelper.resolveWorkingString(starterProperties.getDnsConfig(), false),
                starterProperties.getKeystoreConfigAlias());
            if (targetConfig != null) {
              logger.info(
                  "found dnsConfig " + ConfigHelper.resolveWorkingString(starterProperties.getDnsConfig(), false));
              break;
            }
          } catch (Exception e) {
            logger.warn("error in dns config download" + Ar4kLogger.stackTraceToString(e, 4));
          }
        }
        if (liv == Integer.valueOf(starterProperties.getBaseConfigOrder()) && targetConfig == null
            && starterProperties.getBaseConfig() != null && !starterProperties.getBaseConfig().isEmpty()) {
          try {
            logger.info("try base64Config");
            targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(starterProperties.getBaseConfig());
            if (targetConfig != null) {
              logger.info("found base64Config");
              break;
            }
          } catch (Exception e) {
            logger.warn("error in baseconfig" + Ar4kLogger.stackTraceToString(e, 4));
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
              logger.info(
                  "found fileConfig " + ConfigHelper.resolveWorkingString(starterProperties.getFileConfig(), true));
              break;
            }
          } catch (Exception e) {
            logger.warn("error in fileconfig" + Ar4kLogger.stackTraceToString(e, 4));
          }
        }
      }
    } else {
      targetConfig = reloadConfig;
    }
    return targetConfig;
  }

  private Ar4kConfig dnsConfigDownload(String dnsTarget, String cryptoAlias) {
    String hostPart = dnsTarget.split("\\.")[0];
    String domainPart = dnsTarget.replaceAll("^" + hostPart, "");
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
        logger.warn("error in dns download using H:" + hostPart + " D:" + domainPart + " -> "
            + Ar4kLogger.stackTraceToString(e, 4));
        return null;
      }
    } catch (UnknownHostException | TextParseException e) {
      logger.warn("error in dns download using H:" + hostPart + " D:" + domainPart + " -> "
          + Ar4kLogger.stackTraceToString(e, 4));
      return null;
    }
  }

  private Ar4kConfig loadConfigFromFile(String pathConfig, String cryptoAlias) {
    Ar4kConfig resultConfig = null;
    try {
      String config = "";
      FileReader fileReader = new FileReader(ConfigHelper.resolveWorkingString(pathConfig, true));
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
      if (e instanceof java.io.FileNotFoundException) {
        logger.debug("config file not found " + pathConfig);
      } else if (logger != null) {
        logger.warn("error in config file -> " + Ar4kLogger.stackTraceToString(e, 4));
      }
      return null;
    }
  }

  private Ar4kConfig webConfigDownload(String webConfigTarget, String cryptoAlias) {
    String temporaryFile = agentUniqueName + ".ar4k.conf"
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
    } catch (UnknownHostException u) {
      logger.info(webConfigTarget + " is not a valid host name");
      return null;
    } catch (Exception e) {
      logger.warn("url for web config failed: " + webConfigTarget);
      if (logger != null)
        logger.warn("Error downloading web config -> " + Ar4kLogger.stackTraceToString(e, 4));
      return null;
    }

  }

  // workaround Spring State Machine
  // @OnStateChanged(target = "CONFIGURED")
  synchronized void configureAgent() {
    if (runtimeConfig == null) {
      logger.warn("Required running state without conf");
      animaStateMachine.sendEvent(AnimaEvents.EXCEPTION);
    }
    if (stateTarget == null && runtimeConfig != null) {
      stateTarget = runtimeConfig.targetRunLevel;
    }
    if (runtimeConfig != null && runtimeConfig.updateFileConfig == true) {
      updateFileConfig(runtimeConfig);
    }
    if (stateTarget != null && stateTarget.equals(AnimaStates.RUNNING)) {
      timerScheduler = new Timer();
      animaStateMachine.sendEvent(AnimaEvents.START);
    } else {
      logger.warn("stateTarget is null in runtime config");
    }
  }

  private void updateFileConfig(Ar4kConfig config) {
    String fileTarget = ConfigHelper.resolveWorkingString(starterProperties.getFileConfig(), true);
    if (starterProperties.getKeystoreConfigAlias() != null && !starterProperties.getKeystoreConfigAlias().isEmpty()) {
      try {
        Files.write(Paths.get(fileTarget),
            ConfigHelper.toBase64Crypto(config, starterProperties.getKeystoreConfigAlias()).getBytes(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        logger.info("crypto configuration in " + fileTarget + " updated with runtime config");
      } catch (Exception e) {
        logger.logException("error saving crypto configuration in runtime to " + fileTarget, e);
      }
    } else {
      try {
        Files.write(Paths.get(fileTarget), ConfigHelper.toBase64(config).getBytes(), StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING);
        logger.info("configuration in " + fileTarget + " updated with runtime config");
      } catch (Exception e) {
        logger.logException("error saving configuration in runtime to " + fileTarget, e);
      }
    }
  }

  private Comparator<ServiceConfig> comparatorOrderPots = new Comparator<ServiceConfig>() {
    @Override
    public int compare(ServiceConfig o1, ServiceConfig o2) {
      return Integer.compare(o1.getPriority(), o2.getPriority());
    }
  };

  // workaround Spring State Machine
  // @OnStateChanged(target = "RUNNING")
  // avvia i servizi
  synchronized void runServices() {
    List<ServiceConfig> sortedList = new ArrayList<>(runtimeConfig.pots);
    Collections.sort(sortedList, comparatorOrderPots);
    for (ServiceConfig confServizio : sortedList) {
      if (confServizio instanceof ServiceConfig) {
        logger.info("run " + confServizio + " as service");
        try {
          runSeedService(confServizio);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException e) {
          throw new Ar4kException("problem trying to run service " + confServizio.getName(), e.getCause());
        }
      }
    }
  }

  private void runSeedService(ServiceConfig confServizio)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    AnimaService service = new AnimaService(this, confServizio, timerScheduler);
    components.add(service);
    service.start();
  }

  // adminPassword è escluso
  public Map<String, String> getEnvironmentVariables() {
    Map<String, String> ritorno = new HashMap<>();
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

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Anima.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() throws BeansException {
    return applicationContext;
  }

  public Set<ServiceComponent<Ar4kComponent>> getComponents() {
    return components;
  }

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    logger.debug(" event: " + event.toString());
    if (event instanceof ContextRefreshedEvent) {
      // avvio a contesto spring caricato
      onApplicationEventFlag = true;
      checkDualStart();
    }
  }

  private synchronized void checkDualStart() {
    if (onApplicationEventFlag && afterSpringInitFlag && !firstStateFired) {
      firstStateFired = true;
      if (starterProperties != null) {
        agentUniqueName = ConfigHelper.generateNewUniqueName(starterProperties.getUniqueName(),
            starterProperties.getUniqueNameFile());
      }
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
        sessionId = UUID.randomUUID().toString().replace("-", "");
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

  public DataAddressAnima getDataAddress() {
    return dataAddress;
  }

  public void setDataAddress(DataAddressAnima dataAddress) {
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

  void startCheckingNextConfig() {
    reloadConfig = null;
    if (runtimeConfig != null
        && (runtimeConfig.nextConfigFile != null || runtimeConfig.nextConfigDns != null
            || runtimeConfig.nextConfigWeb != null)
        && runtimeConfig.configCheckPeriod != null && runtimeConfig.configCheckPeriod > 0) {
      if (runtimeConfig.nextConfigFile != null) {
        timerScheduler.schedule(checkFileConfigUpdate(runtimeConfig.nextConfigFile), runtimeConfig.configCheckPeriod,
            runtimeConfig.configCheckPeriod);
        logger.warn("scheduled periodically configuration checking on file " + runtimeConfig.nextConfigFile
            + " with rate time of " + runtimeConfig.configCheckPeriod + " ms");
      }
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
        elaborateNewConfig(newTargetConfig);
      }
    };
  }

  private TimerTask checkDnsConfigUpdate(String nextConfigDns) {
    return new TimerTask() {
      @Override
      public void run() {
        Ar4kConfig newTargetConfig = dnsConfigDownload(nextConfigDns, starterProperties.getKeystoreConfigAlias());
        elaborateNewConfig(newTargetConfig);
      }
    };
  }

  private TimerTask checkFileConfigUpdate(String nextConfigFile) {
    return new TimerTask() {
      @Override
      public void run() {
        Ar4kConfig newTargetConfig = loadConfigFromFile(nextConfigFile, starterProperties.getKeystoreConfigAlias());
        elaborateNewConfig(newTargetConfig);
      }
    };
  }

  public final void elaborateNewConfig(Ar4kConfig newTargetConfig) {
    if (newTargetConfig != null && newTargetConfig.isMoreUpToDateThan(getRuntimeConfig())) {
      logger.warn("Found new config " + newTargetConfig.toString());
      reloadConfig = newTargetConfig;
      runtimeConfig = newTargetConfig;
      if (newTargetConfig.nextConfigReload == null || newTargetConfig.nextConfigReload == false) {
        sendEvent(AnimaEvents.RESTART);
      } else {
        sendEvent(AnimaEvents.COMPLETE_RELOAD);
      }
    }
  }

  public StateMachine<AnimaStates, AnimaEvents> getAnimaStateMachine() {
    return animaStateMachine;
  }

  @Override
  public String toString() {
    return "Anima [agentUniqueName=" + agentUniqueName + ", dbDataStoreName=" + dbDataStoreName + ", animaStateMachine="
        + animaStateMachine + ", animaHomunculus=" + animaHomunculus + ", starterProperties=" + starterProperties
        + ", runtimeConfig=" + runtimeConfig + ", targetConfig=" + targetConfig + ", bootstrapConfig=" + bootstrapConfig
        + ", stateTarget=" + stateTarget + ", statesBefore=" + statesBefore + ", components=" + components
        + ", dataStore=" + dataStore + ", localUsers=" + localUsers + ", beanName=" + beanName + ", beaconClient="
        + beaconClient + ", dataAddress=" + dataAddress + ", myIdentityKeystore=" + myIdentityKeystore
        + ", myAliasCertInKeystore=" + myAliasCertInKeystore + "]";
  }

  public String getFileKeystore() {
    return ConfigHelper.resolveWorkingString(starterProperties.getFileKeystore(), true);
  }

  void runPreScript() {
    if (runtimeConfig.preScript != null && runtimeConfig.preScriptLanguage != null && !runtimeConfig.preScript.isEmpty()
        && !runtimeConfig.preScriptLanguage.isEmpty()) {
      logger.info("run pre script in language " + runtimeConfig.preScriptLanguage);
      String scriptLabel = "prescript";
      try {
        scriptRunner(scriptLabel, runtimeConfig.preScriptLanguage, runtimeConfig.preScript);
      } catch (Exception a) {
        logger.logException("error running " + scriptLabel, a);
      }
    }
  }

  private void scriptRunner(String scriptLabel, String scriptLanguage, String script) {
    ScriptEngineManagerProcess p = new ScriptEngineManagerProcess();
    p.setLabel(scriptLabel);
    p.setEngine(scriptLanguage);
    p.eval(script);
    String sessionId = UUID.randomUUID().toString().replace("-", "") + "_" + scriptLabel;
    animaHomunculus.registerNewSession(sessionId, sessionId);
    RpcConversation rpc = animaHomunculus.getRpc(sessionId);
    rpc.getScriptSessions().put(scriptLabel, p);
  }

  void runPostScript() {
    if (runtimeConfig.postScript != null && runtimeConfig.postScriptLanguage != null
        && !runtimeConfig.postScript.isEmpty() && !runtimeConfig.postScriptLanguage.isEmpty()) {
      logger.info("run post script in language " + runtimeConfig.postScriptLanguage);
      String scriptLabel = "postscript";
      try {
        scriptRunner(scriptLabel, runtimeConfig.postScriptLanguage, runtimeConfig.postScript);
      } catch (Exception a) {
        logger.logException("error running " + scriptLabel, a);
      }
    }
  }

  public BeaconClient getBeaconClient() {
    return beaconClient;
  }

  public Ar4kStarterProperties getStarterProperties() {
    return starterProperties;
  }

  public AuthenticationManager getAuthenticationManager() {
    return authenticationManager;
  }

}
