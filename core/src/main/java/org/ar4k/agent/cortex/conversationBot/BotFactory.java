package org.ar4k.agent.cortex.conversationBot;

import com.google.gson.Gson;

/**
 * Implementa le classi statiche per creare un bot da un file di configurazione
 * (Json,XML) o tramite annotazione (da implementare in futuro)
 * 
 * @author andrea
 *
 */

public class BotFactory {

  /**
   * ritorna un Bot da una configurazione
   * 
   * @param configuration
   * @return
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @SuppressWarnings("unchecked")
  public static BrocaBot<AbstractBotConfiguration> getBotFromAbstractBotConfiguration(String botClass,
      AbstractBotConfiguration configuration)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Class<?> clazz = Class.forName(botClass);
    BrocaBot<AbstractBotConfiguration> bot = (BrocaBot<AbstractBotConfiguration>) clazz.newInstance();
    bot.setConfig(configuration);
    return bot;
  }

  /**
   * ritorna un Bot da una configurazione
   * 
   * @param configuration
   * @return
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public static BrocaBot<AbstractBotConfiguration> getBotFromJsonConfiguration(String botClass, String configuration)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Gson gson = new Gson();
    AbstractBotConfiguration abstractBotConfiguration = gson.fromJson(configuration, AbstractBotConfiguration.class);
    return getBotFromAbstractBotConfiguration(botClass, abstractBotConfiguration);
  }

  /**
   * Ritorna un bot dalle classi annotate
   * 
   * @param basePackage
   * @return
   */
  @SuppressWarnings("unchecked")
  public static BrocaBot<AbstractBotConfiguration> getBotFromAnnotation(String botClass, String basePackage)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Class<?> clazz = Class.forName(botClass);
    BrocaBot<AbstractBotConfiguration> bot = (BrocaBot<AbstractBotConfiguration>) clazz.newInstance();
    // TODO: implementare da un package parent con annotazioni la configurazione di
    // un bot
    return bot;
  }

}
