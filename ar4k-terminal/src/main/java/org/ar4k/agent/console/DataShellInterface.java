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

import javax.validation.Valid;

import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.core.data.channels.IExecutorChannel;
import org.ar4k.agent.core.data.channels.IPriorityChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.core.data.channels.IRendezvousChannel;
import org.ar4k.agent.core.data.messages.StringChatRpcMessage;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
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
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=dataInterface", description = "Ar4k Agent Data Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "dataInterface")
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
    StringChatRpcMessage<String> messageObject = new StringChatRpcMessage<String>();
    messageObject.setPayload(message);
    anima.getDataAddress().getChannel(channelId).send(messageObject, timeout);
  }

  @ShellMethod(value = "Subscribe channel and view the output in console", group = "Data Server Commands")
  @ManagedOperation
  public void subscribeDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId) {
    anima.getDataAddress().getChannel(channelId).subscribe(this);
  }

  @ShellMethod(value = "Poll a message from a channel", group = "Data Server Commands")
  @ManagedOperation
  public void pollDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId,
      @ShellOption(help = "timeout for blocking call") int timeout) {
    anima.getDataAddress().getChannel(channelId).receive(timeout);
  }

  @ShellMethod(value = "Unsubscribe channel", group = "Data Server Commands")
  @ManagedOperation
  public void unsubscribeDataChannel(@ShellOption(help = "channel id (nodeId)") String channelId) {
    anima.getDataAddress().getChannel(channelId).unsubscribe(this);
  }

  @ShellMethod(value = "Add a direct data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataDirectChannel(@ShellOption(optOut = true) @Valid IDirectChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Add a executor data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataExecutorChannel(@ShellOption(optOut = true) @Valid IExecutorChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Add a priority data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataPriorityChannel(@ShellOption(optOut = true) @Valid IPriorityChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Add a publish/subscribe  data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataPubSubChannel(@ShellOption(optOut = true) @Valid IPublishSubscribeChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Add a queue data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataQueueChannel(@ShellOption(optOut = true) @Valid IQueueChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Add a rendezvous data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataRendezvousChannel(@ShellOption(optOut = true) @Valid IRendezvousChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Remove data channel from the address space", group = "Data Server Commands")
  @ManagedOperation
  public void removeDataChannel(@ShellOption(help = "target channel id to remove") String idChannel) {
    anima.getDataAddress().removeDataChannel(idChannel);
  }

  @ShellMethod(value = "Clear address space", group = "Data Server Commands")
  @ManagedOperation
  public void clearDataChannelsAddressSpace() {
    anima.getDataAddress().clearDataChannels();
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    System.out.println(message.getPayload().toString());
  }

}
