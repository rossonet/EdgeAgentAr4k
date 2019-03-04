package org.ar4k.agent.cortex.conversationBot.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class AbstractHomunculusConfigurationJsonAdapter<T extends AbstractHomunculusConfiguration>
    extends TypeAdapter<T> {

  private Class<T> homunculusConfig = null;
  private final String ident = "  ";

  public AbstractHomunculusConfigurationJsonAdapter(Class<T> abstractHomunculusClass) {
    super();
    this.homunculusConfig = (Class<T>) abstractHomunculusClass;
  }

  public T read(JsonReader reader) throws IOException {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(homunculusConfig, new AbstractHomunculusConfigurationJsonAdapter<T>(homunculusConfig))
        .setPrettyPrinting().create();
    try {
      T homunculusConf;
      // System.out.println(homunculusConfig.getName());
      homunculusConf = (T) Class.forName(homunculusConfig.getName()).newInstance();
      reader.beginObject();
      while (reader.hasNext()) {
        switch (reader.nextName()) {
        case "name":
          homunculusConf.name = reader.nextString();
          break;
        case "real-homunculus":
          homunculusConf.realHomunculusName = reader.nextString();
          break;
        case "is-symbolic-link":
          reader.nextBoolean();
          break;
        case "pre-nlp-rules":
          homunculusConf.preNlpRules = gson.fromJson(reader, ArrayList.class);
          break;
        case "post-nlp-rules":
          homunculusConf.postNlpRules = gson.fromJson(reader, ArrayList.class);
          break;
        case "post-children-rules":
          homunculusConf.postChildrenRules = gson.fromJson(reader, ArrayList.class);
          break;
        case "intent-examples":
          homunculusConf.intentExamples = gson.fromJson(reader, Set.class);
          break;
        case "entity-synonyms":
          homunculusConf.entitySynonyms = gson.fromJson(reader, Map.class);
          break;
        case "global-entity-synonyms":
          homunculusConf.globalEntitySynonyms = gson.fromJson(reader, Map.class);
          break;
        }
      }
      reader.endObject();
      return homunculusConf;
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void write(JsonWriter writer, T obj) throws IOException {
    if (obj == null) {
      writer.nullValue();
      return;
    }
    Gson gson = new GsonBuilder()

        .registerTypeAdapter(homunculusConfig, new AbstractHomunculusConfigurationJsonAdapter<T>(homunculusConfig))
        .setPrettyPrinting().create();
    writer.setIndent(ident);
    if (AbstractHomunculusConfiguration.class.isAssignableFrom(obj.getClass())) {
      writer.beginObject();
      // writer.name("name").value(obj.name);
      if (obj.preNlpRules != null) {
        writer.name("pre-nlp-rules");
        gson.toJson(obj.preNlpRules, obj.preNlpRules.getClass(), writer);
      }
      if (obj.intentExamples != null) {
        writer.name("intent-examples");
        gson.toJson(obj.intentExamples, obj.intentExamples.getClass(), writer);
      }
      if (obj.entitySynonyms != null) {
        writer.name("entity-synonyms");
        gson.toJson(obj.entitySynonyms, obj.entitySynonyms.getClass(), writer);
      }
      if (obj.globalEntitySynonyms != null) {
        writer.name("global-entity-synonyms");
        gson.toJson(obj.globalEntitySynonyms, obj.globalEntitySynonyms.getClass(), writer);
      }
      if (obj.postNlpRules != null) {
        writer.name("post-nlp-rules");
        gson.toJson(obj.postNlpRules, obj.postNlpRules.getClass(), writer);
      }
      if (obj.postChildrenRules != null) {
        writer.name("post-children-rules");
        gson.toJson(obj.postChildrenRules, obj.postChildrenRules.getClass(), writer);
      }
      if (obj.realHomunculusName != null) {
        writer.name("is-symbolic-link").value(true);
        writer.name("real-homunculus").value(obj.realHomunculusName);
      } else {
        writer.name("is-symbolic-link").value(false);
      }
      writer.endObject();
    }
  }
}
