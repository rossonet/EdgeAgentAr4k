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
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.ar4k.agent.exception.Ar4kException;
import org.json.JSONException;
import org.json.JSONObject;

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
public class HardwareHelper {

  public static final boolean debugFreezeHal = false;
  private static final int BUFFER_SIZE = 512;

  public static HardwareInfo getSystemInfo() throws IOException, InterruptedException, ParseException {
    final HardwareInfo dato = new HardwareInfo();
    oshi.SystemInfo si = new oshi.SystemInfo();
    Runtime runtime = Runtime.getRuntime();
    OperatingSystemMXBean mbean = ManagementFactory.getOperatingSystemMXBean();
    dato.setSystemLoadAverage(mbean.getSystemLoadAverage());
    dato.setHardwareRuntimetotalmemory(runtime.totalMemory());
    dato.setHardwareRuntimefreememory(runtime.freeMemory());
    List<RootFileSystem> fileSystem = new ArrayList<RootFileSystem>();
    for (File root : File.listRoots()) {
      RootFileSystem rootFs = new RootFileSystem();
      rootFs.setAbsolutePath(root.getAbsoluteFile());
      rootFs.setFreeSpace(root.getFreeSpace());
      rootFs.setTotalSpace(root.getTotalSpace());
      List<RootFileSystem> childs = new ArrayList<RootFileSystem>();
      if (root != null && root.listFiles() != null)
        for (File figlio : root.listFiles()) {
          RootFileSystem figlioFs = new RootFileSystem();
          figlioFs.setAbsolutePath(figlio.getAbsoluteFile());
          figlioFs.setFreeSpace(figlio.getFreeSpace());
          figlioFs.setTotalSpace(figlio.getTotalSpace());
          childs.add(figlioFs);
        }
      rootFs.setChilds(childs);
      fileSystem.add(rootFs);
    }
    // System.out.println("check1");
    dato.setHardwareFilelistroots(fileSystem);
    HardwareAbstractionLayer hal = si.getHardware();
    if (debugFreezeHal) {
      // System.out.println("check1.1");
      try {
        dato.setHardwareComputersystem(hal.getComputerSystem());
      } catch (Exception re) {
      }
      // System.out.println("check1.2");
      try {
        dato.setHardwareDisk(hal.getDiskStores());
      } catch (Exception re) {
      }
      ;
      // System.out.println("check1.3");
      try {
        dato.setHardwareDislay(hal.getDisplays());
      } catch (Exception re) {
      }
      ;
      // System.out.println("check1.4");
      try {
        dato.setHardwareMemorytotal(hal.getMemory().getTotal());
      } catch (Exception re) {
      }
      // System.out.println("check1.5");
      try {
        dato.setHardwareMemoryavailable(hal.getMemory().getAvailable());
      } catch (Exception re) {
      }
      try {
        dato.setHardwareSwaptotal(hal.getMemory().getSwapTotal());
      } catch (Exception re) {
      }
      // System.out.println("check1.6");
      try {
        dato.setHardwareSwapused(hal.getMemory().getSwapUsed());
      } catch (Exception re) {
      }
      // System.out.println("check1.7");
      try {
        dato.setHardwareNetwork(hal.getNetworkIFs());
      } catch (Exception re) {
      }
      // System.out.println("check1.8");
      try {
        dato.setHardwarePower(hal.getPowerSources());
      } catch (Exception re) {
      }
      // System.out.println("check1.9");
      try {
        dato.setHardwareProcessor(hal.getProcessor());
      } catch (Exception re) {
      }
      try {
        dato.setHardwareSensor(hal.getSensors());
      } catch (Exception re) {
      }
      try {
        dato.setHardwareUsb(hal.getUsbDevices(true));
      } catch (Exception re) {
      }
    }
    try {
      OperatingSystem os = si.getOperatingSystem();
      dato.setOperatingSystem(os);
    } catch (Exception ex) {
    }
    // System.out.println("check2");
    // X RaspBerry
    try {
      dato.setPiPlatformName(PlatformManager.getPlatform().getLabel());
    } catch (Exception ex) {
    }
    try {
      dato.setPiPlatformID(PlatformManager.getPlatform().getId());
    } catch (Exception ex) {
    }
    try {
      dato.setPiSerialNumber(SystemInfo.getSerial());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCPURevision(SystemInfo.getCpuRevision());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCPUArchitecture(SystemInfo.getCpuArchitecture());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCPUPart(SystemInfo.getCpuPart());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCPUTemperature(SystemInfo.getCpuTemperature());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCPUCoreVoltage(SystemInfo.getCpuVoltage());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCPUModelName(SystemInfo.getModelName());
    } catch (Exception ex) {
    }
    try {
      dato.setPiProcessor(SystemInfo.getProcessor());
    } catch (Exception ex) {
    }
    try {
      dato.setPiHardware(SystemInfo.getHardware());
    } catch (Exception ex) {
    }
    try {
      dato.setPiHardwareRevision(SystemInfo.getRevision());
    } catch (Exception ex) {
    }
    // in windows da errore
    /*
     * try { dato.put("piIsHardFloatABI", SystemInfo.isHardFloatAbi()); } catch
     * (Exception ex) { }
     */
    try {
      dato.setPiBoardType(SystemInfo.getBoardType().name());
    } catch (Exception ex) {
    }
    try {
      dato.setPiTotalMemory(SystemInfo.getMemoryTotal());
    } catch (Exception ex) {
    }
    try {
      dato.setPiUsedMemory(SystemInfo.getMemoryUsed());
    } catch (Exception ex) {
    }
    try {
      dato.setPiFreeMemory(SystemInfo.getMemoryFree());
    } catch (Exception ex) {
    }
    try {
      dato.setPiSharedMemory(SystemInfo.getMemoryShared());
    } catch (Exception ex) {
    }
    try {
      dato.setPiMemoryBuffers(SystemInfo.getMemoryBuffers());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCachedMemory(SystemInfo.getMemoryCached());
    } catch (Exception ex) {
    }
    try {
      dato.setPiSDRAM_CVoltage(SystemInfo.getMemoryVoltageSDRam_C());
    } catch (Exception ex) {
    }
    try {
      dato.setPiSDRAM_IVoltage(SystemInfo.getMemoryVoltageSDRam_I());
    } catch (Exception ex) {
    }
    try {
      dato.setPiSDRAM_PVoltage(SystemInfo.getMemoryVoltageSDRam_P());
    } catch (Exception ex) {
    }
    try {
      dato.setPiOSName(SystemInfo.getOsName());
    } catch (Exception ex) {
    }
    try {
      dato.setPiOSVersion(SystemInfo.getOsVersion());
    } catch (Exception ex) {
    }
    try {
      dato.setoSArchitecture(SystemInfo.getOsArch());
    } catch (Exception ex) {
    }
    try {
      dato.setPiOSFirmwareBuild(SystemInfo.getOsFirmwareBuild());
    } catch (Exception ex) {
    }
    try {
      dato.setPiOSFirmwareDate(SystemInfo.getOsFirmwareDate());
    } catch (Exception ex) {
    }
    // System.out.println("check3");
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
      for (String ipAddress : NetworkInfo.getIPAddresses()) {
        dato.setPiIPAddresses(a, ipAddress);
        a++;
      }
      for (String fqdn : NetworkInfo.getFQDNs()) {
        dato.setPiFQDN(b, fqdn);
        b++;
      }
      for (String nameserver : NetworkInfo.getNameservers()) {
        dato.setPiNameserver(c, nameserver);
        c++;
      }
    } catch (Exception ex) {
    }
    try {
      dato.setPiH264CodecEnabled(SystemInfo.getCodecH264Enabled());
    } catch (Exception ex) {
    }
    try {
      dato.setPiMPG2CodecEnabled(SystemInfo.getCodecMPG2Enabled());
    } catch (Exception ex) {
    }
    try {
      dato.setPiWVC1CodecEnabled(SystemInfo.getCodecWVC1Enabled());
    } catch (Exception ex) {
    }
    try {
      dato.setPiARMFrequency(SystemInfo.getClockFrequencyArm());
    } catch (Exception ex) {
    }
    try {
      dato.setPiCOREFrequency(SystemInfo.getClockFrequencyCore());
    } catch (Exception ex) {
    }
    try {
      dato.setPiH264Frequency(SystemInfo.getClockFrequencyH264());
    } catch (Exception ex) {
    }
    try {
      dato.setPiISPFrequency(SystemInfo.getClockFrequencyISP());
    } catch (Exception ex) {
    }
    try {
      dato.setPiV3DFrequency(SystemInfo.getClockFrequencyV3D());
    } catch (Exception ex) {
    }
    try {
      dato.setPiUARTFrequency(SystemInfo.getClockFrequencyUART());
    } catch (Exception ex) {
    }
    try {
      dato.setPiPWMFrequency(SystemInfo.getClockFrequencyPWM());
    } catch (Exception ex) {
    }
    try {
      dato.setPiEMMCFrequency(SystemInfo.getClockFrequencyEMMC());
    } catch (Exception ex) {
    }
    try {
      dato.setPiPixelFrequency(SystemInfo.getClockFrequencyPixel());
    } catch (Exception ex) {
    }
    try {
      dato.setPiVECFrequency(SystemInfo.getClockFrequencyVEC());
    } catch (Exception ex) {
    }
    try {
      dato.setPiHDMIFrequency(SystemInfo.getClockFrequencyHDMI());
    } catch (Exception ex) {
    }
    try {
      dato.setPiDPIFrequency(SystemInfo.getClockFrequencyDPI());
    } catch (Exception ex) {
    }
    return dato;
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static long downloadFileFromUrl(String filename, String url) throws MalformedURLException, IOException {
    long result = 0L;
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

  public static void extractTarGz(InputStream in) throws IOException {
    GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
    try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
      TarArchiveEntry entry = null;
      while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
        /** If the entry is a directory, create the directory. **/
        if (entry.isDirectory()) {
          File f = new File(entry.getName());
          boolean created = f.mkdir();
          if (!created) {
            throw new Ar4kException(
                "Unable to create directory " + f.getAbsolutePath() + ", during extraction of archive contents.");
          }
        } else {
          int count;
          byte data[] = new byte[BUFFER_SIZE];
          FileOutputStream fos = new FileOutputStream(entry.getName(), false);
          try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
            while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
              dest.write(data, 0, count);
            }
          }
        }
      }
    }
  }
}
