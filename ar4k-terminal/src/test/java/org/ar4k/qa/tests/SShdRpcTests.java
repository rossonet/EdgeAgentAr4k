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

import java.io.IOException;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.pubkey.AcceptAllPublickeyAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.ar4k.agent.console.chat.sshd.SshdConnectionHandlerHomunculus;
import org.ar4k.agent.console.chat.sshd.SshdShellInterface;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.Shell;
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

@Configuration
@Import({
    // Core runtime
    SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class,
    // Various Resolvers
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class,
    // Built-In Commands
    StandardCommandsAutoConfiguration.class,
    // Sample Commands
    Commands.class, FileValueProvider.class })
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Ignore
public class SShdRpcTests {

  SshdShellInterface component = null;

  public BeanFactory context = null;

  Shell shell;

  @Before
  public void setUp() throws Exception {
    component = new SshdShellInterface();
    context = new AnnotationConfigApplicationContext(this.getClass());
    shell = context.getBean(Shell.class);
    // shell.run(context.getBean(InputProvider.class));
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
  public void testManualApi() throws InterruptedException, IOException {
    SshServer server = null;
    server = SshServer.setUpDefaultServer();
    server.setHost("0.0.0.0");
    server.setPort(6666);
    server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
    server.setPublickeyAuthenticator(AcceptAllPublickeyAuthenticator.INSTANCE);
    server.setShellFactory(new SshdConnectionHandlerHomunculus());
    server.start();
    Thread.sleep(1000 * 60 * 1);
  }

}
