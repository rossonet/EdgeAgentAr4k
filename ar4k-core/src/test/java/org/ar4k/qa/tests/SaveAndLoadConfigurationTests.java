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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

import org.ar4k.agent.config.AnimaStateMachineConfig;
import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.ar4k.gw.studio.tunnels.socket.ssl.SocketFactorySslConfig;
import org.bouncycastle.cms.CMSException;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
    Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaveAndLoadConfigurationTests {

  @Autowired
  Anima anima;

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
    c.pots.add(s1);
    c.pots.add(s2);
    String jsonConfig = ConfigHelper.toJson(c);
    System.out.println("CONFIG:\n" + jsonConfig);
    ConfigSeed a = ConfigHelper.fromJson(jsonConfig, Ar4kConfig.class);
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
    c.pots.add(s1);
    c.pots.add(s2);
    String checkText = ConfigHelper.toBase64(c);
    System.out.println("base64 config: " + checkText);
    ConfigSeed a = ConfigHelper.fromBase64(checkText);
    assertTrue(check.equals(((Ar4kConfig) a).author));
    assertTrue(check.equals(((SshLocalConfig) ((Ar4kConfig) a).pots.toArray()[0]).note));
    assertTrue(check.equals(((SocketFactorySslConfig) ((Ar4kConfig) a).pots.toArray()[1]).note));
  }

  @Test
  public void saveAndRestoreToFromBase64Rsa() throws CertificateEncodingException, ClassNotFoundException,
      NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IOException, CMSException {
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
    c.pots.add(s1);
    c.pots.add(s2);
    System.out.println("Anima -> " + anima);
    String baseCrypto = ConfigHelper.toBase64Crypto(c, "my-keystore-alias");
    System.out.println("CRYPTO " + baseCrypto);
    ConfigSeed a = ConfigHelper.fromBase64Crypto(baseCrypto, "my-keystore-alias");
    assertTrue(check.equals(((Ar4kConfig) a).author));
    assertTrue(check.equals(((SshLocalConfig) ((Ar4kConfig) a).pots.toArray()[0]).note));
    assertTrue(check.equals(((SocketFactorySslConfig) ((Ar4kConfig) a).pots.toArray()[1]).note));
  }

}
