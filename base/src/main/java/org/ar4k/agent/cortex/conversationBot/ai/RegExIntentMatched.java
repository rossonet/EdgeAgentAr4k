package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

public class RegExIntentMatched implements IntentMatched {

  private static final long serialVersionUID = 259131618046011191L;

  List<MatchResult> matcher = new ArrayList<MatchResult>();

  private CortexMessage message = null;

  private Intent intent;

  @Override
  public CortexMessage getMessage() {
    return message;
  }

  public void setMessage(CortexMessage message) {
    this.message = message;
  }

  @Override
  public Map<String, String> getAllEntities() {
    Map<String, String> result = new HashMap<String, String>();
    for (int matchedExampleKey = 0; matchedExampleKey < matcher.size(); matchedExampleKey++) {
      for (int matchedKey = 0; matchedKey < matcher.get(matchedExampleKey).groupCount(); matchedKey++) {
        result.put(String.valueOf(matchedExampleKey) + "." + String.valueOf(matchedKey),
            matcher.get(matchedExampleKey).group());
      }
    }
    return result;
  }

  @Override
  public String getDebugData() {
    return matcher.toString();
  }

  public void addMacher(MatchResult regExmacher) {
    matcher.add(regExmacher);
  }

  public MatchResult getMacher(int index) {
    return matcher.get(index);
  }

  @Override
  public String toJson() {
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

  @Override
  public Intent getIntent() {
    return intent;
  }

  @Override
  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getChild() {
    return intent.getChild();
  }

  @Override
  public Double getScore() {
    return (double) matcher.size();
  }

  @Override
  public void setIntent(Intent intent) {
    this.intent = intent;
  }

}
