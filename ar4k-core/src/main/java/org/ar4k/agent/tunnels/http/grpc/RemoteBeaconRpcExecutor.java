package org.ar4k.agent.tunnels.http.grpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ar4k.agent.rpc.Homunculus;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.RpcMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

public class RemoteBeaconRpcExecutor implements RpcExecutor {

  private RemoteBeaconAgentHomunculus remoteHomunculus = null;
  private Agent me = null;
  private RpcServiceV1BlockingStub blockingStub = null;

  public RemoteBeaconRpcExecutor(Agent me, RemoteBeaconAgentHomunculus remoteHomunculus,
      RpcServiceV1BlockingStub blockingStub) {
    this.me = me;
    this.remoteHomunculus = remoteHomunculus;
    this.blockingStub = blockingStub;
  }

  @Override
  public String elaborateMessage(String message) {
    ElaborateMessageRequest request = ElaborateMessageRequest.newBuilder().setAgentSender(me)
        .setAgentTarget(remoteHomunculus.getRemoteAgent()).setCommandMessage(message).build();
    ElaborateMessageReply reply = blockingStub.elaborateMessage(request);
    return reply.getReply();
  }

  @Override
  public RpcMessage<? extends String> elaborateMessage(RpcMessage<? extends String> message) {
    // TODO valutare l'implementazione del cmd su RemoteBeaconExecutor via Spring
    // message
    return null;
  }

  @Override
  public Map<String, MethodTarget> listCommands() {
    ListCommandsRequest request = ListCommandsRequest.newBuilder().setAgentSender(me)
        .setAgentTarget(remoteHomunculus.getRemoteAgent()).build();
    ListCommandsReply reply = blockingStub.listCommands(request);
    Map<String, MethodTarget> formattedReply = new HashMap<>();
    for (Command r : reply.getCommandsList()) {
      formattedReply.put(r.getCommand(), null);
    }
    return formattedReply;
  }

  @Override
  public List<CompletionProposal> complete(CompletionContext context) {
    CompleteCommandRequest request = CompleteCommandRequest.newBuilder().setAgentSender(me)
        .setAgentTarget(remoteHomunculus.getRemoteAgent()).setPosition(context.getPosition())
        .setWordIndex(context.getWordIndex()).addAllWords(context.getWords()).build();
    CompleteCommandReply reply = blockingStub.completeCommand(request);
    List<CompletionProposal> formattedReply = new ArrayList<>();
    for (String cp : reply.getRepliesList()) {
      CompletionProposal sp = new CompletionProposal(cp);
      formattedReply.add(sp);
    }
    return formattedReply;
  }

  @Override
  public void setHomunculus(Homunculus homunculus) {
    setRemoteHomunculus((RemoteBeaconAgentHomunculus) homunculus);
  }

  public RemoteBeaconAgentHomunculus getRemoteHomunculus() {
    return remoteHomunculus;
  }

  private void setRemoteHomunculus(RemoteBeaconAgentHomunculus remoteHomunculus) {
    this.remoteHomunculus = remoteHomunculus;
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub
    
  }

}
