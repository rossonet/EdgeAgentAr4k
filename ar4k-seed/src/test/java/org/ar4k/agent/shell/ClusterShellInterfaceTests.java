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
package org.ar4k.agent.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.docker.DockerShellInterface;
import org.ar4k.agent.kubernetes.KubernetesShellInterface;
import org.ar4k.agent.openshift.OpenShiftShellInterface;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgekuserDetailsService;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Homunculus.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class, EdgekuserDetailsService.class,
    EdgeAuthenticationManager.class, BCryptPasswordEncoder.class, KubernetesShellInterface.class,
    OpenShiftShellInterface.class, DockerShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ClusterShellInterfaceTests {

  @Autowired
  Shell shell;

  @Autowired
  Homunculus homunculus;

  @Before
  public void setUp() throws Exception {
    Thread.sleep(3000L);
    System.out.println(homunculus.getState());
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
    assertEquals(homunculus.getState(), HomunculusStates.STAMINAL);
    Map<String, MethodTarget> listCommands = shell.listCommands();
    System.out.println("commands: " + listCommands);
    assertTrue(listCommands.containsKey("add-k8s-name-space"));
    assertTrue(listCommands.containsKey("apply-k8s-config-from-file"));
    assertTrue(listCommands.containsKey("apply-k8s-config-from-url"));
    assertTrue(listCommands.containsKey("clear"));
    assertTrue(listCommands.containsKey("connect-to-docker-daemon"));
    assertTrue(listCommands.containsKey("connect-to-docker-daemon-with-parameters"));
    assertTrue(listCommands.containsKey("connect-to-k8s-api"));
    assertTrue(listCommands.containsKey("connect-to-k8s-api-from-file"));
    assertTrue(listCommands.containsKey("connect-to-k8s-api-from-password"));
    assertTrue(listCommands.containsKey("connect-to-k8s-api-from-token"));
    assertTrue(listCommands.containsKey("connect-to-k8s-api-inside-cluster"));
    assertTrue(listCommands.containsKey("create-container-on-docker"));
    assertTrue(listCommands.containsKey("create-container-on-docker-with-cmd"));
    assertTrue(listCommands.containsKey("create-k8s-with-mini-kube"));
    assertTrue(listCommands.containsKey("disconnect-from-docker"));
    assertTrue(listCommands.containsKey("disconnect-from-k8s-api"));
    assertTrue(listCommands.containsKey("exec-command-on-docker-container"));
    assertTrue(listCommands.containsKey("exit"));
    assertTrue(listCommands.containsKey("get-docker-container-inspect"));
    assertTrue(listCommands.containsKey("get-docker-container-logs-from-container"));
    assertTrue(listCommands.containsKey("get-info-from-docker"));
    assertTrue(listCommands.containsKey("get-mini-kube-dashboard"));
    assertTrue(listCommands.containsKey("get-mini-kube-status"));
    assertTrue(listCommands.containsKey("help"));
    assertTrue(listCommands.containsKey("history"));
    assertTrue(listCommands.containsKey("initialize-swarm"));
    assertTrue(listCommands.containsKey("install-kube-flow"));
    assertTrue(listCommands.containsKey("kill-container-on-docker"));
    assertTrue(listCommands.containsKey("list-docker-container-images"));
    assertTrue(listCommands.containsKey("list-docker-container-networks"));
    assertTrue(listCommands.containsKey("list-docker-container-services"));
    assertTrue(listCommands.containsKey("list-docker-container-volumes"));
    assertTrue(listCommands.containsKey("list-docker-containers"));
    assertTrue(listCommands.containsKey("list-k8s-component-status"));
    assertTrue(listCommands.containsKey("list-k8s-config-map-for-all-namespaces"));
    assertTrue(listCommands.containsKey("list-k8s-limit-range-for-all-namespaces"));
    assertTrue(listCommands.containsKey("list-k8s-nodes"));
    assertTrue(listCommands.containsKey("list-k8s-persistent-volume"));
    assertTrue(listCommands.containsKey("list-k8s-pods-for-all-namespaces"));
    assertTrue(listCommands.containsKey("list-k8s-secret-config-map-for-all-namespaces"));
    assertTrue(listCommands.containsKey("list-k8s-service-for-all-namespaces"));
    assertTrue(listCommands.containsKey("prune-docker"));
    assertTrue(listCommands.containsKey("pull-container-on-docker"));
    assertTrue(listCommands.containsKey("quit"));
    assertTrue(listCommands.containsKey("remove-container-from-docker"));
    assertTrue(listCommands.containsKey("remove-image-from-docker"));
    assertTrue(listCommands.containsKey("remove-k8s-with-mini-kube"));
    assertTrue(listCommands.containsKey("run-helm-command-line"));
    assertTrue(listCommands.containsKey("run-kops-command-line"));
    assertTrue(listCommands.containsKey("run-kubectl-command-line"));
    assertTrue(listCommands.containsKey("run-mini-kube-command-line"));
    assertTrue(listCommands.containsKey("script"));
    assertTrue(listCommands.containsKey("stacktrace"));
    assertTrue(listCommands.containsKey("start-container-on-docker"));
    assertTrue(listCommands.containsKey("start-portainer-container"));
    assertTrue(listCommands.containsKey("stop-container-on-docker"));
    assertTrue(listCommands.containsKey("stop-mini-kube-dashboard"));
    // printCheckNow(listCommands);
  }

  @SuppressWarnings("unused")
  private void printCheckNow(Map<String, MethodTarget> listCommands) {
    for (String command : listCommands.keySet()) {
      System.out.println("assertTrue(listCommands.containsKey(\"" + command + "\"));");
    }

  }

}
