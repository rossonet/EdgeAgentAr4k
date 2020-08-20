package org.ar4k.agent.activemq;

import javax.validation.Valid;

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

  ActiveMqService broker = null;

  protected Availability testActiveMQNull() {
    return broker == null ? Availability.available()
        : Availability.unavailable("a ActiveMQ broker exists with name " + broker.getBroker().getBrokerName());
  }

  protected Availability testActiveMQRunning() {
    return broker != null ? Availability.available() : Availability.unavailable("activeMQ broker is not running");
  }

  @ShellMethod(value = "Add ActiveMQ MQTT broker configuration", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addActiveMq(@ShellOption(optOut = true) @Valid ActiveMqConfig activeMqConfig) {
    getWorkingConfig().pots.add(activeMqConfig);
  }

  @ShellMethod(value = "Start ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQNull")
  public void activeMqStart(@ShellOption(optOut = true) @Valid ActiveMqConfig activeMqConfig) {
    ActiveMqService activeMqInstance = (ActiveMqService) activeMqConfig.instantiate();
    activeMqInstance.init();
    broker = activeMqInstance;
  }

  @ShellMethod(value = "Stop ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public void activeMqStop() throws Exception {
    broker.kill();
    broker = null;
  }

  @ShellMethod(value = "Get ActiveMQ MQTT broker uptime", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public String activeMqUptime() throws Exception {
    return broker.getBroker().getUptime();
  }

  @ShellMethod(value = "List active connection to ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public long activeMqListConnections() throws Exception {
    return broker.getBroker().getCurrentConnections();
  }

  @ShellMethod(value = "Get System usage of ActiveMQ MQTT broker", group = "ActiveMQ Commands")
  @ManagedOperation
  @ShellMethodAvailability("testActiveMQRunning")
  public MetricsManager activeMqUsage() throws Exception {
    return broker.getBroker().getSystemUsage();
  }

}
