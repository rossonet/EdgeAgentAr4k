package org.ar4k.agent.cortex.drools;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.cortex.annotation.Ar4kDroolsContext;
import org.ar4k.agent.cortex.annotation.DroolsGlobalClass;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         AI Drools
 */
public class DroolsService extends AbstractAr4kService {

  private static final String ANIMA_GLOBAL_DROOLS = "anima";
  private static final String DATA_ADDRESS_GLOBAL_DROOLS = "data-address";
  private static final String DATA_STORE_GLOBAL_DROOLS = "data-store";
  private DroolsConfig configuration = null;
  private KieContainer kieContainer = null;
  private KieServices kieServices = null;
  private KieRepository kieRepository = null;
  private List<Resource> allResources = new ArrayList<>();
  private KieModule kieModule = null;
  private KieSession kieSession = null;
  private StatelessKieSession kieStatelessSession = null;

  public boolean isStateless() {
    return configuration.isStateless();
  }

  public FactHandle insertOrExecute(Object newObject, boolean fireAllRules) {
    if (isStateless()) {
      getKieStalessSession(configuration.getSessionName()).execute(newObject);
      return null;
    } else {
      FactHandle r = getKieSession(configuration.getSessionName()).insert(newObject);
      if (fireAllRules) {
        getKieSession(configuration.getSessionName()).fireAllRules();
      }
      return r;
    }
  }

  public void delete(FactHandle factHandle, boolean fireAllRules) {
    if (isStateless()) {
      throw new IllegalArgumentException("you can not delete fact from stateless session");
    } else {
      getKieSession(configuration.getSessionName()).delete(factHandle);
      if (fireAllRules) {
        getKieSession(configuration.getSessionName()).fireAllRules();
      }
    }
  }

  public void fireUntilHalt() {
    if (isStateless()) {
      throw new IllegalArgumentException("you can not call fireUntilHalt() in stateless session");
    } else {
      getKieSession(configuration.getSessionName()).fireUntilHalt();
    }
  }

  private KieSession getKieSession(String sessionName) {
    if (kieSession == null) {
      kieSession = getKieContainer().newKieSession(sessionName);
    }
    return kieSession;
  }

  private StatelessKieSession getKieStalessSession(String sessionName) {
    if (kieStatelessSession == null) {
      kieStatelessSession = getKieContainer().newStatelessKieSession(sessionName);
    }
    return kieStatelessSession;
  }

  private KieServices getKieService() {
    if (kieServices == null) {
      kieServices = KieServices.Factory.get();
    }
    return kieServices;
  }

  private KieContainer getKieContainer() {
    if (kieContainer == null) {
      kieContainer = getKieService().newKieContainer(getKieModule().getReleaseId());
    }
    return kieContainer;
  }

  private KieModule getKieModule() {
    if (kieModule == null) {
      if (allResources.isEmpty()) {
        KieBuilder kieBuilder = getKieService().newKieBuilder(new File("./"));
        kieModule = kieBuilder.getKieModule();
      } else {
        for (Resource m : allResources) {
          kieModule = getKieRepository().addKieModule(m);
        }
      }
    }
    return kieModule;
  }

  private KieRepository getKieRepository() {
    if (kieRepository == null) {
      kieRepository = getKieService().getRepository();
    }
    return kieRepository;
  }

  private void addUrlModule(String pathResource) {
    allResources.add(getKieService().getResources().newUrlResource(pathResource));
  }

  private void addFileModule(String fileName) {
    allResources.add(getKieService().getResources().newFileSystemResource(fileName));
  }

  private void addStringModule(String stringResource) {
    Reader reader = new StringReader(stringResource);
    allResources.add(getKieService().getResources().newReaderResource(reader));
  }

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(DroolsService.class.toString());

  @Override
  public void stop() {
    if (kieSession != null) {
      kieSession.dispose();
    }
    if (kieContainer != null) {
      kieContainer.dispose();
    }
    kieContainer = null;
    kieServices = null;
    kieRepository = null;
    allResources.clear();
    kieModule = null;
    kieSession = null;
    kieStatelessSession = null;
  }

  @Override
  public void init() {
    for (String urlModule : configuration.getUrlModules()) {
      addUrlModule(urlModule);
    }
    for (String fileModule : configuration.getFileModules()) {
      addFileModule(fileModule);
    }
    for (String stringModule : configuration.getStringModules()) {
      addStringModule(stringModule);
    }
    if (isStateless()) {
      getKieStalessSession(configuration.getSessionName());
    } else {
      getKieSession(configuration.getSessionName());
    }
    popolateClassObjects();
    popolateNlp();
    popolateContext();
    popolateGlobalData();
    logger.info("Drools service " + configuration.getSessionName() + " started");
  }

  private void popolateNlp() {
    if (configuration.isOpenNlpEnable()) {
      // TODO inserire ogetti per NLP
    }
  }

  private void popolateClassObjects() {
    for (Object singleDroolsGlobalClass : listDroolsContextObjects(configuration.getBasePath())) {
      if (singleDroolsGlobalClass instanceof DroolsGlobalClass) {
        try {
          for (Entry<String, Object> singleObject : ((DroolsGlobalClass) singleDroolsGlobalClass).getAllObjects()
              .entrySet())
            if (isStateless()) {
              getKieStalessSession(configuration.getSessionName()).setGlobal(singleObject.getKey(),
                  singleObject.getValue());
            } else {
              getKieSession(configuration.getSessionName()).setGlobal(singleObject.getKey(), singleObject.getValue());
            }
        } catch (Exception e) {
          logger.logException(e);
        }
      }
    }
  }

  private Set<Object> listDroolsContextObjects(String packageName) {
    Set<Object> rit = new HashSet<>();
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(Ar4kDroolsContext.class));
    Set<BeanDefinition> classes = provider.findCandidateComponents(packageName);
    for (BeanDefinition c : classes) {
      try {
        Class<?> classe = Class.forName(c.getBeanClassName());
        Constructor<?> con = classe.getConstructor();
        rit.add(con.newInstance());
      } catch (Exception e) {
        logger.logException(e);
      }
    }
    return rit;
  }

  private void popolateContext() {
    if (Anima.getApplicationContext().getBean(Anima.class) != null && configuration.insertAnima()) {
      if (isStateless()) {
        getKieStalessSession(configuration.getSessionName()).setGlobal(ANIMA_GLOBAL_DROOLS,
            Anima.getApplicationContext().getBean(Anima.class));
      } else {
        getKieSession(configuration.getSessionName()).setGlobal(ANIMA_GLOBAL_DROOLS,
            Anima.getApplicationContext().getBean(Anima.class));
      }
    }
    if (Anima.getApplicationContext().getBean(Anima.class) != null && configuration.insertDataAddress()) {
      if (isStateless()) {
        getKieStalessSession(configuration.getSessionName()).setGlobal(DATA_ADDRESS_GLOBAL_DROOLS,
            Anima.getApplicationContext().getBean(Anima.class).getDataAddress());
      } else {
        getKieSession(configuration.getSessionName()).setGlobal(DATA_ADDRESS_GLOBAL_DROOLS,
            Anima.getApplicationContext().getBean(Anima.class).getDataAddress());
      }
    }
    if (Anima.getApplicationContext().getBean(Anima.class) != null && configuration.insertDataStore()) {
      if (isStateless()) {
        getKieStalessSession(configuration.getSessionName()).setGlobal(DATA_STORE_GLOBAL_DROOLS,
            Anima.getApplicationContext().getBean(Anima.class).getDataStore());
      } else {
        getKieSession(configuration.getSessionName()).setGlobal(DATA_STORE_GLOBAL_DROOLS,
            Anima.getApplicationContext().getBean(Anima.class).getDataStore());
      }
    }
  }

  private void popolateGlobalData() {
    for (Entry<String, String> singleData : configuration.getGlobalData().entrySet()) {
      if (isStateless()) {
        getKieStalessSession(configuration.getSessionName()).setGlobal(singleData.getKey(), singleData.getValue());
      } else {
        getKieSession(configuration.getSessionName()).setGlobal(singleData.getKey(), singleData.getValue());
      }
    }
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (DroolsConfig) configuration;

  }

  @Override
  public JSONObject getStatusJson() {
    JsonElement result = null;
    Gson gson = new Gson();
    if (isStateless()) {
      result = gson.toJsonTree(getKieStalessSession(configuration.getSessionName()).getGlobals());
    } else {
      result = gson.toJsonTree(getKieSession(configuration.getSessionName()).getGlobals());
    }
    return new JSONObject(result.getAsString());
  }

  @Override
  public void close() throws Exception {
    stop();
  }

  @Override
  public void loop() {
    // TODO Auto-generated method stub

  }

}
