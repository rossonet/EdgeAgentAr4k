package org.ar4k.agent.cortex.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.OntologyTag;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.messages.MemoryFact;
import org.joda.time.Instant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BotTimeContextConversation implements TimeContextConversation {

  private static final long serialVersionUID = 7053619694825881787L;
  private HashMap<String, Object> attributes = new HashMap<String, Object>();
  private HashMap<String, Object> longTermAttributes = new HashMap<String, Object>();
  private Locale locale = Locale.getDefault();
  private TimeZone timeZone = TimeZone.getDefault();
  private String uniqueInterlocutor = null;
  private Focus focus = null;
  private List<MemoryFact> memory = new ArrayList<MemoryFact>();

  @Override
  public String getUniqueInterlocutor() {
    return uniqueInterlocutor;
  }

  @Override
  public void setUniqueInterlocutor(String uniqueInterlocutor) {
    this.uniqueInterlocutor = uniqueInterlocutor;
  }

  @Override
  public Focus focusNow() {
    return focus;
  }

  @Override
  public boolean changeFocus(Focus newFocus) {
    focus = newFocus;
    return true;
  }

  @Override
  public String getShortDescription() {
    return "Dialogo tra l'agente e l'operatore tramite console";
  }

  @Override
  public List<? extends CortexMessage> getTrascription() {
    List<CortexMessage> ritorno = new ArrayList<CortexMessage>();
    for (MemoryFact a : memory) {
      if (a instanceof CortexMessage) {
        ritorno.add((CortexMessage) a);
      }
    }
    return ritorno;
  }

  @Override
  public List<? extends CortexMessage> getTrascription(MemoryFact from, MemoryFact to) {
    List<CortexMessage> ritorno = new ArrayList<CortexMessage>();
    boolean active = false;
    for (MemoryFact a : memory) {
      if (a.equals(from)) {
        active = true;
      }
      if (a.equals(to)) {
        active = false;
        break;
      }
      if (a instanceof CortexMessage && active) {
        ritorno.add((CortexMessage) a);
      }
    }
    return ritorno;
  }

  @Override
  public List<? extends CortexMessage> getTrascription(Instant from, Instant to) {
    List<CortexMessage> ritorno = new ArrayList<CortexMessage>();
    for (MemoryFact a : memory) {
      if (a instanceof CortexMessage && a.getGenerationTime().isAfter(from) && a.getGenerationTime().isBefore(to)) {
        ritorno.add((CortexMessage) a);
      }
    }
    return ritorno;
  }

  @Override
  public List<? extends CortexMessage> getTrascription(Instant from, MemoryFact to) {
    List<CortexMessage> ritorno = new ArrayList<CortexMessage>();
    boolean active = false;
    for (MemoryFact a : memory) {
      if (a.getGenerationTime().isAfter(from)) {
        active = true;
      }
      if (a.equals(to)) {
        active = false;
        break;
      }
      if (a instanceof CortexMessage && active) {
        ritorno.add((CortexMessage) a);
      }
    }
    return ritorno;
  }

  @Override
  public List<? extends CortexMessage> getTrascription(MemoryFact from, Instant to) {
    List<CortexMessage> ritorno = new ArrayList<CortexMessage>();
    boolean active = false;
    for (MemoryFact a : memory) {
      if (a.equals(from)) {
        active = true;
      }
      if (a.getGenerationTime().isAfter(to)) {
        active = false;
        break;
      }
      if (a instanceof CortexMessage && active) {
        ritorno.add((CortexMessage) a);
      }
    }
    return ritorno;
  }

  @Override
  public TimeContextConversation getSection(MemoryFact from, MemoryFact to) {
    TimeContextConversation ritorno = null;
    try {
      ritorno = (TimeContextConversation) this.clone();
      ritorno.reset();
      boolean active = false;
      for (MemoryFact a : memory) {
        if (a.equals(from)) {
          active = true;
        }
        if (a.equals(to)) {
          active = false;
          break;
        }
        if (active) {
          ritorno.addMemoryFact(a);
        }
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return ritorno;
  }

  @Override
  public TimeContextConversation getSection(Instant from, Instant to) {
    TimeContextConversation ritorno = null;
    try {
      ritorno = (TimeContextConversation) this.clone();
      ritorno.reset();
      for (MemoryFact a : memory) {
        if (a.getGenerationTime().isAfter(from) && a.getGenerationTime().isBefore(to)) {
          ritorno.addMemoryFact(a);
        }
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return ritorno;
  }

  @Override
  public TimeContextConversation getSection(Instant from, MemoryFact to) {
    TimeContextConversation ritorno = null;
    try {
      ritorno = (TimeContextConversation) this.clone();
      ritorno.reset();
      boolean active = false;
      for (MemoryFact a : memory) {
        if (a.getGenerationTime().isAfter(from)) {
          active = true;
        }
        if (a.equals(to)) {
          active = false;
          break;
        }
        if (active) {
          ritorno.addMemoryFact(a);
        }
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return ritorno;
  }

  @Override
  public TimeContextConversation getSection(MemoryFact from, Instant to) {
    TimeContextConversation ritorno = null;
    try {
      ritorno = (TimeContextConversation) this.clone();
      ritorno.reset();
      boolean active = false;
      for (MemoryFact a : memory) {
        if (a.equals(from)) {
          active = true;
        }
        if (a.getGenerationTime().isAfter(to)) {
          active = false;
          break;
        }
        if (active) {
          ritorno.addMemoryFact(a);
        }
      }
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return ritorno;
  }

  @Override
  public TimeContextConversation getSection() {
    return this;
  }

  @Override
  public List<? extends MemoryFact> getTimeLine() {
    return memory;
  }

  @Override
  public List<? extends MemoryFact> getTimeLine(Set<OntologyTag> filters) {
    List<MemoryFact> ritorno = new ArrayList<MemoryFact>();
    for (MemoryFact m : memory) {
      boolean selected = false;
      for (OntologyTag filtro : filters) {
        if (m.getTags().contains(filtro)) {
          selected = true;
          break;
        }
      }
      if (selected) {
        ritorno.add(m);
      }
    }
    return ritorno;
  }

  @Override
  public CortexMessage getLastMessage() {
    List<? extends CortexMessage> tuttiMessaggi = getTrascription();
    return tuttiMessaggi.get(tuttiMessaggi.size() - 1);
  }

  @Override
  public CortexMessage getLastMessage(String sender) {
    List<? extends CortexMessage> tuttiMessaggi = getTrascription();
    List<CortexMessage> filteredMessaggi = new ArrayList<CortexMessage>();
    for (CortexMessage a : tuttiMessaggi) {
      if (a.getSender().equals(sender)) {
        filteredMessaggi.add(a);
      }
    }
    return filteredMessaggi.get(filteredMessaggi.size() - 1);
  }

  @Override
  public TimeZone getTimeZone() {
    return timeZone;
  }

  @Override
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  @Override
  public Locale getLocale() {
    return locale;
  }

  @Override
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  @Override
  public Object getAttribute(String index) {
    return attributes.get(index);
  }

  @Override
  public void reset() {
    attributes = new HashMap<String, Object>();
    longTermAttributes = new HashMap<String, Object>();
    locale = Locale.getDefault();
    timeZone = TimeZone.getDefault();
    memory = new ArrayList<MemoryFact>();
  }

  @Override
  public void removeAttribute(String index) {
    attributes.remove(index);
  }

  @Override
  public void addAttribute(String index, Object value) {
    attributes.put(index, value);
  }

  @Override
  public void addLongTermAttribute(String index, Object value) {
    longTermAttributes.put(index, value);
  }

  @Override
  public void removeLongTermAttribute(String index) {
    longTermAttributes.remove(index);
  }

  @Override
  public Object getLongTermAttribute(String index) {
    return longTermAttributes.get(index);
  }

  @Override
  public String toJson() {
    Gson gson = new GsonBuilder().disableInnerClassSerialization().setPrettyPrinting().create();
    return gson.toJson(getTimeLine());
  }

  @Override
  public synchronized long addMemoryFact(MemoryFact fact) {
    memory.add(fact);
    return (long) memory.size();
  }

  @Override
  public String getMessageContext() {
    Gson gson = new GsonBuilder().disableInnerClassSerialization().setPrettyPrinting().create();
    return gson.toJson(this);
  }

  @Override
  public Meme getReminiscence() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Meme fromJson(String json) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toBase64() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Meme fromBase64(String base64) {
    // TODO Auto-generated method stub
    return null;
  }

}
