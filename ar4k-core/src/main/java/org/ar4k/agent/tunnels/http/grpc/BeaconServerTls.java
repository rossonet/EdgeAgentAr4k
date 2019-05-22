package org.ar4k.agent.tunnels.http.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;

public class BeaconServerTls {
  private static final Logger logger = Logger.getLogger(BeaconServerTls.class.getName());

  private Server server;

  private final int port;
  private final String certChainFilePath;
  private final String privateKeyFilePath;
  private final String trustCertCollectionFilePath;

  public BeaconServerTls(int port, String certChainFilePath, String privateKeyFilePath,
      String trustCertCollectionFilePath) {
    this.port = port;
    this.certChainFilePath = certChainFilePath;
    this.privateKeyFilePath = privateKeyFilePath;
    this.trustCertCollectionFilePath = trustCertCollectionFilePath;
  }

  private SslContextBuilder getSslContextBuilder() {
    SslContextBuilder sslClientContextBuilder = SslContextBuilder.forServer(new File(certChainFilePath),
        new File(privateKeyFilePath));
    if (trustCertCollectionFilePath != null) {
      sslClientContextBuilder.trustManager(new File(trustCertCollectionFilePath));
      sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
    }
    return GrpcSslContexts.configure(sslClientContextBuilder);
  }

  public void start() throws IOException {
    server = NettyServerBuilder.forPort(port).addService(new GreeterImpl()).sslContext(getSslContextBuilder().build())
        .build().start();
    logger.info("Server Beacon started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown
        // hook.
        System.err.println("*** shutting down Beacon server since JVM is shutting down");
        BeaconServerTls.this.stop();
        System.err.println("*** server Beacon shut down");
      }
    });
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /*
   * public static void main(String[] args) throws IOException,
   * InterruptedException {
   * 
   * if (args.length < 3 || args.length > 4) { System.out.
   * println("USAGE: HelloWorldServerTls port certChainFilePath privateKeyFilePath "
   * +
   * "[trustCertCollectionFilePath]\n  Note: You only need to supply trustCertCollectionFilePath if you want "
   * + "to enable Mutual TLS."); System.exit(0); }
   * 
   * final BeaconServer server = new BeaconServer(Integer.parseInt(args[0]),
   * args[1], args[2], args.length == 4 ? args[3] : null); server.start();
   * server.blockUntilShutdown(); }
   */

  static class GreeterImpl extends RpcServiceV1Grpc.RpcServiceV1ImplBase {
/*
    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
    */
  }
}
