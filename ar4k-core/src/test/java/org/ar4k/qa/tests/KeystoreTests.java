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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
    Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class KeystoreTests {

  private static final String passwordKeystore = "passw!ord";
  private static final String fileKeystore = "./tmp/testks";

  @Before
  public void setUp() throws Exception {
    File serverKeyStore = new File(fileKeystore);
    if (!serverKeyStore.exists()) {
      serverKeyStore.delete();
    }
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
  public void createSelfSignedCert() throws Exception {
    Map<String, Object> ritorno = new HashMap<>();
    // KeyStoreLoader loader = new KeyStoreLoader();
    KeystoreLoader.create("master", fileKeystore, passwordKeystore);
    ritorno.put("crt-master",
        KeystoreLoader.getClientCertificateBase64("master", fileKeystore, passwordKeystore).toString());
    ritorno.put("key-master", KeystoreLoader.getPrivateKeyBase64("master", fileKeystore, passwordKeystore).toString());
    ritorno.put("list", KeystoreLoader.listCertificate(fileKeystore, passwordKeystore));
    Map<String, Object> root = new HashMap<>();
    root.put("data", ritorno);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(root));
  }

  @Test
  public void createCsr() throws Exception {
    Map<String, Object> ritorno = new HashMap<>();
    // KeyStoreLoader loader = new KeyStoreLoader();
    KeystoreLoader.create("master", fileKeystore, passwordKeystore);
    KeystoreLoader.createSelfSignedCert("client1", fileKeystore, passwordKeystore);
    ritorno.put("csr", KeystoreLoader.getPKCS10CertificationRequest("client1", fileKeystore, passwordKeystore));
    ritorno.put("csr-base64",
        KeystoreLoader.getPKCS10CertificationRequestBase64("client1", fileKeystore, passwordKeystore));
    ritorno.put("list", KeystoreLoader.listCertificate(fileKeystore, passwordKeystore));
    Map<String, Object> root = new HashMap<>();
    root.put("data", ritorno);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(root));
  }

  @Test
  public void createCsrAndFirm() throws Exception {
    Map<String, Object> ritorno = new HashMap<>();
    // KeyStoreLoader loader = new KeyStoreLoader();
    KeystoreLoader.create("master", fileKeystore, passwordKeystore);
    KeystoreLoader.createSelfSignedCert("client1", fileKeystore, passwordKeystore);
    PKCS10CertificationRequest csr = KeystoreLoader.getPKCS10CertificationRequest("client1", fileKeystore,
        passwordKeystore);
    // ritorno.put("csr", csr);
    ritorno.put("csr-base64",
        KeystoreLoader.getPKCS10CertificationRequestBase64("client1", fileKeystore, passwordKeystore));
    ritorno.put("signed",
        KeystoreLoader.signCertificateBase64(csr, "client1-signed", 4, "client1", fileKeystore, passwordKeystore));
    ritorno.put("list", KeystoreLoader.listCertificate(fileKeystore, passwordKeystore));
    Map<String, Object> root = new HashMap<>();
    root.put("data", ritorno);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(root));
  }

  @Test
  public void createMasterAndSelfSignedCert() throws Exception {
    Map<String, Object> ritorno = new HashMap<>();
    KeystoreLoader.create("master", fileKeystore, passwordKeystore);
    ritorno.put("crt-master",
        KeystoreLoader.getClientCertificateBase64("master", fileKeystore, passwordKeystore).toString());
    ritorno.put("key-master", KeystoreLoader.getPrivateKeyBase64("master", fileKeystore, passwordKeystore).toString());
    Map<String, Object> root = new HashMap<>();
    KeystoreLoader.createSelfSignedCert("client1", fileKeystore, passwordKeystore);
    // ritorno.put("crt-client",
    // KeyStoreLoader.getClientCertificateBase64("client1").toString());
    // ritorno.put("key-client",
    // KeyStoreLoader.getPrivateKeyBase64("client1").toString());
    ritorno.put("list", KeystoreLoader.listCertificate(fileKeystore, passwordKeystore));
    root.put("data", ritorno);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(root));
  }

}
