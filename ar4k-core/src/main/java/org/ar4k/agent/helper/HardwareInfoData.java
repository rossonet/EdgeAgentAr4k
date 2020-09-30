package org.ar4k.agent.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.HWDiskStore;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.UsbDevice;
import oshi.software.os.OperatingSystem;

public class HardwareInfoData {
  private final List<RootFileSystem> fileSystems = new ArrayList<>();
  private double systemLoadAverage = 0;
  private long hardwareRuntimetotalmemory = 0;
  private long hardwareRuntimefreememory = 0;
  private List<RootFileSystem> hardwareFilelistroots = null;
  private ComputerSystem hardwareComputersystem = null;
  private HWDiskStore[] hardwareDisk = null;
  private Display[] hardwareDislay = null;
  private long hardwareMemorytotal = 0;
  private long hardwareMemoryavailable = 0;
  private long hardwareSwaptotal = 0;
  private long hardwareSwapused = 0;
  private NetworkIF[] hardwareNetwork = null;
  private PowerSource[] hardwarePower = null;
  private CentralProcessor hardwareProcessor = null;
  private Sensors hardwareSensor = null;
  private UsbDevice[] hardwareUsb = null;
  private OperatingSystem operatingSystem = null;
  private String piPlatformName = null;
  private String piPlatformID = null;
  private String piSerialNumber = null;
  private String piCPURevision = null;
  private String piCPUArchitecture = null;
  private String piCPUPart = null;
  private float piCPUTemperature = 0;
  private float piCPUCoreVoltage = 0;
  private String piCPUModelName = null;
  private String piProcessor = null;
  private String piHardware = null;
  private String piHardwareRevision = null;
  private String piBoardType = null;
  private long piTotalMemory = 0;
  private long piUsedMemory = 0;
  private long piFreeMemory = 0;
  private long piSharedMemory = 0;
  private long piMemoryBuffers = 0;
  private long piCachedMemory = 0;
  private float piSDRAM_CVoltage = 0;
  private float piSDRAM_IVoltage = 0;
  private float piSDRAM_PVoltage = 0;
  private String piOSName = null;
  private String piOSVersion = null;
  private String oSArchitecture = null;
  private String piOSFirmwareBuild = null;
  private String piOSFirmwareDate = null;
  private String piJavaVendor = null;
  private String piJavaVendorURL = null;
  private String piJavaVersion = null;
  private String piJavaVM = null;
  private String piJavaRuntime = null;
  private String piHostname = null;
  private String[] piIPAddresses = null;
  private String[] piFQDN = null;
  private String[] piNameserver = null;
  private Boolean piH264CodecEnabled = null;
  private Boolean piMPG2CodecEnabled = null;
  private Boolean piWVC1CodecEnabled = null;
  private long piARMFrequency = 0;
  private long piCOREFrequency = 0;
  private long piH264Frequency = 0;
  private long piISPFrequency = 0;
  private long piV3DFrequency = 0;
  private long piUARTFrequency = 0;
  private long piPWMFrequency = 0;
  private long piEMMCFrequency = 0;
  private long piPixelFrequency = 0;
  private long piVECFrequency = 0;
  private long piHDMIFrequency = 0;
  private long piDPIFrequency = 0;

  public double getSystemLoadAverage() {
    return systemLoadAverage;
  }

  public void setSystemLoadAverage(double d) {
    this.systemLoadAverage = d;
  }

  public long getHardwareRuntimetotalmemory() {
    return hardwareRuntimetotalmemory;
  }

  public void setHardwareRuntimetotalmemory(long l) {
    this.hardwareRuntimetotalmemory = l;
  }

  public long getHardwareRuntimefreememory() {
    return hardwareRuntimefreememory;
  }

  public void setHardwareRuntimefreememory(long l) {
    this.hardwareRuntimefreememory = l;
  }

  public List<RootFileSystem> getHardwareFilelistroots() {
    return hardwareFilelistroots;
  }

  public void setHardwareFilelistroots(List<RootFileSystem> fileSystem) {
    this.hardwareFilelistroots = fileSystem;
  }

  public ComputerSystem getHardwareComputersystem() {
    return hardwareComputersystem;
  }

  public void setHardwareComputersystem(ComputerSystem computerSystem) {
    this.hardwareComputersystem = computerSystem;
  }

  public HWDiskStore[] getHardwareDisk() {
    return hardwareDisk;
  }

  public void setHardwareDisk(HWDiskStore[] hwDiskStores) {
    this.hardwareDisk = hwDiskStores;
  }

  public Display[] getHardwareDislay() {
    return hardwareDislay;
  }

  public void setHardwareDislay(Display[] displays) {
    this.hardwareDislay = displays;
  }

  public long getHardwareMemorytotal() {
    return hardwareMemorytotal;
  }

  public void setHardwareMemorytotal(long l) {
    this.hardwareMemorytotal = l;
  }

  public long getHardwareMemoryavailable() {
    return hardwareMemoryavailable;
  }

  public void setHardwareMemoryavailable(long l) {
    this.hardwareMemoryavailable = l;
  }

  public long getHardwareSwaptotal() {
    return hardwareSwaptotal;
  }

  public void setHardwareSwaptotal(long l) {
    this.hardwareSwaptotal = l;
  }

  public long getHardwareSwapused() {
    return hardwareSwapused;
  }

  public void setHardwareSwapused(long l) {
    this.hardwareSwapused = l;
  }

  public NetworkIF[] getHardwareNetwork() {
    return hardwareNetwork;
  }

  public void setHardwareNetwork(NetworkIF[] networkIFs) {
    this.hardwareNetwork = networkIFs;
  }

  public PowerSource[] getHardwarePower() {
    return hardwarePower;
  }

  public void setHardwarePower(PowerSource[] powerSources) {
    this.hardwarePower = powerSources;
  }

  public CentralProcessor getHardwareProcessor() {
    return hardwareProcessor;
  }

  public void setHardwareProcessor(CentralProcessor centralProcessor) {
    this.hardwareProcessor = centralProcessor;
  }

  public Sensors getHardwareSensor() {
    return hardwareSensor;
  }

  public void setHardwareSensor(Sensors sensors) {
    this.hardwareSensor = sensors;
  }

  public UsbDevice[] getHardwareUsb() {
    return hardwareUsb;
  }

  public void setHardwareUsb(UsbDevice[] usbDevices) {
    this.hardwareUsb = usbDevices;
  }

  public OperatingSystem getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(OperatingSystem os) {
    this.operatingSystem = os;
  }

  public String getPiPlatformName() {
    return piPlatformName;
  }

  public void setPiPlatformName(String piPlatformName) {
    this.piPlatformName = piPlatformName;
  }

  public String getPiPlatformID() {
    return piPlatformID;
  }

  public void setPiPlatformID(String piPlatformID) {
    this.piPlatformID = piPlatformID;
  }

  public String getPiSerialNumber() {
    return piSerialNumber;
  }

  public void setPiSerialNumber(String piSerialNumber) {
    this.piSerialNumber = piSerialNumber;
  }

  public String getPiCPURevision() {
    return piCPURevision;
  }

  public void setPiCPURevision(String piCPURevision) {
    this.piCPURevision = piCPURevision;
  }

  public String getPiCPUArchitecture() {
    return piCPUArchitecture;
  }

  public void setPiCPUArchitecture(String piCPUArchitecture) {
    this.piCPUArchitecture = piCPUArchitecture;
  }

  public String getPiCPUPart() {
    return piCPUPart;
  }

  public void setPiCPUPart(String piCPUPart) {
    this.piCPUPart = piCPUPart;
  }

  public float getPiCPUTemperature() {
    return piCPUTemperature;
  }

  public void setPiCPUTemperature(float f) {
    this.piCPUTemperature = f;
  }

  public float getPiCPUCoreVoltage() {
    return piCPUCoreVoltage;
  }

  public void setPiCPUCoreVoltage(float f) {
    this.piCPUCoreVoltage = f;
  }

  public String getPiCPUModelName() {
    return piCPUModelName;
  }

  public void setPiCPUModelName(String piCPUModelName) {
    this.piCPUModelName = piCPUModelName;
  }

  public String getPiProcessor() {
    return piProcessor;
  }

  public void setPiProcessor(String piProcessor) {
    this.piProcessor = piProcessor;
  }

  public String getPiHardware() {
    return piHardware;
  }

  public void setPiHardware(String piHardware) {
    this.piHardware = piHardware;
  }

  public String getPiHardwareRevision() {
    return piHardwareRevision;
  }

  public void setPiHardwareRevision(String piHardwareRevision) {
    this.piHardwareRevision = piHardwareRevision;
  }

  public String getPiBoardType() {
    return piBoardType;
  }

  public void setPiBoardType(String piBoardType) {
    this.piBoardType = piBoardType;
  }

  public long getPiTotalMemory() {
    return piTotalMemory;
  }

  public void setPiTotalMemory(long l) {
    this.piTotalMemory = l;
  }

  public long getPiUsedMemory() {
    return piUsedMemory;
  }

  public void setPiUsedMemory(long l) {
    this.piUsedMemory = l;
  }

  public long getPiFreeMemory() {
    return piFreeMemory;
  }

  public void setPiFreeMemory(long l) {
    this.piFreeMemory = l;
  }

  public long getPiSharedMemory() {
    return piSharedMemory;
  }

  public void setPiSharedMemory(long l) {
    this.piSharedMemory = l;
  }

  public long getPiMemoryBuffers() {
    return piMemoryBuffers;
  }

  public void setPiMemoryBuffers(long l) {
    this.piMemoryBuffers = l;
  }

  public long getPiCachedMemory() {
    return piCachedMemory;
  }

  public void setPiCachedMemory(long l) {
    this.piCachedMemory = l;
  }

  public float getPiSDRAM_CVoltage() {
    return piSDRAM_CVoltage;
  }

  public void setPiSDRAM_CVoltage(float f) {
    this.piSDRAM_CVoltage = f;
  }

  public float getPiSDRAM_IVoltage() {
    return piSDRAM_IVoltage;
  }

  public void setPiSDRAM_IVoltage(float f) {
    this.piSDRAM_IVoltage = f;
  }

  public float getPiSDRAM_PVoltage() {
    return piSDRAM_PVoltage;
  }

  public void setPiSDRAM_PVoltage(float f) {
    this.piSDRAM_PVoltage = f;
  }

  public String getPiOSName() {
    return piOSName;
  }

  public void setPiOSName(String piOSName) {
    this.piOSName = piOSName;
  }

  public String getPiOSVersion() {
    return piOSVersion;
  }

  public void setPiOSVersion(String piOSVersion) {
    this.piOSVersion = piOSVersion;
  }

  public String getoSArchitecture() {
    return oSArchitecture;
  }

  public void setoSArchitecture(String oSArchitecture) {
    this.oSArchitecture = oSArchitecture;
  }

  public String getPiOSFirmwareBuild() {
    return piOSFirmwareBuild;
  }

  public void setPiOSFirmwareBuild(String piOSFirmwareBuild) {
    this.piOSFirmwareBuild = piOSFirmwareBuild;
  }

  public String getPiOSFirmwareDate() {
    return piOSFirmwareDate;
  }

  public void setPiOSFirmwareDate(String piOSFirmwareDate) {
    this.piOSFirmwareDate = piOSFirmwareDate;
  }

  public String getPiJavaVendor() {
    return piJavaVendor;
  }

  public void setPiJavaVendor(String piJavaVendor) {
    this.piJavaVendor = piJavaVendor;
  }

  public String getPiJavaVendorURL() {
    return piJavaVendorURL;
  }

  public void setPiJavaVendorURL(String piJavaVendorURL) {
    this.piJavaVendorURL = piJavaVendorURL;
  }

  public String getPiJavaVersion() {
    return piJavaVersion;
  }

  public void setPiJavaVersion(String piJavaVersion) {
    this.piJavaVersion = piJavaVersion;
  }

  public String getPiJavaVM() {
    return piJavaVM;
  }

  public void setPiJavaVM(String piJavaVM) {
    this.piJavaVM = piJavaVM;
  }

  public String getPiJavaRuntime() {
    return piJavaRuntime;
  }

  public void setPiJavaRuntime(String piJavaRuntime) {
    this.piJavaRuntime = piJavaRuntime;
  }

  public String getPiHostname() {
    return piHostname;
  }

  public void setPiHostname(String piHostname) {
    this.piHostname = piHostname;
  }

  public String[] getPiIPAddresses() {
    return piIPAddresses;
  }

  public void setPiIPAddresses(int index, String piIPAddresses) {
    this.piIPAddresses[index] = piIPAddresses;
  }

  public String[] getPiFQDN() {
    return piFQDN;
  }

  public void setPiFQDN(int index, String piFQDN) {
    this.piFQDN[index] = piFQDN;
  }

  public String[] getPiNameserver() {
    return piNameserver;
  }

  public void setPiNameserver(int index, String piNameserver) {
    this.piNameserver[index] = piNameserver;
  }

  public boolean getPiH264CodecEnabled() {
    return piH264CodecEnabled;
  }

  public void setPiH264CodecEnabled(boolean b) {
    this.piH264CodecEnabled = b;
  }

  public boolean getPiMPG2CodecEnabled() {
    return piMPG2CodecEnabled;
  }

  public void setPiMPG2CodecEnabled(boolean b) {
    this.piMPG2CodecEnabled = b;
  }

  public boolean getPiWVC1CodecEnabled() {
    return piWVC1CodecEnabled;
  }

  public void setPiWVC1CodecEnabled(boolean b) {
    this.piWVC1CodecEnabled = b;
  }

  public long getPiARMFrequency() {
    return piARMFrequency;
  }

  public void setPiARMFrequency(long l) {
    this.piARMFrequency = l;
  }

  public long getPiCOREFrequency() {
    return piCOREFrequency;
  }

  public void setPiCOREFrequency(long l) {
    this.piCOREFrequency = l;
  }

  public long getPiH264Frequency() {
    return piH264Frequency;
  }

  public void setPiH264Frequency(long l) {
    this.piH264Frequency = l;
  }

  public long getPiISPFrequency() {
    return piISPFrequency;
  }

  public void setPiISPFrequency(long l) {
    this.piISPFrequency = l;
  }

  public long getPiV3DFrequency() {
    return piV3DFrequency;
  }

  public void setPiV3DFrequency(long l) {
    this.piV3DFrequency = l;
  }

  public long getPiUARTFrequency() {
    return piUARTFrequency;
  }

  public void setPiUARTFrequency(long l) {
    this.piUARTFrequency = l;
  }

  public long getPiPWMFrequency() {
    return piPWMFrequency;
  }

  public void setPiPWMFrequency(long l) {
    this.piPWMFrequency = l;
  }

  public long getPiEMMCFrequency() {
    return piEMMCFrequency;
  }

  public void setPiEMMCFrequency(long l) {
    this.piEMMCFrequency = l;
  }

  public long getPiPixelFrequency() {
    return piPixelFrequency;
  }

  public void setPiPixelFrequency(long l) {
    this.piPixelFrequency = l;
  }

  public long getPiVECFrequency() {
    return piVECFrequency;
  }

  public void setPiVECFrequency(long l) {
    this.piVECFrequency = l;
  }

  public long getPiHDMIFrequency() {
    return piHDMIFrequency;
  }

  public void setPiHDMIFrequency(long l) {
    this.piHDMIFrequency = l;
  }

  public long getPiDPIFrequency() {
    return piDPIFrequency;
  }

  public void setPiDPIFrequency(long l) {
    this.piDPIFrequency = l;
  }

  public List<RootFileSystem> getFileSystems() {
    return fileSystems;
  }

  @Override
  public String toString() {
    return "HardwareInfo [fileSystems=" + fileSystems + ", systemLoadAverage=" + systemLoadAverage
        + ", hardwareRuntimetotalmemory=" + hardwareRuntimetotalmemory + ", hardwareRuntimefreememory="
        + hardwareRuntimefreememory + ", hardwareFilelistroots=" + hardwareFilelistroots + ", hardwareComputersystem="
        + hardwareComputersystem + ", hardwareDisk=" + Arrays.toString(hardwareDisk) + ", hardwareDislay="
        + Arrays.toString(hardwareDislay) + ", hardwareMemorytotal=" + hardwareMemorytotal
        + ", hardwareMemoryavailable=" + hardwareMemoryavailable + ", hardwareSwaptotal=" + hardwareSwaptotal
        + ", hardwareSwapused=" + hardwareSwapused + ", hardwareNetwork=" + Arrays.toString(hardwareNetwork)
        + ", hardwarePower=" + Arrays.toString(hardwarePower) + ", hardwareProcessor=" + hardwareProcessor
        + ", hardwareSensor=" + hardwareSensor + ", hardwareUsb=" + Arrays.toString(hardwareUsb) + ", operatingSystem="
        + operatingSystem + ", piPlatformName=" + piPlatformName + ", piPlatformID=" + piPlatformID
        + ", piSerialNumber=" + piSerialNumber + ", piCPURevision=" + piCPURevision + ", piCPUArchitecture="
        + piCPUArchitecture + ", piCPUPart=" + piCPUPart + ", piCPUTemperature=" + piCPUTemperature
        + ", piCPUCoreVoltage=" + piCPUCoreVoltage + ", piCPUModelName=" + piCPUModelName + ", piProcessor="
        + piProcessor + ", piHardware=" + piHardware + ", piHardwareRevision=" + piHardwareRevision + ", piBoardType="
        + piBoardType + ", piTotalMemory=" + piTotalMemory + ", piUsedMemory=" + piUsedMemory + ", piFreeMemory="
        + piFreeMemory + ", piSharedMemory=" + piSharedMemory + ", piMemoryBuffers=" + piMemoryBuffers
        + ", piCachedMemory=" + piCachedMemory + ", piSDRAM_CVoltage=" + piSDRAM_CVoltage + ", piSDRAM_IVoltage="
        + piSDRAM_IVoltage + ", piSDRAM_PVoltage=" + piSDRAM_PVoltage + ", piOSName=" + piOSName + ", piOSVersion="
        + piOSVersion + ", oSArchitecture=" + oSArchitecture + ", piOSFirmwareBuild=" + piOSFirmwareBuild
        + ", piOSFirmwareDate=" + piOSFirmwareDate + ", piJavaVendor=" + piJavaVendor + ", piJavaVendorURL="
        + piJavaVendorURL + ", piJavaVersion=" + piJavaVersion + ", piJavaVM=" + piJavaVM + ", piJavaRuntime="
        + piJavaRuntime + ", piHostname=" + piHostname + ", piIPAddresses=" + Arrays.toString(piIPAddresses)
        + ", piFQDN=" + Arrays.toString(piFQDN) + ", piNameserver=" + Arrays.toString(piNameserver)
        + ", piH264CodecEnabled=" + piH264CodecEnabled + ", piMPG2CodecEnabled=" + piMPG2CodecEnabled
        + ", piWVC1CodecEnabled=" + piWVC1CodecEnabled + ", piARMFrequency=" + piARMFrequency + ", piCOREFrequency="
        + piCOREFrequency + ", piH264Frequency=" + piH264Frequency + ", piISPFrequency=" + piISPFrequency
        + ", piV3DFrequency=" + piV3DFrequency + ", piUARTFrequency=" + piUARTFrequency + ", piPWMFrequency="
        + piPWMFrequency + ", piEMMCFrequency=" + piEMMCFrequency + ", piPixelFrequency=" + piPixelFrequency
        + ", piVECFrequency=" + piVECFrequency + ", piHDMIFrequency=" + piHDMIFrequency + ", piDPIFrequency="
        + piDPIFrequency + "]";
  }

  public Map<String, Object> getHealthIndicator() {
    final Map<String, Object> result = new HashMap<>();
    result.put("fileSystems", fileSystems);
    result.put("systemLoadAverage", systemLoadAverage);
    result.put("hardwareRuntimetotalmemory", hardwareRuntimetotalmemory);
    result.put("hardwareRuntimefreememory", hardwareRuntimefreememory);
    result.put("hardwareFilelistroots", hardwareFilelistroots);
    result.put("hardwareComputersystem", hardwareComputersystem);
    result.put("hardwareDisk", hardwareDisk);
    result.put("hardwareDislay", hardwareDislay);
    result.put("hardwareMemorytotal", hardwareMemorytotal);
    result.put("hardwareMemoryavailable", hardwareMemoryavailable);
    result.put("hardwareSwaptotal", hardwareSwaptotal);
    result.put("hardwareSwapused", hardwareSwapused);
    result.put("hardwareNetwork", hardwareNetwork);
    result.put("hardwarePower", hardwarePower);
    result.put("hardwareProcessor", hardwareProcessor);
    result.put("hardwareSensor", hardwareSensor);
    result.put("hardwareUsb", hardwareUsb);
    result.put("operatingSystem", operatingSystem);
    return result;
  }
}
