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
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: hello_streaming.proto")
public final class StreamingGreeterGrpc {

  private StreamingGreeterGrpc() {}

  public static final String SERVICE_NAME = "beacon.StreamingGreeter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> getSayHelloStreamingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SayHelloStreaming",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> getSayHelloStreamingMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest, org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> getSayHelloStreamingMethod;
    if ((getSayHelloStreamingMethod = StreamingGreeterGrpc.getSayHelloStreamingMethod) == null) {
      synchronized (StreamingGreeterGrpc.class) {
        if ((getSayHelloStreamingMethod = StreamingGreeterGrpc.getSayHelloStreamingMethod) == null) {
          StreamingGreeterGrpc.getSayHelloStreamingMethod = getSayHelloStreamingMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest, org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.StreamingGreeter", "SayHelloStreaming"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply.getDefaultInstance()))
                  .setSchemaDescriptor(new StreamingGreeterMethodDescriptorSupplier("SayHelloStreaming"))
                  .build();
          }
        }
     }
     return getSayHelloStreamingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> getSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SayHello",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> getSayHelloMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest, org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> getSayHelloMethod;
    if ((getSayHelloMethod = StreamingGreeterGrpc.getSayHelloMethod) == null) {
      synchronized (StreamingGreeterGrpc.class) {
        if ((getSayHelloMethod = StreamingGreeterGrpc.getSayHelloMethod) == null) {
          StreamingGreeterGrpc.getSayHelloMethod = getSayHelloMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest, org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.StreamingGreeter", "SayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply.getDefaultInstance()))
                  .setSchemaDescriptor(new StreamingGreeterMethodDescriptorSupplier("SayHello"))
                  .build();
          }
        }
     }
     return getSayHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StreamingGreeterStub newStub(io.grpc.Channel channel) {
    return new StreamingGreeterStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StreamingGreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StreamingGreeterBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StreamingGreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StreamingGreeterFutureStub(channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class StreamingGreeterImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Streams a many greetings
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest> sayHelloStreaming(
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> responseObserver) {
      return asyncUnimplementedStreamingCall(getSayHelloStreamingMethod(), responseObserver);
    }

    /**
     */
    public void sayHello(org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSayHelloMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSayHelloStreamingMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply>(
                  this, METHODID_SAY_HELLO_STREAMING)))
          .addMethod(
            getSayHelloMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply>(
                  this, METHODID_SAY_HELLO)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class StreamingGreeterStub extends io.grpc.stub.AbstractStub<StreamingGreeterStub> {
    private StreamingGreeterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StreamingGreeterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamingGreeterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StreamingGreeterStub(channel, callOptions);
    }

    /**
     * <pre>
     * Streams a many greetings
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest> sayHelloStreaming(
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getSayHelloStreamingMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void sayHello(org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class StreamingGreeterBlockingStub extends io.grpc.stub.AbstractStub<StreamingGreeterBlockingStub> {
    private StreamingGreeterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StreamingGreeterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamingGreeterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StreamingGreeterBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply sayHello(org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest request) {
      return blockingUnaryCall(
          getChannel(), getSayHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class StreamingGreeterFutureStub extends io.grpc.stub.AbstractStub<StreamingGreeterFutureStub> {
    private StreamingGreeterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StreamingGreeterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamingGreeterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StreamingGreeterFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply> sayHello(
        org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAY_HELLO = 0;
  private static final int METHODID_SAY_HELLO_STREAMING = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StreamingGreeterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StreamingGreeterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAY_HELLO:
          serviceImpl.sayHello((org.ar4k.agent.tunnels.http.grpc.beacon.HelloRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply>) responseObserver);
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
        case METHODID_SAY_HELLO_STREAMING:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sayHelloStreaming(
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.HelloReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StreamingGreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StreamingGreeterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.HelloStreaming.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StreamingGreeter");
    }
  }

  private static final class StreamingGreeterFileDescriptorSupplier
      extends StreamingGreeterBaseDescriptorSupplier {
    StreamingGreeterFileDescriptorSupplier() {}
  }

  private static final class StreamingGreeterMethodDescriptorSupplier
      extends StreamingGreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StreamingGreeterMethodDescriptorSupplier(String methodName) {
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
      synchronized (StreamingGreeterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StreamingGreeterFileDescriptorSupplier())
              .addMethod(getSayHelloStreamingMethod())
              .addMethod(getSayHelloMethod())
              .build();
        }
      }
    }
    return result;
  }
}
