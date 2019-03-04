package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.Map;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

public interface IntentMatched extends Meme {

  public CortexMessage getMessage();

  public Map<String, String> getAllEntities();

  public String getDebugData();

  public Intent getIntent();

  public Double getScore();

  public void setIntent(Intent intent);

  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getChild();

}
