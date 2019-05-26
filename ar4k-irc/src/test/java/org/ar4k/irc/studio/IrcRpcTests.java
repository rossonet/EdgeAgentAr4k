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
package org.ar4k.irc.studio;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ar4k.agent.console.chat.irc.IrcConnectionHandlerHomunculus;
import org.jline.builtins.Commands;
import org.jline.reader.LineReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.client.library.event.user.UserAwayMessageEvent;
import org.kitteh.irc.client.library.event.user.UserQuitEvent;
import org.kitteh.irc.client.library.util.Format;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.Input;
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
import org.springframework.util.StringUtils;

import net.engio.mbassy.listener.Handler;

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
public class IrcRpcTests {

  Client client = null;

  public BeanFactory context = null;

  Shell shell;

  @Bean
  @Autowired
  public InputProvider inputProvider(LineReader lineReader, PromptProvider promptProvider) {
    return new InteractiveShellApplicationRunner.JLineInputProvider(lineReader, promptProvider);
  }

  @Before
  public void setUp() throws Exception {
    connectToServer();
    context = new AnnotationConfigApplicationContext(this.getClass());
    shell = context.getBean(Shell.class);
    // shell.run(context.getBean(InputProvider.class));
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

  public class ListenerCmd {
    @Handler
    public void onUserJoinChannel(ChannelJoinEvent event) {
      if (event.getClient().isUser(event.getUser())) { // It's me!
        event.getChannel().sendMessage(Format.GREEN + "Pronto a ricevere comandi");
        return;
      } else {
        // It's not me!
        event.getChannel().sendMessage(Format.RED + "Benvenuto, " + event.getUser().getNick());
      }
    }

    @Handler
    public void onUserQuitEvent(UserQuitEvent event) {
      event.getAffectedChannel().get().sendMessage(Format.RED + event.getUser().getNick() + " è uscito dal canale");
    }

    @Handler
    public void onUserQuitEvent(UserAwayMessageEvent event) {
      client.sendMessage("#test", Format.RED + event.getActor().getNick() + " è andato via dal server");
    }

    @Handler
    public void onChannelMessageEvent(ChannelMessageEvent event) {
      // System.out.println(event.getActor() + " channel message: " +
      // event.getMessage());
      if (!event.getClient().isUser(event.getActor())) {
        if (event.getMessage().equals("healt")) {
          client.sendMessage(event.getChannel(), Format.GREEN + "il mio stato");
        } else {
          client.sendMessage(event.getChannel(), Format.RED + "posso ricevere comandi solo come messaggi privati");
        }
      }
    }

    @Handler
    public void onPrivateMessageEvent(PrivateMessageEvent event) throws Exception {
      // System.out.println(event.getActor() + "private message: " +
      if (!event.getClient().isUser(event.getActor())) {
        String message = event.getMessage();
        // CommandLineRunner commandRunner =
        // context.getBean(ExampleCommandLineRunner.class);
        // commandRunner.run(message);
        System.out.println("run " + message);
        Input i = new Input() {
          @Override
          public List<String> words() {
            return Arrays.asList(StringUtils.tokenizeToStringArray(message, " "));
          }

          @Override
          public String rawText() {
            return message;
          }
        };
        System.out.println(shell.evaluate(i).toString());
        for (String line : shell.evaluate(i).toString().split("\\r\\n|\\r|\\n")) {
          client.sendMessage(event.getActor(), line);
        }
        /*
         * client.sendMessage("#test", Format.RED + event.getActor().getName() + "(" +
         * event.getActor().getHost() + ") mi ha scritto: " + event.getMessage());
         */
      }
    }
  }

  private String dumpThreadDump() {
    String ritorno = "";
    ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    for (ThreadInfo ti : threadMxBean.dumpAllThreads(true, true)) {
      ritorno = ritorno + "\n" + ti.toString();
    }
    return ritorno;
  }

  public class Listener {
    @Handler
    public void onUserJoinChannel(ChannelJoinEvent event) {
      if (event.getClient().isUser(event.getUser())) { // It's me!
        event.getChannel().sendMessage(Format.RED + "Ciao a tutti!");
        return;
      } else {
        // It's not me!
        event.getChannel().sendMessage(Format.RED + "Benvenuto, " + event.getUser().getNick() + "!");
      }
    }

    @Handler
    public void onUserQuitEvent(UserQuitEvent event) {
      event.getAffectedChannel().get().sendMessage(Format.RED + event.getUser().getNick() + " è uscito dal canale...!");
    }

    @Handler
    public void onUserQuitEvent(UserAwayMessageEvent event) {
      client.sendMessage("#cmd", Format.RED + event.getActor().getNick() + " è andato via dal server!");
    }

    @Handler
    public void onChannelMessageEvent(ChannelMessageEvent event) {
      // System.out.println(event.getActor() + " channel message: " +
      // event.getMessage());
      if (!event.getClient().isUser(event.getActor())) {
        client.sendMessage(event.getChannel(), Format.RED + "ehi " + event.getActor().getName() + "("
            + event.getActor().getHost() + ") hai scritto: " + event.getMessage());
      }
    }

    @Handler
    public void onPrivateMessageEvent(PrivateMessageEvent event) {
      // System.out.println(event.getActor() + "private message: " +
      // event.getMessage());
      if (!event.getClient().isUser(event.getActor())) {
        client.sendMessage(event.getActor(), Format.RED + "ehi " + event.getActor().getName() + "("
            + event.getActor().getHost() + ") hai scritto: " + event.getMessage() + " in privato");
        client.sendMessage("#cmd", Format.RED + event.getActor().getName() + "(" + event.getActor().getHost()
            + ") mi ha scritto: " + event.getMessage());
      }
    }
  }

  @Test
  public void testSendMessage() throws InterruptedException {
    client.addChannel("#test");
    client.getEventManager().registerEventListener(new Listener());
    // client.sendMessage("#test", "Hello World!");
    System.out.println(client.getServerInfo().toString());
    System.out.println(dumpThreadDump());
    Thread.sleep(2400000L);
    client.shutdown();
  }

  @Test
  public void testCmdLookUp() throws InterruptedException, BeansException, IOException {
    client.addChannel("#cmd");
    client.getEventManager().registerEventListener(new ListenerCmd());
    // client.sendMessage("#test", "Hello World!");
    System.out.println(client.getServerInfo().toString());
    Thread.sleep(2400000L);
    client.shutdown();
  }

  @Test
  public void testHelp() {
    IrcConnectionHandlerHomunculus rpc = new IrcConnectionHandlerHomunculus(client, shell);
    System.out.println(rpc.executeMessage("help"));
  }
  
  @Test
  public void testComplete() {
    IrcConnectionHandlerHomunculus rpc = new IrcConnectionHandlerHomunculus(client, shell);
    System.out.println(rpc.completeMessage("h?"));
    System.out.println(rpc.completeMessage("history --fi?"));
    System.out.println(rpc.completeMessage("history --file?"));
    System.out.println(rpc.completeMessage("history --file ?"));
  }

  private void connectToServer() {
    Builder builder = Client.builder().nick("testBot").server().host("celestini.rossonet.net").port(6667).secure(false)
        .then();
    // debug
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    builder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
    builder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
    builder.listeners().exception(Throwable::printStackTrace);
    // debu
    client = builder.build();
    // client.setDefaultMessageMap(new SimpleDefaultMessageMap());
    client.connect();
    System.out.println(client.getServerInfo().toString());
  }

}
