/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ar4k.agent.tunnels.http.grpc;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Sample client code that makes gRPC calls to the server.
 */
public class BeaconClient {
  private static final Logger logger = Logger.getLogger(BeaconClient.class.getName());

  private final ManagedChannel channel;
  private final RpcServiceV1BlockingStub blockingStub;
  private final RpcServiceV1Stub asyncStub;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final String secretKey = UUID.randomUUID().toString();
  private int pollingFreq = 1000;
  private String registerCode = null;

  public BeaconClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
  }

  public BeaconClient(ManagedChannelBuilder<?> channelBuilder) {
    channel = channelBuilder.build();
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
    asyncStub = RpcServiceV1Grpc.newStub(channel);
    logger.info("Client Beacon started, connected state " + channel.getState(true));
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public ConnectivityState getStateConnection() {
    return channel.getState(true);
  }

  public String registerToBeacon(String uniqueName) {
    RegisterRequest request;
    String result = "BAD";
    try {
      request = RegisterRequest.newBuilder().setJsonHealth(gson.toJson(HardwareHelper.getSystemInfo()))
          .setSecretKey(secretKey).setName(uniqueName).build();
      RegisterReply reply = null;
      reply = blockingStub.register(request);
      pollingFreq = reply.getMonitoringFrequency();
      registerCode = reply.getRegisterCode();
      result = reply.getResult().name();
    } catch (IOException | InterruptedException | ParseException e) {
      e.printStackTrace();
    }
    return result;
  }

  public int getPollingFreq() {
    return pollingFreq;
  }

  public void setPollingFreq(int pollingFreq) {
    this.pollingFreq = pollingFreq;
  }

  public String getRegisterCode() {
    return registerCode;
  }

}
