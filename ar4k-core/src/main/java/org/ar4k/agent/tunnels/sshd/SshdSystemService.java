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
package org.ar4k.agent.tunnels.sshd;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.sshd.common.channel.ChannelListener;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.forward.DefaultForwarderFactory;
import org.apache.sshd.common.forward.PortForwardingEventListener;
import org.apache.sshd.common.future.CloseFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionListener;
import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.sshd.firstCommand.Ar4kProcessShellFactory;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni ssh.
 *
 */
//TODO verificare tunnel R
public class SshdSystemService implements Ar4kComponent, SshFutureListener<CloseFuture>, SessionListener,
    ChannelListener, PortForwardingEventListener {

  @Override
  public void establishingExplicitTunnel(Session session, SshdSocketAddress local, SshdSocketAddress remote,
      boolean localForwarding) throws IOException {
    if (local != null && remote != null) {
      logger.info("new ssh tunnel from " + local.getHostName() + ":" + local.getPort() + " to " + remote.getHostName()
          + ":" + remote.getPort());
    }
    PortForwardingEventListener.super.establishingExplicitTunnel(session, local, remote, localForwarding);
  }

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SshdSystemService.class.toString());

  // iniettata vedi set/get
  private SshdSystemConfig configuration = null;
  private SshServer server = null;
  private final static Gson gson = new GsonBuilder().create();

  private Anima anima = null;

  private DataAddress dataspace = null;

  private ServiceStatus serviceStatus = ServiceStatus.INIT;

  @Override
  public SshdSystemConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    this.configuration = ((SshdSystemConfig) configuration);
  }

  @Override
  public synchronized void init() {
    server = SshServer.setUpDefaultServer();
    PasswordAuthenticator passwordAuthenticator = new Ar4kPasswordAuthenticator(anima);
    server.setPasswordAuthenticator(passwordAuthenticator);
    PublickeyAuthenticator publickeyAuthenticator = new Ar4kPublickeyAuthenticator(
        Paths.get(ConfigHelper.resolveWorkingString(configuration.authorizedKeys, true)));
    server.setPublickeyAuthenticator(publickeyAuthenticator);
    server.setHost(configuration.bindHost);
    server.setPort(configuration.port);
    server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
    ProcessShellFactory shellFactory = new Ar4kProcessShellFactory(configuration.cmd.split("\\s+"));
    server.setShellFactory(shellFactory);
    server.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
    server.addCloseFutureListener(this);
    server.addSessionListener(this);
    server.addChannelListener(this);
    server.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
    // ForwardingFilterFactory forwarderFactory = new Ar4kForwardingFilterFactory();
    server.setForwarderFactory(DefaultForwarderFactory.INSTANCE);
    server.addPortForwardingEventListener(this);
    server.setFileSystemFactory(new VirtualFileSystemFactory());
    try {
      server.start();
      serviceStatus = ServiceStatus.RUNNING;
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @Override
  public void close() throws IOException {
    kill();
  }

  @Override
  public void kill() {
    serviceStatus = ServiceStatus.KILLED;
    if (server != null)
      try {
        server.stop();
        server.close();
      } catch (IOException e) {
        logger.logException(e);
      }
  }

  @Override
  public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
    return serviceStatus;
  }

  @Override
  public Anima getAnima() {
    return anima;
  }

  @Override
  public DataAddress getDataAddress() {
    return dataspace;
  }

  @Override
  public void setDataAddress(DataAddress dataAddress) {
    dataspace = dataAddress;
  }

  @Override
  public void setAnima(Anima anima) {
    this.anima = anima;
  }

  @Override
  public JSONObject getDescriptionJson() {
    Map<String, String> data = new HashMap<>();
    if (server != null) {
      int sc = 0;
      for (SocketAddress ia : server.getBoundAddresses()) {
        if (ia instanceof InetSocketAddress) {
          InetSocketAddress a = (InetSocketAddress) ia;
          InetAddress inetAddress = a.getAddress();
          int inetPort = a.getPort();
          if (inetAddress instanceof Inet4Address)
            data.put("bound-address-ipv4-" + sc, inetAddress.toString() + ":" + inetPort);
          else if (inetAddress instanceof Inet6Address)
            data.put("bound-address-ipv6-" + sc, inetAddress.toString() + ":" + inetPort);
          else
            data.put("bound-address-" + sc, inetAddress.toString());
        } else {
          logger.debug("not an internet protocol socket..");
        }
        sc++;
      }
      int ssc = 0;
      for (AbstractSession s : server.getActiveSessions()) {
        data.put("active-session-" + ssc, s.toString());
        ssc++;
      }
      data.put("version", server.getVersion());
    }
    data.put("configuration", configuration.toString());
    String json = gson.toJson(data);
    return new JSONObject(json);
  }

  @Override
  public void operationComplete(CloseFuture future) {
    reloadServer();
  }

  public synchronized void reloadServer() {
    if (serviceStatus.equals(ServiceStatus.RUNNING)) {
      logger.info("server sshd closed in running state, restart after 60 seconds");
      kill();
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        logger.logExceptionDebug(e);
      }
      init();
    }
  }

}
