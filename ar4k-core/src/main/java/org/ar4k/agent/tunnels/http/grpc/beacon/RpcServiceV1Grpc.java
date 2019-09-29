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

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> getPollingCmdQueueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PollingCmdQueue",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> getPollingCmdQueueMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> getPollingCmdQueueMethod;
    if ((getPollingCmdQueueMethod = RpcServiceV1Grpc.getPollingCmdQueueMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getPollingCmdQueueMethod = RpcServiceV1Grpc.getPollingCmdQueueMethod) == null) {
          RpcServiceV1Grpc.getPollingCmdQueueMethod = getPollingCmdQueueMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "PollingCmdQueue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("PollingCmdQueue"))
                  .build();
          }
        }
     }
     return getPollingCmdQueueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> getSubscriptionCmdQueueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscriptionCmdQueue",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> getSubscriptionCmdQueueMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> getSubscriptionCmdQueueMethod;
    if ((getSubscriptionCmdQueueMethod = RpcServiceV1Grpc.getSubscriptionCmdQueueMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSubscriptionCmdQueueMethod = RpcServiceV1Grpc.getSubscriptionCmdQueueMethod) == null) {
          RpcServiceV1Grpc.getSubscriptionCmdQueueMethod = getSubscriptionCmdQueueMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SubscriptionCmdQueue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SubscriptionCmdQueue"))
                  .build();
          }
        }
     }
     return getSubscriptionCmdQueueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendChatMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendChatMessage",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendChatMessageMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendChatMessageMethod;
    if ((getSendChatMessageMethod = RpcServiceV1Grpc.getSendChatMessageMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendChatMessageMethod = RpcServiceV1Grpc.getSendChatMessageMethod) == null) {
          RpcServiceV1Grpc.getSendChatMessageMethod = getSendChatMessageMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendChatMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendChatMessage"))
                  .build();
          }
        }
     }
     return getSendChatMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendCommandReplyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendCommandReply",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendCommandReplyMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendCommandReplyMethod;
    if ((getSendCommandReplyMethod = RpcServiceV1Grpc.getSendCommandReplyMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendCommandReplyMethod = RpcServiceV1Grpc.getSendCommandReplyMethod) == null) {
          RpcServiceV1Grpc.getSendCommandReplyMethod = getSendCommandReplyMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendCommandReply"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendCommandReply"))
                  .build();
          }
        }
     }
     return getSendCommandReplyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendHealthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendHealth",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendHealthMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendHealthMethod;
    if ((getSendHealthMethod = RpcServiceV1Grpc.getSendHealthMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendHealthMethod = RpcServiceV1Grpc.getSendHealthMethod) == null) {
          RpcServiceV1Grpc.getSendHealthMethod = getSendHealthMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendHealth"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendHealth"))
                  .build();
          }
        }
     }
     return getSendHealthMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendLogMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendLog",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendLogMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendLogMethod;
    if ((getSendLogMethod = RpcServiceV1Grpc.getSendLogMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendLogMethod = RpcServiceV1Grpc.getSendLogMethod) == null) {
          RpcServiceV1Grpc.getSendLogMethod = getSendLogMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendLog"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendLog"))
                  .build();
          }
        }
     }
     return getSendLogMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendExceptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendException",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendExceptionMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getSendExceptionMethod;
    if ((getSendExceptionMethod = RpcServiceV1Grpc.getSendExceptionMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendExceptionMethod = RpcServiceV1Grpc.getSendExceptionMethod) == null) {
          RpcServiceV1Grpc.getSendExceptionMethod = getSendExceptionMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendException"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendException"))
                  .build();
          }
        }
     }
     return getSendExceptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport,
      org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply> getSendConfigRuntimeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendConfigRuntime",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport,
      org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply> getSendConfigRuntimeMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport, org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply> getSendConfigRuntimeMethod;
    if ((getSendConfigRuntimeMethod = RpcServiceV1Grpc.getSendConfigRuntimeMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendConfigRuntimeMethod = RpcServiceV1Grpc.getSendConfigRuntimeMethod) == null) {
          RpcServiceV1Grpc.getSendConfigRuntimeMethod = getSendConfigRuntimeMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport, org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendConfigRuntime"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendConfigRuntime"))
                  .build();
          }
        }
     }
     return getSendConfigRuntimeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply> getListAgentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAgents",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Empty.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply> getListAgentsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Empty, org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply> getListAgentsMethod;
    if ((getListAgentsMethod = RpcServiceV1Grpc.getListAgentsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListAgentsMethod = RpcServiceV1Grpc.getListAgentsMethod) == null) {
          RpcServiceV1Grpc.getListAgentsMethod = getListAgentsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Empty, org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListAgents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListAgents"))
                  .build();
          }
        }
     }
     return getListAgentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply> getListSslAuthoritiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListSslAuthorities",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Empty.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply> getListSslAuthoritiesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Empty, org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply> getListSslAuthoritiesMethod;
    if ((getListSslAuthoritiesMethod = RpcServiceV1Grpc.getListSslAuthoritiesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListSslAuthoritiesMethod = RpcServiceV1Grpc.getListSslAuthoritiesMethod) == null) {
          RpcServiceV1Grpc.getListSslAuthoritiesMethod = getListSslAuthoritiesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Empty, org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListSslAuthorities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListSslAuthorities"))
                  .build();
          }
        }
     }
     return getListSslAuthoritiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getAddSslAuthoritiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddSslAuthorities",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getAddSslAuthoritiesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getAddSslAuthoritiesMethod;
    if ((getAddSslAuthoritiesMethod = RpcServiceV1Grpc.getAddSslAuthoritiesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getAddSslAuthoritiesMethod = RpcServiceV1Grpc.getAddSslAuthoritiesMethod) == null) {
          RpcServiceV1Grpc.getAddSslAuthoritiesMethod = getAddSslAuthoritiesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "AddSslAuthorities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("AddSslAuthorities"))
                  .build();
          }
        }
     }
     return getAddSslAuthoritiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getDropSslAuthoritiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DropSslAuthorities",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getDropSslAuthoritiesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getDropSslAuthoritiesMethod;
    if ((getDropSslAuthoritiesMethod = RpcServiceV1Grpc.getDropSslAuthoritiesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getDropSslAuthoritiesMethod = RpcServiceV1Grpc.getDropSslAuthoritiesMethod) == null) {
          RpcServiceV1Grpc.getDropSslAuthoritiesMethod = getDropSslAuthoritiesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "DropSslAuthorities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("DropSslAuthorities"))
                  .build();
          }
        }
     }
     return getDropSslAuthoritiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getKickAgentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "KickAgent",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http.grpc.beacon.Status> getKickAgentMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Status> getKickAgentMethod;
    if ((getKickAgentMethod = RpcServiceV1Grpc.getKickAgentMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getKickAgentMethod = RpcServiceV1Grpc.getKickAgentMethod) == null) {
          RpcServiceV1Grpc.getKickAgentMethod = getKickAgentMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "KickAgent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("KickAgent"))
                  .build();
          }
        }
     }
     return getKickAgentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply> getElaborateMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ElaborateMessage",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply> getElaborateMessageMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest, org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply> getElaborateMessageMethod;
    if ((getElaborateMessageMethod = RpcServiceV1Grpc.getElaborateMessageMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getElaborateMessageMethod = RpcServiceV1Grpc.getElaborateMessageMethod) == null) {
          RpcServiceV1Grpc.getElaborateMessageMethod = getElaborateMessageMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest, org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ElaborateMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ElaborateMessage"))
                  .build();
          }
        }
     }
     return getElaborateMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply> getListCommandsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCommands",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply> getListCommandsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest, org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply> getListCommandsMethod;
    if ((getListCommandsMethod = RpcServiceV1Grpc.getListCommandsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListCommandsMethod = RpcServiceV1Grpc.getListCommandsMethod) == null) {
          RpcServiceV1Grpc.getListCommandsMethod = getListCommandsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest, org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListCommands"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListCommands"))
                  .build();
          }
        }
     }
     return getListCommandsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply> getCompleteCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CompleteCommand",
      requestType = org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest.class,
      responseType = org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest,
      org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply> getCompleteCommandMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest, org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply> getCompleteCommandMethod;
    if ((getCompleteCommandMethod = RpcServiceV1Grpc.getCompleteCommandMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getCompleteCommandMethod = RpcServiceV1Grpc.getCompleteCommandMethod) == null) {
          RpcServiceV1Grpc.getCompleteCommandMethod = getCompleteCommandMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest, org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "CompleteCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("CompleteCommand"))
                  .build();
          }
        }
     }
     return getCompleteCommandMethod;
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
     * for agent registration
     * </pre>
     */
    public void register(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     * <pre>
     *rpc GetConfigTarget (Agent) returns (ConfigReply) {} // TODO: implementare la gestione del cambio configurazione e la gestione delle configurazioni su beacon server
     * </pre>
     */
    public void pollingCmdQueue(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getPollingCmdQueueMethod(), responseObserver);
    }

    /**
     */
    public void subscriptionCmdQueue(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscriptionCmdQueueMethod(), responseObserver);
    }

    /**
     */
    public void sendChatMessage(org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendChatMessageMethod(), responseObserver);
    }

    /**
     */
    public void sendCommandReply(org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendCommandReplyMethod(), responseObserver);
    }

    /**
     */
    public void sendHealth(org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendHealthMethod(), responseObserver);
    }

    /**
     */
    public void sendLog(org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendLogMethod(), responseObserver);
    }

    /**
     */
    public void sendException(org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendExceptionMethod(), responseObserver);
    }

    /**
     */
    public void sendConfigRuntime(org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendConfigRuntimeMethod(), responseObserver);
    }

    /**
     * <pre>
     * for console
     * list agents connected 
     * </pre>
     */
    public void listAgents(org.ar4k.agent.tunnels.http.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListAgentsMethod(), responseObserver);
    }

    /**
     * <pre>
     * TODO gestione ssl con Beacon
     * </pre>
     */
    public void listSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListSslAuthoritiesMethod(), responseObserver);
    }

    /**
     */
    public void addSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getAddSslAuthoritiesMethod(), responseObserver);
    }

    /**
     */
    public void dropSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getDropSslAuthoritiesMethod(), responseObserver);
    }

    /**
     * <pre>
     * TODO gestione kick agent con Beacon
     * </pre>
     */
    public void kickAgent(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getKickAgentMethod(), responseObserver);
    }

    /**
     * <pre>
     * RPC on agent
     * </pre>
     */
    public void elaborateMessage(org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply> responseObserver) {
      asyncUnimplementedUnaryCall(getElaborateMessageMethod(), responseObserver);
    }

    /**
     */
    public void listCommands(org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListCommandsMethod(), responseObserver);
    }

    /**
     */
    public void completeCommand(org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCompleteCommandMethod(), responseObserver);
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
            getPollingCmdQueueMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage>(
                  this, METHODID_POLLING_CMD_QUEUE)))
          .addMethod(
            getSubscriptionCmdQueueMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage>(
                  this, METHODID_SUBSCRIPTION_CMD_QUEUE)))
          .addMethod(
            getSendChatMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_SEND_CHAT_MESSAGE)))
          .addMethod(
            getSendCommandReplyMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_SEND_COMMAND_REPLY)))
          .addMethod(
            getSendHealthMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_SEND_HEALTH)))
          .addMethod(
            getSendLogMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_SEND_LOG)))
          .addMethod(
            getSendExceptionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_SEND_EXCEPTION)))
          .addMethod(
            getSendConfigRuntimeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport,
                org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply>(
                  this, METHODID_SEND_CONFIG_RUNTIME)))
          .addMethod(
            getListAgentsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Empty,
                org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply>(
                  this, METHODID_LIST_AGENTS)))
          .addMethod(
            getListSslAuthoritiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Empty,
                org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply>(
                  this, METHODID_LIST_SSL_AUTHORITIES)))
          .addMethod(
            getAddSslAuthoritiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_ADD_SSL_AUTHORITIES)))
          .addMethod(
            getDropSslAuthoritiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_DROP_SSL_AUTHORITIES)))
          .addMethod(
            getKickAgentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http.grpc.beacon.Status>(
                  this, METHODID_KICK_AGENT)))
          .addMethod(
            getElaborateMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply>(
                  this, METHODID_ELABORATE_MESSAGE)))
          .addMethod(
            getListCommandsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply>(
                  this, METHODID_LIST_COMMANDS)))
          .addMethod(
            getCompleteCommandMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest,
                org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply>(
                  this, METHODID_COMPLETE_COMMAND)))
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
     * for agent registration
     * </pre>
     */
    public void register(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *rpc GetConfigTarget (Agent) returns (ConfigReply) {} // TODO: implementare la gestione del cambio configurazione e la gestione delle configurazioni su beacon server
     * </pre>
     */
    public void pollingCmdQueue(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPollingCmdQueueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscriptionCmdQueue(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscriptionCmdQueueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendChatMessage(org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendChatMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendCommandReply(org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendCommandReplyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendHealth(org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendHealthMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendLog(org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendLogMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendException(org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendExceptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendConfigRuntime(org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendConfigRuntimeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * for console
     * list agents connected 
     * </pre>
     */
    public void listAgents(org.ar4k.agent.tunnels.http.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListAgentsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * TODO gestione ssl con Beacon
     * </pre>
     */
    public void listSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListSslAuthoritiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddSslAuthoritiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void dropSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDropSslAuthoritiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * TODO gestione kick agent con Beacon
     * </pre>
     */
    public void kickAgent(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getKickAgentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * RPC on agent
     * </pre>
     */
    public void elaborateMessage(org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElaborateMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCommands(org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListCommandsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void completeCommand(org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCompleteCommandMethod(), getCallOptions()), request, responseObserver);
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
     * for agent registration
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply register(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *rpc GetConfigTarget (Agent) returns (ConfigReply) {} // TODO: implementare la gestione del cambio configurazione e la gestione delle configurazioni su beacon server
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage pollingCmdQueue(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getPollingCmdQueueMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> subscriptionCmdQueue(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscriptionCmdQueueMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status sendChatMessage(org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendChatMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status sendCommandReply(org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendCommandReplyMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status sendHealth(org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendHealthMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status sendLog(org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendLogMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status sendException(org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendExceptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply sendConfigRuntime(org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport request) {
      return blockingUnaryCall(
          getChannel(), getSendConfigRuntimeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * for console
     * list agents connected 
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply listAgents(org.ar4k.agent.tunnels.http.grpc.beacon.Empty request) {
      return blockingUnaryCall(
          getChannel(), getListAgentsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * TODO gestione ssl con Beacon
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply listSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.Empty request) {
      return blockingUnaryCall(
          getChannel(), getListSslAuthoritiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status addSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request) {
      return blockingUnaryCall(
          getChannel(), getAddSslAuthoritiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status dropSslAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request) {
      return blockingUnaryCall(
          getChannel(), getDropSslAuthoritiesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * TODO gestione kick agent con Beacon
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status kickAgent(org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getKickAgentMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * RPC on agent
     * </pre>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply elaborateMessage(org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest request) {
      return blockingUnaryCall(
          getChannel(), getElaborateMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply listCommands(org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest request) {
      return blockingUnaryCall(
          getChannel(), getListCommandsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply completeCommand(org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest request) {
      return blockingUnaryCall(
          getChannel(), getCompleteCommandMethod(), getCallOptions(), request);
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
     * for agent registration
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply> register(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *rpc GetConfigTarget (Agent) returns (ConfigReply) {} // TODO: implementare la gestione del cambio configurazione e la gestione delle configurazioni su beacon server
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage> pollingCmdQueue(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getPollingCmdQueueMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> sendChatMessage(
        org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getSendChatMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> sendCommandReply(
        org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendCommandReplyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> sendHealth(
        org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendHealthMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> sendLog(
        org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendLogMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> sendException(
        org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendExceptionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply> sendConfigRuntime(
        org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport request) {
      return futureUnaryCall(
          getChannel().newCall(getSendConfigRuntimeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * for console
     * list agents connected 
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply> listAgents(
        org.ar4k.agent.tunnels.http.grpc.beacon.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getListAgentsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * TODO gestione ssl con Beacon
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply> listSslAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getListSslAuthoritiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> addSslAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request) {
      return futureUnaryCall(
          getChannel().newCall(getAddSslAuthoritiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> dropSslAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority request) {
      return futureUnaryCall(
          getChannel().newCall(getDropSslAuthoritiesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * TODO gestione kick agent con Beacon
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.Status> kickAgent(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getKickAgentMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * RPC on agent
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply> elaborateMessage(
        org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getElaborateMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply> listCommands(
        org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListCommandsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply> completeCommand(
        org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCompleteCommandMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER = 0;
  private static final int METHODID_POLLING_CMD_QUEUE = 1;
  private static final int METHODID_SUBSCRIPTION_CMD_QUEUE = 2;
  private static final int METHODID_SEND_CHAT_MESSAGE = 3;
  private static final int METHODID_SEND_COMMAND_REPLY = 4;
  private static final int METHODID_SEND_HEALTH = 5;
  private static final int METHODID_SEND_LOG = 6;
  private static final int METHODID_SEND_EXCEPTION = 7;
  private static final int METHODID_SEND_CONFIG_RUNTIME = 8;
  private static final int METHODID_LIST_AGENTS = 9;
  private static final int METHODID_LIST_SSL_AUTHORITIES = 10;
  private static final int METHODID_ADD_SSL_AUTHORITIES = 11;
  private static final int METHODID_DROP_SSL_AUTHORITIES = 12;
  private static final int METHODID_KICK_AGENT = 13;
  private static final int METHODID_ELABORATE_MESSAGE = 14;
  private static final int METHODID_LIST_COMMANDS = 15;
  private static final int METHODID_COMPLETE_COMMAND = 16;

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
        case METHODID_POLLING_CMD_QUEUE:
          serviceImpl.pollingCmdQueue((org.ar4k.agent.tunnels.http.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage>) responseObserver);
          break;
        case METHODID_SUBSCRIPTION_CMD_QUEUE:
          serviceImpl.subscriptionCmdQueue((org.ar4k.agent.tunnels.http.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage>) responseObserver);
          break;
        case METHODID_SEND_CHAT_MESSAGE:
          serviceImpl.sendChatMessage((org.ar4k.agent.tunnels.http.grpc.beacon.ChatMessage) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_COMMAND_REPLY:
          serviceImpl.sendCommandReply((org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_HEALTH:
          serviceImpl.sendHealth((org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_LOG:
          serviceImpl.sendLog((org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_EXCEPTION:
          serviceImpl.sendException((org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_CONFIG_RUNTIME:
          serviceImpl.sendConfigRuntime((org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply>) responseObserver);
          break;
        case METHODID_LIST_AGENTS:
          serviceImpl.listAgents((org.ar4k.agent.tunnels.http.grpc.beacon.Empty) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply>) responseObserver);
          break;
        case METHODID_LIST_SSL_AUTHORITIES:
          serviceImpl.listSslAuthorities((org.ar4k.agent.tunnels.http.grpc.beacon.Empty) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply>) responseObserver);
          break;
        case METHODID_ADD_SSL_AUTHORITIES:
          serviceImpl.addSslAuthorities((org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_DROP_SSL_AUTHORITIES:
          serviceImpl.dropSslAuthorities((org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_KICK_AGENT:
          serviceImpl.kickAgent((org.ar4k.agent.tunnels.http.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_ELABORATE_MESSAGE:
          serviceImpl.elaborateMessage((org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply>) responseObserver);
          break;
        case METHODID_LIST_COMMANDS:
          serviceImpl.listCommands((org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply>) responseObserver);
          break;
        case METHODID_COMPLETE_COMMAND:
          serviceImpl.completeCommand((org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply>) responseObserver);
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
              .addMethod(getPollingCmdQueueMethod())
              .addMethod(getSubscriptionCmdQueueMethod())
              .addMethod(getSendChatMessageMethod())
              .addMethod(getSendCommandReplyMethod())
              .addMethod(getSendHealthMethod())
              .addMethod(getSendLogMethod())
              .addMethod(getSendExceptionMethod())
              .addMethod(getSendConfigRuntimeMethod())
              .addMethod(getListAgentsMethod())
              .addMethod(getListSslAuthoritiesMethod())
              .addMethod(getAddSslAuthoritiesMethod())
              .addMethod(getDropSslAuthoritiesMethod())
              .addMethod(getKickAgentMethod())
              .addMethod(getElaborateMessageMethod())
              .addMethod(getListCommandsMethod())
              .addMethod(getCompleteCommandMethod())
              .build();
        }
      }
    }
    return result;
  }
}
