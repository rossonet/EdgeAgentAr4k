package org.ar4k.agent.exception;

public class ExceptionNetworkEvent extends BeaconTunnelException {

	public ExceptionNetworkEvent(String cause) {
		super(cause);
	}

	public ExceptionNetworkEvent(Exception cause) {
		super(cause);
	}

	private static final long serialVersionUID = 6433787125090619247L;

}
