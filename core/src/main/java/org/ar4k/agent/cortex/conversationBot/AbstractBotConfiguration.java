package org.ar4k.agent.cortex.conversationBot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractBotConfiguration implements Meme, HomunculusClassGenerator {
  private static final long serialVersionUID = -2718856465439365132L;

  @Parameter(names = "--tree", description = "tree of homunculus")
  public String symbolicLinksTree = null;
  @Parameter(names = "--botName", description = "short name for the bot")
  public String botName = null;
  @Parameter(names = "--botDescription", description = "description of the bot")
  public String botDescription = null;
  @Parameter(names = "--bootstrapData", description = "first data for the runtime data map of the bot", variableArity = true)
  public Map<String, Object> bootstrapData = new HashMap<String, Object>();

  public AbstractBrocaBot<? extends AbstractBotConfiguration> getBot() throws BotBootException {
    // CLASSE ASTRATTA
    return null;
  }

  @Override
  public String toString() {
    return botName;
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
