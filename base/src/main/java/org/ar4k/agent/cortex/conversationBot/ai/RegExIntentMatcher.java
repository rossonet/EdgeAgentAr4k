package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ar4k.agent.cortex.conversationBot.exceptions.IntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;

public class RegExIntentMatcher implements IntentMatcher {
  private Set<RegExIntent> intents = new HashSet<RegExIntent>();
  private Map<RegExIntent, Set<Pattern>> intentPatterns = new HashMap<RegExIntent, Set<Pattern>>();

  private boolean trained = false;
  private EntitySynonyms entities = new EntitySynonyms();

  @Override
  public boolean isOnline() {
    return true;
  }

  @Override
  public boolean isTrained() {
    return trained;
  }

  @Override
  public synchronized void train() throws TrainingIntentMatcherException {
    try {
      intentPatterns = new HashMap<RegExIntent, Set<Pattern>>();
      for (RegExIntent intent : intents) {
        Set<Pattern> patterns = new HashSet<Pattern>();
        for (String regEx : intent.getExamples()) {
          patterns.add(Pattern.compile(regEx));
        }
        intentPatterns.put(intent, patterns);
      }
      trained = true;
    } catch (Exception ee) {
      throw new TrainingIntentMatcherException(ee.getMessage());
    }
  }

  @Override
  public List<IntentMatched> parse(CortexMessage message) throws IntentMatcherException {
    List<IntentMatched> ritorno = new ArrayList<IntentMatched>();
    try {
      for (RegExIntent chiave : intentPatterns.keySet()) {
        RegExIntentMatched found = null;
        for (Pattern regex : intentPatterns.get(chiave)) {
          Matcher matcher = regex.matcher(message.getMessage());
          if (matcher.find()) {
            if (found == null) {
              found = new RegExIntentMatched();
            }
            found.addMacher(matcher.toMatchResult());
            //System.out.println("matcher -> " + matcher.toMatchResult());
            found.setIntent(chiave);
          }
        }
        if (found != null) {
          ritorno.add(found);
        }
      }
    } catch (Exception ee) {
      ee.printStackTrace();
      throw new IntentMatcherException(ee.getMessage());
    }
    return ritorno;
  }

  @Override
  public Collection<Intent> getIntents() {
    Collection<Intent> target = new HashSet<Intent>();
    for (RegExIntent e : intents) {
      target.add(e);
    }
    return target;
  }

  @Override
  public void addIntent(Intent intent) {
    intents.add((RegExIntent) intent);
  }

  @Override
  public void delIntent(Intent intent) {
    intents.add((RegExIntent) intent);
  }

  @Override
  public EntitySynonyms getEntities() {
    return entities;
  }

}
