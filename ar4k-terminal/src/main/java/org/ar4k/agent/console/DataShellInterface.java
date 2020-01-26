/**
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
package org.ar4k.agent.console;

import java.util.Collection;

import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.core.data.channels.IExecutorChannel;
import org.ar4k.agent.core.data.channels.IPriorityChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.core.data.channels.IRendezvousChannel;
import org.ar4k.agent.core.data.messages.StringChatRpcMessage;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia gestione servizi dati.
 */

@ShellCommandGroup("Data Server Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=dataInterface", description = "Ar4k Agent Data Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "dataInterface")
@RestController
@RequestMapping("/dataInterface")
public class DataShellInterface extends AbstractShellHelper implements MessageHandler {

  @ShellMethod(value = "List all data channels in Spring Integration enviroments", group = "Data Server Commands")
  @ManagedOperation
  public Collection<String> listSpringDataChannels() {
    Collection<String> result = anima.getDataAddress().listSpringIntegrationChannels();
    return result;
  }

  @ShellMethod(value = "List all managed data channels", group = "Data Server Commands")
  @ManagedOperation
  public Collection<String> listDataChannels() {
    return anima.getDataAddress().listChannels();
  }

  @ShellMethod(value = "get details of a single channel", group = "Data Server Commands")
  @ManagedOperation
  public String getDataChannelDetails(@ShellOption(help = "channel id (nodeId)") String channelId) {
    return anima.getDataAddress().getChannel(channelId).toString();
  }

  @ShellMethod(value = "Send a message to a channel", group = "Data Server Commands")
  @ManagedOperation
  public void sendToDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId,
      @ShellOption(help = "message to send") String message,
      @ShellOption(help = "timeout for blocking call") int timeout) {
    StringChatRpcMessage messageObject = new StringChatRpcMessage();
    messageObject.setPayload(message);
    if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IPriorityChannel.class))
      ((IPriorityChannel) anima.getDataAddress().getChannel(channelId)).send(messageObject, timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IQueueChannel.class))
      ((IQueueChannel) anima.getDataAddress().getChannel(channelId)).send(messageObject, timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IRendezvousChannel.class))
      ((IRendezvousChannel) anima.getDataAddress().getChannel(channelId)).send(messageObject, timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IDirectChannel.class))
      ((IDirectChannel) anima.getDataAddress().getChannel(channelId)).send(messageObject, timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IExecutorChannel.class))
      ((IExecutorChannel) anima.getDataAddress().getChannel(channelId)).send(messageObject, timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IPublishSubscribeChannel.class))
      ((IPublishSubscribeChannel) anima.getDataAddress().getChannel(channelId)).send(messageObject, timeout);
    else
      logger.error("can't send message to " + channelId);
  }

  @ShellMethod(value = "Subscribe channel and view the output in console", group = "Data Server Commands")
  @ManagedOperation
  public void subscribeDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId) {
    if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IDirectChannel.class))
      ((IDirectChannel) anima.getDataAddress().getChannel(channelId)).subscribe(this);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IExecutorChannel.class))
      ((IExecutorChannel) anima.getDataAddress().getChannel(channelId)).subscribe(this);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IPublishSubscribeChannel.class))
      ((IPublishSubscribeChannel) anima.getDataAddress().getChannel(channelId)).subscribe(this);
    else
      logger.error(channelId + " is not subscribable");
  }

  @ShellMethod(value = "Poll a message from a channel", group = "Data Server Commands")
  @ManagedOperation
  public void pollDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId,
      @ShellOption(help = "timeout for blocking call") int timeout) {
    if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IPriorityChannel.class))
      ((IPriorityChannel) anima.getDataAddress().getChannel(channelId)).receive(timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IQueueChannel.class))
      ((IQueueChannel) anima.getDataAddress().getChannel(channelId)).receive(timeout);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IRendezvousChannel.class))
      ((IRendezvousChannel) anima.getDataAddress().getChannel(channelId)).receive(timeout);
    else
      logger.error(channelId + " is not pollable");
  }

  @ShellMethod(value = "Unsubscribe channel", group = "Data Server Commands")
  @ManagedOperation
  public void unsubscribeDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId) {
    if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IDirectChannel.class))
      ((IDirectChannel) anima.getDataAddress().getChannel(channelId)).unsubscribe(this);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IExecutorChannel.class))
      ((IExecutorChannel) anima.getDataAddress().getChannel(channelId)).unsubscribe(this);
    else if (anima.getDataAddress().getChannel(channelId).getChannelClass().equals(IPublishSubscribeChannel.class))
      ((IPublishSubscribeChannel) anima.getDataAddress().getChannel(channelId)).unsubscribe(this);
    else
      logger.error(channelId + " is not subscribable");
  }

  @ShellMethod(value = "Add a direct data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataDirectChannel(@ShellOption(help = "node name") String dataChannel,
      @ShellOption(help = "description") String description) {
    anima.getDataAddress().createOrGetDataChannel(dataChannel, IDirectChannel.class, description, (String) null, null);
  }

  @ShellMethod(value = "Add a executor data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataExecutorChannel(@ShellOption(help = "node name") String dataChannel,
      @ShellOption(help = "description") String description) {
    anima.getDataAddress().createOrGetDataChannel(dataChannel, IExecutorChannel.class, description, (String) null,
        null);
  }

  @ShellMethod(value = "Add a priority data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataPriorityChannel(@ShellOption(help = "node name") String dataChannel,
      @ShellOption(help = "description") String description) {
    anima.getDataAddress().createOrGetDataChannel(dataChannel, IPriorityChannel.class, description, (String) null,
        null);
  }

  @ShellMethod(value = "Add a publish/subscribe  data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataPubSubChannel(@ShellOption(help = "node name") String dataChannel,
      @ShellOption(help = "description") String description) {
    anima.getDataAddress().createOrGetDataChannel(dataChannel, IPublishSubscribeChannel.class, description,
        (String) null, null);
  }

  @ShellMethod(value = "Add a queue data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataQueueChannel(@ShellOption(help = "node name") String dataChannel,
      @ShellOption(help = "description") String description) {
    anima.getDataAddress().createOrGetDataChannel(dataChannel, IQueueChannel.class, description, (String) null, null);
  }

  @ShellMethod(value = "Add a rendezvous data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataRendezvousChannel(@ShellOption(help = "node name") String dataChannel,
      @ShellOption(help = "description") String description) {
    anima.getDataAddress().createOrGetDataChannel(dataChannel, IRendezvousChannel.class, description, (String) null,
        null);
  }

  @ShellMethod(value = "Remove data channel from the address space", group = "Data Server Commands")
  @ManagedOperation
  public void removeDataChannel(@ShellOption(help = "target channel id to remove") String idChannel) {
    anima.getDataAddress().removeDataChannel(idChannel, true);
  }

  @ShellMethod(value = "Clear address space", group = "Data Server Commands")
  @ManagedOperation
  public void clearDataChannelsInAddressSpace() {
    anima.getDataAddress().clearDataChannels();
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    System.out.println(message.getPayload().toString());
  }

}
