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
package org.ar4k.agent.helper;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class NetworkHelper {

  private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(NetworkHelper.class.toString());

  private NetworkHelper() {
    throw new UnsupportedOperationException("Just for static usage");
  }

  public static boolean checkLocalPortAvailable(int port) {
    boolean portTaken = false;
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(port);
      socket.setReuseAddress(true);
    } catch (IOException e) {
      logger.logException(e);
      portTaken = true;
    } finally {
      if (socket != null)
        try {
          socket.close();
        } catch (IOException e) {
          logger.logException(e);
        }
    }
    return !portTaken;
  }

  public static int findAvailablePort(int defaultPort) {
    try {
      ServerSocket socket = new ServerSocket(0);
      socket.setReuseAddress(true);
      int port = socket.getLocalPort();
      socket.close();
      return port;
    } catch (IOException ex) {
      logger.logException(ex);
      return defaultPort;
    }
  }

  public static String getFirstMacAddressAsString() {
    try {
      InetAddress ip = InetAddress.getLocalHost();
      NetworkInterface network = NetworkInterface.getByInetAddress(ip);
      if (network != null) {
        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
          sb.append(String.format("%02X%s", mac[i], ""));
        }
        return sb.toString().toLowerCase();
      } else {
        return "xxxxxx";
      }
    } catch (Exception e) {
      logger.info("searching mac of localhost\n" + EdgeLogger.stackTraceToString(e, 5));
      return "xxxxxx";
    }
  }

  /*
   * @return the local hostname, if possible. Failure results in "localhost".
   */
  public static String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      logger.logException(e);
      return "localhost";
    }
  }

  /*
   * Given an address resolve it to as many unique addresses or hostnames as can
   * be found.
   *
   * @param address the address to resolve.
   *
   * @return the addresses and hostnames that were resolved from {@code address}.
   */
  public static Set<String> getHostnames(String address) {
    Set<String> hostnames = new HashSet<>();
    try {
      InetAddress inetAddress = InetAddress.getByName(address);

      if (inetAddress.isAnyLocalAddress()) {
        try {
          Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

          for (NetworkInterface ni : Collections.list(nis)) {
            Collections.list(ni.getInetAddresses()).forEach(ia -> {
              if (ia instanceof Inet4Address) {
                hostnames.add(ia.getHostName());
                hostnames.add(ia.getHostAddress());
                hostnames.add(ia.getCanonicalHostName());
              }
            });
          }
        } catch (SocketException e) {
          logger.logException(e);
        }
      } else {
        hostnames.add(inetAddress.getHostName());
        hostnames.add(inetAddress.getHostAddress());
        hostnames.add(inetAddress.getCanonicalHostName());
      }
    } catch (UnknownHostException e) {
      logger.logException(e);
    }
    return hostnames;
  }

}
