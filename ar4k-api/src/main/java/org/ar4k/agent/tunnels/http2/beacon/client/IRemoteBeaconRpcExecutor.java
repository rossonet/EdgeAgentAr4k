package org.ar4k.agent.tunnels.http2.beacon.client;

import org.ar4k.agent.rpc.RpcExecutor;

public interface IRemoteBeaconRpcExecutor extends RpcExecutor{

	RemoteBeaconAgentHomunculus getRemoteHomunculus();

}
