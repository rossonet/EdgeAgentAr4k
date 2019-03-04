package org.ar4k.agent.cortex.conversationBot.utils;

import java.io.IOException;
import java.util.HashMap;

import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.helper.SymbolicLinksTree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class AbstractBotConfigurationJsonAdapter<T extends AbstractBotConfiguration> extends TypeAdapter<T> {

  @Override
  public void write(JsonWriter out, T value) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public T read(JsonReader in) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }
/*
  private Class<? extends AbstractBotConfiguration> botConfig;
  private final String ident = "  ";

  public AbstractBotConfigurationJsonAdapter(Class<? extends AbstractBotConfiguration> abstractBotConfigurationClass) {
    super();
    this.botConfig = abstractBotConfigurationClass;
  }

  public T read(JsonReader reader) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    T botConf = null;
    try {
      botConf = botConfig.newInstance();
      reader.beginObject();
      while (reader.hasNext()) {
        if (reader.peek() == JsonToken.END_OBJECT) {
          break;
        } else {
          switch (reader.nextName()) {
          case "name":
            botConf.botName = reader.nextString();
            break;
          case "bot-description":
            botConf.botDescription = reader.nextString();
            break;
          case "tree":
            botConf.symbolicLinksTree = gson.fromJson(reader, SymbolicLinksTree.class);
            break;
          case "data":
            botConf.bootstrapData = gson.fromJson(reader, HashMap.class);
            break;
          }
        }
      }
      reader.endObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return botConf;
  }

  public void write(JsonWriter writer, T obj) throws IOException {
    if (obj == null) {
      writer.nullValue();
      return;
    }
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SymbolicLinksTree.class,
            new SymbolicLinksTreeHomunculusJsonAdapter<SymbolicLinksTree<?>>(botConfig, homunculusConfig))
        .registerTypeAdapter(botConfig, new AbstractBotConfigurationJsonAdapter<T>(botConfig, homunculusConfig))
        .registerTypeAdapter(homunculusConfig,
            new AbstractHomunculusConfigurationJsonAdapter<AbstractHomunculusConfiguration>(botConfig,
                homunculusConfig))
        .setPrettyPrinting().create();
    writer.setIndent(ident);
    writer.beginObject();
    writer.name("name").value(obj.botName);
    writer.name("bot-description").value(obj.botDescription);
    writer.name("data");
    gson.toJson(obj.bootstrapData, obj.bootstrapData.getClass(), writer);
    writer.name("tree");
    gson.toJson(obj.symbolicLinksTree, obj.symbolicLinksTree.getClass(), writer);
    writer.endObject();
  }
  */
}
