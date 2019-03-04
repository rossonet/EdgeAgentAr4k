
package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntitySynonyms {
  private Map<String, Collection<String>> entitySynonyms = new HashMap<String, Collection<String>>();

  public Map<String, Collection<String>> getEntitySynonyms() {
    return entitySynonyms;
  }

  public void setEntitySynonyms(Map<String, Collection<String>> entitySynonymsTable) {
    this.entitySynonyms = entitySynonymsTable;
  }

  public Collection<String> getSynonyms(String entity) {
    return entitySynonyms.get(entity);
  }

  public void addSynonym(String entity, String text) {
    entitySynonyms.get(entity).add(text);
  }

  public void addSynonyms(String entity, Collection<String> synonyms) {
    entitySynonyms.put(entity, synonyms);
  }
}
