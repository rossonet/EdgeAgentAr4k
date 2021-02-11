package org.ar4k.agent.tunnels.http2.beacon.client;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;

public class BeaconClientAuthorizationInterceptor implements ClientInterceptor {

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
			CallOptions callOptions, Channel next) {
		// System.out.println("client callOptions.getAuthority ->" +
		// callOptions.getAuthority());
		return next.newCall(method, callOptions);
	}

}
