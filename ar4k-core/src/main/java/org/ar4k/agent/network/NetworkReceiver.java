package org.ar4k.agent.network;

/*
import io.grpc.netty.shaded.io.netty.channel.ChannelFuture;
import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel;
*/
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;

public interface NetworkReceiver extends AutoCloseable {

	public static enum NetworkStatus {
		INIT, ACTIVE, INACTIVE, FAULT
	}

	long getTunnelId();

	NetworkStatus getStatus();

	long getPacketSend();

	long getPacketReceived();

	long getPacketError();

	int getWaitingPackagesCount();

	void incrementPacketSend();

	void incrementPacketReceived();

	void incrementPacketError();

	void confirmPacketReceived(long messageUuid);

	void exceptionPacketReceived(long messageUuid);

	ChannelFuture getOrCreateClientHandler(long sessionId);

	SocketChannel getOrCreateServerSocketChannel(long sessionId);

	void deleteClientHandler(long sessionId);

	void deleteServerSocketChannel(long sessionId);

}
