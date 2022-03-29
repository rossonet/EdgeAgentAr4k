package org.ar4k.agent.tunnels.http2.beacon.server;

import org.ar4k.agent.tunnels.http2.grpc.beacon.AddressSpace;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.DataServiceV1Grpc;
import org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData;
import org.ar4k.agent.tunnels.http2.grpc.beacon.PollingRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestWrite;
import org.ar4k.agent.tunnels.http2.grpc.beacon.SubscribeRequest;

import io.grpc.stub.StreamObserver;

//TO______DO DATASERVICE in beacon server/client
class BeaconServerDataService extends DataServiceV1Grpc.DataServiceV1ImplBase {

	/**
	 * 
	 */
	private final BeaconServer beaconServer;

	/**
	 * @param beaconServer
	 */
	BeaconServerDataService(BeaconServer beaconServer) {
		this.beaconServer = beaconServer;
	}

	@Override
	public void getRemoteAddressSpace(Agent request, StreamObserver<AddressSpace> responseObserver) {
		// DATASERVICE Auto-generated method stub
		super.getRemoteAddressSpace(request, responseObserver);
	}

	@Override
	public void polling(PollingRequest request, StreamObserver<FlowMessageData> responseObserver) {
		// DATASERVICE Auto-generated method stub
		super.polling(request, responseObserver);
	}

	@Override
	public void sendAddressSpace(AddressSpace request, StreamObserver<AddressSpace> responseObserver) {
		// DATASERVICE Auto-generated method stub
		super.sendAddressSpace(request, responseObserver);
	}

	@Override
	public void subscription(SubscribeRequest request, StreamObserver<FlowMessageData> responseObserver) {
		// DATASERVICE Auto-generated method stub
		super.subscription(request, responseObserver);
	}

	@Override
	public void write(RequestWrite request, StreamObserver<FlowMessageData> responseObserver) {
		// DATASERVICE Auto-generated method stub
		super.write(request, responseObserver);
	}

	@Override
	public StreamObserver<RequestWrite> writeSubscription(StreamObserver<FlowMessageData> responseObserver) {
		// DATASERVICE Auto-generated method stub
		return super.writeSubscription(responseObserver);
	}
}