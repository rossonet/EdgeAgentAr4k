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
public final class DataServiceV1Grpc {

  private DataServiceV1Grpc() {}

  public static final String SERVICE_NAME = "beacon.DataServiceV1";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> getPollingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Polling",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> getPollingMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> getPollingMethod;
    if ((getPollingMethod = DataServiceV1Grpc.getPollingMethod) == null) {
      synchronized (DataServiceV1Grpc.class) {
        if ((getPollingMethod = DataServiceV1Grpc.getPollingMethod) == null) {
          DataServiceV1Grpc.getPollingMethod = getPollingMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.DataServiceV1", "Polling"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData.getDefaultInstance()))
                  .setSchemaDescriptor(new DataServiceV1MethodDescriptorSupplier("Polling"))
                  .build();
          }
        }
     }
     return getPollingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> getSubscriptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Subscription",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> getSubscriptionMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> getSubscriptionMethod;
    if ((getSubscriptionMethod = DataServiceV1Grpc.getSubscriptionMethod) == null) {
      synchronized (DataServiceV1Grpc.class) {
        if ((getSubscriptionMethod = DataServiceV1Grpc.getSubscriptionMethod) == null) {
          DataServiceV1Grpc.getSubscriptionMethod = getSubscriptionMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.DataServiceV1", "Subscription"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData.getDefaultInstance()))
                  .setSchemaDescriptor(new DataServiceV1MethodDescriptorSupplier("Subscription"))
                  .build();
          }
        }
     }
     return getSubscriptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> getPollingTxtMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PollingTxt",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> getPollingTxtMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> getPollingTxtMethod;
    if ((getPollingTxtMethod = DataServiceV1Grpc.getPollingTxtMethod) == null) {
      synchronized (DataServiceV1Grpc.class) {
        if ((getPollingTxtMethod = DataServiceV1Grpc.getPollingTxtMethod) == null) {
          DataServiceV1Grpc.getPollingTxtMethod = getPollingTxtMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.DataServiceV1", "PollingTxt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson.getDefaultInstance()))
                  .setSchemaDescriptor(new DataServiceV1MethodDescriptorSupplier("PollingTxt"))
                  .build();
          }
        }
     }
     return getPollingTxtMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> getSubscriptionTxtMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscriptionTxt",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> getSubscriptionTxtMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> getSubscriptionTxtMethod;
    if ((getSubscriptionTxtMethod = DataServiceV1Grpc.getSubscriptionTxtMethod) == null) {
      synchronized (DataServiceV1Grpc.class) {
        if ((getSubscriptionTxtMethod = DataServiceV1Grpc.getSubscriptionTxtMethod) == null) {
          DataServiceV1Grpc.getSubscriptionTxtMethod = getSubscriptionTxtMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.DataServiceV1", "SubscriptionTxt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson.getDefaultInstance()))
                  .setSchemaDescriptor(new DataServiceV1MethodDescriptorSupplier("SubscriptionTxt"))
                  .build();
          }
        }
     }
     return getSubscriptionTxtMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "write",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getWriteMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getWriteMethod;
    if ((getWriteMethod = DataServiceV1Grpc.getWriteMethod) == null) {
      synchronized (DataServiceV1Grpc.class) {
        if ((getWriteMethod = DataServiceV1Grpc.getWriteMethod) == null) {
          DataServiceV1Grpc.getWriteMethod = getWriteMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.DataServiceV1", "write"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new DataServiceV1MethodDescriptorSupplier("write"))
                  .build();
          }
        }
     }
     return getWriteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataServiceV1Stub newStub(io.grpc.Channel channel) {
    return new DataServiceV1Stub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataServiceV1BlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DataServiceV1BlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataServiceV1FutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DataServiceV1FutureStub(channel);
  }

  /**
   */
  public static abstract class DataServiceV1ImplBase implements io.grpc.BindableService {

    /**
     */
    public void polling(org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> responseObserver) {
      asyncUnimplementedUnaryCall(getPollingMethod(), responseObserver);
    }

    /**
     */
    public void subscription(org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscriptionMethod(), responseObserver);
    }

    /**
     */
    public void pollingTxt(org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> responseObserver) {
      asyncUnimplementedUnaryCall(getPollingTxtMethod(), responseObserver);
    }

    /**
     */
    public void subscriptionTxt(org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscriptionTxtMethod(), responseObserver);
    }

    /**
     */
    public void write(org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getWriteMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPollingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData>(
                  this, METHODID_POLLING)))
          .addMethod(
            getSubscriptionMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData>(
                  this, METHODID_SUBSCRIPTION)))
          .addMethod(
            getPollingTxtMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson>(
                  this, METHODID_POLLING_TXT)))
          .addMethod(
            getSubscriptionTxtMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson>(
                  this, METHODID_SUBSCRIPTION_TXT)))
          .addMethod(
            getWriteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_WRITE)))
          .build();
    }
  }

  /**
   */
  public static final class DataServiceV1Stub extends io.grpc.stub.AbstractStub<DataServiceV1Stub> {
    private DataServiceV1Stub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataServiceV1Stub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataServiceV1Stub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataServiceV1Stub(channel, callOptions);
    }

    /**
     */
    public void polling(org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPollingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscription(org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscriptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pollingTxt(org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPollingTxtMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscriptionTxt(org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscriptionTxtMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void write(org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DataServiceV1BlockingStub extends io.grpc.stub.AbstractStub<DataServiceV1BlockingStub> {
    private DataServiceV1BlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataServiceV1BlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataServiceV1BlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataServiceV1BlockingStub(channel, callOptions);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData polling(org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request) {
      return blockingUnaryCall(
          getChannel(), getPollingMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> subscription(
        org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscriptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson pollingTxt(org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request) {
      return blockingUnaryCall(
          getChannel(), getPollingTxtMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> subscriptionTxt(
        org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscriptionTxtMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status write(org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite request) {
      return blockingUnaryCall(
          getChannel(), getWriteMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DataServiceV1FutureStub extends io.grpc.stub.AbstractStub<DataServiceV1FutureStub> {
    private DataServiceV1FutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataServiceV1FutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataServiceV1FutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataServiceV1FutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData> polling(
        org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request) {
      return futureUnaryCall(
          getChannel().newCall(getPollingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson> pollingTxt(
        org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling request) {
      return futureUnaryCall(
          getChannel().newCall(getPollingTxtMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> write(
        org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite request) {
      return futureUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_POLLING = 0;
  private static final int METHODID_SUBSCRIPTION = 1;
  private static final int METHODID_POLLING_TXT = 2;
  private static final int METHODID_SUBSCRIPTION_TXT = 3;
  private static final int METHODID_WRITE = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DataServiceV1ImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DataServiceV1ImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_POLLING:
          serviceImpl.polling((org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData>) responseObserver);
          break;
        case METHODID_SUBSCRIPTION:
          serviceImpl.subscription((org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData>) responseObserver);
          break;
        case METHODID_POLLING_TXT:
          serviceImpl.pollingTxt((org.ar4k.agent.tunnels.http.grpc.beacon.RequestPolling) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson>) responseObserver);
          break;
        case METHODID_SUBSCRIPTION_TXT:
          serviceImpl.subscriptionTxt((org.ar4k.agent.tunnels.http.grpc.beacon.RequestSubscription) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageDataJson>) responseObserver);
          break;
        case METHODID_WRITE:
          serviceImpl.write((org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
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

  private static abstract class DataServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataServiceV1BaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataServiceV1");
    }
  }

  private static final class DataServiceV1FileDescriptorSupplier
      extends DataServiceV1BaseDescriptorSupplier {
    DataServiceV1FileDescriptorSupplier() {}
  }

  private static final class DataServiceV1MethodDescriptorSupplier
      extends DataServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DataServiceV1MethodDescriptorSupplier(String methodName) {
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
      synchronized (DataServiceV1Grpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataServiceV1FileDescriptorSupplier())
              .addMethod(getPollingMethod())
              .addMethod(getSubscriptionMethod())
              .addMethod(getPollingTxtMethod())
              .addMethod(getSubscriptionTxtMethod())
              .addMethod(getWriteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
