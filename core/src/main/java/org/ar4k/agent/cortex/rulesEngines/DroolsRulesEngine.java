package org.ar4k.agent.cortex.rulesEngines;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ar4k.agent.cortex.actions.Actions;
import org.ar4k.agent.cortex.conversationBot.exceptions.RuleException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.memory.TimeContextConversation;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;

public class DroolsRulesEngine implements RulesEngine {

  private String ruleName = UUID.randomUUID().toString();
  private ArrayList<DroolsRule> rules = new ArrayList<DroolsRule>();
  private Environment environment = null;
  private Map<String, Object> globalData = new HashMap<String, Object>();
  private String pathStorage = "src/main/resources/";
  private String classPath = "org.ar4k.agent.cortex.rulesEngines";
  private String baseVersion = "org.ar4k.agent.cortex.rulesEngines";

  private KieServices kieServices = KieServices.Factory.get();
  private KieContainer kc = null;

  @Override
  protected void finalize() {
    stop();
  }

  @Override
  public void start() {
    ReleaseId releaseId = new ReleaseIdImpl(classPath, ruleName,
        baseVersion + UUID.randomUUID().toString().replaceAll("-", ""));
    ArrayList<String> regoleDrools = new ArrayList<String>();
    for (DroolsRule regola : rules) {
      regoleDrools.add(regola.getDroolsRule());
    }
    Resource resource = kieServices.getResources().newByteArrayResource(createKJar(releaseId, regoleDrools));
    KieModule module = kieServices.getRepository().addKieModule(resource);
    kc = kieServices.newKieContainer(module.getReleaseId());
    // System.out.println("kc train " + kc);
  }

  @Override
  public void reset() {
    stop();
    start();
  }

  @Override
  public void stop() {
    kc.dispose();
    kc = null;
    globalData = new HashMap<String, Object>();
  }

  private byte[] createKJar(ReleaseId releaseId, Collection<String> drls) {
    KieServices kieServices = KieServices.Factory.get();
    KieFileSystem kfs = kieServices.newKieFileSystem();
    kfs.generateAndWritePomXML(releaseId);
    new File(pathStorage).mkdirs();
    drls.forEach(drl -> kfs.write(pathStorage + drl.hashCode() + ".drl", drl));
    KieBuilder kb = kieServices.newKieBuilder(kfs).buildAll();
    if (kb.getResults().hasMessages(Message.Level.ERROR)) {
      // for (Message result : kb.getResults().getMessages()) {
      // System.out.println(result.getText());
      // }
      return null;
    }
    InternalKieModule kieModule = (InternalKieModule) kieServices.getRepository().getKieModule(releaseId);
    byte[] jar = kieModule.getBytes();
    return jar;
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  public List<DroolsRule> getRules() {
    return rules;
  }

  public void setRules(ArrayList<DroolsRule> rules) {
    this.rules = rules;
  }

  public void addRule(DroolsRule rule) {
    this.rules.add(rule);
  }

  public void delRule(DroolsRule rule) {
    this.rules.remove(rule);
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public Map<String, Object> getGlobalData() {
    return globalData;
  }

  public void setGlobalData(Map<String, Object> globalData) {
    this.globalData = globalData;
  }

  public String getPathStorage() {
    return pathStorage;
  }

  public void setPathStorage(String pathStorage) {
    this.pathStorage = pathStorage;
  }

  public String getClassPath() {
    return classPath;
  }

  public void setClassPath(String classPath) {
    this.classPath = classPath;
  }

  public String getBaseVersion() {
    return baseVersion;
  }

  public void setBaseVersion(String baseVersion) {
    this.baseVersion = baseVersion;
  }

  @Override
  public void fireRule(
      Actions<? extends CortexMessage, ? extends TimeContextConversation, ? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>> actions)
      throws RuleException {
    try {
      // KieBaseConfiguration kieBaseConf = kieServices.newKieBaseConfiguration();
      // KieBase kieBase = kc.newKieBase(kieBaseConf);
      KieSessionConfiguration kieSessionConfiguration = kieServices.newKieSessionConfiguration();
      // System.out.println("kc -> " + kc);
      KieSession ksession = kc.newKieSession(environment, kieSessionConfiguration);
      ksession.insert(actions);
      ksession.insert(globalData);
      ksession.fireAllRules();
      ksession.dispose();
    } catch (Exception ee) {
      ee.printStackTrace();
      throw new RuleException(ee.getMessage());
    }

  }

}
