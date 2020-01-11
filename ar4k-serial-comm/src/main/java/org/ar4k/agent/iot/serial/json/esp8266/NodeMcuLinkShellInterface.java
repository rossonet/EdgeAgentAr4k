package org.ar4k.agent.iot.serial.json.esp8266;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */

@ShellCommandGroup("IoT Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=iotNodeMcuInterface", description = "Ar4k Agent IoT NodeMcu Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "iotNodeMcuInterface")
public class NodeMcuLinkShellInterface extends AbstractShellHelper {

  @ShellMethod(value = "Add a NodeMcu controller connected via wifi", group = "IoT Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addNodeMcuService(@ShellOption(optOut = true) @Valid NodeMcuLinkConfig service) {
    getWorkingConfig().pots.add(service);
  }

}
