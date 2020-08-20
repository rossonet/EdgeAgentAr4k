package org.ar4k.agent.cortex.opennlp.bot.intents;

import java.net.URL;
import java.util.HashMap;

import org.ar4k.agent.cortex.opennlp.core.Entity;
import org.ar4k.agent.cortex.opennlp.core.EntityMatch;
import org.ar4k.agent.cortex.opennlp.core.EntityMatcher;
import org.ar4k.agent.cortex.opennlp.core.Intent;
import org.ar4k.agent.cortex.opennlp.core.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Implements a SlotMatcher that uses OpenNLP's NER framework.
 *
 * @author rabidgremlin
 *
 */
public class OpenNLPEntityMatcher implements EntityMatcher {
  /** Logger. */
  private Logger log = LoggerFactory.getLogger(OpenNLPEntityMatcher.class);

  /** Map of NER models. */
  private HashMap<String, TokenNameFinderModel> nerModels = new HashMap<>();

  /** Map of slot models. These share the NER models. */
  private HashMap<String, TokenNameFinderModel> entityModels = new HashMap<>();

  /** The tokenizer to use. */
  private Tokenizer tokenizer;

  /**
   * Constructor. Allows tokenizer to be supplied because NER can use case etc as
   * cues, so may require different tokenizer than used for intent matching.
   *
   * @param tokenizer The tokenizer to use on an utterance for NER.
   */
  public OpenNLPEntityMatcher(Tokenizer tokenizer) {
    this.tokenizer = tokenizer;
  }

  /**
   * This set the NER model to use for a slot.
   *
   * @param entityName The name of the slot. Should match the name of slots on
   *                   intents added to the matcher.
   * @param nerModel   The file name of the NER model. This file must be on the
   *                   classpath.
   */
  public void addEntityModel(String entityName, String nerModel) {
    TokenNameFinderModel tnfModel = nerModels.get(nerModel);
    if (tnfModel == null) {
      try {
        URL modelUrl = Thread.currentThread().getContextClassLoader().getResource(nerModel);
        tnfModel = new TokenNameFinderModel(modelUrl);
      } catch (Exception e) {
        throw new IllegalArgumentException("Unable to load NER model", e);
      }
    }

    entityModels.put(entityName.toLowerCase(), tnfModel);
  }

  @Override
  public HashMap<Entity, EntityMatch> match(Intent intent, String utterance) {
    String[] utteranceTokens = tokenizer.tokenize(utterance);

    HashMap<Entity, EntityMatch> matchedEntities = new HashMap<>();

    for (Entity entity : intent.getEntities()) {
      log.debug("Looking for entity {}", entity.getName());

      TokenNameFinderModel tnfModel = entityModels.get(entity.getName().toLowerCase());
      if (tnfModel == null) {
        log.warn("Could not find NER model for entity {}", entity.getName());
        continue;
      }

      NameFinderME nameFinder = new NameFinderME(tnfModel);
      Span[] spans = nameFinder.find(utteranceTokens);

      if (spans.length > 0) {
        String[] matches = Span.spansToStrings(spans, utteranceTokens);

        log.debug("Matching for {} against {}", entity.getName(), matches);

        // TODO what to do with multi matches?
        EntityMatch match = entity.match(matches[0]);
        if (match != null) {
          matchedEntities.put(entity, match);
          log.debug("Match found {}", match);
        } else {
          log.debug("No Match found entity: {} text: {} ", entity.getName(), matches);
        }
        /*
         * } else if (entity instanceof DefaultValueEntity) { Object defaultValue =
         * ((DefaultValueEntity) entity).getDefaultValue(); matchedEntities.put(entity,
         * new EntityMatch(entity, utterance, defaultValue));
         * log.debug("No Match found entity: {} Using default value: {} ",
         * entity.getName(), defaultValue);
         */
      } else {
        log.debug("Did not find entity {} utteranceTokens {} ", entity.getName(), utteranceTokens);
      }
    }

    return matchedEntities;
  }

}
