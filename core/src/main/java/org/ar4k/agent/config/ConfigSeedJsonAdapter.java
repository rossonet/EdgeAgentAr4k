package org.ar4k.agent.config;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

// TODO: verificare se serve per ConfigHelper
public class ConfigSeedJsonAdapter<T extends ConfigSeed> extends TypeAdapter<T> {

  private final String ident = "  ";
  private static transient final String labelText = "text";
  private static transient final String labelIntent = "intent";
  private static transient final String labelEntities = "entities";
  
  @SuppressWarnings("unchecked")
  public T read(JsonReader reader) throws IOException {
    /*
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(homunculusConfig, new AbstractHomunculusConfigurationJsonAdapter<T>(homunculusConfig))
        .setPrettyPrinting().create();
    try {
      T homunculusConf;
      homunculusConf = (T) Class.forName(homunculusConfig.getName()).newInstance();
      reader.beginObject();
      while (reader.hasNext()) {
        switch (reader.nextName()) {
        case "name":
          homunculusConf.name = reader.nextString();
          break;
        case "order":
          homunculusConf.order = reader.nextInt();
          break;
        case "real-homunculus":
          homunculusConf.realHomunculusName = reader.nextString();
          break;
        case "is-symbolic-link":
          reader.nextBoolean();
          break;
        case "pre-nlp-rules":
          reader.beginArray();
          while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY) {
            homunculusConf.preNlpRules.add(gson.fromJson(reader, DroolsRule.class));
          }
          reader.endArray();
          break;
        case "post-nlp-rules":
          reader.beginArray();
          while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY) {
            homunculusConf.postNlpRules.add(gson.fromJson(reader, DroolsRule.class));
          }
          reader.endArray();
          break;
        case "post-children-rules":
          reader.beginArray();
          while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY) {
            homunculusConf.postChildrenRules.add(gson.fromJson(reader, DroolsRule.class));
          }
          reader.endArray();
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
    */
    return null;
  }

  public void write(JsonWriter writer, T obj) throws IOException {
    /*
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
      if (obj.order != null) {
        writer.name("order").value(obj.order);
      }
      writer.endObject();
    }
    */
  }
}
