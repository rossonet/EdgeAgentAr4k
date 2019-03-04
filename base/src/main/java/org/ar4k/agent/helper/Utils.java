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
import java.net.ServerSocket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Helper generico.
 *
 */
public class Utils {

  public static final boolean debugFreezeHal = false;

  public static Map<String, Object> getSystemInfo() throws IOException, InterruptedException, ParseException {
    Map<String, Object> dato = new HashMap<String, Object>();
    oshi.SystemInfo si = new oshi.SystemInfo();
    Runtime runtime = Runtime.getRuntime();
    OperatingSystemMXBean mbean = ManagementFactory.getOperatingSystemMXBean();
    dato.put("system-load-average", mbean.getSystemLoadAverage());
    dato.put("hardware-runtime-total-memory", runtime.totalMemory());
    dato.put("hardware-runtime-free-memory", runtime.freeMemory());
    List<Map<String, Object>> fileSystem = new ArrayList<Map<String, Object>>();
    for (File root : File.listRoots()) {
      Map<String, Object> rootFs = new HashMap<String, Object>();
      rootFs.put("absolutePath", root.getAbsoluteFile());
      rootFs.put("freeSpace", root.getFreeSpace());
      rootFs.put("totalSpace", root.getTotalSpace());
      List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
      for (File figlio : root.listFiles()) {
        Map<String, Object> figlioFs = new HashMap<String, Object>();
        figlioFs.put("absolutePath", figlio.getAbsoluteFile());
        figlioFs.put("freeSpace", figlio.getFreeSpace());
        figlioFs.put("totalSpace", figlio.getTotalSpace());
        childs.add(figlioFs);
      }
      rootFs.put("childs", childs);
      fileSystem.add(rootFs);
    }
    // System.out.println("check1");
    dato.put("hardware-file-list-roots", fileSystem);
    HardwareAbstractionLayer hal = si.getHardware();
    if (debugFreezeHal) {
      // System.out.println("check1.1");
      try {
        dato.put("hardware-computer-system", hal.getComputerSystem());
      } catch (Exception re) {
      }
      // System.out.println("check1.2");
      try {
        dato.put("hardware-disk", hal.getDiskStores());
      } catch (Exception re) {
      }
      ;
      // System.out.println("check1.3");
      try {
        dato.put("hardware-dislay", hal.getDisplays());
      } catch (Exception re) {
      }
      ;
      // System.out.println("check1.4");
      try {
        dato.put("hardware-memory-total", hal.getMemory().getTotal());
      } catch (Exception re) {
      }
      // System.out.println("check1.5");
      try {
        dato.put("hardware-memory-available", hal.getMemory().getAvailable());
      } catch (Exception re) {
      }
      try {
        dato.put("hardware-swap-total", hal.getMemory().getSwapTotal());
      } catch (Exception re) {
      }
      // System.out.println("check1.6");
      try {
        dato.put("hardware-swap-used", hal.getMemory().getSwapUsed());
      } catch (Exception re) {
      }
      // System.out.println("check1.7");
      try {
        dato.put("hardware-network", hal.getNetworkIFs());
      } catch (Exception re) {
      }
      // System.out.println("check1.8");
      try {
        dato.put("hardware-power", hal.getPowerSources());
      } catch (Exception re) {
      }
      // System.out.println("check1.9");
      try {
        dato.put("hardware-processor", hal.getProcessor());
      } catch (Exception re) {
      }
      try {
        dato.put("hardware-sensor", hal.getSensors());
      } catch (Exception re) {
      }
      try {
        dato.put("hardware-usb", hal.getUsbDevices(true));
      } catch (Exception re) {
      }
    }
    try {
      OperatingSystem os = si.getOperatingSystem();
      dato.put("operating-system", os);
    } catch (Exception ex) {
    }
    // System.out.println("check2");
    // X RaspBerry
    try {
      dato.put("pi-Platform-Name", PlatformManager.getPlatform().getLabel());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Platform-ID", PlatformManager.getPlatform().getId());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Serial-Number", SystemInfo.getSerial());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CPU-Revision", SystemInfo.getCpuRevision());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CPU-Architecture", SystemInfo.getCpuArchitecture());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CPU-Part", SystemInfo.getCpuPart());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CPU-Temperature", SystemInfo.getCpuTemperature());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CPU-Core-Voltage", SystemInfo.getCpuVoltage());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CPU-Model-Name", SystemInfo.getModelName());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Processor", SystemInfo.getProcessor());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Hardware", SystemInfo.getHardware());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Hardware-Revision", SystemInfo.getRevision());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Is-Hard-Float-ABI", SystemInfo.isHardFloatAbi());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Board-Type", SystemInfo.getBoardType().name());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Total-Memory", SystemInfo.getMemoryTotal());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Used-Memory", SystemInfo.getMemoryUsed());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Free-Memory", SystemInfo.getMemoryFree());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Shared-Memory", SystemInfo.getMemoryShared());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Memory-Buffers", SystemInfo.getMemoryBuffers());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Cached-Memory", SystemInfo.getMemoryCached());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-SDRAM_C-Voltage", SystemInfo.getMemoryVoltageSDRam_C());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-SDRAM_I-Voltage", SystemInfo.getMemoryVoltageSDRam_I());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-SDRAM_P-Voltage", SystemInfo.getMemoryVoltageSDRam_P());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-OS-Name", SystemInfo.getOsName());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-OS-Version", SystemInfo.getOsVersion());
    } catch (Exception ex) {
    }
    try {
      dato.put("OS-Architecture", SystemInfo.getOsArch());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-OS-Firmware-Build", SystemInfo.getOsFirmwareBuild());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-OS-Firmware-Date", SystemInfo.getOsFirmwareDate());
    } catch (Exception ex) {
    }
    // System.out.println("check3");
    try {
      dato.put("pi-Java-Vendor", SystemInfo.getJavaVendor());
      dato.put("pi-Java-Vendor-URL", SystemInfo.getJavaVendorUrl());
      dato.put("pi-Java-Version", SystemInfo.getJavaVersion());
      dato.put("pi-Java-VM", SystemInfo.getJavaVirtualMachine());
      dato.put("pi-Java-Runtime", SystemInfo.getJavaRuntime());
      dato.put("pi-Hostname-", NetworkInfo.getHostname());
      int a = 0;
      int b = 0;
      int c = 0;
      for (String ipAddress : NetworkInfo.getIPAddresses()) {
        dato.put("pi-IP-Addresses-" + String.valueOf(a), ipAddress);
        a++;
      }
      for (String fqdn : NetworkInfo.getFQDNs()) {
        dato.put("pi-FQDN-" + String.valueOf(b), fqdn);
        b++;
      }
      for (String nameserver : NetworkInfo.getNameservers()) {
        dato.put("pi-Nameserver" + String.valueOf(c), nameserver);
        c++;
      }
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-H264-Codec-Enabled", SystemInfo.getCodecH264Enabled());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-MPG2-Codec-Enabled", SystemInfo.getCodecMPG2Enabled());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-WVC1-Codec-Enabled", SystemInfo.getCodecWVC1Enabled());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-ARM-Frequency", SystemInfo.getClockFrequencyArm());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-CORE-Frequency", SystemInfo.getClockFrequencyCore());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-H264-Frequency", SystemInfo.getClockFrequencyH264());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-ISP-Frequency", SystemInfo.getClockFrequencyISP());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-V3D-Frequency", SystemInfo.getClockFrequencyV3D());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-UART-Frequency", SystemInfo.getClockFrequencyUART());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-PWM-Frequency", SystemInfo.getClockFrequencyPWM());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-EMMC-Frequency", SystemInfo.getClockFrequencyEMMC());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-Pixel-Frequency", SystemInfo.getClockFrequencyPixel());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-VEC-Frequency", SystemInfo.getClockFrequencyVEC());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-HDMI-Frequency", SystemInfo.getClockFrequencyHDMI());
    } catch (Exception ex) {
    }
    try {
      dato.put("pi-DPI-Frequency", SystemInfo.getClockFrequencyDPI());
    } catch (Exception ex) {
    }
    return dato;
  }

  public static boolean checkLocalPortAvailable(int port) {
    boolean portTaken = false;
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(port);
    } catch (IOException e) {
      portTaken = true;
    } finally {
      if (socket != null)
        try {
          socket.close();
        } catch (IOException e) {
        }
    }
    return !portTaken;
  }
}
