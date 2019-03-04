package org.ar4k.agent.iot.serial.marlin;

import javax.validation.Valid;

import org.ar4k.agent.core.Anima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

@ShellCommandGroup("CNC Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=marlinInterface", description = "Ar4k Agent Marlin Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "marlinInterface")
public class MarlinShellInterface {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Anima anima;

	@Override
	protected void finalize() {
	}

	@ShellMethod(value = "Add a 3D printer with Marlin firmware to the selected configuration", group = "CNC Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addMarlinService(@ShellOption(optOut = true) @Valid MarlinConfig service) {
		anima.getWorkingConfig().services.add(service);
	}

}
