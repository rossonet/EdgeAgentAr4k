package org.ar4k.agent.iot.serial.marlin;

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

@ShellCommandGroup("CNC Commands")
@ShellComponent
public class MarlinShellInterface extends AbstractShellHelper {

	@ShellMethod(value = "Add a 3D printer with Marlin firmware to the selected configuration", group = "CNC Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addMarlinService(@ShellOption(optOut = true) @Valid MarlinConfig service) {
		getWorkingConfig().pots.add(service);
	}

}
