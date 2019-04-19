/**
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.tunnels.sshClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.tunnels.core.AbstractTunnelComponent;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelDirectTCPIP;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Servizio tunnel SSH.
 *
 */
public class SshTunnel extends AbstractTunnelComponent {

  private SshConfig configuration = null;

  private JSch jsch = null;
  private Session session = null;
  private Channel channel = null;
  private SocketChannel socketChannel = null;

  private void connetti() {
    try {
      jsch = new JSch();
      if (configuration.authkey != null)
        jsch.addIdentity(configuration.authkey);
      /*
       * System.out.println(AbstractTunnelComponent.getSocks(configuration.
       * connectionSock).getLocalAddress()); InetAddress host =
       * AbstractTunnelComponent.getSocks(configuration.connectionSock).
       * getLocalAddress(); int port =
       * AbstractTunnelComponent.getSocks(configuration.connectionSock).getLocalPort()
       * ;
       * 
       * session = jsch.getSession(configuration.username, host.getHostAddress(),
       * port);
       */
      // session.setSocketFactory(sfactory);
      SSHUserInfo ui = new SSHUserInfo();
      if (configuration.password != null)
        ui.setPassword(configuration.password);
      session.setUserInfo(ui);
      session.connect();
      ChannelDirectTCPIP channel = (ChannelDirectTCPIP) session.openChannel("direct-tcpip");
      channel.setHost(configuration.redirectServer);
      channel.setPort(configuration.redirectPort);
      channel.setOrgIPAddress(configuration.bindHost);
      channel.setOrgPort(configuration.bindPort);
      channel.connect();
      InetSocketAddress a = new InetSocketAddress("localhost", configuration.bindPort);
      socketChannel = SocketChannel.open();
      socketChannel.connect(a);
      // socket = socketChannel.socket();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public SshConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((SshConfig) configuration);
  }

  @Override
  public void kill() {
    if (session != null)
      session.disconnect();
    session = null;
    jsch = null;
  }

  @Override
  protected void finalize() {
    if (session != null)
      session.disconnect();
    session = null;
    jsch = null;
  }

  @Override
  public void init() {
    //checkConnession();
  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonElement getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }

}
