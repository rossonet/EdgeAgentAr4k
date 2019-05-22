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
public final class RpcServiceV1Grpc {

  private RpcServiceV1Grpc() {}

  public static final String SERVICE_NAME = "beacon.RpcServiceV1";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Register",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getRegisterMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getRegisterMethod;
    if ((getRegisterMethod = RpcServiceV1Grpc.getRegisterMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getRegisterMethod = RpcServiceV1Grpc.getRegisterMethod) == null) {
          RpcServiceV1Grpc.getRegisterMethod = getRegisterMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("Register"))
                  .build();
          }
        }
     }
     return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getPollingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Polling",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getPollingMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getPollingMethod;
    if ((getPollingMethod = RpcServiceV1Grpc.getPollingMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getPollingMethod = RpcServiceV1Grpc.getPollingMethod) == null) {
          RpcServiceV1Grpc.getPollingMethod = getPollingMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "Polling"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("Polling"))
                  .build();
          }
        }
     }
     return getPollingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSubscriptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Subscription",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSubscriptionMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSubscriptionMethod;
    if ((getSubscriptionMethod = RpcServiceV1Grpc.getSubscriptionMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSubscriptionMethod = RpcServiceV1Grpc.getSubscriptionMethod) == null) {
          RpcServiceV1Grpc.getSubscriptionMethod = getSubscriptionMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "Subscription"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("Subscription"))
                  .build();
          }
        }
     }
     return getSubscriptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendHealthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendHealth",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendHealthMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendHealthMethod;
    if ((getSendHealthMethod = RpcServiceV1Grpc.getSendHealthMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendHealthMethod = RpcServiceV1Grpc.getSendHealthMethod) == null) {
          RpcServiceV1Grpc.getSendHealthMethod = getSendHealthMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendHealth"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendHealth"))
                  .build();
          }
        }
     }
     return getSendHealthMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendLogMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendLog",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendLogMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendLogMethod;
    if ((getSendLogMethod = RpcServiceV1Grpc.getSendLogMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendLogMethod = RpcServiceV1Grpc.getSendLogMethod) == null) {
          RpcServiceV1Grpc.getSendLogMethod = getSendLogMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendLog"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendLog"))
                  .build();
          }
        }
     }
     return getSendLogMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendExceptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendException",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendExceptionMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getSendExceptionMethod;
    if ((getSendExceptionMethod = RpcServiceV1Grpc.getSendExceptionMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendExceptionMethod = RpcServiceV1Grpc.getSendExceptionMethod) == null) {
          RpcServiceV1Grpc.getSendExceptionMethod = getSendExceptionMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendException"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendException"))
                  .build();
          }
        }
     }
     return getSendExceptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getGetConfigTargetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetConfigTarget",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getGetConfigTargetMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getGetConfigTargetMethod;
    if ((getGetConfigTargetMethod = RpcServiceV1Grpc.getGetConfigTargetMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getGetConfigTargetMethod = RpcServiceV1Grpc.getGetConfigTargetMethod) == null) {
          RpcServiceV1Grpc.getGetConfigTargetMethod = getGetConfigTargetMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "GetConfigTarget"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("GetConfigTarget"))
                  .build();
          }
        }
     }
     return getGetConfigTargetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getOpenBidirectionalSocketTunnelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OpenBidirectionalSocketTunnel",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getOpenBidirectionalSocketTunnelMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getOpenBidirectionalSocketTunnelMethod;
    if ((getOpenBidirectionalSocketTunnelMethod = RpcServiceV1Grpc.getOpenBidirectionalSocketTunnelMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getOpenBidirectionalSocketTunnelMethod = RpcServiceV1Grpc.getOpenBidirectionalSocketTunnelMethod) == null) {
          RpcServiceV1Grpc.getOpenBidirectionalSocketTunnelMethod = getOpenBidirectionalSocketTunnelMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "OpenBidirectionalSocketTunnel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("OpenBidirectionalSocketTunnel"))
                  .build();
          }
        }
     }
     return getOpenBidirectionalSocketTunnelMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListAgentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAgents",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListAgentsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListAgentsMethod;
    if ((getListAgentsMethod = RpcServiceV1Grpc.getListAgentsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListAgentsMethod = RpcServiceV1Grpc.getListAgentsMethod) == null) {
          RpcServiceV1Grpc.getListAgentsMethod = getListAgentsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListAgents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListAgents"))
                  .build();
          }
        }
     }
     return getListAgentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListSslAuthoritiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListSslAuthorities",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListSslAuthoritiesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListSslAuthoritiesMethod;
    if ((getListSslAuthoritiesMethod = RpcServiceV1Grpc.getListSslAuthoritiesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListSslAuthoritiesMethod = RpcServiceV1Grpc.getListSslAuthoritiesMethod) == null) {
          RpcServiceV1Grpc.getListSslAuthoritiesMethod = getListSslAuthoritiesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListSslAuthorities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListSslAuthorities"))
                  .build();
          }
        }
     }
     return getListSslAuthoritiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getAddSslAuthoritiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddSslAuthorities",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getAddSslAuthoritiesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getAddSslAuthoritiesMethod;
    if ((getAddSslAuthoritiesMethod = RpcServiceV1Grpc.getAddSslAuthoritiesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getAddSslAuthoritiesMethod = RpcServiceV1Grpc.getAddSslAuthoritiesMethod) == null) {
          RpcServiceV1Grpc.getAddSslAuthoritiesMethod = getAddSslAuthoritiesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "AddSslAuthorities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("AddSslAuthorities"))
                  .build();
          }
        }
     }
     return getAddSslAuthoritiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getDropSslAuthoritiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DropSslAuthorities",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getDropSslAuthoritiesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getDropSslAuthoritiesMethod;
    if ((getDropSslAuthoritiesMethod = RpcServiceV1Grpc.getDropSslAuthoritiesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getDropSslAuthoritiesMethod = RpcServiceV1Grpc.getDropSslAuthoritiesMethod) == null) {
          RpcServiceV1Grpc.getDropSslAuthoritiesMethod = getDropSslAuthoritiesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "DropSslAuthorities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("DropSslAuthorities"))
                  .build();
          }
        }
     }
     return getDropSslAuthoritiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getKickAgentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "KickAgent",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getKickAgentMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getKickAgentMethod;
    if ((getKickAgentMethod = RpcServiceV1Grpc.getKickAgentMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getKickAgentMethod = RpcServiceV1Grpc.getKickAgentMethod) == null) {
          RpcServiceV1Grpc.getKickAgentMethod = getKickAgentMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "KickAgent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("KickAgent"))
                  .build();
          }
        }
     }
     return getKickAgentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCreateRpcWorkspaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateRpcWorkspace",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCreateRpcWorkspaceMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCreateRpcWorkspaceMethod;
    if ((getCreateRpcWorkspaceMethod = RpcServiceV1Grpc.getCreateRpcWorkspaceMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getCreateRpcWorkspaceMethod = RpcServiceV1Grpc.getCreateRpcWorkspaceMethod) == null) {
          RpcServiceV1Grpc.getCreateRpcWorkspaceMethod = getCreateRpcWorkspaceMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "CreateRpcWorkspace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("CreateRpcWorkspace"))
                  .build();
          }
        }
     }
     return getCreateRpcWorkspaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getDropRpcWorkspaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DropRpcWorkspace",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getDropRpcWorkspaceMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getDropRpcWorkspaceMethod;
    if ((getDropRpcWorkspaceMethod = RpcServiceV1Grpc.getDropRpcWorkspaceMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getDropRpcWorkspaceMethod = RpcServiceV1Grpc.getDropRpcWorkspaceMethod) == null) {
          RpcServiceV1Grpc.getDropRpcWorkspaceMethod = getDropRpcWorkspaceMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "DropRpcWorkspace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("DropRpcWorkspace"))
                  .build();
          }
        }
     }
     return getDropRpcWorkspaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListRpcWorkspaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListRpcWorkspace",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListRpcWorkspaceMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListRpcWorkspaceMethod;
    if ((getListRpcWorkspaceMethod = RpcServiceV1Grpc.getListRpcWorkspaceMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListRpcWorkspaceMethod = RpcServiceV1Grpc.getListRpcWorkspaceMethod) == null) {
          RpcServiceV1Grpc.getListRpcWorkspaceMethod = getListRpcWorkspaceMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListRpcWorkspace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListRpcWorkspace"))
                  .build();
          }
        }
     }
     return getListRpcWorkspaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getElaborateMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ElaborateMessage",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getElaborateMessageMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getElaborateMessageMethod;
    if ((getElaborateMessageMethod = RpcServiceV1Grpc.getElaborateMessageMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getElaborateMessageMethod = RpcServiceV1Grpc.getElaborateMessageMethod) == null) {
          RpcServiceV1Grpc.getElaborateMessageMethod = getElaborateMessageMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ElaborateMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ElaborateMessage"))
                  .build();
          }
        }
     }
     return getElaborateMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListCommandsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCommands",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListCommandsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListCommandsMethod;
    if ((getListCommandsMethod = RpcServiceV1Grpc.getListCommandsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListCommandsMethod = RpcServiceV1Grpc.getListCommandsMethod) == null) {
          RpcServiceV1Grpc.getListCommandsMethod = getListCommandsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListCommands"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListCommands"))
                  .build();
          }
        }
     }
     return getListCommandsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCompleteCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CompleteCommand",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCompleteCommandMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCompleteCommandMethod;
    if ((getCompleteCommandMethod = RpcServiceV1Grpc.getCompleteCommandMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getCompleteCommandMethod = RpcServiceV1Grpc.getCompleteCommandMethod) == null) {
          RpcServiceV1Grpc.getCompleteCommandMethod = getCompleteCommandMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "CompleteCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("CompleteCommand"))
                  .build();
          }
        }
     }
     return getCompleteCommandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCreateProxySocksOnAgentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateProxySocksOnAgent",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCreateProxySocksOnAgentMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCreateProxySocksOnAgentMethod;
    if ((getCreateProxySocksOnAgentMethod = RpcServiceV1Grpc.getCreateProxySocksOnAgentMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getCreateProxySocksOnAgentMethod = RpcServiceV1Grpc.getCreateProxySocksOnAgentMethod) == null) {
          RpcServiceV1Grpc.getCreateProxySocksOnAgentMethod = getCreateProxySocksOnAgentMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "CreateProxySocksOnAgent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("CreateProxySocksOnAgent"))
                  .build();
          }
        }
     }
     return getCreateProxySocksOnAgentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getExposeAgentPortMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExposeAgentPort",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getExposeAgentPortMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getExposeAgentPortMethod;
    if ((getExposeAgentPortMethod = RpcServiceV1Grpc.getExposeAgentPortMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getExposeAgentPortMethod = RpcServiceV1Grpc.getExposeAgentPortMethod) == null) {
          RpcServiceV1Grpc.getExposeAgentPortMethod = getExposeAgentPortMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ExposeAgentPort"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ExposeAgentPort"))
                  .build();
          }
        }
     }
     return getExposeAgentPortMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListTunnelsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListTunnels",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListTunnelsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getListTunnelsMethod;
    if ((getListTunnelsMethod = RpcServiceV1Grpc.getListTunnelsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListTunnelsMethod = RpcServiceV1Grpc.getListTunnelsMethod) == null) {
          RpcServiceV1Grpc.getListTunnelsMethod = getListTunnelsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListTunnels"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListTunnels"))
                  .build();
          }
        }
     }
     return getListTunnelsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCloseTunnelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CloseTunnel",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCloseTunnelMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getCloseTunnelMethod;
    if ((getCloseTunnelMethod = RpcServiceV1Grpc.getCloseTunnelMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getCloseTunnelMethod = RpcServiceV1Grpc.getCloseTunnelMethod) == null) {
          RpcServiceV1Grpc.getCloseTunnelMethod = getCloseTunnelMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "CloseTunnel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("CloseTunnel"))
                  .build();
          }
        }
     }
     return getCloseTunnelMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcServiceV1Stub newStub(io.grpc.Channel channel) {
    return new RpcServiceV1Stub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcServiceV1BlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcServiceV1BlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RpcServiceV1FutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcServiceV1FutureStub(channel);
  }

  /**
   */
  public static abstract class RpcServiceV1ImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * for agent
     * </pre>
     */
    public void register(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     */
    public void polling(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getPollingMethod(), responseObserver);
    }

    /**
     */
    public void subscription(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscriptionMethod(), responseObserver);
    }

    /**
     */
    public void sendHealth(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendHealthMethod(), responseObserver);
    }

    /**
     */
    public void sendLog(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendLogMethod(), responseObserver);
    }

    /**
     */
    public void sendException(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendExceptionMethod(), responseObserver);
    }

    /**
     */
    public void getConfigTarget(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetConfigTargetMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest> openBidirectionalSocketTunnel(
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      return asyncUnimplementedStreamingCall(getOpenBidirectionalSocketTunnelMethod(), responseObserver);
    }

    /**
     * <pre>
     * for console
     * </pre>
     */
    public void listAgents(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListAgentsMethod(), responseObserver);
    }

    /**
     */
    public void listSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListSslAuthoritiesMethod(), responseObserver);
    }

    /**
     */
    public void addSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getAddSslAuthoritiesMethod(), responseObserver);
    }

    /**
     */
    public void dropSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getDropSslAuthoritiesMethod(), responseObserver);
    }

    /**
     */
    public void kickAgent(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getKickAgentMethod(), responseObserver);
    }

    /**
     */
    public void createRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateRpcWorkspaceMethod(), responseObserver);
    }

    /**
     */
    public void dropRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getDropRpcWorkspaceMethod(), responseObserver);
    }

    /**
     */
    public void listRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListRpcWorkspaceMethod(), responseObserver);
    }

    /**
     */
    public void elaborateMessage(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getElaborateMessageMethod(), responseObserver);
    }

    /**
     */
    public void listCommands(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListCommandsMethod(), responseObserver);
    }

    /**
     */
    public void completeCommand(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCompleteCommandMethod(), responseObserver);
    }

    /**
     */
    public void createProxySocksOnAgent(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateProxySocksOnAgentMethod(), responseObserver);
    }

    /**
     */
    public void exposeAgentPort(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getExposeAgentPortMethod(), responseObserver);
    }

    /**
     */
    public void listTunnels(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListTunnelsMethod(), responseObserver);
    }

    /**
     */
    public void closeTunnel(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseTunnelMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_REGISTER)))
          .addMethod(
            getPollingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_POLLING)))
          .addMethod(
            getSubscriptionMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_SUBSCRIPTION)))
          .addMethod(
            getSendHealthMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_SEND_HEALTH)))
          .addMethod(
            getSendLogMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_SEND_LOG)))
          .addMethod(
            getSendExceptionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_SEND_EXCEPTION)))
          .addMethod(
            getGetConfigTargetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_GET_CONFIG_TARGET)))
          .addMethod(
            getOpenBidirectionalSocketTunnelMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_OPEN_BIDIRECTIONAL_SOCKET_TUNNEL)))
          .addMethod(
            getListAgentsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_LIST_AGENTS)))
          .addMethod(
            getListSslAuthoritiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_LIST_SSL_AUTHORITIES)))
          .addMethod(
            getAddSslAuthoritiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_ADD_SSL_AUTHORITIES)))
          .addMethod(
            getDropSslAuthoritiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_DROP_SSL_AUTHORITIES)))
          .addMethod(
            getKickAgentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_KICK_AGENT)))
          .addMethod(
            getCreateRpcWorkspaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_CREATE_RPC_WORKSPACE)))
          .addMethod(
            getDropRpcWorkspaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_DROP_RPC_WORKSPACE)))
          .addMethod(
            getListRpcWorkspaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_LIST_RPC_WORKSPACE)))
          .addMethod(
            getElaborateMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_ELABORATE_MESSAGE)))
          .addMethod(
            getListCommandsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_LIST_COMMANDS)))
          .addMethod(
            getCompleteCommandMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_COMPLETE_COMMAND)))
          .addMethod(
            getCreateProxySocksOnAgentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_CREATE_PROXY_SOCKS_ON_AGENT)))
          .addMethod(
            getExposeAgentPortMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_EXPOSE_AGENT_PORT)))
          .addMethod(
            getListTunnelsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_LIST_TUNNELS)))
          .addMethod(
            getCloseTunnelMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>(
                  this, METHODID_CLOSE_TUNNEL)))
          .build();
    }
  }

  /**
   */
  public static final class RpcServiceV1Stub extends io.grpc.stub.AbstractStub<RpcServiceV1Stub> {
    private RpcServiceV1Stub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcServiceV1Stub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcServiceV1Stub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcServiceV1Stub(channel, callOptions);
    }

    /**
     * <pre>
     * for agent
     * </pre>
     */
    public void register(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void polling(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPollingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscription(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscriptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendHealth(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendHealthMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendLog(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendLogMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendException(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendExceptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getConfigTarget(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetConfigTargetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest> openBidirectionalSocketTunnel(
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getOpenBidirectionalSocketTunnelMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * for console
     * </pre>
     */
    public void listAgents(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListAgentsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListSslAuthoritiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddSslAuthoritiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void dropSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDropSslAuthoritiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void kickAgent(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getKickAgentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateRpcWorkspaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void dropRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDropRpcWorkspaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListRpcWorkspaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void elaborateMessage(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElaborateMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCommands(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListCommandsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void completeCommand(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCompleteCommandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createProxySocksOnAgent(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateProxySocksOnAgentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void exposeAgentPort(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExposeAgentPortMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listTunnels(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListTunnelsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeTunnel(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseTunnelMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RpcServiceV1BlockingStub extends io.grpc.stub.AbstractStub<RpcServiceV1BlockingStub> {
    private RpcServiceV1BlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcServiceV1BlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcServiceV1BlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcServiceV1BlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * for agent
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply register(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply polling(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getPollingMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> subscription(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscriptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply sendHealth(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendHealthMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply sendLog(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendLogMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply sendException(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendExceptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply getConfigTarget(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetConfigTargetMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * for console
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply listAgents(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getListAgentsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply listSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getListSslAuthoritiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply addSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getAddSslAuthoritiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply dropSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getDropSslAuthoritiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply kickAgent(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getKickAgentMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply createRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateRpcWorkspaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply dropRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getDropRpcWorkspaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply listRpcWorkspace(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getListRpcWorkspaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply elaborateMessage(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getElaborateMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply listCommands(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getListCommandsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply completeCommand(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getCompleteCommandMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply createProxySocksOnAgent(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateProxySocksOnAgentMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply exposeAgentPort(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getExposeAgentPortMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply listTunnels(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getListTunnelsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply closeTunnel(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getCloseTunnelMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RpcServiceV1FutureStub extends io.grpc.stub.AbstractStub<RpcServiceV1FutureStub> {
    private RpcServiceV1FutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcServiceV1FutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcServiceV1FutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcServiceV1FutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * for agent
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> register(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> polling(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPollingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> sendHealth(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendHealthMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> sendLog(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendLogMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> sendException(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendExceptionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> getConfigTarget(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetConfigTargetMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * for console
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> listAgents(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListAgentsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> listSslAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListSslAuthoritiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> addSslAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAddSslAuthoritiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> dropSslAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDropSslAuthoritiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> kickAgent(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getKickAgentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> createRpcWorkspace(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateRpcWorkspaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> dropRpcWorkspace(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDropRpcWorkspaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> listRpcWorkspace(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListRpcWorkspaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> elaborateMessage(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getElaborateMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> listCommands(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListCommandsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> completeCommand(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCompleteCommandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> createProxySocksOnAgent(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateProxySocksOnAgentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> exposeAgentPort(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExposeAgentPortMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> listTunnels(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListTunnelsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> closeTunnel(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseTunnelMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER = 0;
  private static final int METHODID_POLLING = 1;
  private static final int METHODID_SUBSCRIPTION = 2;
  private static final int METHODID_SEND_HEALTH = 3;
  private static final int METHODID_SEND_LOG = 4;
  private static final int METHODID_SEND_EXCEPTION = 5;
  private static final int METHODID_GET_CONFIG_TARGET = 6;
  private static final int METHODID_LIST_AGENTS = 7;
  private static final int METHODID_LIST_SSL_AUTHORITIES = 8;
  private static final int METHODID_ADD_SSL_AUTHORITIES = 9;
  private static final int METHODID_DROP_SSL_AUTHORITIES = 10;
  private static final int METHODID_KICK_AGENT = 11;
  private static final int METHODID_CREATE_RPC_WORKSPACE = 12;
  private static final int METHODID_DROP_RPC_WORKSPACE = 13;
  private static final int METHODID_LIST_RPC_WORKSPACE = 14;
  private static final int METHODID_ELABORATE_MESSAGE = 15;
  private static final int METHODID_LIST_COMMANDS = 16;
  private static final int METHODID_COMPLETE_COMMAND = 17;
  private static final int METHODID_CREATE_PROXY_SOCKS_ON_AGENT = 18;
  private static final int METHODID_EXPOSE_AGENT_PORT = 19;
  private static final int METHODID_LIST_TUNNELS = 20;
  private static final int METHODID_CLOSE_TUNNEL = 21;
  private static final int METHODID_OPEN_BIDIRECTIONAL_SOCKET_TUNNEL = 22;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcServiceV1ImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RpcServiceV1ImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER:
          serviceImpl.register((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_POLLING:
          serviceImpl.polling((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_SUBSCRIPTION:
          serviceImpl.subscription((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_SEND_HEALTH:
          serviceImpl.sendHealth((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_SEND_LOG:
          serviceImpl.sendLog((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_SEND_EXCEPTION:
          serviceImpl.sendException((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_GET_CONFIG_TARGET:
          serviceImpl.getConfigTarget((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_LIST_AGENTS:
          serviceImpl.listAgents((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_LIST_SSL_AUTHORITIES:
          serviceImpl.listSslAuthorities((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_ADD_SSL_AUTHORITIES:
          serviceImpl.addSslAuthorities((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_DROP_SSL_AUTHORITIES:
          serviceImpl.dropSslAuthorities((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_KICK_AGENT:
          serviceImpl.kickAgent((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_CREATE_RPC_WORKSPACE:
          serviceImpl.createRpcWorkspace((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_DROP_RPC_WORKSPACE:
          serviceImpl.dropRpcWorkspace((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_LIST_RPC_WORKSPACE:
          serviceImpl.listRpcWorkspace((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_ELABORATE_MESSAGE:
          serviceImpl.elaborateMessage((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_LIST_COMMANDS:
          serviceImpl.listCommands((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_COMPLETE_COMMAND:
          serviceImpl.completeCommand((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_CREATE_PROXY_SOCKS_ON_AGENT:
          serviceImpl.createProxySocksOnAgent((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_EXPOSE_AGENT_PORT:
          serviceImpl.exposeAgentPort((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_LIST_TUNNELS:
          serviceImpl.listTunnels((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_CLOSE_TUNNEL:
          serviceImpl.closeTunnel((org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
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
        case METHODID_OPEN_BIDIRECTIONAL_SOCKET_TUNNEL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.openBidirectionalSocketTunnel(
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RpcServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RpcServiceV1BaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RpcServiceV1");
    }
  }

  private static final class RpcServiceV1FileDescriptorSupplier
      extends RpcServiceV1BaseDescriptorSupplier {
    RpcServiceV1FileDescriptorSupplier() {}
  }

  private static final class RpcServiceV1MethodDescriptorSupplier
      extends RpcServiceV1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RpcServiceV1MethodDescriptorSupplier(String methodName) {
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
      synchronized (RpcServiceV1Grpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RpcServiceV1FileDescriptorSupplier())
              .addMethod(getRegisterMethod())
              .addMethod(getPollingMethod())
              .addMethod(getSubscriptionMethod())
              .addMethod(getSendHealthMethod())
              .addMethod(getSendLogMethod())
              .addMethod(getSendExceptionMethod())
              .addMethod(getGetConfigTargetMethod())
              .addMethod(getOpenBidirectionalSocketTunnelMethod())
              .addMethod(getListAgentsMethod())
              .addMethod(getListSslAuthoritiesMethod())
              .addMethod(getAddSslAuthoritiesMethod())
              .addMethod(getDropSslAuthoritiesMethod())
              .addMethod(getKickAgentMethod())
              .addMethod(getCreateRpcWorkspaceMethod())
              .addMethod(getDropRpcWorkspaceMethod())
              .addMethod(getListRpcWorkspaceMethod())
              .addMethod(getElaborateMessageMethod())
              .addMethod(getListCommandsMethod())
              .addMethod(getCompleteCommandMethod())
              .addMethod(getCreateProxySocksOnAgentMethod())
              .addMethod(getExposeAgentPortMethod())
              .addMethod(getListTunnelsMethod())
              .addMethod(getCloseTunnelMethod())
              .build();
        }
      }
    }
    return result;
  }
}
