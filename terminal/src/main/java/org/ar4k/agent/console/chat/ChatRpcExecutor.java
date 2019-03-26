package org.ar4k.agent.console.chat;

public interface ChatRpcExecutor {

  public String elaborateMessage(String message);

  public ChatRpcMessage<? extends String> elaborateMessage(ChatRpcMessage<? extends String> message);

}
