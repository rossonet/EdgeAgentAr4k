package org.ar4k.agent.console.chat.irc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.rpc.Homunculus;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.client.library.util.Format;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Shell;

import net.engio.mbassy.listener.Handler;

public class IrcConnectionHandlerHomunculus implements Homunculus {

  private static final CharSequence COMPLETION_CHAR = "?";
  private Client client = null;
  private RpcConversation executor = new RpcConversation();

  public IrcConnectionHandlerHomunculus(Client client, Shell shell) {
    this.client = client;
    executor.setShell(shell);
  }

  @Handler
  public void onUserJoinChannel(ChannelJoinEvent event) {
    if (event.getClient().isUser(event.getUser())) {
      event.getChannel().sendMessage(Format.GREEN + "Ready for commands");
      return;
    } else {
      event.getChannel().sendMessage(Format.YELLOW + "Hi " + event.getUser().getNick());
    }
  }

  @Handler
  public void onChannelMessageEvent(ChannelMessageEvent event) {
    if (!event.getClient().isUser(event.getActor())) {
      if (event.getMessage().contains("health")) {
        sendTextMessage(event.getChannel(),
            Format.RED + Anima.getApplicationContext().getBean(Anima.class).getState().toString());
        sendTextMessage(event.getChannel(),
            Format.BLUE + Anima.getApplicationContext().getBean(Anima.class).getEnvironmentVariablesAsString());
      } else {
        client.sendMessage(event.getChannel(), Format.YELLOW
            + "you can just call health command in the channel chat. Also you can send a me a private message.");
      }
    }
  }

  private void sendTextMessage(Channel channel, String resultMessage) {
    for (String line : resultMessage.split("\\r\\n|\\r|\\n")) {
      client.sendMultiLineNotice(channel, line);
    }
  }

  @Handler
  public void onPrivateMessageEvent(PrivateMessageEvent event) throws Exception {
    if (!event.getClient().isUser(event.getActor())) {
      String message = event.getMessage();
      if (!message.contains(COMPLETION_CHAR)) {
        sendTextMessage(executeMessage(message), event.getActor());
      } else {
        String response = completeMessage(message);
        sendTextMessage(response, event.getActor());
      }
    }
  }

  public String executeMessage(String message) {
    return executor.elaborateMessage(message);
  }

  public String completeMessage(String message) {
    List<String> m = Arrays.asList(StringUtils.split(message));
    List<String> clean = new ArrayList<>(m.size());
    int pos = 0;
    int word = 0;
    int counter = 0;
    for (String p : m) {
      if (p.contains(COMPLETION_CHAR)) {
        word = counter;
        pos = p.indexOf(COMPLETION_CHAR.toString());
        if (!p.equals(COMPLETION_CHAR.toString())) {
          // System.out.println("add " + p.replace(COMPLETION_CHAR, ""));
          clean.add(p.replace(COMPLETION_CHAR, ""));
        } else {
          // System.out.println("add " + p);
          clean.add(p);
        }
      } else {
        clean.add(p);
      }
      counter++;
    }
    CompletionContext context = new CompletionContext(clean, word, pos);
    List<CompletionProposal> listCompletionProposal = executor.complete(context);
    StringBuffer response = new StringBuffer();
    for (CompletionProposal prop : listCompletionProposal) {
      response.append(prop.toString() + (prop.description() != null ? " => " + prop.description() : "") + "\n");
    }
    return response.toString();
  }

  private void sendTextMessage(String resultMessage, User targetUser) {
    for (String line : resultMessage.split("\\r\\n|\\r|\\n")) {
      client.sendMultiLineMessage(targetUser, line);
    }
  }

}
