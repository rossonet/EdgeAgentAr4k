package org.ar4k.agent.cortex.conversationBot.messages;

import org.ar4k.agent.cortex.memory.TimeContextConversation;

/**
 * 
 * rappresenta un fatto collegato a un timeline
 * 
 * @author andrea
 *
 */
public interface ConversationFact extends MemoryFact {

  public TimeContextConversation getContext();

  public void setContext(TimeContextConversation context);
}
