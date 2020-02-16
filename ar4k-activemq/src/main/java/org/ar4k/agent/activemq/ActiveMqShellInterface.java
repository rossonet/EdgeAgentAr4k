package org.ar4k.agent.activemq;

import org.apache.activemq.artemis.core.server.metrics.MetricsManager;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

/**
 *
 * Interfaccia da linea di comando
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("ActiveMQ Commands")
@ShellComponent
public class ActiveMqShellInterface extends AbstractShellHelper {

  ActiveMqBroker broker = null;

  protected Availability testActiveMQNull() {
    return broker == null ? Availability.available()
        : Availability.unavailable("a ActiveMQ broker exists with name " + broker.getBrokerName());
  }

  protected Availability testActiveMQRunning() {
    return broker != null ? Availability.available() : Availability.unavailable("activeMQ broker is not running");
  }

  @ShellMethod(value = "Start ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQNull")
  public void activeMqStart(@ShellOption(help = "port to expose the MQTT service", defaultValue = "1883") String port,
      @ShellOption(help = "port to expose the MQTT service ssl", defaultValue = "8883") String portSsl,
      @ShellOption(help = "port to expose the ws service", defaultValue = "8883") String portWs) throws Exception {
    broker = new ActiveMqBroker(new ActiveMqSecurityManager(), port, portSsl, portWs,
        anima.getMyIdentityKeystore().filePath(), anima.getMyIdentityKeystore().keystorePassword);
    broker.start();
  }

  @ShellMethod(value = "Stop ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public void activeMqStop() throws Exception {
    broker.stop();
    broker = null;
  }

  @ShellMethod(value = "Get ActiveMQ MQTT broker uptime", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public String activeMqUptime() throws Exception {
    return broker.getUptime();
  }

  @ShellMethod(value = "List active connection to ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public long activeMqListConnections() throws Exception {
    return broker.getCurrentConnections();
  }

  @ShellMethod(value = "Get System usage of ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public MetricsManager activeMqUsage() throws Exception {
    return broker.getSystemUsage();
  }

}
