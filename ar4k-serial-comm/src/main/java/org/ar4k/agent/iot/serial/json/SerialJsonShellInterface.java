package org.ar4k.agent.iot.serial.json;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */

@ShellCommandGroup("Serial Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=iotSerialInterface", description = "Ar4k Agent IoT Serial Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "iotSerialInterface")
public class SerialJsonShellInterface extends AbstractShellHelper {

  private SerialJsonService serialServiceJson = null;

  protected Availability testSerialServiceJsonNull() {
    return serialServiceJson == null ? Availability.available()
        : Availability.unavailable("a Serial JSON test service exists with status " + serialServiceJson);
  }

  protected Availability testSerialServiceRunning() {
    return serialServiceJson != null ? Availability.available()
        : Availability.unavailable("no serial JSON service are running");
  }

  @ShellMethod(value = "Add a serial interface with json semantic", group = "Serial Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addJsonSerialService(@ShellOption(optOut = true) @Valid SerialJsonConfig service) {
    getWorkingConfig().pots.add(service);
  }

  @ShellMethod(value = "Create Serial JSON service", group = "Serial Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSerialServiceJsonNull")
  public void createSerialJsonService(@ShellOption(optOut = true) @Valid SerialJsonConfig configuration) {
    serialServiceJson = new SerialJsonService();
    serialServiceJson.setConfiguration(configuration);
    serialServiceJson.init();
  }

  @ShellMethod(value = "Stop serial json instance", group = "Serial Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSerialServiceRunning")
  public void serialInstanceJsonStop() {
    serialServiceJson.kill();
    serialServiceJson = null;
  }

}
