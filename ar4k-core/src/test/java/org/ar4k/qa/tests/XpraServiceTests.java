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
package org.ar4k.qa.tests;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.ar4k.agent.rpc.process.xpra.XpraSessionProcess;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

@Ignore
public class XpraServiceTests {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void xpraTestClass() throws InterruptedException, IOException, URISyntaxException {
    XpraSessionProcess xpra = new XpraSessionProcess();
    xpra.debug = false;
    xpra.eval("xterm");
    Thread.sleep(20 * 1000);
    System.out.println(xpra.getOutput());
    System.out.println("on browser: http://127.0.0.1:" + xpra.getTcpPort());
    System.out.println("XPRA PORT FOR TEST: xpra attach tcp:127.0.0.1:" + xpra.getTcpPort());
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop().browse(new URI("http://127.0.0.1:" + xpra.getTcpPort()));
    }
    Thread.sleep(10 * 60 * 1000);
    xpra.close();
  }

}
