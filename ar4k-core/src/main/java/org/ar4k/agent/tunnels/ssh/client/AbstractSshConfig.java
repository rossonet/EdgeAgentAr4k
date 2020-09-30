
package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.config.AbstractServiceConfig;
//import org.ar4k.agent.tunnels.socket.ISocketFactoryComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio tunnel SSH astratto
 */
public abstract class AbstractSshConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -5164761698374285171L;

	@Parameter(names = "--host", description = "host for the connection")
	public String host = null;

	@Parameter(names = "--port", description = "port for the connection")
	public int port = 0;

	@Parameter(names = "--username", description = "username for the connection")
	public String username = null;

	@Parameter(names = "--password", description = "password for the connection")
	public String password = null;

	@Parameter(names = "--authkey", description = "private auth key for the connection")
	public String authkey = null;

	@Parameter(names = "--trustAllCert", description = "enable/disable trust check of ssh server key")
	public boolean trustAllCert = true;

	@Parameter(names = "--soTimeout", description = "enable/disable SO_TIMEOUT with the specified timeout, in milliseconds")
	public Integer soTimeout = null;
	@Parameter(names = "--tcpNoDelay", description = "enable/disable TCP_NODELAY (disable/enable Nagle's algorithm)")
	public Boolean tcpNoDelay = null;
	@Parameter(names = "--keepAlive", description = "enable/disable SO_KEEPALIVE")
	public Boolean keepAlive = null;
	@Parameter(names = "--receiveBufferSize", description = "sets the SO_RCVBUF option to the specified value for this Socket")
	public Integer receiveBufferSize = null;
	@Parameter(names = "--reuseAddress", description = "enable/disable the SO_REUSEADDR socket option")
	public Boolean reuseAddress = null;

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractSshConfig [host=").append(host).append(", port=").append(port).append(", username=")
				.append(username).append(", password=").append(password).append(", authkey=").append(authkey)
				.append(", trustAllCert=").append(trustAllCert).append(", soTimeout=").append(soTimeout)
				.append(", tcpNoDelay=").append(tcpNoDelay).append(", keepAlive=").append(keepAlive)
				.append(", receiveBufferSize=").append(receiveBufferSize).append(", reuseAddress=").append(reuseAddress)
				.append("]");
		return builder.toString();
	}

}
