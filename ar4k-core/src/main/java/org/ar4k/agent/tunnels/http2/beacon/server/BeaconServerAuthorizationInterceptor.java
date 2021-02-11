package org.ar4k.agent.tunnels.http2.beacon.server;

import java.security.cert.Certificate;
import java.util.regex.Pattern;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

class BeaconServerAuthorizationInterceptor implements ServerInterceptor {

	/**
	 * 
	 */
	private final BeaconServer beaconServer;

	/**
	 * @param beaconServer
	 */
	BeaconServerAuthorizationInterceptor(BeaconServer beaconServer) {
		this.beaconServer = beaconServer;
	}

	@Override
	public <ReqT, RespT> Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> serverCall, final Metadata metadata,
			final ServerCallHandler<ReqT, RespT> serverCallHandler) {
		try {
			final SSLSession sslSession = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_SSL_SESSION);
			for (final Certificate i : sslSession.getPeerCertificates()) {
				BeaconServer.logger.debug(" - " + i.toString());
			}
			if (sslSession.isValid()) {
				authSslOk(serverCall, metadata);
			} else {
				authSslNotFound(serverCall, metadata);
			}
		} catch (final SSLPeerUnverifiedException e) {
			authSslNotFound(serverCall, metadata);
		}
		return serverCallHandler.startCall(serverCall, metadata);
	}

	private <ReqT, RespT> void authSslNotFound(final ServerCall<ReqT, RespT> serverCall, final Metadata metadata) {
		if (serverCall.getMethodDescriptor().getFullMethodName().equals("beacon.RpcServiceV1/Register")) {
			BeaconServer.logger.info("session not ok but register call");
		} else {
			BeaconServer.logger.info("session not ok");
			final io.grpc.Status status = io.grpc.Status.PERMISSION_DENIED;
			serverCall.close(status, metadata);
		}
	}

	private <ReqT, RespT> void authSslOk(ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
		if (this.beaconServer.filterActiveCommand != null && !this.beaconServer.filterActiveCommand.isEmpty()) {
			if (this.beaconServer.filterActiveCommandPattern == null) {
				this.beaconServer.filterActiveCommandPattern = Pattern.compile(this.beaconServer.filterActiveCommand);
			}
			final String metodo = serverCall.getMethodDescriptor().getFullMethodName();
			String name = "";
			try {
				name = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_SSL_SESSION).getPeerPrincipal().getName();
			} catch (final SSLPeerUnverifiedException e) {
				BeaconServer.logger.logException(e);
			}
			if (metodo.equals("beacon.RpcServiceV1/ListCommands")
					|| metodo.equals("beacon.RpcServiceV1/CompleteCommand")
					|| metodo.equals("beacon.RpcServiceV1/ElaborateMessage")
					|| metodo.equals("beacon.RpcServiceV1/KickAgent")
					|| metodo.equals("beacon.RpcServiceV1/ApproveAgentRequest")
					|| metodo.equals("beacon.RpcServiceV1/ListAgents")
					|| metodo.equals("beacon.RpcServiceV1/ListAgentsRequestComplete")
					|| metodo.equals("beacon.RpcServiceV1/ListAgentsRequestToDo")) {
				if (this.beaconServer.filterActiveCommandPattern.matcher(name).matches()) {
					BeaconServer.logger.debug("session ok");
				} else {
					BeaconServer.logger.info("client not authorized." + name + " not matches the regex filter "
							+ this.beaconServer.filterActiveCommand);
					final io.grpc.Status status = io.grpc.Status.PERMISSION_DENIED;
					serverCall.close(status, metadata);
				}
			}
		}
	}
}