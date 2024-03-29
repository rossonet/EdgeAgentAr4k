package org.ar4k.agent.rpc;

import java.util.List;
import java.util.Map;

import org.ar4k.agent.core.data.messages.EdgeMessage;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

/**
 * interfaccia da implementare per scrivere un componente RPC
 * 
 * @author andrea
 *
 */
public interface RpcExecutor extends AutoCloseable {

  String elaborateMessage(String message);

  EdgeMessage<? extends String> elaborateMessage(EdgeMessage<? extends String> message);

  Map<String, MethodTarget> listCommands();

  List<CompletionProposal> complete(CompletionContext context);

  void setHomunculus(IHomunculusRpc homunculusRpc);

}
