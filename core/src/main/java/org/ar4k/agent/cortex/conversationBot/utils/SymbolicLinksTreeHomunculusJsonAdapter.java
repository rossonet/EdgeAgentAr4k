package org.ar4k.agent.cortex.conversationBot.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.AbstractBrocaBot;
import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.helper.SymbolicLinksTree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class SymbolicLinksTreeHomunculusJsonAdapter<T extends SymbolicLinksTree<AbstractHomunculusConfiguration>>
    extends TypeAdapter<T> {

  private T tree = null;
  private ArrayList<AbstractHomunculusConfiguration> fathers = new ArrayList<AbstractHomunculusConfiguration>();
  private AbstractBrocaBot<? extends AbstractBotConfiguration> bot = null;
  private final String ident = "  ";
  private final String randomLink = UUID.randomUUID().toString();
  private boolean debug = false;

  public SymbolicLinksTreeHomunculusJsonAdapter(AbstractBrocaBot<? extends AbstractBotConfiguration> bot, T tree) {
    super();
    this.bot = bot;
    this.tree = tree;
  }

  public SymbolicLinksTreeHomunculusJsonAdapter(AbstractBrocaBot<? extends AbstractBotConfiguration> bot, T tree,
      ArrayList<AbstractHomunculusConfiguration> fathers) {
    super();
    this.bot = bot;
    this.tree = tree;
    this.fathers = fathers;
  }

  public class FoundObject {
    public String tmpText = randomLink;
    public AbstractHomunculusConfiguration confhomunculus = null;
  }

  public T read(JsonReader reader) throws IOException {
    FoundObject oFound = new FoundObject();
    try {
      oFound.confhomunculus = bot.newHomunculusConfiguration(oFound.tmpText);
    } catch (BotBootException e1) {
      e1.printStackTrace();
    }
    AbstractHomunculusConfiguration father = null;
    if (fathers.isEmpty()) {
      if (debug == true)
        System.out.println("father - " + tree.getHead());
      tree.getTree(tree.getHead()).changeHead(oFound.confhomunculus);
      father = oFound.confhomunculus;
    } else {
      father = fathers.get(fathers.size() - 1);
      tree.getTree(father).addLeaf(oFound.confhomunculus);
      if (debug == true)
        System.out.println("child of - " + father);
      father = oFound.confhomunculus;
    }
    try {
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(SymbolicLinksTree.class,
              new SymbolicLinksTreeHomunculusJsonAdapter<T>(bot, tree, fathers))
          .registerTypeAdapter(AbstractHomunculusConfiguration.class,
              new AbstractHomunculusConfigurationJsonAdapter<AbstractHomunculusConfiguration>(
                  (Class<AbstractHomunculusConfiguration>) bot.homunculusConfigurationClass()))
          .setPrettyPrinting().create();
      reader.beginObject();
      while (reader.hasNext() && reader.peek() != JsonToken.END_OBJECT) {
        switch (reader.nextName()) {
        case "text":
          oFound.tmpText = reader.nextString();
          if (debug == true)
            System.out.println("real name - " + oFound.tmpText);
          break;
        case "children":
          reader.beginArray();
          fathers.add(father);
          while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY) {
            if (debug == true)
              System.out.println("level - " + fathers.size());
            gson.fromJson(reader, SymbolicLinksTree.class);
          }
          fathers.remove(father);
          reader.endArray();
          break;
        case "homunculus":
          oFound.confhomunculus = gson.fromJson(reader, AbstractHomunculusConfiguration.class);
          break;
        }
      }
      // cerca se stesso
      for (AbstractHomunculusConfiguration node : tree.getDescent()) {
        if (node.name.equals(randomLink)) {
          if (debug == true)
            System.out.println("me - " + node.name);
          if (debug == true)
            System.out.println("ck - " + oFound.confhomunculus.equals(node));
          tree.getTree(node).changeHead(oFound.confhomunculus);
          if (oFound.tmpText != null) {
            oFound.confhomunculus.name = oFound.tmpText;
          }
          if (debug == true)
            System.out.println("ck - " + oFound.confhomunculus.equals(node));
          if (debug == true)
            System.out.println("node: " + node.name + " / me: " + oFound.confhomunculus.name);
        }
      }
      reader.endObject();
      return (T) tree;
    } catch (

    Exception e) {
      // if (debug==true) System.out.println(reader.nextString());
      e.printStackTrace();
      return null;
    }
  }

  public void write(JsonWriter writer, T obj) throws IOException {
    if (obj == null) {
      writer.nullValue();
      return;
    }
    try {
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(SymbolicLinksTree.class, new SymbolicLinksTreeHomunculusJsonAdapter<T>(bot, tree))
          .registerTypeAdapter(AbstractHomunculusConfiguration.class,
              new AbstractHomunculusConfigurationJsonAdapter<AbstractHomunculusConfiguration>(
                  (Class<AbstractHomunculusConfiguration>) bot.homunculusConfigurationClass()))
          .setPrettyPrinting().create();

      writer.setIndent(ident);
      writer.beginObject();
      if (AbstractHomunculusConfiguration.class.isAssignableFrom(obj.getHead().getClass())) {
        writer.name("homunculus");
        gson.toJson(obj.getHead(), obj.getHead().getClass(), writer);
      }
      writer.name("text").value(obj.getHead().toString());
      writer.name("children");
      gson.toJson(obj.getSubTrees(), obj.getSubTrees().getClass(), writer);
      writer.endObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
