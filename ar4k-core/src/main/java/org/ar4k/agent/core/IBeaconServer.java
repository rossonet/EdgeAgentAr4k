package org.ar4k.agent.core;

import java.io.IOException;
import java.util.List;

import org.ar4k.agent.tunnels.http.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http.beacon.socket.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest;

public interface IBeaconServer {

  void start() throws IOException;

  void stop();

  void blockUntilShutdown() throws InterruptedException;

  String getStatus();

  List<TunnelRunnerBeaconServer> getTunnels();

  CommandReplyRequest waitReply(String idRequest, long defaultTimeOut) throws InterruptedException;

  boolean isStopped();

  int getPort();

  int getDefaultPollTime();

  void setDefaultPollTime(int defaultPollTime);

  List<BeaconAgent> getAgentLabelRegisterReplies();

  void sendFlashUdp();

  int getDefaultBeaconFlashMoltiplicator();

  void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator);

  int getDiscoveryPort();

  void setDiscoveryPort(int discoveryPort);

  String getBroadcastAddress();

  void setBroadcastAddress(String broadcastAddress);

  String getStringDiscovery();

  void setStringDiscovery(String stringDiscovery);

  boolean isAcceptAllCerts();

  String getCertChainFile();

  String getPrivateKeyFile();

  void close();

  void clearOldData();

}