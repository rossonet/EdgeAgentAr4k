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
public final class PotServiceV1Grpc {

  private PotServiceV1Grpc() {}

  public static final String SERVICE_NAME = "beacon.PotServiceV1";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendEvent",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendEventMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendEventMethod;
    if ((getSendEventMethod = PotServiceV1Grpc.getSendEventMethod) == null) {
      synchronized (PotServiceV1Grpc.class) {
        if ((getSendEventMethod = PotServiceV1Grpc.getSendEventMethod) == null) {
          PotServiceV1Grpc.getSendEventMethod = getSendEventMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.PotServiceV1", "sendEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new PotServiceV1MethodDescriptorSupplier("sendEvent"))
                  .build();
          }
        }
     }
     return getSendEventMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> getPollingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Polling",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> getPollingMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> getPollingMethod;
    if ((getPollingMethod = PotServiceV1Grpc.getPollingMethod) == null) {
      synchronized (PotServiceV1Grpc.class) {
        if ((getPollingMethod = PotServiceV1Grpc.getPollingMethod) == null) {
          PotServiceV1Grpc.getPollingMethod = getPollingMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.PotServiceV1", "Polling"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot.getDefaultInstance()))
                  .setSchemaDescriptor(new PotServiceV1MethodDescriptorSupplier("Polling"))
                  .build();
          }
        }
     }
     return getPollingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> getSubscriptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Subscription",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> getSubscriptionMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> getSubscriptionMethod;
    if ((getSubscriptionMethod = PotServiceV1Grpc.getSubscriptionMethod) == null) {
      synchronized (PotServiceV1Grpc.class) {
        if ((getSubscriptionMethod = PotServiceV1Grpc.getSubscriptionMethod) == null) {
          PotServiceV1Grpc.getSubscriptionMethod = getSubscriptionMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.PotServiceV1", "Subscription"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot.getDefaultInstance()))
                  .setSchemaDescriptor(new PotServiceV1MethodDescriptorSupplier("Subscription"))
                  .build();
          }
        }
     }
     return getSubscriptionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PotServiceV1Stub newStub(io.grpc.Channel channel) {
    return new PotServiceV1Stub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PotServiceV1BlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PotServiceV1BlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PotServiceV1FutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PotServiceV1FutureStub(channel);
  }

  /**
   */
  public static abstract class PotServiceV1ImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendEvent(org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendEventMethod(), responseObserver);
    }

    /**
     */
    public void polling(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> responseObserver) {
      asyncUnimplementedUnaryCall(getPollingMethod(), responseObserver);
    }

    /**
     */
    public void subscription(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscriptionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendEventMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_SEND_EVENT)))
          .addMethod(
            getPollingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot>(
                  this, METHODID_POLLING)))
          .addMethod(
            getSubscriptionMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot>(
                  this, METHODID_SUBSCRIPTION)))
          .build();
    }
  }

  /**
   */
  public static final class PotServiceV1Stub extends io.grpc.stub.AbstractStub<PotServiceV1Stub> {
    private PotServiceV1Stub(io.grpc.Channel channel) {
      super(channel);
    }

    private PotServiceV1Stub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PotServiceV1Stub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PotServiceV1Stub(channel, callOptions);
    }

    /**
     */
    public void sendEvent(org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendEventMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void polling(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPollingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscription(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscriptionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PotServiceV1BlockingStub extends io.grpc.stub.AbstractStub<PotServiceV1BlockingStub> {
    private PotServiceV1BlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PotServiceV1BlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PotServiceV1BlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PotServiceV1BlockingStub(channel, callOptions);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status sendEvent(org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot polling(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getPollingMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> subscription(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscriptionMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PotServiceV1FutureStub extends io.grpc.stub.AbstractStub<PotServiceV1FutureStub> {
    private PotServiceV1FutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PotServiceV1FutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PotServiceV1FutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PotServiceV1FutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> sendEvent(
        org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendEventMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot> polling(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getPollingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_EVENT = 0;
  private static final int METHODID_POLLING = 1;
  private static final int METHODID_SUBSCRIPTION = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PotServiceV1ImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PotServiceV1ImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_EVENT:
          serviceImpl.sendEvent((org.ar4k.agent.tunnels.http.grpc.beacon.MessageEventRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_POLLING:
          serviceImpl.polling((org.ar4k.agent.tunnels.http.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot>) responseObserver);
          break;
        case METHODID_SUBSCRIPTION:
          serviceImpl.subscription((org.ar4k.agent.tunnels.http.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessagePot>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PotServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PotServiceV1BaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PotServiceV1");
    }
  }

  private static final class PotServiceV1FileDescriptorSupplier
      extends PotServiceV1BaseDescriptorSupplier {
    PotServiceV1FileDescriptorSupplier() {}
  }

  private static final class PotServiceV1MethodDescriptorSupplier
      extends PotServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PotServiceV1MethodDescriptorSupplier(String methodName) {
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
      synchronized (PotServiceV1Grpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PotServiceV1FileDescriptorSupplier())
              .addMethod(getSendEventMethod())
              .addMethod(getPollingMethod())
              .addMethod(getSubscriptionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
