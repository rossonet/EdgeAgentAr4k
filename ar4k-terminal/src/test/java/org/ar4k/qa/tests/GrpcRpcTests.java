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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.tunnels.http.grpc.BeaconClient;
import org.ar4k.agent.tunnels.http.grpc.BeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.jline.builtins.Commands;
import org.jline.reader.LineReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.jline.PromptProvider;
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
public class GrpcRpcTests {

  public BeanFactory context = null;
  BeaconServer server = null;
  BeaconClient client = null;
  RpcConversation rpcConversation = null;
  int port = 12589;

  Shell shell;

  @Bean
  @Autowired
  public InputProvider inputProvider(LineReader lineReader, PromptProvider promptProvider) {
    return new InteractiveShellApplicationRunner.JLineInputProvider(lineReader, promptProvider);
  }

  @Before
  public void setUp() throws Exception {
    context = new AnnotationConfigApplicationContext(this.getClass());
    shell = context.getBean(Shell.class);
    server = new BeaconServer(port);
    server.start();
    rpcConversation = new RpcConversation();
    rpcConversation.setShell(shell);
    Thread.sleep(3000L);
    client = new BeaconClient(rpcConversation, "127.0.0.1", port);
  }

  @After
  public void tearDown() throws Exception {
    client.shutdown();
    server.stop();
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void checkRemoteListCommands() throws InterruptedException, IOException, ParseException {
    Thread.sleep(2000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
    String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out.println("REGISTER STATUS: " + status + " [register code] " + client.getAgentUniqueName());
    assertEquals("GOOD", status);
    List<Agent> list = client.listAgentsConnectedToBeacon();
    assertEquals(list.isEmpty(), false);
    System.out.println("I'm " + list.get(0).getAgentUniqueName());
    ListCommandsReply repCommList = client.listCommadsOnAgent(list.get(0).getAgentUniqueName());
    System.out.println("List of my command " + repCommList.toString());
  }

  @Test
  public void checkRemoteExecCommand() throws InterruptedException, IOException, ParseException {
    Thread.sleep(2000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
    String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out.println("REGISTER STATUS: " + status + " [register code] " + client.getAgentUniqueName());
    assertEquals("GOOD", status);
    List<Agent> list = client.listAgentsConnectedToBeacon();
    assertEquals(list.isEmpty(), false);
    System.out.println("I'm " + list.get(0).getAgentUniqueName());
    ElaborateMessageReply repCommList = client.runCommadsOnAgent(list.get(0).getAgentUniqueName(), "help");
    System.out.println(repCommList.getReply());
  }

  @Test
  public void checkRemoteExecCompletitioCommand() throws InterruptedException, IOException, ParseException {
    Thread.sleep(2000L);
    String ls = client.getStateConnection().name();
    System.out.println("LAST STATE: " + ls);
    assertEquals("READY", ls);
    // server.blockUntilShutdown();
    String status = client.registerToBeacon(Anima.generateNewUniqueName());
    System.out.println("REGISTER STATUS: " + status + " [register code] " + client.getAgentUniqueName());
    assertEquals("GOOD", status);
    List<Agent> list = client.listAgentsConnectedToBeacon();
    assertEquals(list.isEmpty(), false);
    System.out.println("I'm " + list.get(0).getAgentUniqueName());
    CompleteCommandReply repCommList = client.runCompletitionOnAgent(list.get(0).getAgentUniqueName(), "hel?");
    System.out.println(repCommList.getRepliesList());
  }

}
