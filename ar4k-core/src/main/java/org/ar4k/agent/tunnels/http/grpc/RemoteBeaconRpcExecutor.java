package org.ar4k.agent.tunnels.http.grpc;

import java.util.List;
import java.util.Map;

import org.ar4k.agent.rpc.Homunculus;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.RpcMessage;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

public class RemoteBeaconRpcExecutor implements RpcExecutor {
  
  private RemoteBeaconAgentHomunculus remoteHomunculus = null;

  @Override
  public String elaborateMessage(String message) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RpcMessage<? extends String> elaborateMessage(RpcMessage<? extends String> message) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, MethodTarget> listCommands() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<CompletionProposal> complete(CompletionContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setHomunculus(Homunculus homunculus) {
    // TODO Auto-generated method stub

  }

}
