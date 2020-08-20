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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.ar4k.agent.console.BeaconShellInterface;
import org.ar4k.agent.console.DataShellInterface;
import org.ar4k.agent.console.KeystoreShellInterface;
import org.ar4k.agent.console.ShellInterface;
import org.ar4k.agent.console.SshShellInterface;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
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
import org.springframework.shell.MethodTarget;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
    Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class, KeystoreShellInterface.class,
    BeaconShellInterface.class, DataShellInterface.class, ShellInterface.class, SshShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AllShellInterfaceTests {

  @Autowired
  Shell shell;

  @Autowired
  Anima anima;

  @Before
  public void setUp() throws Exception {
    Thread.sleep(3000L);
    System.out.println(anima.getState());
  }

  @After
  public void tearDownAfterClass() throws Exception {

  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void listCommandAndCheck() throws InterruptedException, IOException {
    Thread.sleep(10000);
    assertEquals(anima.getState(), AnimaStates.STAMINAL);
    Map<String, MethodTarget> listCommands = shell.listCommands();
    System.out.println("commands: " + listCommands);
    assertTrue(listCommands.containsKey("add-beacon-service"));
    assertTrue(listCommands.containsKey("add-data-direct-channel"));
    assertTrue(listCommands.containsKey("add-data-executor-channel"));
    assertTrue(listCommands.containsKey("add-data-priority-channel"));
    assertTrue(listCommands.containsKey("add-data-pub-sub-channel"));
    assertTrue(listCommands.containsKey("add-data-queue-channel"));
    assertTrue(listCommands.containsKey("add-data-rendezvous-channel"));
    assertTrue(listCommands.containsKey("add-keystore"));
    assertTrue(listCommands.containsKey("add-keystore-runtime"));
    assertTrue(listCommands.containsKey("add-ssh-tunnel-remote-port-to-locale"));
    assertTrue(listCommands.containsKey("add-ssh-tunnel-local-port-to-remote"));
    assertTrue(listCommands.containsKey("check-keystore"));
    assertTrue(listCommands.containsKey("clear"));
    assertTrue(listCommands.containsKey("clear-data-channels-in-address-space"));
    assertTrue(listCommands.containsKey("clone-config"));
    assertTrue(listCommands.containsKey("clone-runtime-config"));
    assertTrue(listCommands.containsKey("close-session-and-logout"));
    assertTrue(listCommands.containsKey("complete-reload"));
    assertTrue(listCommands.containsKey("connect-to-beacon-service"));
    assertTrue(listCommands.containsKey("create-beacon-tunnel"));
    assertTrue(listCommands.containsKey("create-selected-config"));
    assertTrue(listCommands.containsKey("create-self-signed-cert"));
    assertTrue(listCommands.containsKey("create-self-signed-cert-and-sign"));
    assertTrue(listCommands.containsKey("create-user-account"));
    assertTrue(listCommands.containsKey("delete-user-account"));
    assertTrue(listCommands.containsKey("exit"));
    assertTrue(listCommands.containsKey("get-agent-status"));
    assertTrue(listCommands.containsKey("get-anima"));
    assertTrue(listCommands.containsKey("get-beans-info"));
    assertTrue(listCommands.containsKey("get-client-certificate-base64"));
    assertTrue(listCommands.containsKey("get-data-channel-details"));
    assertTrue(listCommands.containsKey("get-environment-variables"));
    assertTrue(listCommands.containsKey("get-hardware-info"));
    assertTrue(listCommands.containsKey("get-keystore-for-dns"));
    assertTrue(listCommands.containsKey("get-log-level"));
    assertTrue(listCommands.containsKey("get-pkcs10certification-request-base64"));
    assertTrue(listCommands.containsKey("get-private-key-base64"));
    assertTrue(listCommands.containsKey("get-roles-authority"));
    assertTrue(listCommands.containsKey("get-runtime-config-json"));
    assertTrue(listCommands.containsKey("get-runtime-config-yaml"));
    assertTrue(listCommands.containsKey("get-selected-config-base64"));
    assertTrue(listCommands.containsKey("get-selected-config-base64crypto"));
    assertTrue(listCommands.containsKey("get-selected-config-for-dns"));
    assertTrue(listCommands.containsKey("get-selected-config-for-dns-encrypted"));
    assertTrue(listCommands.containsKey("get-selected-config-json"));
    assertTrue(listCommands.containsKey("get-selected-config-yaml"));
    assertTrue(listCommands.containsKey("get-threads-info"));
    assertTrue(listCommands.containsKey("get-unique-name"));
    assertTrue(listCommands.containsKey("get-users-list"));
    assertTrue(listCommands.containsKey("goodbye"));
    assertTrue(listCommands.containsKey("help"));
    assertTrue(listCommands.containsKey("history"));
    assertTrue(listCommands.containsKey("import-selected-config-base64"));
    assertTrue(listCommands.containsKey("import-selected-config-base64crypted"));
    assertTrue(listCommands.containsKey("import-selected-config-json"));
    assertTrue(listCommands.containsKey("import-selected-config-yaml"));
    assertTrue(listCommands.containsKey("initialize-keystore-ca"));
    assertTrue(listCommands.containsKey("kill-process"));
    assertTrue(listCommands.containsKey("list-beacon-agents"));
    assertTrue(listCommands.containsKey("list-beacon-agents-connected"));
    assertTrue(listCommands.containsKey("list-beacon-tunnels"));
    assertTrue(listCommands.containsKey("list-commands-on-remote-agent"));
    assertTrue(listCommands.containsKey("list-config"));
    assertTrue(listCommands.containsKey("list-data-channels"));
    assertTrue(listCommands.containsKey("list-jmx-endpoints"));
    assertTrue(listCommands.containsKey("list-jsr223script-engines-in-runtime"));
    assertTrue(listCommands.containsKey("list-keys-in-keystore"));
    assertTrue(listCommands.containsKey("list-keystore-keys"));
    assertTrue(listCommands.containsKey("list-keystores"));
    assertTrue(listCommands.containsKey("list-processes"));
    assertTrue(listCommands.containsKey("list-service"));
    assertTrue(listCommands.containsKey("list-services-selected-config"));
    assertTrue(listCommands.containsKey("list-sessions"));
    assertTrue(listCommands.containsKey("list-spring-data-channels"));
    assertTrue(listCommands.containsKey("list-xpra-servers"));
    assertTrue(listCommands.containsKey("load-selected-config-base64"));
    assertTrue(listCommands.containsKey("load-selected-config-base64crypted"));
    assertTrue(listCommands.containsKey("load-selected-config-json"));
    assertTrue(listCommands.containsKey("load-selected-config-yaml"));
    assertTrue(listCommands.containsKey("login"));
    assertTrue(listCommands.containsKey("logout"));
    assertTrue(listCommands.containsKey("me"));
    assertTrue(listCommands.containsKey("pause"));
    assertTrue(listCommands.containsKey("poll-data-channel"));
    assertTrue(listCommands.containsKey("quit"));
    assertTrue(listCommands.containsKey("remove-data-channel"));
    assertTrue(listCommands.containsKey("remove-service-selected-config"));
    assertTrue(listCommands.containsKey("restart"));
    assertTrue(listCommands.containsKey("run-beacon-server"));
    assertTrue(listCommands.containsKey("run-command-line"));
    assertTrue(listCommands.containsKey("run-command-on-remote-agent"));
    assertTrue(listCommands.containsKey("run-jsr223script"));
    assertTrue(listCommands.containsKey("run-xpra-server"));
    assertTrue(listCommands.containsKey("save-selected-config-base64"));
    assertTrue(listCommands.containsKey("save-selected-config-base64crypto"));
    assertTrue(listCommands.containsKey("save-selected-config-json"));
    assertTrue(listCommands.containsKey("save-selected-config-yaml"));
    assertTrue(listCommands.containsKey("script"));
    assertTrue(listCommands.containsKey("select-config"));
    assertTrue(listCommands.containsKey("send-log-message-error"));
    assertTrue(listCommands.containsKey("send-log-message-info"));
    assertTrue(listCommands.containsKey("send-to-data-channel"));
    assertTrue(listCommands.containsKey("set-agent-status"));
    assertTrue(listCommands.containsKey("set-client-key-pair"));
    assertTrue(listCommands.containsKey("set-log-level"));
    assertTrue(listCommands.containsKey("set-selected-config-as-runtime"));
    assertTrue(listCommands.containsKey("sign-certificate-base64"));
    assertTrue(listCommands.containsKey("stacktrace"));
    assertTrue(listCommands.containsKey("stop-beacon-client"));
    assertTrue(listCommands.containsKey("stop-beacon-server"));
    assertTrue(listCommands.containsKey("subscribe-data-channel"));
    assertTrue(listCommands.containsKey("unset-selected-config"));
    assertTrue(listCommands.containsKey("unsubscribe-data-channel"));
    assertTrue(listCommands.containsKey("view-key-in-keystore"));
    printCheckNow(listCommands);
  }

  @SuppressWarnings("unused")
  private void printCheckNow(Map<String, MethodTarget> listCommands) {
    for (String command : listCommands.keySet()) {
      System.out.println("- " + command);
    }

  }

}
