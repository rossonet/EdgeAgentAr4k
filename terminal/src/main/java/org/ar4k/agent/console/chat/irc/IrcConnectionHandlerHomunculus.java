package org.ar4k.agent.console.chat.irc;

import org.ar4k.agent.console.rpc.BaseShellChatRpcExecutor;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.rpc.Ar4kSession;
import org.ar4k.agent.rpc.Homunculus;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.client.library.util.Format;
import org.springframework.shell.Shell;

import net.engio.mbassy.listener.Handler;

public class IrcConnectionHandlerHomunculus implements Homunculus<Ar4kSession> {

  private Client client = null;
  private BaseShellChatRpcExecutor executor = new BaseShellChatRpcExecutor();

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
      sendTextMessage(executor.elaborateMessage(event.getMessage()), event.getActor());
    }
  }

  private void sendTextMessage(String resultMessage, User targetUser) {
    for (String line : resultMessage.split("\\r\\n|\\r|\\n")) {
      client.sendMultiLineMessage(targetUser, line);
    }
  }

}
