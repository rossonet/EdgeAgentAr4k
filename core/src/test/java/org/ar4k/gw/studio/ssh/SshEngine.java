/*
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
package org.ar4k.gw.studio.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. <andrea.ambrosini@rossonet.com>
 *
 */
public class SshEngine {

  List<Session> connessioniSsh = new ArrayList<Session>();
  List<Channel> canaliSsh = new ArrayList<Channel>();
  JSch jsch;

  // Aggiunge una sessione ed eventualmente istanzia la classe JSch
  Session addConnession(String username, String host, int port, String password) throws JSchException {
    if (jsch == null) {
      jsch = new JSch();
    }
    Session session = jsch.getSession(username, host, port);
    SSHUserInfo ui = new SSHUserInfo();
    ui.setPassword(password);
    session.setUserInfo(ui);
    session.connect();
    connessioniSsh.add(session);
    return session;
  }

  // Lista le sessioni
  List<Session> listConnession() {
    return connessioniSsh;
  }

  void addRTunnel(Session ricerca, int rport, String lhost, int lport) throws JSchException {
    ricerca.setPortForwardingR(rport, lhost, lport);
  }

  Integer addLTunnel(Session ricerca, int lport, String rhost, int rport) throws JSchException {
    return ricerca.setPortForwardingL(lport, rhost, rport);

  }

  // Gestisce l'esecuzione di comandi
  String esegui(Session ricerca, String comando) throws JSchException, IOException {
    String risultato = "";
    Channel channel;
    channel = ricerca.openChannel("exec");
    ((ChannelExec) channel).setCommand(comando);
    channel.setInputStream(null);
    ((ChannelExec) channel).setErrStream(System.err);
    InputStream input = channel.getInputStream();
    channel.connect();
    byte[] tmp = new byte[1024];
    while (true) {
      while (input.available() > 0) {
        int i = input.read(tmp, 0, 1024);
        if (i < 0)
          break;
        risultato += new String(tmp, 0, i);
      }
      if (channel.isClosed()) {
        if (input.available() > 0)
          continue;
        System.out.println("Comando: " + comando + " [stato:" + String.valueOf(channel.getExitStatus()) + "]");
        break;
      }
      try {
        Thread.sleep(500);
      } catch (Exception ee) {
      }
    }
    channel.disconnect();
    return risultato;
  }

  // connette la console
  Channel console(Session ricerca) throws JSchException {
    Channel channel = ricerca.openChannel("shell");
    channel.connect();
    canaliSsh.add(channel);
    return channel;
  }

  // connette la console
  Channel stream(Session ricerca, String hostTarget, Integer porta) throws JSchException {
    Channel channel = ricerca.getStreamForwarder(hostTarget, porta);
    channel.connect();
    canaliSsh.add(channel);
    return channel;
  }

  List<Integer> listChannel() {
    List<Integer> lista = new ArrayList<Integer>();
    for (Channel ch : canaliSsh) {
      lista.add(ch.getId());
    }
    return lista;
  }

}
