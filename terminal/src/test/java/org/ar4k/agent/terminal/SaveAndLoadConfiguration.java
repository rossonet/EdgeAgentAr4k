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
package org.ar4k.agent.terminal;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.console.ShellInterface;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.sshClientTunnel.SshConfig;
import org.ar4k.agent.stunnel.StunnelConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class SaveAndLoadConfiguration {

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
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void saveAndRestoreToFromJson() throws InterruptedException, IOException, ClassNotFoundException {
    Anima a = new Anima();
    Ar4kConfig c = new Ar4kConfig();
    String check = UUID.randomUUID().toString();
    c.name = "test salvataggio json";
    c.author = check;
    SshConfig s1 = new SshConfig();
    s1.name = "ssh config";
    s1.note = check;
    StunnelConfig s2 = new StunnelConfig();
    s2.name = "stunnel config";
    s2.note = check;
    c.pots.add(s1);
    c.pots.add(s2);
    a.setWorkingConfig(c);
    ShellInterface si = new ShellInterface();
    si.setAnima(a);
    si.saveSelectedConfigJson("~/Scrivania/provaJson");
    si.loadSelectedConfigJson("~/Scrivania/provaJson");
    assertTrue(check.equals(a.getWorkingConfig().author));
    assertTrue(check.equals(((SshConfig) a.getWorkingConfig().services.toArray()[0]).note));
    assertTrue(check.equals(((StunnelConfig) a.getWorkingConfig().services.toArray()[1]).note));
  }

  @Test
  public void saveAndRestoreToFromBase64() throws InterruptedException, ClassNotFoundException, IOException {
    Anima a = new Anima();
    Ar4kConfig c = new Ar4kConfig();
    String check = UUID.randomUUID().toString();
    c.author = check;
    c.name = "test salvataggio base64";
    SshConfig s1 = new SshConfig();
    s1.name = "ssh config";
    s1.note = check;
    StunnelConfig s2 = new StunnelConfig();
    s2.name = "stunnel config";
    s2.note = check;
    c.pots.add(s1);
    c.pots.add(s2);
    a.setWorkingConfig(c);
    ShellInterface si = new ShellInterface();
    si.setAnima(a);
    si.saveSelectedConfigBase64("~/Scrivania/provaBase64");
    si.loadSelectedConfigBase64("~/Scrivania/provaBase64");
    assertTrue(check.equals(a.getWorkingConfig().author));
    assertTrue(check.equals(((SshConfig) a.getWorkingConfig().services.toArray()[0]).note));
    assertTrue(check.equals(((StunnelConfig) a.getWorkingConfig().services.toArray()[1]).note));
  }

  @Test
  public void saveAndRestoreToFromBase64Rsa()
      throws InterruptedException, ClassNotFoundException, IOException, InvalidKeyException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, UnrecoverableEntryException {
    Anima a = new Anima();
    Ar4kConfig c = new Ar4kConfig();
    String check = UUID.randomUUID().toString();
    c.name = "test salvataggio base64";
    c.author = check;
    SshConfig s1 = new SshConfig();
    s1.name = "ssh config";
    s1.note = check;
    StunnelConfig s2 = new StunnelConfig();
    s2.name = "stunnel config";
    s2.note = check;
    c.pots.add(s1);
    c.pots.add(s2);
    a.setWorkingConfig(c);
    ShellInterface si = new ShellInterface();
    si.setAnima(a);
    si.saveSelectedConfigBase64Rsa("~/Scrivania/provaBase64Rsa", "root");
    si.loadSelectedConfigBase64Rsa("~/Scrivania/provaBase64Rsa");
    assertTrue(check.equals(a.getWorkingConfig().author));
    assertTrue(check.equals(((SshConfig) a.getWorkingConfig().services.toArray()[0]).note));
    assertTrue(check.equals(((StunnelConfig) a.getWorkingConfig().services.toArray()[1]).note));
  }

}
