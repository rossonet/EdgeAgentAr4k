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
package org.ar4k.agent.pcap;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Interfaccia a riga di comando per la gestione dell'interfaccia PCAP.
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("Pcap Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=pcapInterface", description = "Ar4k Agent pcap interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "pcapInterface")
@RestController
@RequestMapping("/pcapInterface")
public class PcapShellInterface extends AbstractShellHelper {

  @ShellMethod(value = "Send a register pcap file from a network interface", group = "Pcap Commands")
  @ManagedOperation
  public void playPcapFileOnInterface(
      @ShellOption(help = "complete path of pcap file to send in net netword") String fileToSend,
      @ShellOption(help = "interface to transmit from") String interfaceName,
      @ShellOption(help = "sleep time between packets in ms") Long waitTime) {
    sendDataFromPcapFile(fileToSend, interfaceName, waitTime);
  }

  @ShellMethod(value = "View all the packets on a interface with filter", group = "Pcap Commands")
  @ManagedOperation
  public void sniffOnInterface(@ShellOption(help = "network interface to sniff") String interfaceName,
      @ShellOption(help = "pcap filter -https://wiki.wireshark.org/CaptureFilters-") String pcapFilter,
      @ShellOption(help = "analyzer class -implements PackerAnalyzer-") String analyzer,
      @ShellOption(help = "number of packets to sniff") int countPackets) {
    basePcap(interfaceName, pcapFilter, analyzer, countPackets);
  }

  @ShellMethod(value = "List packets analyzer", group = "Pcap Commands")
  @ManagedOperation
  public Set<String> listPacketAnalyzer(@ShellOption(help = "package for searching") String packageName) {
    Set<String> rit = new HashSet<>();
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(Ar4kPcapAnalyzer.class));
    Set<BeanDefinition> classes = provider.findCandidateComponents(packageName);
    for (BeanDefinition c : classes) {
      rit.add(c.getBeanClassName());
    }
    return rit;
  }

  @ShellMethod(value = "List network devices from pcap API", group = "Pcap Commands")
  @ManagedOperation
  public Set<String> listNetworkDevices() {
    Set<String> rit = new HashSet<>();
    try {
      List<PcapNetworkInterface> lista = Pcaps.findAllDevs();
      for (PcapNetworkInterface a : lista) {
        rit.add(a.getName());
      }
    } catch (PcapNativeException e) {
      logger.logException(e);
    }
    return rit;
  }

  @ShellMethod(value = "Exec a PacketAnalizer filter on a pcap file", group = "Pcap Commands")
  @ManagedOperation
  public void analyzePcapFile(@ShellOption(help = "complete path of pcap file to analyze") String fileToScan,
      @ShellOption(help = "analyzer class -implements PackerAnalyzer-") String analyzer) {
    elaborateFilePcap(fileToScan, analyzer);
  }

  private static void elaborateFilePcap(String file, String analyzerClass) {
    PcapHandle handle;
    Constructor<?> con;
    try {
      Class<?> classe = Class.forName(analyzerClass);
      con = classe.getConstructor();
      handle = Pcaps.openOffline(file);
      Packet packet = null;
      boolean continua = true;
      PackerAnalyzer pa = (PackerAnalyzer) con.newInstance();
      while (continua) {
        try {
          packet = handle.getNextPacketEx();
        } catch (Exception a) {
          logger.logException(a);
          continua = false;
        }
        pa.elaboratePacket(packet);
      }
      handle.close();
    } catch (PcapNativeException | IOException | ClassNotFoundException | NoSuchMethodException | SecurityException
        | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      logger.logException(e);
    }
  }

  private static void basePcap(String interfaceName, String pcapFilter, String analyzerClass, int count) {
    if (pcapFilter == null)
      pcapFilter = "";
    PcapHandle handle = null;
    try {
      Class<?> classe = Class.forName(analyzerClass);
      Constructor<?> con = classe.getConstructor();
      PackerAnalyzer pa = (PackerAnalyzer) con.newInstance();
      PacketListener listener = new PacketListener() {
        @Override
        public void gotPacket(Packet packet) {
          try {
            pa.elaboratePacket(packet);
          } catch (IOException e) {
            logger.logException(e);
          }
        }
      };
      PcapNetworkInterface nif = Pcaps.getDevByName(interfaceName);
      int snapLen = 65536;
      PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
      int timeout = 10;
      handle = nif.openLive(snapLen, mode, timeout);
      handle.setFilter(pcapFilter, BpfCompileMode.OPTIMIZE);
      handle.loop(count, listener);
      handle.close();
    } catch (PcapNativeException | InterruptedException | NotOpenException | ClassNotFoundException
        | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      if (handle != null)
        handle.close();
      logger.logException(e);
    }
  }

  private static void sendDataFromPcapFile(String file, String interfaceName, Long waitTime) {
    PcapHandle handleFile;
    PcapHandle handleSender;
    try {
      handleFile = Pcaps.openOffline(file);
      int snapLen = 65536;
      PromiscuousMode mode = PromiscuousMode.NONPROMISCUOUS;
      PcapNetworkInterface nif = Pcaps.getDevByName(interfaceName);
      int timeout = 10000;
      handleSender = nif.openLive(snapLen, mode, timeout);
      Packet packet = null;
      boolean continua = true;
      while (continua) {
        try {
          packet = handleFile.getNextPacketEx();
        } catch (Exception a) {
          logger.logException(a);
          continua = false;
        }
        if (packet.contains(IpPacket.class)) {
          IpPacket ipp = packet.get(IpPacket.class);
          System.out.println("IP srcAddress: " + ipp.getHeader().getSrcAddr().getHostAddress() + " dstAddress: "
              + ipp.getHeader().getDstAddr().getHostAddress());
        }
        if (packet.contains(UdpPacket.class)) {
          UdpPacket udpp = packet.get(UdpPacket.class);
          System.out.println("UDP srcPort: " + udpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
              + udpp.getHeader().getDstPort().valueAsInt());
        }
        if (packet.contains(TcpPacket.class)) {
          TcpPacket tcpp = packet.get(TcpPacket.class);
          System.out.println("TCP srcPort: " + tcpp.getHeader().getSrcPort().valueAsInt() + " dstPort: "
              + tcpp.getHeader().getDstPort().valueAsInt());
        }
        if (packet.contains(IcmpV4CommonPacket.class)) {
          IcmpV4CommonPacket icpp = packet.get(IcmpV4CommonPacket.class);
          System.out.println("ICMP name: " + icpp.getHeader().getCode().name() + " value: "
              + icpp.getHeader().getCode().valueAsString());
        }
        if (packet != null) {
          System.out.println(Hex.encodeHexString(packet.getRawData()));
        }
        handleSender.sendPacket(packet);
        Thread.sleep(waitTime);
      }
      handleFile.close();
    } catch (PcapNativeException | NotOpenException | InterruptedException e) {
      logger.logException(e);
    }
  }

}
