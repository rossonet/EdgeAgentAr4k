package org.ar4k.agent.iot.serial.json;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
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
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=iotSerialInterface", description = "Ar4k Agent IoT Serial Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "iotSerialInterface")
public class SerialJsonShellInterface extends AbstractShellHelper {

  @ShellMethod(value = "Add a IoT serial interface", group = "IoT Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addIotSerialService(@ShellOption(optOut = true) @Valid SerialJsonConfig service) {
    getWorkingConfig().pots.add(service);
  }

}
