package org.ar4k.agent.rpc;

import java.util.List;
import java.util.Map;

import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

public interface RpcExecutor extends AutoCloseable {

  public String elaborateMessage(String message);

  public RpcMessage<? extends String> elaborateMessage(RpcMessage<? extends String> message);

  public Map<String, MethodTarget> listCommands();

  public List<CompletionProposal> complete(CompletionContext context);

  public void setHomunculus(Homunculus homunculus);

}
