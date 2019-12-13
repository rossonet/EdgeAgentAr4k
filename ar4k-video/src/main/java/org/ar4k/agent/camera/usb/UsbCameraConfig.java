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
package org.ar4k.agent.camera.usb;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione di una camera usb.
 */
public class UsbCameraConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -864167271161787678L;

  @Parameter(names = "--cameraId", description = "usb port of the camera")
  private String cameraId = "/dev/video0";
  @Parameter(names = "--driver", description = "driver for the camera system")
  private String driver = "VlcjDriver";

  @Override
  public UsbCameraService instantiate() {
    UsbCameraService ss = new UsbCameraService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 8;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new UsbCameraConfigJsonAdapter();
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getCameraId() {
    return cameraId;
  }

  public void setCameraId(String cameraId) {
    this.cameraId = cameraId;
  }
}
