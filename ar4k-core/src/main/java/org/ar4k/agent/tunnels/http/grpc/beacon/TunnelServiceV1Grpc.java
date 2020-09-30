package org.ar4k.agent.tunnels.http.grpc.beacon;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: ar4k_beacon.proto")
public final class TunnelServiceV1Grpc {

  private TunnelServiceV1Grpc() {}

  public static final String SERVICE_NAME = "beacon.TunnelServiceV1";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage,
      org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> getOpenNetworkChannelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "openNetworkChannel",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage,
      org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> getOpenNetworkChannelMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage, org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> getOpenNetworkChannelMethod;
    if ((getOpenNetworkChannelMethod = TunnelServiceV1Grpc.getOpenNetworkChannelMethod) == null) {
      synchronized (TunnelServiceV1Grpc.class) {
        if ((getOpenNetworkChannelMethod = TunnelServiceV1Grpc.getOpenNetworkChannelMethod) == null) {
          TunnelServiceV1Grpc.getOpenNetworkChannelMethod = getOpenNetworkChannelMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage, org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.TunnelServiceV1", "openNetworkChannel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new TunnelServiceV1MethodDescriptorSupplier("openNetworkChannel"))
                  .build();
          }
        }
     }
     return getOpenNetworkChannelMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage,
      org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel> getRequestTunnelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RequestTunnel",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage,
      org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel> getRequestTunnelMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage, org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel> getRequestTunnelMethod;
    if ((getRequestTunnelMethod = TunnelServiceV1Grpc.getRequestTunnelMethod) == null) {
      synchronized (TunnelServiceV1Grpc.class) {
        if ((getRequestTunnelMethod = TunnelServiceV1Grpc.getRequestTunnelMethod) == null) {
          TunnelServiceV1Grpc.getRequestTunnelMethod = getRequestTunnelMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage, org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.TunnelServiceV1", "RequestTunnel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel.getDefaultInstance()))
                  .setSchemaDescriptor(new TunnelServiceV1MethodDescriptorSupplier("RequestTunnel"))
                  .build();
          }
        }
     }
     return getRequestTunnelMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TunnelServiceV1Stub newStub(io.grpc.Channel channel) {
    return new TunnelServiceV1Stub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TunnelServiceV1BlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TunnelServiceV1BlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TunnelServiceV1FutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TunnelServiceV1FutureStub(channel);
  }

  /**
   */
  public static abstract class TunnelServiceV1ImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> openNetworkChannel(
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getOpenNetworkChannelMethod(), responseObserver);
    }

    /**
     */
    public void requestTunnel(org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestTunnelMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getOpenNetworkChannelMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage,
                org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage>(
                  this, METHODID_OPEN_NETWORK_CHANNEL)))
          .addMethod(
            getRequestTunnelMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage,
                org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel>(
                  this, METHODID_REQUEST_TUNNEL)))
          .build();
    }
  }

  /**
   */
  public static final class TunnelServiceV1Stub extends io.grpc.stub.AbstractStub<TunnelServiceV1Stub> {
    private TunnelServiceV1Stub(io.grpc.Channel channel) {
      super(channel);
    }

    private TunnelServiceV1Stub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TunnelServiceV1Stub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TunnelServiceV1Stub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> openNetworkChannel(
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getOpenNetworkChannelMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void requestTunnel(org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestTunnelMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TunnelServiceV1BlockingStub extends io.grpc.stub.AbstractStub<TunnelServiceV1BlockingStub> {
    private TunnelServiceV1BlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TunnelServiceV1BlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TunnelServiceV1BlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TunnelServiceV1BlockingStub(channel, callOptions);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel requestTunnel(org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage request) {
      return blockingUnaryCall(
          getChannel(), getRequestTunnelMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TunnelServiceV1FutureStub extends io.grpc.stub.AbstractStub<TunnelServiceV1FutureStub> {
    private TunnelServiceV1FutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TunnelServiceV1FutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TunnelServiceV1FutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TunnelServiceV1FutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel> requestTunnel(
        org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestTunnelMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST_TUNNEL = 0;
  private static final int METHODID_OPEN_NETWORK_CHANNEL = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TunnelServiceV1ImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TunnelServiceV1ImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_TUNNEL:
          serviceImpl.requestTunnel((org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_OPEN_NETWORK_CHANNEL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.openNetworkChannel(
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TunnelServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TunnelServiceV1BaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TunnelServiceV1");
    }
  }

  private static final class TunnelServiceV1FileDescriptorSupplier
      extends TunnelServiceV1BaseDescriptorSupplier {
    TunnelServiceV1FileDescriptorSupplier() {}
  }

  private static final class TunnelServiceV1MethodDescriptorSupplier
      extends TunnelServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TunnelServiceV1MethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TunnelServiceV1Grpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TunnelServiceV1FileDescriptorSupplier())
              .addMethod(getOpenNetworkChannelMethod())
              .addMethod(getRequestTunnelMethod())
              .build();
        }
      }
    }
    return result;
  }
}
