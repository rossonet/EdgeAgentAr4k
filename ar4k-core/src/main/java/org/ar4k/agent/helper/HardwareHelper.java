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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.ar4k.agent.exception.EdgeException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import oshi.hardware.Display;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.UsbDevice;
import oshi.software.os.OperatingSystem;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Helper generico.
 *
 */
public class HardwareHelper {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(HardwareHelper.class);

	public static final boolean DEBUG_FREEZE_HAL = false;

	private static final int BUFFER_SIZE = 512;

	private HardwareHelper() {
		throw new UnsupportedOperationException("Just for static usage");
	}

	public static long downloadFileFromUrl(String filename, String url) throws MalformedURLException, IOException {
		final long result = 0L;
		FileOutputStream fileOutputStream = null;
		ReadableByteChannel readableByteChannel = null;
		FileChannel fileChannel = null;
		try {
			readableByteChannel = Channels.newChannel(new URL(url).openStream());
			fileOutputStream = new FileOutputStream(filename);
			fileChannel = fileOutputStream.getChannel();
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			fileOutputStream.flush();
		} finally {
			if (fileChannel != null)
				fileChannel.close();
			if (fileOutputStream != null)
				fileOutputStream.close();
		}
		return result;
	}

	public static void extractTarGz(String path, InputStream in) throws IOException {
		final GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
			TarArchiveEntry entry = null;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				/** If the entry is a directory, create the directory. **/
				if (entry.isDirectory()) {
					final File f = new File(path + "/" + entry.getName());
					final boolean created = f.mkdirs();
					if (!created) {
						throw new EdgeException("Unable to create directory " + f.getAbsolutePath()
								+ ", during extraction of archive contents.");
					}
				} else {
					int count;
					final byte data[] = new byte[BUFFER_SIZE];
					final FileOutputStream fos = new FileOutputStream(path + "/" + entry.getName(), false);
					try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
						while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
							dest.write(data, 0, count);
						}
					}
				}
			}
		}
	}

	public static HardwareInfoData getSystemInfo() throws IOException, InterruptedException, ParseException {
		final HardwareInfoData dato = new HardwareInfoData();
		final oshi.SystemInfo si = new oshi.SystemInfo();
		final Runtime runtime = Runtime.getRuntime();
		final OperatingSystemMXBean mbean = ManagementFactory.getOperatingSystemMXBean();
		dato.setSystemLoadAverage(mbean.getSystemLoadAverage());
		dato.setHardwareRuntimetotalmemory(runtime.totalMemory());
		dato.setHardwareRuntimefreememory(runtime.freeMemory());
		final List<RootFileSystem> fileSystem = new ArrayList<>();
		for (final File root : File.listRoots()) {
			final RootFileSystem rootFs = new RootFileSystem();
			rootFs.setAbsolutePath(root.getAbsoluteFile());
			rootFs.setFreeSpace(root.getFreeSpace());
			rootFs.setTotalSpace(root.getTotalSpace());
			final List<RootFileSystem> childs = new ArrayList<>();
			if (root != null && root.listFiles() != null)
				for (final File figlio : root.listFiles()) {
					final RootFileSystem figlioFs = new RootFileSystem();
					figlioFs.setAbsolutePath(figlio.getAbsoluteFile());
					figlioFs.setFreeSpace(figlio.getFreeSpace());
					figlioFs.setTotalSpace(figlio.getTotalSpace());
					childs.add(figlioFs);
				}
			rootFs.setChilds(childs);
			fileSystem.add(rootFs);
		}
		dato.setHardwareFilelistroots(fileSystem);
		final HardwareAbstractionLayer hal = si.getHardware();
		if (DEBUG_FREEZE_HAL) {
			try {
				dato.setHardwareComputersystem(hal.getComputerSystem());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareDisk(hal.getDiskStores().toArray(new HWDiskStore[0]));
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareDislay(hal.getDisplays().toArray(new Display[0]));
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareMemorytotal(hal.getMemory().getTotal());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareMemoryavailable(hal.getMemory().getAvailable());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareSwaptotal(hal.getMemory().getVirtualMemory().getSwapTotal());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareSwapused(hal.getMemory().getVirtualMemory().getSwapUsed());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareNetwork(hal.getNetworkIFs().toArray(new NetworkIF[0]));
			} catch (final Exception re) {
			}
			try {
				dato.setHardwarePower(hal.getPowerSources().toArray(new PowerSource[0]));
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareProcessor(hal.getProcessor());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareSensor(hal.getSensors());
			} catch (final Exception re) {
			}
			try {
				dato.setHardwareUsb(hal.getUsbDevices(true).toArray(new UsbDevice[0]));
			} catch (final Exception re) {
			}
		}
		try {
			final OperatingSystem os = si.getOperatingSystem();
			dato.setOperatingSystem(os);
		} catch (final Exception ex) {
		}
		// System.out.println("check2");
		// X RaspBerry
		/*
		 * try { dato.setPiPlatformName(PlatformManager.getPlatform().getLabel()); }
		 * catch (Exception ex) { }
		 */
		try {
			dato.setPiPlatformID(PlatformManager.getPlatform().getId());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiSerialNumber(SystemInfo.getSerial());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCPURevision(SystemInfo.getCpuRevision());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCPUArchitecture(SystemInfo.getCpuArchitecture());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCPUPart(SystemInfo.getCpuPart());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCPUTemperature(SystemInfo.getCpuTemperature());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCPUCoreVoltage(SystemInfo.getCpuVoltage());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCPUModelName(SystemInfo.getModelName());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiProcessor(SystemInfo.getProcessor());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiHardware(SystemInfo.getHardware());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiHardwareRevision(SystemInfo.getRevision());
		} catch (final Exception ex) {
		}
		// in windows da errore
		/*
		 * try { dato.put("piIsHardFloatABI", SystemInfo.isHardFloatAbi()); } catch
		 * (Exception ex) { }
		 */
		try {
			dato.setPiBoardType(SystemInfo.getBoardType().name());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiTotalMemory(SystemInfo.getMemoryTotal());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiUsedMemory(SystemInfo.getMemoryUsed());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiFreeMemory(SystemInfo.getMemoryFree());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiSharedMemory(SystemInfo.getMemoryShared());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiMemoryBuffers(SystemInfo.getMemoryBuffers());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCachedMemory(SystemInfo.getMemoryCached());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiSDRAM_CVoltage(SystemInfo.getMemoryVoltageSDRam_C());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiSDRAM_IVoltage(SystemInfo.getMemoryVoltageSDRam_I());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiSDRAM_PVoltage(SystemInfo.getMemoryVoltageSDRam_P());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiOSName(SystemInfo.getOsName());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiOSVersion(SystemInfo.getOsVersion());
		} catch (final Exception ex) {
		}
		try {
			dato.setoSArchitecture(SystemInfo.getOsArch());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiOSFirmwareBuild(SystemInfo.getOsFirmwareBuild());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiOSFirmwareDate(SystemInfo.getOsFirmwareDate());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiJavaVendor(SystemInfo.getJavaVendor());
			dato.setPiJavaVendorURL(SystemInfo.getJavaVendorUrl());
			dato.setPiJavaVersion(SystemInfo.getJavaVersion());
			dato.setPiJavaVM(SystemInfo.getJavaVirtualMachine());
			dato.setPiJavaRuntime(SystemInfo.getJavaRuntime());
			dato.setPiHostname(NetworkInfo.getHostname());
			int a = 0;
			int b = 0;
			int c = 0;
			for (final String ipAddress : NetworkInfo.getIPAddresses()) {
				dato.setPiIPAddresses(a, ipAddress);
				a++;
			}
			for (final String fqdn : NetworkInfo.getFQDNs()) {
				dato.setPiFQDN(b, fqdn);
				b++;
			}
			for (final String nameserver : NetworkInfo.getNameservers()) {
				dato.setPiNameserver(c, nameserver);
				c++;
			}
		} catch (final Exception ex) {
		}
		try {
			dato.setPiH264CodecEnabled(SystemInfo.getCodecH264Enabled());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiMPG2CodecEnabled(SystemInfo.getCodecMPG2Enabled());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiWVC1CodecEnabled(SystemInfo.getCodecWVC1Enabled());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiARMFrequency(SystemInfo.getClockFrequencyArm());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiCOREFrequency(SystemInfo.getClockFrequencyCore());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiH264Frequency(SystemInfo.getClockFrequencyH264());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiISPFrequency(SystemInfo.getClockFrequencyISP());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiV3DFrequency(SystemInfo.getClockFrequencyV3D());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiUARTFrequency(SystemInfo.getClockFrequencyUART());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiPWMFrequency(SystemInfo.getClockFrequencyPWM());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiEMMCFrequency(SystemInfo.getClockFrequencyEMMC());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiPixelFrequency(SystemInfo.getClockFrequencyPixel());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiVECFrequency(SystemInfo.getClockFrequencyVEC());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiHDMIFrequency(SystemInfo.getClockFrequencyHDMI());
		} catch (final Exception ex) {
		}
		try {
			dato.setPiDPIFrequency(SystemInfo.getClockFrequencyDPI());
		} catch (final Exception ex) {
		}
		return dato;
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		final JSONObject json = new JSONObject(readTxtFromUrl(url));
		return json;
	}

	public static String readTxtFromUrl(String url) throws IOException, JSONException {
		final InputStream is = new URL(url).openStream();
		try {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			final String text = readAll(rd);
			return text;
		} finally {
			is.close();
		}
	}

	public static String resolveFileFromDns(final String hostPart, final String domainPart, final int retry)
			throws TextParseException, UnknownHostException {
		final StringBuilder resultString = new StringBuilder();
		final Set<String> errors = new HashSet<>();
		final Lookup l = new Lookup(hostPart + "-max" + domainPart, Type.TXT, DClass.IN);
		l.setResolver(new SimpleResolver());
		l.run();
		if (l.getResult() == Lookup.SUCCESSFUL) {
			final int chunkSize = Integer
					.parseInt(l.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
			if (chunkSize > 0) {
				for (int c = 0; c < chunkSize; c++) {
					final Lookup cl = new Lookup(hostPart + "-" + String.valueOf(c) + domainPart, Type.TXT, DClass.IN);
					cl.setResolver(new SimpleResolver());
					cl.run();
					if (cl.getResult() == Lookup.SUCCESSFUL) {
						resultString
								.append(cl.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
					} else {
						errors.add("error in chunk " + hostPart + "-" + String.valueOf(c) + domainPart + " -> "
								+ cl.getErrorString());
					}
				}
			} else {
				errors.add("error, size of data is " + l.getAnswers()[0].rdataToString());
			}
		} else {
			errors.add("no " + hostPart + "-max" + domainPart + " record found -> " + l.getErrorString());
			if (retry > 0) {
				return resolveFileFromDns(hostPart, domainPart, retry - 1);
			} else {
				return null;
			}
		}
		if (!errors.isEmpty()) {
			logger.error(errors.toString());
			return null;
		} else {
			return resultString.toString();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
