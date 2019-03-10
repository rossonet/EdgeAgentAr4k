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
package org.ar4k.gw.studio;

import org.ar4k.agent.pcap.PcapShellInterface;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class Pcap {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    pcapShellInterface = new PcapShellInterface();
  }

  @After
  public void tearDown() throws Exception {
    pcapShellInterface = null;
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  PcapShellInterface pcapShellInterface;

  @Test
  public void listDevices() {
    System.out.println(pcapShellInterface.listNetworkDevices());
  }

  @Test
  public void checkDecoder() {
    char[] files = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'l', 'm' };
    for (char test : files) {
      String nomeFile = "example/" + test + ".pcap";
      pcapShellInterface.analyzePcapFile(nomeFile, "org.ar4k.agent.pcap.BasePacketAnalyzer");
    }
  }

  @Test
  // necessita dei permessi di root
  public void checkSender() {
    char[] files = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'l', 'm' };
    for (char test : files) {
      String nomeFile = "example/" + test + ".pcap";
      pcapShellInterface.playPcapFileOnInterface(nomeFile, "wlp1s0", 200L);
    }
  }

  @Test
  public void listAnalyzer() {
    System.out.println(pcapShellInterface.listPacketAnalyzer("org.ar4k.agent.pcap.ice"));
  }

}
