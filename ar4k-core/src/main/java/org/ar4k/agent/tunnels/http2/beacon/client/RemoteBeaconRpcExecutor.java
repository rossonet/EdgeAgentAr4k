package org.ar4k.agent.tunnels.http2.beacon.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ar4k.agent.core.data.messages.EdgeMessage;
import org.ar4k.agent.rpc.IHomunculusRpc;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

public class RemoteBeaconRpcExecutor implements IRemoteBeaconRpcExecutor {

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
	public void close() throws Exception {
		if (remoteHomunculus != null) {
			remoteHomunculus.close();
			remoteHomunculus = null;
		}
		me = null;
		blockingStub = null;
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
	public EdgeMessage<? extends String> elaborateMessage(EdgeMessage<? extends String> message) {
		// TO______DO valutare l'implementazione del cmd su RemoteBeaconExecutor via Spring
		// message
		return null;
	}

	@Override
	public String elaborateMessage(String message) {
		ElaborateMessageRequest request = ElaborateMessageRequest.newBuilder().setAgentSender(me)
				.setAgentTarget(remoteHomunculus.getRemoteAgent()).setCommandMessage(message).build();
		ElaborateMessageReply reply = blockingStub.elaborateMessage(request);
		return reply.getReply();
	}

	public RemoteBeaconAgentHomunculus getRemoteHomunculus() {
		return remoteHomunculus;
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
	public void setHomunculus(IHomunculusRpc homunculusRpc) {
		setRemoteHomunculus((RemoteBeaconAgentHomunculus) homunculusRpc);
	}

	private void setRemoteHomunculus(RemoteBeaconAgentHomunculus remoteHomunculus) {
		this.remoteHomunculus = remoteHomunculus;
	}

}
