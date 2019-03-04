package org.ar4k.agent.cortex.conversationBot.nodes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.rulesEngines.DroolsRule;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractHomunculusConfiguration implements Meme, HomunculusMaker {

  public AbstractHomunculusConfiguration() {
    super();
  }

  public AbstractHomunculusConfiguration(String name) {
    super();
    this.name = name;
  }

  public AbstractHomunculusConfiguration(Set<String> intentExamples, Map<String, String> entitySynonyms,
      Map<String, String> globalEntitySynonyms, String name, ArrayList<DroolsRule> preNlpRules,
      ArrayList<DroolsRule> postNlpRules, ArrayList<DroolsRule> postChildrenRules, String realHomunculusName) {
    super();
    this.intentExamples = intentExamples;
    this.entitySynonyms = entitySynonyms;
    this.name = name;
    this.preNlpRules = preNlpRules;
    this.postNlpRules = postNlpRules;
    this.postChildrenRules = postChildrenRules;
    this.realHomunculusName = realHomunculusName;
    this.globalEntitySynonyms = globalEntitySynonyms;
  }

  private static final long serialVersionUID = -4978192908782276319L;

  @Parameter(names = "--intentExamples", description = "examples for intent matcher", variableArity = true)
  public Set<String> intentExamples = new HashSet<String>();

  @Parameter(names = "--entitySynonyms", description = "entity synonyms for training", variableArity = true)
  public Map<String, String> entitySynonyms = new HashMap<String, String>();

  @Parameter(names = "--globalEntitySynonyms", description = "entity synonyms for training valid for this homunculus and the children", variableArity = true)
  public Map<String, String> globalEntitySynonyms = new HashMap<String, String>();

  @Parameter(names = "--name", description = "name for this node")
  public String name = null;

  @Parameter(names = "--preNlpRules", description = "regole eseguite prima del passaggio in NLP", variableArity = true)
  public ArrayList<DroolsRule> preNlpRules = new ArrayList<DroolsRule>();

  @Parameter(names = "--postNlpRules", description = "regole eseguite dopo il passaggio in NLP, prima della richiesta ai figli", variableArity = true)
  public ArrayList<DroolsRule> postNlpRules = new ArrayList<DroolsRule>();

  @Parameter(names = "--postChildrenRules", description = "regole eseguite dopo il passaggio in NLP e la distibuzione ai figli", variableArity = true)
  public ArrayList<DroolsRule> postChildrenRules = new ArrayList<DroolsRule>();

  @Parameter(names = "--homunculusClassName", description = "class for the homunculus implementation")
  public String homunculusClassName = "org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus";

  /**
   * Per i link simbolici // utilizzare il parametro name
   */
  public String realHomunculusName = null;

  @Override
  public String toString() {
    return name;
  }

  @Override
  public String toJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }

  @Override
  public Meme fromJson(String json) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.fromJson(json, this.getClass());
  }

  @Override
  public String toBase64() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(this);
      oos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  @Override
  public Meme fromBase64(String base64) {
    AbstractBotConfiguration rit = null;
    try {
      byte[] data = Base64.getDecoder().decode(base64);
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
      rit = ((AbstractBotConfiguration) ois.readObject());
      ois.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return rit;
  }

}
