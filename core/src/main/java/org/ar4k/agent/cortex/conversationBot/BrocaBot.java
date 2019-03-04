package org.ar4k.agent.cortex.conversationBot;

import java.util.Collection;
import java.util.Map;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusBootException;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.LockedHomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.helper.SymbolicLinksTree;
import org.joda.time.Instant;
import org.springframework.messaging.MessageHandler;

/**
 * interfaccia per il bot. Mantiene lo stato del Bot come insieme di nodi e per
 * mette il suo utilizzo. E' anche l'interfaccia di collegamento per utilizzare
 * un bot come servizio.
 * 
 * Per retrocompatibilità è implementato come BOT - multi NLU e BOT full Watson
 * 
 * @author andrea
 *
 */
public interface BrocaBot<T extends Meme> extends MessageHandler {

  /**
   * Rappresenta l'albero dei nodi
   * 
   * @return
   */

  /**
   * ritorna il nome proprio del bot
   * 
   * @return
   */
  public String getBotName();

  /**
   * ritorna la descrizione delle competenze del BOT
   * 
   * @return
   */
  public String getBotDescription();

  /**
   * Costruisce il boot dalla configurazione in runtime
   * 
   * @throws HomunculusBootException
   * @throws TrainingIntentMatcherException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws LockedHomunculusException
   * @throws SecurityException
   * 
   */
  public void bootstrapBot() throws BotBootException, HomunculusBootException, TrainingIntentMatcherException;

  /**
   * Ritorna la configurazioni attuale
   */
  public T getConfig();

  public String getConfigAsJson();

  /**
   * setta la configurazione
   */
  public void setConfig(T configuration);

  /**
   * controlla se la configurazione è formalmente accettabile, eventualmente
   * ritorna una lista di errori
   * 
   * @param configuration
   * @return
   */
  public Collection<String> checkConfig(T configuration);

  // TODO: implementare i contatori con libreria Spring

  /**
   * invia un messaggio attendendo una risposta
   * 
   * @param utterance
   * @return
   * @throws HomunculusException
   */
  public CortexMessage query(CortexMessage utterance) throws HomunculusException;

  /**
   * sottopone un messaggio senza attendere una risposta
   * 
   * @param utterance
   * @return
   * @throws HomunculusException
   */
  public void submit(CortexMessage utterance) throws HomunculusException;

  public Map<String, Object> getBotStorageMap();

  public Collection<String> getBotStorageKeys();

  public Object getBotStorageObject(String key);

  public Object addBotStorageObject(String key, Object data);

  public void runtimeDataFromConfig();

  public void configDataFromRuntime();

  /**
   * controlla lo stato del bot senza chiedere esecuzione di controlli
   */
  public boolean isOnline();

  /**
   * esegue il controllo del bot e ritorna il risultato
   * 
   * @return
   */
  public boolean selfTest();

  /**
   * il Bot passivo risponde solo se invocato; il bot attivo invia messaggi verso
   * i canali noti alla sola chiamata per avviare le elaborazioni
   */
  public boolean isActiveBot();

  /**
   * chiama l'aggiornamento delle attività per i bot active
   */
  public void nextStep();

  /**
   * Ritorna l'ultima volta che il bot ha eseguito lavoro
   */
  public Instant getLastRunTime();

  /**
   * reset del boot (cancella la storia delle conversazioni)
   */
  public void reset();

  /**
   * accesso amministrativo al bot
   * 
   * @throws HomunculusException
   * 
   */
  public CortexMessage consoleGodQuery(CortexMessage adminMessage) throws HomunculusException;

  public String consoleGodQuery(String adminMessage) throws HomunculusException;

  public SymbolicLinksTree<? extends AbstractHomunculusConfiguration> getRuntimeSymbolicLinksTree();

  public void setRuntimeSymbolicLinksTree(SymbolicLinksTree<? extends AbstractHomunculusConfiguration> tree);

  public AbstractHomunculusConfiguration getBootNodeConfig();

  public void setBootNodeConfig(AbstractHomunculusConfiguration bootNode);

  public AbstractHomunculusConfiguration newHomunculusConfiguration(String name) throws BotBootException;

  public Class<? extends AbstractHomunculusConfiguration> homunculusConfigurationClass();

}
