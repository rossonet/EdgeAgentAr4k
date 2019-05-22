package org.ar4k.agent.tunnels.http.grpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.SSLException;

import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;

public class BeaconClientTls {
  private static final Logger logger = Logger.getLogger(BeaconClientTls.class.getName());

  private final ManagedChannel channel;
  private final RpcServiceV1Grpc.RpcServiceV1BlockingStub blockingStub;

  /*
   * private static SslContext buildSslContext(String trustCertCollectionFilePath,
   * String clientCertChainFilePath, String clientPrivateKeyFilePath) throws
   * SSLException { SslContextBuilder builder = GrpcSslContexts.forClient(); if
   * (trustCertCollectionFilePath != null) { builder.trustManager(new
   * File(trustCertCollectionFilePath)); } if (clientCertChainFilePath != null &&
   * clientPrivateKeyFilePath != null) { builder.keyManager(new
   * File(clientCertChainFilePath), new File(clientPrivateKeyFilePath)); } return
   * builder.build(); }
   */

  public BeaconClientTls(String host, int port, SslContext sslContext) throws SSLException {
    this(NettyChannelBuilder.forAddress(host, port).sslContext(sslContext).build());
  }

  BeaconClientTls(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /*
   * public void greet(String name) { logger.info("Will try to greet " + name +
   * " ..."); HelloRequest request =
   * HelloRequest.newBuilder().setName(name).build(); HelloReply response; try {
   * response = blockingStub.sayHello(request); } catch (StatusRuntimeException e)
   * { logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus()); return; }
   * logger.info("Greeting: " + response.getMessage()); }
   */
  /*
   * public static void main(String[] args) throws Exception {
   * 
   * if (args.length < 2 || args.length == 4 || args.length > 5) { System.out.
   * println("USAGE: HelloWorldClientTls host port [trustCertCollectionFilePath] "
   * +
   * "[clientCertChainFilePath clientPrivateKeyFilePath]\n  Note: clientCertChainFilePath and "
   * + "clientPrivateKeyFilePath are only needed if mutual auth is desired.");
   * System.exit(0); }
   * 
   * BeaconClient client; switch (args.length) { case 2: client = new
   * BeaconClient(args[0], Integer.parseInt(args[1]), buildSslContext(null, null,
   * null)); break; case 3: client = new BeaconClient(args[0],
   * Integer.parseInt(args[1]), buildSslContext(args[2], null, null)); break;
   * default: client = new BeaconClient(args[0], Integer.parseInt(args[1]),
   * buildSslContext(args[2], args[3], args[4])); }
   * 
   * try { // Access a service running on the local machine on port 50051 String
   * user = "world"; if (args.length > 0) { user = args[0]; // Use the arg as the
   * name to greet if provided } client.greet(user); } finally {
   * client.shutdown(); } }
   */
}
