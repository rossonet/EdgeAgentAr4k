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
package org.ar4k.agent.camera;

import javax.validation.Valid;

import org.ar4k.agent.camera.usb.UsbCameraConfig;
import org.ar4k.agent.camera.usb.UsbCameraService;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per configurazione della connessione
 *         seriale.
 *
 */

@ShellCommandGroup("Camera Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=cameraInterface", description = "Ar4k Agent Main Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "cameraInterface")
@RestController
@RequestMapping("/cameraInterface")
public class CameraShellInterface extends AbstractShellHelper {

  private UsbCameraService camera = null;

  protected Availability testUsbCameraServiceNull() {
    return camera == null ? Availability.available()
        : Availability.unavailable("a camera exists with status " + camera.getStatusString());
  }

  protected Availability testUsbCameraServiceRunning() {
    return camera != null ? Availability.available() : Availability.unavailable("no camera service are running");
  }

  @ShellMethod(value = "List camera attached to this host", group = "Camera Commands")
  @ManagedOperation
  public String listUsbCameras(
      @ShellOption(help = "driver type", valueProvider = CameraDriverValuesProvider.class) String driver) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(UsbCameraService.getUsbCameras(driver));
  }

  @ShellMethod(value = "Add a usb camera service to the selected configuration", group = "Camera Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addUsbCameraService(@ShellOption(optOut = true) @Valid UsbCameraConfig service) {
    getWorkingConfig().pots.add(service);
  }

  @ShellMethod(value = "Create a usb camera service", group = "Camera Commands")
  @ManagedOperation
  @ShellMethodAvailability("testUsbCameraServiceNull")
  public void createUsbCameraService(@ShellOption(optOut = true) @Valid UsbCameraConfig configuration) {
    logger.info("Camera driver " + configuration.getDriver());
    camera = new UsbCameraService();
    camera.setConfiguration(configuration);
    camera.init();
  }

  @ShellMethod(value = "Stop camera instance", group = "Camera Commands")
  @ManagedOperation
  @ShellMethodAvailability("testUsbCameraServiceRunning")
  public void usbCameraInstanceStop() {
    camera.kill();
    camera = null;
  }

}
