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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.ar4k.agent.config.AnimaStateMachineConfig;
import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.ar4k.agent.tunnel.TunnelComponent;
//import org.ar4k.agent.tribe.AtomixTribeComponent;
import org.joda.time.Instant;
import org.slf4j.Logger;
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
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

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
@WithStateMachine
public class Anima implements ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware, Closeable {
  private static final Logger logger = Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private final String dbDataStorePath = "~/.ar4k/anima_datastore-" + UUID.randomUUID().toString();
  private final String dbDataStoreName = "datastore";

  public Anima() {
  }

  @Autowired
  private StateMachine<AnimaStates, AnimaEvents> animaStateMachine;

  @Autowired
  private AnimaStateMachineConfig animaStateMachineConfig;

  @Autowired
  private AnimaHomunculus animaHomunculus;

  @Autowired
  AuthenticationManager authenticationManager;

  // TODO: implementare exception

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

  // array di configurazioni disponibili
  // private Set<Ar4kConfig> configs = new HashSet<Ar4kConfig>();
  // configurazione di lavoro per editare
  // private Ar4kConfig workingConfig = null;

  // gestione stati TODO: controllare se viene effettivamente usato nel post
  // config per arrivare allo stato desiderato
  private AnimaStates stateTarget = null;
  private Map<Instant, AnimaStates> statesBefore = new HashMap<Instant, AnimaStates>();

  // array keystore disponibili
  private Set<KeystoreConfig> keyStores = new HashSet<KeystoreConfig>();

  private Instant timeCounterLambda = null;
  private Timer timerLambda = null;

  // assegnato da Spring tramite setter al boot
  private static ApplicationContext applicationContext;

  private Set<Ar4kComponent> components = new HashSet<Ar4kComponent>();

  private Map<String, Object> dataStore = null;

  private Collection<Ar4kUserDetails> localUsers = new HashSet<>();

  // per HashMap su disco (map reduction)
  private transient RecordManager recMan = null;

  private boolean init = false;

  private String beanName = null;

  // LAMBDA quando chiamato da cron sul sistema con regolarità o tramite AWS
  // Lambda,Google Function o, in generale, in modalità function as a service
  // STASIS per la funzione di mantenimento a basso consumo,
  // Bot sistema autonomo (switch tra le configurazioni selezionato da Cortex)
  public static enum AnimaStates {
    INIT, STARTING, STAMINAL, CONFIGURED, RUNNING, SERVICE, CONSOLE, LAMBDA, BOT, PAUSED, STOPED, KILLED, FAULTED,
    STASIS
  }

  // tipi di router interno supportato
  public static enum AnimaRouterType {
    NONE, PRODUCTION, DEVELOP, ROAD
  }

  public static enum AnimaEvents {
    BOOTSTRAP, BORN, SETCONF, START, PAUSE, STOP, HIBERNATION, FINALIZE, EXCEPTION
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
    boolean risposta = false;
    if (animaStateMachineConfig != null && animaStateMachineConfig.getRunningStates() != null
        && animaStateMachineConfig.getRunningStates().contains(getState())) {
      risposta = true;
    }
    return risposta;
  }

  @Override
  public void close() {
    animaStateMachine.sendEvent(AnimaEvents.FINALIZE);
    animaStateMachine = null;
  }

  @OnStateChanged()
  public synchronized void stateChanged() {
    statesBefore.put(new Instant(), getState());
  }

  @OnStateChanged(target = "KILLED")
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
      admin.setPassword(adminPassword);
      SimpleGrantedAuthority grantedAuthorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
      SimpleGrantedAuthority grantedAuthorityUser = new SimpleGrantedAuthority("ROLE_USER");
      ((Set<SimpleGrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityAdmin);
      ((Set<SimpleGrantedAuthority>) admin.getAuthorities()).add(grantedAuthorityUser);
      localUsers.add(admin);
    }
    init = true;
  }

  @OnStateChanged(target = "STARTING")
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
    }
    setInitialAuth();
    bootStrapConfig = resolveBootstrapConfig();
    animaStateMachine.sendEvent(AnimaEvents.BORN);
  }

  // trova la configurazione appropriata per il bootstrap in funzione dei
  // parametri di configurazione
  private Ar4kConfig resolveBootstrapConfig() {
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
        // in caso di parità sui livelli vale questo ordine
        if (liv == webConfigOrder && targetConfig == null) {
          // TODO: caricamento da webconfig
        }
        if (liv == dnsConfigOrder && targetConfig == null) {
          // TODO: caricamento da dnsconfig
        }
        if (liv == base64ConfigOrder && targetConfig == null && baseConfig != null && !baseConfig.equals("")) {
          try {
            targetConfig = (Ar4kConfig) ConfigHelper.fromBase64(baseConfig);
          } catch (ClassNotFoundException | IOException e) {
            if (logger != null)
              logger.warn(e.getMessage());
          }
        }
        if (liv == fileConfigOrder && targetConfig == null && fileConfig != null && !fileConfig.equals("")) {
          try {
            String config = "";
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
              logger.warn(e.getMessage());
          }
        }
      }
    }
    return targetConfig;
  }

  @OnStateChanged(target = "STAMINAL")
  public synchronized void staminalAgent() {
    // se non è presente una configurazione runtime e un target ed è presente quella
    // di boot, utilizzarla per l'avvio
    if (runtimeConfig == null && targetConfig == null && bootStrapConfig != null) {
      targetConfig = bootStrapConfig;
    }
    if (runtimeConfig != null || (runtimeConfig == null && targetConfig != null)) {
      runtimeConfig = targetConfig;
      animaStateMachine.sendEvent(AnimaEvents.BOOTSTRAP);
    }
  }

  @OnStateChanged(target = "CONFIGURED")
  public synchronized void configureAgent() {
    if (runtimeConfig == null) {
      logger.warn("Required running state without conf");
      animaStateMachine.sendEvent(AnimaEvents.EXCEPTION);
    }
    if (stateTarget == null && runtimeConfig != null) {
      stateTarget = runtimeConfig.targetRunLevel;
    }
    runPots();
  }

  @OnStateChanged(source = "CONFIGURED", target = "SERVICE")
  public synchronized void runService() {
    // joinTribes();
    runAgent();
  }

  @OnStateChanged(source = "CONFIGURED", target = "CONSOLE")
  public synchronized void runConsole() {
    // joinTribes();
    runAgent();
  }

  @OnStateChanged(source = "CONFIGURED", target = "BOT")
  public synchronized void runBot() {
    // joinTribes();
    runAgent();
  }

  @OnStateChanged(source = "CONFIGURED", target = "LAMBDA")
  public synchronized void runLambda() {
    // joinTribes();
    timeCounterLambda = new Instant();
    timerLambda = new Timer("Timer");
    long delay = 1000L;
    timerLambda.schedule(task, delay);
    runAgent();
  }

  @OnStateChanged(source = "LAMBDA")
  public synchronized void exitLambda() {
    if (timerLambda != null) {
      timerLambda.purge();
      timerLambda = null;
    }
  }

  private boolean checkLambdaFinished() {
    boolean ritorno = false;
    if (new Instant().getMillis() - timeCounterLambda.getMillis() > runtimeConfig.clockAfterFinishCallLambda) {
      ritorno = true;
    }
    return ritorno;
  }

  private TimerTask task = new TimerTask() {
    public void run() {
      if (checkLambdaFinished()) {
        animaStateMachine.sendEvent(AnimaEvents.STOP);
      }
    }
  };

  /*
   * TODO: implementare in atomix
   */
  /*
   * private synchronized void joinTribes() { if (runtimeConfig.beans != null &&
   * runtimeConfig.beans.size() > 0) { for (ConfigSeed tc : runtimeConfig.beans) {
   * if (tc instanceof TribeConfig) { Ar4kComponent tribe = tc.instanziate();
   * tribe.init(); components.add(tribe); } } } }
   */
  private synchronized void runAgent() {
    runServices();
  }

  private void runServices() {
    for (ServiceConfig confServizio : runtimeConfig.services) {
      try {
        runSeedService(confServizio);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        e.printStackTrace();
      }
    }
  }

  private void runPots() {
    for (PotConfig confVaso : runtimeConfig.pots) {
      try {
        runSeedPot(confVaso);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        e.printStackTrace();
      }
    }
  }

  private void runSeedPot(PotConfig potConfig)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = potConfig.getClass().getMethod("instantiate");
    Ar4kComponent targetService;
    targetService = (Ar4kComponent) method.invoke(null);
    targetService.setConfiguration((PotConfig) potConfig);
    components.add(targetService);
    targetService.init();
  }

  private void runSeedService(ServiceConfig confServizio)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = confServizio.getClass().getMethod("instantiate");
    ServiceComponent targetService;
    targetService = (ServiceComponent) method.invoke(null);
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
    configTxt += "ar4k.keystorePassword: " + keystorePassword + "\n";
    configTxt += "ar4k.keystoreCaAlias: " + keystoreCaAlias + "\n";
    configTxt += "ar4k.confPath: " + confPath + "\n";
    configTxt += "ar4k.fileConfig: " + fileConfig + "\n";
    configTxt += "ar4k.webConfig: " + webConfig + "\n";
    configTxt += "ar4k.dnsConfig: " + dnsConfig + "\n";
    configTxt += "ar4k.baseConfig: " + baseConfig + "\n";
    configTxt += "ar4k.otpRegistrationSeed: " + otpRegistrationSeed + "\n";
    configTxt += "ar4k.webRegistrationEndpoint: " + webRegistrationEndpoint + "\n";
    configTxt += "ar4k.dnsRegistrationEndpoint: " + dnsRegistrationEndpoint + "\n";
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

  public void waitFirstState() {
    while (init == false) {
      try {
        System.out.println("wait first status " + getState());
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    Anima.applicationContext = ac;
  }

  public static ApplicationContext getApplicationContext() throws BeansException {
    return applicationContext;
  }

  /*
   * TODO: implementare in atomix
   */
  /*
   * public Set<AtomixTribeComponent> getTribes() { Set<AtomixTribeComponent>
   * target = new HashSet<AtomixTribeComponent>(); for (Ar4kComponent bean :
   * components) { if (bean instanceof AtomixTribeComponent) {
   * target.add((AtomixTribeComponent) bean); } } return target; }
   */

  public Collection<TunnelComponent> getTunnels() {
    Set<TunnelComponent> target = new HashSet<TunnelComponent>();
    for (Ar4kComponent bean : components) {
      if (bean instanceof TunnelComponent) {
        target.add((TunnelComponent) bean);
      }
    }
    return target;
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

  /*
   * TODO: IMPLEMENTARE in atomix
   */
  /*
   * public void addTribe(AtomixTribeComponent tribe) {
   * this.components.add(tribe); }
   */
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

  // coda principale logger
  @Bean
  public MessageChannel mainLogChannel() {
    return new PublishSubscribeChannel();
  }

  // coda principale exceptions
  @Bean
  public MessageChannel mainExceptionChannel() {
    return new PublishSubscribeChannel();
  }

  @Override
  public void setBeanName(String name) {
    beanName = name;
  }

  public Collection<Ar4kUserDetails> getLocalUsers() {
    return localUsers;
  }

  public String login(String username, String password) {
    String sessionId = null;
    UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
    Authentication result = authenticationManager.authenticate(request);
    SecurityContextHolder.getContext().setAuthentication(result);
    sessionId = UUID.randomUUID().toString();
    animaHomunculus.registerNewSession(sessionId, result);
    return sessionId;
  }

  public void logout(String sessionId) {
    animaHomunculus.removeSessionInformation(sessionId);
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

  public AnimaHomunculus getAnimaHomunculus() {
    return animaHomunculus;
  }

  protected String getBeanName() {
    return beanName;
  }

  @Override
  public String toString() {
    return "Anima [runtimeConfig=" + runtimeConfig + ", stateTarget=" + stateTarget + ", init=" + init + ", getState()="
        + getState() + ", isRunning()=" + isRunning() + ", getEnvironmentVariablesAsString()="
        + getEnvironmentVariablesAsString() + ", getBeanName()=" + getBeanName() + ", hashCode()=" + hashCode() + "]";
  }
}
