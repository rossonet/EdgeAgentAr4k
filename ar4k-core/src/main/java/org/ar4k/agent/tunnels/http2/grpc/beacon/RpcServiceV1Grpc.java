package org.ar4k.agent.tunnels.http2.grpc.beacon;

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
  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Register",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply> getRegisterMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply> getRegisterMethod;
    if ((getRegisterMethod = RpcServiceV1Grpc.getRegisterMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getRegisterMethod = RpcServiceV1Grpc.getRegisterMethod) == null) {
          RpcServiceV1Grpc.getRegisterMethod = getRegisterMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("Register"))
                  .build();
          }
        }
     }
     return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> getPollingCmdQueueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PollingCmdQueue",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> getPollingCmdQueueMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> getPollingCmdQueueMethod;
    if ((getPollingCmdQueueMethod = RpcServiceV1Grpc.getPollingCmdQueueMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getPollingCmdQueueMethod = RpcServiceV1Grpc.getPollingCmdQueueMethod) == null) {
          RpcServiceV1Grpc.getPollingCmdQueueMethod = getPollingCmdQueueMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "PollingCmdQueue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("PollingCmdQueue"))
                  .build();
          }
        }
     }
     return getPollingCmdQueueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> getSubscriptionCmdQueueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscriptionCmdQueue",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> getSubscriptionCmdQueueMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> getSubscriptionCmdQueueMethod;
    if ((getSubscriptionCmdQueueMethod = RpcServiceV1Grpc.getSubscriptionCmdQueueMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSubscriptionCmdQueueMethod = RpcServiceV1Grpc.getSubscriptionCmdQueueMethod) == null) {
          RpcServiceV1Grpc.getSubscriptionCmdQueueMethod = getSubscriptionCmdQueueMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SubscriptionCmdQueue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SubscriptionCmdQueue"))
                  .build();
          }
        }
     }
     return getSubscriptionCmdQueueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendChatMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendChatMessage",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendChatMessageMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendChatMessageMethod;
    if ((getSendChatMessageMethod = RpcServiceV1Grpc.getSendChatMessageMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendChatMessageMethod = RpcServiceV1Grpc.getSendChatMessageMethod) == null) {
          RpcServiceV1Grpc.getSendChatMessageMethod = getSendChatMessageMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendChatMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendChatMessage"))
                  .build();
          }
        }
     }
     return getSendChatMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendCommandReplyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendCommandReply",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendCommandReplyMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendCommandReplyMethod;
    if ((getSendCommandReplyMethod = RpcServiceV1Grpc.getSendCommandReplyMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendCommandReplyMethod = RpcServiceV1Grpc.getSendCommandReplyMethod) == null) {
          RpcServiceV1Grpc.getSendCommandReplyMethod = getSendCommandReplyMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendCommandReply"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendCommandReply"))
                  .build();
          }
        }
     }
     return getSendCommandReplyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendHealthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendHealth",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendHealthMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendHealthMethod;
    if ((getSendHealthMethod = RpcServiceV1Grpc.getSendHealthMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendHealthMethod = RpcServiceV1Grpc.getSendHealthMethod) == null) {
          RpcServiceV1Grpc.getSendHealthMethod = getSendHealthMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendHealth"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendHealth"))
                  .build();
          }
        }
     }
     return getSendHealthMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendLogMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendLog",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendLogMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendLogMethod;
    if ((getSendLogMethod = RpcServiceV1Grpc.getSendLogMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendLogMethod = RpcServiceV1Grpc.getSendLogMethod) == null) {
          RpcServiceV1Grpc.getSendLogMethod = getSendLogMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendLog"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendLog"))
                  .build();
          }
        }
     }
     return getSendLogMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendExceptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendException",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendExceptionMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getSendExceptionMethod;
    if ((getSendExceptionMethod = RpcServiceV1Grpc.getSendExceptionMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendExceptionMethod = RpcServiceV1Grpc.getSendExceptionMethod) == null) {
          RpcServiceV1Grpc.getSendExceptionMethod = getSendExceptionMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendException"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendException"))
                  .build();
          }
        }
     }
     return getSendExceptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getSendConfigRuntimeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendConfigRuntime",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getSendConfigRuntimeMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport, org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getSendConfigRuntimeMethod;
    if ((getSendConfigRuntimeMethod = RpcServiceV1Grpc.getSendConfigRuntimeMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getSendConfigRuntimeMethod = RpcServiceV1Grpc.getSendConfigRuntimeMethod) == null) {
          RpcServiceV1Grpc.getSendConfigRuntimeMethod = getSendConfigRuntimeMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport, org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "SendConfigRuntime"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("SendConfigRuntime"))
                  .build();
          }
        }
     }
     return getSendConfigRuntimeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getGetConfigRuntimeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetConfigRuntime",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getGetConfigRuntimeMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getGetConfigRuntimeMethod;
    if ((getGetConfigRuntimeMethod = RpcServiceV1Grpc.getGetConfigRuntimeMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getGetConfigRuntimeMethod = RpcServiceV1Grpc.getGetConfigRuntimeMethod) == null) {
          RpcServiceV1Grpc.getGetConfigRuntimeMethod = getGetConfigRuntimeMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "GetConfigRuntime"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("GetConfigRuntime"))
                  .build();
          }
        }
     }
     return getGetConfigRuntimeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getGetRuntimeProvidesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRuntimeProvides",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getGetRuntimeProvidesMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getGetRuntimeProvidesMethod;
    if ((getGetRuntimeProvidesMethod = RpcServiceV1Grpc.getGetRuntimeProvidesMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getGetRuntimeProvidesMethod = RpcServiceV1Grpc.getGetRuntimeProvidesMethod) == null) {
          RpcServiceV1Grpc.getGetRuntimeProvidesMethod = getGetRuntimeProvidesMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "GetRuntimeProvides"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("GetRuntimeProvides"))
                  .build();
          }
        }
     }
     return getGetRuntimeProvidesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getGetRuntimeRequiredMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRuntimeRequired",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getGetRuntimeRequiredMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getGetRuntimeRequiredMethod;
    if ((getGetRuntimeRequiredMethod = RpcServiceV1Grpc.getGetRuntimeRequiredMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getGetRuntimeRequiredMethod = RpcServiceV1Grpc.getGetRuntimeRequiredMethod) == null) {
          RpcServiceV1Grpc.getGetRuntimeRequiredMethod = getGetRuntimeRequiredMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "GetRuntimeRequired"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("GetRuntimeRequired"))
                  .build();
          }
        }
     }
     return getGetRuntimeRequiredMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply> getListAgentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAgents",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Empty.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply> getListAgentsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply> getListAgentsMethod;
    if ((getListAgentsMethod = RpcServiceV1Grpc.getListAgentsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListAgentsMethod = RpcServiceV1Grpc.getListAgentsMethod) == null) {
          RpcServiceV1Grpc.getListAgentsMethod = getListAgentsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListAgents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListAgents"))
                  .build();
          }
        }
     }
     return getListAgentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> getListAgentsRequestCompleteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAgentsRequestComplete",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Empty.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> getListAgentsRequestCompleteMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> getListAgentsRequestCompleteMethod;
    if ((getListAgentsRequestCompleteMethod = RpcServiceV1Grpc.getListAgentsRequestCompleteMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListAgentsRequestCompleteMethod = RpcServiceV1Grpc.getListAgentsRequestCompleteMethod) == null) {
          RpcServiceV1Grpc.getListAgentsRequestCompleteMethod = getListAgentsRequestCompleteMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListAgentsRequestComplete"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListAgentsRequestComplete"))
                  .build();
          }
        }
     }
     return getListAgentsRequestCompleteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> getListAgentsRequestToDoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAgentsRequestToDo",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Empty.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> getListAgentsRequestToDoMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> getListAgentsRequestToDoMethod;
    if ((getListAgentsRequestToDoMethod = RpcServiceV1Grpc.getListAgentsRequestToDoMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListAgentsRequestToDoMethod = RpcServiceV1Grpc.getListAgentsRequestToDoMethod) == null) {
          RpcServiceV1Grpc.getListAgentsRequestToDoMethod = getListAgentsRequestToDoMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Empty, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListAgentsRequestToDo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListAgentsRequestToDo"))
                  .build();
          }
        }
     }
     return getListAgentsRequestToDoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getApproveAgentRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ApproveAgentRequest",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getApproveAgentRequestMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getApproveAgentRequestMethod;
    if ((getApproveAgentRequestMethod = RpcServiceV1Grpc.getApproveAgentRequestMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getApproveAgentRequestMethod = RpcServiceV1Grpc.getApproveAgentRequestMethod) == null) {
          RpcServiceV1Grpc.getApproveAgentRequestMethod = getApproveAgentRequestMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ApproveAgentRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ApproveAgentRequest"))
                  .build();
          }
        }
     }
     return getApproveAgentRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getKickAgentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "KickAgent",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
      org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getKickAgentMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Status> getKickAgentMethod;
    if ((getKickAgentMethod = RpcServiceV1Grpc.getKickAgentMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getKickAgentMethod = RpcServiceV1Grpc.getKickAgentMethod) == null) {
          RpcServiceV1Grpc.getKickAgentMethod = getKickAgentMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "KickAgent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("KickAgent"))
                  .build();
          }
        }
     }
     return getKickAgentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply> getElaborateMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ElaborateMessage",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply> getElaborateMessageMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply> getElaborateMessageMethod;
    if ((getElaborateMessageMethod = RpcServiceV1Grpc.getElaborateMessageMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getElaborateMessageMethod = RpcServiceV1Grpc.getElaborateMessageMethod) == null) {
          RpcServiceV1Grpc.getElaborateMessageMethod = getElaborateMessageMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ElaborateMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ElaborateMessage"))
                  .build();
          }
        }
     }
     return getElaborateMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply> getListCommandsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCommands",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply> getListCommandsMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply> getListCommandsMethod;
    if ((getListCommandsMethod = RpcServiceV1Grpc.getListCommandsMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getListCommandsMethod = RpcServiceV1Grpc.getListCommandsMethod) == null) {
          RpcServiceV1Grpc.getListCommandsMethod = getListCommandsMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "ListCommands"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RpcServiceV1MethodDescriptorSupplier("ListCommands"))
                  .build();
          }
        }
     }
     return getListCommandsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply> getCompleteCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CompleteCommand",
      requestType = org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest.class,
      responseType = org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest,
      org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply> getCompleteCommandMethod() {
    io.grpc.MethodDescriptor<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply> getCompleteCommandMethod;
    if ((getCompleteCommandMethod = RpcServiceV1Grpc.getCompleteCommandMethod) == null) {
      synchronized (RpcServiceV1Grpc.class) {
        if ((getCompleteCommandMethod = RpcServiceV1Grpc.getCompleteCommandMethod) == null) {
          RpcServiceV1Grpc.getCompleteCommandMethod = getCompleteCommandMethod = 
              io.grpc.MethodDescriptor.<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest, org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "beacon.RpcServiceV1", "CompleteCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply.getDefaultInstance()))
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
     */
    public void register(org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     */
    public void pollingCmdQueue(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getPollingCmdQueueMethod(), responseObserver);
    }

    /**
     */
    public void subscriptionCmdQueue(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscriptionCmdQueueMethod(), responseObserver);
    }

    /**
     */
    public void sendChatMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendChatMessageMethod(), responseObserver);
    }

    /**
     */
    public void sendCommandReply(org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendCommandReplyMethod(), responseObserver);
    }

    /**
     */
    public void sendHealth(org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendHealthMethod(), responseObserver);
    }

    /**
     */
    public void sendLog(org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendLogMethod(), responseObserver);
    }

    /**
     */
    public void sendException(org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getSendExceptionMethod(), responseObserver);
    }

    /**
     */
    public void sendConfigRuntime(org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendConfigRuntimeMethod(), responseObserver);
    }

    /**
     */
    public void getConfigRuntime(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetConfigRuntimeMethod(), responseObserver);
    }

    /**
     */
    public void getRuntimeProvides(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRuntimeProvidesMethod(), responseObserver);
    }

    /**
     */
    public void getRuntimeRequired(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRuntimeRequiredMethod(), responseObserver);
    }

    /**
     */
    public void listAgents(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListAgentsMethod(), responseObserver);
    }

    /**
     */
    public void listAgentsRequestComplete(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListAgentsRequestCompleteMethod(), responseObserver);
    }

    /**
     */
    public void listAgentsRequestToDo(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListAgentsRequestToDoMethod(), responseObserver);
    }

    /**
     */
    public void approveAgentRequest(org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getApproveAgentRequestMethod(), responseObserver);
    }

    /**
     */
    public void kickAgent(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getKickAgentMethod(), responseObserver);
    }

    /**
     */
    public void elaborateMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply> responseObserver) {
      asyncUnimplementedUnaryCall(getElaborateMessageMethod(), responseObserver);
    }

    /**
     */
    public void listCommands(org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getListCommandsMethod(), responseObserver);
    }

    /**
     */
    public void completeCommand(org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCompleteCommandMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply>(
                  this, METHODID_REGISTER)))
          .addMethod(
            getPollingCmdQueueMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage>(
                  this, METHODID_POLLING_CMD_QUEUE)))
          .addMethod(
            getSubscriptionCmdQueueMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage>(
                  this, METHODID_SUBSCRIPTION_CMD_QUEUE)))
          .addMethod(
            getSendChatMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_SEND_CHAT_MESSAGE)))
          .addMethod(
            getSendCommandReplyMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_SEND_COMMAND_REPLY)))
          .addMethod(
            getSendHealthMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_SEND_HEALTH)))
          .addMethod(
            getSendLogMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_SEND_LOG)))
          .addMethod(
            getSendExceptionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_SEND_EXCEPTION)))
          .addMethod(
            getSendConfigRuntimeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply>(
                  this, METHODID_SEND_CONFIG_RUNTIME)))
          .addMethod(
            getGetConfigRuntimeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply>(
                  this, METHODID_GET_CONFIG_RUNTIME)))
          .addMethod(
            getGetRuntimeProvidesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply>(
                  this, METHODID_GET_RUNTIME_PROVIDES)))
          .addMethod(
            getGetRuntimeRequiredMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply>(
                  this, METHODID_GET_RUNTIME_REQUIRED)))
          .addMethod(
            getListAgentsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply>(
                  this, METHODID_LIST_AGENTS)))
          .addMethod(
            getListAgentsRequestCompleteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply>(
                  this, METHODID_LIST_AGENTS_REQUEST_COMPLETE)))
          .addMethod(
            getListAgentsRequestToDoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Empty,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply>(
                  this, METHODID_LIST_AGENTS_REQUEST_TO_DO)))
          .addMethod(
            getApproveAgentRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_APPROVE_AGENT_REQUEST)))
          .addMethod(
            getKickAgentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.Agent,
                org.ar4k.agent.tunnels.http2.grpc.beacon.Status>(
                  this, METHODID_KICK_AGENT)))
          .addMethod(
            getElaborateMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply>(
                  this, METHODID_ELABORATE_MESSAGE)))
          .addMethod(
            getListCommandsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply>(
                  this, METHODID_LIST_COMMANDS)))
          .addMethod(
            getCompleteCommandMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest,
                org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply>(
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
     */
    public void register(org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pollingCmdQueue(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPollingCmdQueueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscriptionCmdQueue(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscriptionCmdQueueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendChatMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendChatMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendCommandReply(org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendCommandReplyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendHealth(org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendHealthMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendLog(org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendLogMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendException(org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendExceptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendConfigRuntime(org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendConfigRuntimeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getConfigRuntime(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetConfigRuntimeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRuntimeProvides(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetRuntimeProvidesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRuntimeRequired(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetRuntimeRequiredMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listAgents(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListAgentsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listAgentsRequestComplete(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListAgentsRequestCompleteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listAgentsRequestToDo(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListAgentsRequestToDoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void approveAgentRequest(org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getApproveAgentRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void kickAgent(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getKickAgentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void elaborateMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElaborateMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCommands(org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListCommandsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void completeCommand(org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest request,
        io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply> responseObserver) {
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
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply register(org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage pollingCmdQueue(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getPollingCmdQueueMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> subscriptionCmdQueue(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscriptionCmdQueueMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status sendChatMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendChatMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status sendCommandReply(org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendCommandReplyMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status sendHealth(org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendHealthMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status sendLog(org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendLogMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status sendException(org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendExceptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply sendConfigRuntime(org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport request) {
      return blockingUnaryCall(
          getChannel(), getSendConfigRuntimeMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply getConfigRuntime(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getGetConfigRuntimeMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply getRuntimeProvides(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getGetRuntimeProvidesMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply getRuntimeRequired(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getGetRuntimeRequiredMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply listAgents(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request) {
      return blockingUnaryCall(
          getChannel(), getListAgentsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply listAgentsRequestComplete(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request) {
      return blockingUnaryCall(
          getChannel(), getListAgentsRequestCompleteMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply listAgentsRequestToDo(org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request) {
      return blockingUnaryCall(
          getChannel(), getListAgentsRequestToDoMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status approveAgentRequest(org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest request) {
      return blockingUnaryCall(
          getChannel(), getApproveAgentRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status kickAgent(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return blockingUnaryCall(
          getChannel(), getKickAgentMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply elaborateMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest request) {
      return blockingUnaryCall(
          getChannel(), getElaborateMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply listCommands(org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest request) {
      return blockingUnaryCall(
          getChannel(), getListCommandsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply completeCommand(org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest request) {
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
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply> register(
        org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage> pollingCmdQueue(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getPollingCmdQueueMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> sendChatMessage(
        org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getSendChatMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> sendCommandReply(
        org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendCommandReplyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> sendHealth(
        org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendHealthMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> sendLog(
        org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendLogMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> sendException(
        org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendExceptionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> sendConfigRuntime(
        org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport request) {
      return futureUnaryCall(
          getChannel().newCall(getSendConfigRuntimeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply> getConfigRuntime(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getGetConfigRuntimeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getRuntimeProvides(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getGetRuntimeProvidesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply> getRuntimeRequired(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getGetRuntimeRequiredMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply> listAgents(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getListAgentsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> listAgentsRequestComplete(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getListAgentsRequestCompleteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply> listAgentsRequestToDo(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getListAgentsRequestToDoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> approveAgentRequest(
        org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getApproveAgentRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.Status> kickAgent(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent request) {
      return futureUnaryCall(
          getChannel().newCall(getKickAgentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply> elaborateMessage(
        org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getElaborateMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply> listCommands(
        org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListCommandsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply> completeCommand(
        org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest request) {
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
  private static final int METHODID_GET_CONFIG_RUNTIME = 9;
  private static final int METHODID_GET_RUNTIME_PROVIDES = 10;
  private static final int METHODID_GET_RUNTIME_REQUIRED = 11;
  private static final int METHODID_LIST_AGENTS = 12;
  private static final int METHODID_LIST_AGENTS_REQUEST_COMPLETE = 13;
  private static final int METHODID_LIST_AGENTS_REQUEST_TO_DO = 14;
  private static final int METHODID_APPROVE_AGENT_REQUEST = 15;
  private static final int METHODID_KICK_AGENT = 16;
  private static final int METHODID_ELABORATE_MESSAGE = 17;
  private static final int METHODID_LIST_COMMANDS = 18;
  private static final int METHODID_COMPLETE_COMMAND = 19;

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
          serviceImpl.register((org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply>) responseObserver);
          break;
        case METHODID_POLLING_CMD_QUEUE:
          serviceImpl.pollingCmdQueue((org.ar4k.agent.tunnels.http2.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage>) responseObserver);
          break;
        case METHODID_SUBSCRIPTION_CMD_QUEUE:
          serviceImpl.subscriptionCmdQueue((org.ar4k.agent.tunnels.http2.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage>) responseObserver);
          break;
        case METHODID_SEND_CHAT_MESSAGE:
          serviceImpl.sendChatMessage((org.ar4k.agent.tunnels.http2.grpc.beacon.ChatMessage) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_COMMAND_REPLY:
          serviceImpl.sendCommandReply((org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_HEALTH:
          serviceImpl.sendHealth((org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_LOG:
          serviceImpl.sendLog((org.ar4k.agent.tunnels.http2.grpc.beacon.LogRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_EXCEPTION:
          serviceImpl.sendException((org.ar4k.agent.tunnels.http2.grpc.beacon.ExceptionRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_SEND_CONFIG_RUNTIME:
          serviceImpl.sendConfigRuntime((org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply>) responseObserver);
          break;
        case METHODID_GET_CONFIG_RUNTIME:
          serviceImpl.getConfigRuntime((org.ar4k.agent.tunnels.http2.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply>) responseObserver);
          break;
        case METHODID_GET_RUNTIME_PROVIDES:
          serviceImpl.getRuntimeProvides((org.ar4k.agent.tunnels.http2.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply>) responseObserver);
          break;
        case METHODID_GET_RUNTIME_REQUIRED:
          serviceImpl.getRuntimeRequired((org.ar4k.agent.tunnels.http2.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply>) responseObserver);
          break;
        case METHODID_LIST_AGENTS:
          serviceImpl.listAgents((org.ar4k.agent.tunnels.http2.grpc.beacon.Empty) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply>) responseObserver);
          break;
        case METHODID_LIST_AGENTS_REQUEST_COMPLETE:
          serviceImpl.listAgentsRequestComplete((org.ar4k.agent.tunnels.http2.grpc.beacon.Empty) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply>) responseObserver);
          break;
        case METHODID_LIST_AGENTS_REQUEST_TO_DO:
          serviceImpl.listAgentsRequestToDo((org.ar4k.agent.tunnels.http2.grpc.beacon.Empty) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply>) responseObserver);
          break;
        case METHODID_APPROVE_AGENT_REQUEST:
          serviceImpl.approveAgentRequest((org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_KICK_AGENT:
          serviceImpl.kickAgent((org.ar4k.agent.tunnels.http2.grpc.beacon.Agent) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.Status>) responseObserver);
          break;
        case METHODID_ELABORATE_MESSAGE:
          serviceImpl.elaborateMessage((org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply>) responseObserver);
          break;
        case METHODID_LIST_COMMANDS:
          serviceImpl.listCommands((org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply>) responseObserver);
          break;
        case METHODID_COMPLETE_COMMAND:
          serviceImpl.completeCommand((org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest) request,
              (io.grpc.stub.StreamObserver<org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply>) responseObserver);
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
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.getDescriptor();
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
              .addMethod(getGetConfigRuntimeMethod())
              .addMethod(getGetRuntimeProvidesMethod())
              .addMethod(getGetRuntimeRequiredMethod())
              .addMethod(getListAgentsMethod())
              .addMethod(getListAgentsRequestCompleteMethod())
              .addMethod(getListAgentsRequestToDoMethod())
              .addMethod(getApproveAgentRequestMethod())
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
