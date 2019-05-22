package org.ar4k.agent.tunnels.http.grpc;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;
import org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Sample client code that makes gRPC calls to the server.
 */
public class BeaconClient implements Runnable {
  private static final Logger logger = Logger.getLogger(BeaconClient.class.getName());

  private final ManagedChannel channel;
  private final RpcServiceV1BlockingStub blockingStub;
  private final RpcServiceV1Stub asyncStub;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final String secretKey = UUID.randomUUID().toString();
  // private int pollingFreq = 1000;
  // private String registerCode = null;

  private boolean running = false;

  private Thread process = null;

  private Agent me = null;

  private RpcConversation localExecutor = new RpcConversation();

  private List<RemoteBeaconRpcExecutor> remoteExecutors = new ArrayList<>();

  public BeaconClient(RpcConversation rpcConversation, String host, int port) {
    this(rpcConversation, ManagedChannelBuilder.forAddress(host, port).usePlaintext());
  }

  public BeaconClient(RpcConversation rpcConversation, ManagedChannelBuilder<?> channelBuilder) {
    this.localExecutor = rpcConversation;
    channel = channelBuilder.build();
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
    asyncStub = RpcServiceV1Grpc.newStub(channel);
    logger.info("Client Beacon started, connected state " + channel.getState(true));
    if (process == null) {
      process = new Thread(this);
      process.start();
    }
    running = true;
  }

  public RemoteBeaconRpcExecutor getRemoteExecutor(Agent agent) {
    // TODO
    return null;
  }

  public List<Agent> listAgentsConnectedToBeacon() {
    // TODO
    return null;
  }

  public void shutdown() throws InterruptedException {
    running = false;
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public ConnectivityState getStateConnection() {
    return channel.getState(true);
  }

  public String registerToBeacon(String uniqueName) throws IOException, InterruptedException, ParseException {
    RegisterRequest request;
    String result = "BAD";
    long timeRequest = new Date().getTime();
    request = RegisterRequest.newBuilder().setJsonHealth(gson.toJson(HardwareHelper.getSystemInfo()))
        .setSecretKey(secretKey).setName(uniqueName).setTime(Timestamp.newBuilder().setSeconds(timeRequest)).build();
    RegisterReply reply = blockingStub.register(request);
    me = Agent.newBuilder().setAgentUniqueName(reply.getRegisterCode())
        .setPollingFrequency(reply.getMonitoringFrequency()).setTimestampRegistration(timeRequest).build();
    result = reply.getResult().name();
    return result;
  }

  public int getPollingFreq() {
    return me.getPollingFrequency();
  }

  @Override
  public void run() {
    while (running) {
      try {
        checkPollChannel();
        sendHardwareInfo();
        Thread.sleep((long) getPollingFreq());
      } catch (InterruptedException e) {
        logger.info("in Beacon client loop " + e.getMessage());
      }
    }
  }

  private void checkPollChannel() {
    elaborateRequestFromBus(blockingStub.polling(me));
  }

  private void elaborateRequestFromBus(FlowMessage messages) {
    for (RequestToAgent m : messages.getToDoListList()) {
      switch (m.getType()) {
      case COMPLETE_COMMAND:
        CompletionContext cc = new CompletionContext();
        
        List<CompletionProposal> listProposal = localExecutor.complete(context)
        CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
            .setAgentSender(me).setUniqueIdRequest(m.getUniqueIdRequest()).build();
        blockingStub.sendCommandReply(request);
        break;
      case ELABORATE_MESSAGE_COMMAND:
        break;
      case EXPOSE_PORT:
        break;
      case LIST_COMMANDS:
        break;
      case OPEN_PROXY_SOCKS:
        break;
      case UNRECOGNIZED:
        break;
      default:
        break;
      }
    }
  }

  private void sendHardwareInfo() {
    // TODO
  }

  public void sendLoggerLine(String message, String level) {
    // TODO
  }

  public void sendException(Exception message) {
    // TODO
  }

  public String getAgentUniqueName() {
    return me.getAgentUniqueName();
  }

}
