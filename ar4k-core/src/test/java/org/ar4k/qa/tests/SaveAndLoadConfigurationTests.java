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
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.tunnels.socket.ssl.SocketFactorySslConfig;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.ar4k.gw.anima.TestApplicationRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ComponentScan("org.ar4k.agent")
@Import(TestApplicationRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaveAndLoadConfigurationTests {

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
    Ar4kConfig c = new Ar4kConfig();
    String check = UUID.randomUUID().toString();
    c.name = "test salvataggio json";
    c.author = check;
    SshLocalConfig s1 = new SshLocalConfig();
    s1.name = "ssh config";
    s1.note = check;
    SocketFactorySslConfig s2 = new SocketFactorySslConfig();
    s2.name = "stunnel config";
    s2.note = check;
    c.services.add(s1);
    c.pots.add(s2);
    ConfigSeed a = ConfigHelper.fromJson(ConfigHelper.toJson(c));
    assertTrue(check.equals(((Ar4kConfig) a).author));
    assertTrue(check.equals(((SshLocalConfig) ((Ar4kConfig) a).pots.toArray()[0]).note));
    assertTrue(check.equals(((SocketFactorySslConfig) ((Ar4kConfig) a).pots.toArray()[1]).note));
  }

  @Test
  public void saveAndRestoreToFromBase64() throws InterruptedException, ClassNotFoundException, IOException {
    Ar4kConfig c = new Ar4kConfig();
    String check = UUID.randomUUID().toString();
    c.name = "test salvataggio json";
    c.author = check;
    SshLocalConfig s1 = new SshLocalConfig();
    s1.name = "ssh config";
    s1.note = check;
    SocketFactorySslConfig s2 = new SocketFactorySslConfig();
    s2.name = "stunnel config";
    s2.note = check;
    c.services.add(s1);
    c.pots.add(s2);
    String checkText = ConfigHelper.toBase64(c);
    System.out.println("base64 config: " + checkText);
    ConfigSeed a = ConfigHelper.fromBase64(checkText);
    assertTrue(check.equals(((Ar4kConfig) a).author));
    assertTrue(check.equals(((SshLocalConfig) ((Ar4kConfig) a).pots.toArray()[0]).note));
    assertTrue(check.equals(((SocketFactorySslConfig) ((Ar4kConfig) a).pots.toArray()[1]).note));
  }

  @Test
  public void saveAndRestoreToFromBase64Rsa()
      throws InterruptedException, ClassNotFoundException, IOException, InvalidKeyException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, UnrecoverableEntryException {
    Ar4kConfig c = new Ar4kConfig();
    String check = UUID.randomUUID().toString();
    c.name = "test salvataggio json";
    c.author = check;
    SshLocalConfig s1 = new SshLocalConfig();
    s1.name = "ssh config";
    s1.note = check;
    SocketFactorySslConfig s2 = new SocketFactorySslConfig();
    s2.name = "stunnel config";
    s2.note = check;
    c.services.add(s1);
    c.pots.add(s2);
    ConfigSeed a = ConfigHelper.fromBase64Rsa(ConfigHelper.toBase64Rsa(c, "privateKeyAlias"));
    assertTrue(check.equals(((Ar4kConfig) a).author));
    assertTrue(check.equals(((SshLocalConfig) ((Ar4kConfig) a).pots.toArray()[0]).note));
    assertTrue(check.equals(((SocketFactorySslConfig) ((Ar4kConfig) a).pots.toArray()[1]).note));
  }

}
