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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class NetworkHelper {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(NetworkHelper.class);

	private final static Long[] SUBNET_MASK = new Long[] { 4294934528L, 4294950912L, 4294959104L, 4294963200L,
			4294965248L, 4294966272L, 4294966784L, 4294967040L, 4294967168L, 4294967232L, 4294967264L, 4294967280L,
			4294967288L, 4294967292L, 4294967294L, 4294967295L };

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

	public static boolean isValidIPAddress(String ip) {
		String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
		String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
		Pattern p = Pattern.compile(regex);
		if (ip == null) {
			return false;
		}
		Matcher m = p.matcher(ip);
		return m.matches();
	}

	private static long ipAddressToLong(String ipAddress) {
		if (ipAddress != null) {
			String[] s = ipAddress.split("\\.");
			if (s != null && s.length == 4) {
				long result = 0;
				for (int i = 3; i >= 0; i--) {
					try {
						long n = Long.parseLong(s[3 - i]);
						result |= n << (i * 8);
					} catch (Exception ex) {
						return -1;
					}
				}
				return result;
			}
		}
		return -1;
	}

	public static boolean isValidSubnetMask(String subnetMask) {
		if (subnetMask != null && isValidIPAddress(subnetMask)) {
			long lSubnetMask = ipAddressToLong(subnetMask);
			if (lSubnetMask > 0) {
				return Arrays.asList(SUBNET_MASK).contains(lSubnetMask);
			}
		}
		return false;
	}

	public static boolean isValidMacAddress(String macAddress) {
		String regex = "^([0-9A-Fa-f]{2}[:-])" + "{5}([0-9A-Fa-f]{2})|" + "([0-9a-fA-F]{4}\\." + "[0-9a-fA-F]{4}\\."
				+ "[0-9a-fA-F]{4})$";
		Pattern p = Pattern.compile(regex);
		if (macAddress == null) {
			return false;
		}
		Matcher m = p.matcher(macAddress);
		return m.matches();
	}

	public static String getBrodcastForNetwork(String network, String mask) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<String> getAllIpsInNetworkSorted(String network, String mask) {
		// TODO Auto-generated method stub
		return null;
	}
}
