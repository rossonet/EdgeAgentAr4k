package org.ar4k.agent.tunnels.http2.beacon.socket.server;

import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public interface ITunnelRunnerBeaconServer extends AutoCloseable {

	long getTunnelId();

	Agent getServerAgent();

	void onNext(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver);

	Agent getClientAgent();

	void onError(Throwable t, StreamObserver<TunnelMessage> responseObserver);

	void onCompleted(StreamObserver<TunnelMessage> responseObserver);

}