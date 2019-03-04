package org.ar4k.agent.cortex.memory;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.OntologyTag;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.messages.MemoryFact;
import org.joda.time.Instant;

public interface TimeContextConversation extends Meme {
  // gestisce il ritmo della conversazione
  // e serve per bilanciare le richieste di dati da parte degli attori
  public static enum Focus {
    RELAXED, // la conversazione è "tanto per parlare" o su convenevoli nessuno stato
             // particolare viene salvato
    FOCUS, // la conversazione è gestita da una maschera specifica con obbiettivi specifici
    IPNOSI, // la conversazione si svolge su una sola maschera senza intervento del master
            // (ottimo per console di comando in maschera e comunicazione machine to machine
            // )
    JOKE, // conversazione frizzante, sproloqui, etc (inibita con certi utenti)
    SPAM, // l'interlocutore è marcato come spammer (può essere per esaurimento del
          // credito o segnalazione)
    RETROSPECTIVE, // per ogni operazione il bot devo considerare tutta la conversazione. Stato per
                   // l'estrazione dei dati per la rimebranza
    WAIT_HUMAN_OPERATOR // in attesa di una maschera con attore umano
  }

  public String getUniqueInterlocutor();

  public void setUniqueInterlocutor(String uniqueInterlocutor);

  public Focus focusNow();

  public boolean changeFocus(Focus newFocus);

  public String getShortDescription();

  public List<? extends CortexMessage> getTrascription();

  public List<? extends CortexMessage> getTrascription(MemoryFact from, MemoryFact to);

  public List<? extends CortexMessage> getTrascription(Instant from, Instant to);

  public List<? extends CortexMessage> getTrascription(Instant from, MemoryFact to);

  public List<? extends CortexMessage> getTrascription(MemoryFact from, Instant to);

  public TimeContextConversation getSection(MemoryFact from, MemoryFact to);

  public TimeContextConversation getSection(Instant from, Instant to);

  public TimeContextConversation getSection(Instant from, MemoryFact to);

  public TimeContextConversation getSection(MemoryFact from, Instant to);

  public TimeContextConversation getSection();

  public List<? extends MemoryFact> getTimeLine();

  public List<? extends MemoryFact> getTimeLine(Set<OntologyTag> filters);

  public CortexMessage getLastMessage();

  public CortexMessage getLastMessage(String sender);

  public TimeZone getTimeZone();

  public void setTimeZone(TimeZone timeZone);

  public Locale getLocale();

  public void setLocale(Locale locale);

  public Object getAttribute(String index);

  public void reset();

  public void removeAttribute(String index);

  public void addAttribute(String index, Object value);

  public void addLongTermAttribute(String index, Object value);

  public void removeLongTermAttribute(String index);

  public Object getLongTermAttribute(String index);

  public String toJson();

  public String getMessageContext();

  public Meme getReminiscence();

  public long addMemoryFact(MemoryFact fact);
}
