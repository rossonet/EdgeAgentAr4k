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

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//import org.ar4k.agent.logger.EdgeLogger;
//import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

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

	// private static final EdgeLogger logger =
	// EdgeStaticLoggerBinder.getClassLogger(HardwareHelper.class);

	public static final boolean DEBUG_FREEZE_HAL = false;

	private HardwareHelper() {
		throw new UnsupportedOperationException("Just for static usage");
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

}
