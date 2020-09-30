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
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione di una camera usb.
 */
public class StreamCameraConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -864167271161787678L;

  @Parameter(names = "--cameraId", description = "usb port of the camera")
  private String cameraId = "/dev/video0";
  @Parameter(names = "--workingSize", description = "set working size for camera")
  private String workingSize = "640x480";
  @Parameter(names = "--ipCamName", description = "name of camera for ipCam")
  private String ipCamName = "laboratorio";
  @Parameter(names = "--ipCamPath", description = "ipCam path of image remote")
  private String ipCamPath = "http://admin:xxxxx@192.168.0.100/tmpfs/auto.jpg";
  @Parameter(names = "--ipCamMode", description = "ipCam mode. Should be PULL or PUSH")
  private String ipCamMode = "PULL";
  @Parameter(names = "--vlcjDriverPath", description = "vlc extra path")
  private String vlcjDriverPath = "rtsp://192.168.1.189:554/ch01.264?ptype=udp";
  @Parameter(names = "--vlcjName", description = "vlc name for the camera")
  private String vlcjName = "esterno";
  @Parameter(names = "--driver", description = "driver for the camera system")
  private String driver = "VlcjDriver";
  @Parameter(names = "--globalMotionDetectQueue", description = "queue of motion detection, if null the function is disabled")
  private String globalMotionDetectQueue = "trigger-global";
  @Parameter(names = "--globalMotionDetectinterval", description = "global motion detection interval (ms)")
  private Integer globalMotionDetectinterval = 500;
  @Parameter(names = "--globalImageQueue", description = "queue of image")
  private String globalImageQueue = "image-global";
  @Parameter(names = "--globalImageinterval", description = "global update image interval for every thread (ms)")
  private Integer globalImageinterval = 1000;
  @Parameter(names = "--globalThreads", description = "global number of threads for capturing")
  private Integer globalThreads = 1000;
  @Parameter(names = "--fatherOfChannels", description = "directory channel for message topics")
  private String fatherOfChannels = "video";
  @Parameter(names = "--scopeOfChannels", description = "scope for the parent channel. If null take the default of the address space")
  private String scopeOfChannels = null;

  @Override
  public EdgeComponent instantiate() {
    StreamCameraService ss = new StreamCameraService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 8;
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

  public String getWorkingSize() {
    return workingSize;
  }

  public void setWorkingSize(String workingSize) {
    this.workingSize = workingSize;
  }

  public String getIpCamName() {
    return ipCamName;
  }

  public void setIpCamName(String ipCamName) {
    this.ipCamName = ipCamName;
  }

  public String getIpCamPath() {
    return ipCamPath;
  }

  public void setIpCamPath(String ipCamPath) {
    this.ipCamPath = ipCamPath;
  }

  public String getIpCamMode() {
    return ipCamMode;
  }

  public void setIpCamMode(String ipCamMode) {
    this.ipCamMode = ipCamMode;
  }

  public String getGlobalMotionDetectQueue() {
    return globalMotionDetectQueue;
  }

  public void setGlobalMotionDetectQueue(String globalMotionDetectQueue) {
    this.globalMotionDetectQueue = globalMotionDetectQueue;
  }

  public Integer getGlobalMotionDetectinterval() {
    return globalMotionDetectinterval;
  }

  public void setGlobalMotionDetectinterval(Integer globalMotionDetectinterval) {
    this.globalMotionDetectinterval = globalMotionDetectinterval;
  }

  public String getVlcjDriverPath() {
    return vlcjDriverPath;
  }

  public void setVlcjDriverPath(String vlcjDriverPath) {
    this.vlcjDriverPath = vlcjDriverPath;
  }

  public String getVlcjName() {
    return vlcjName;
  }

  public void setVlcjName(String vlcjName) {
    this.vlcjName = vlcjName;
  }

  public String getGlobalImageQueue() {
    return globalImageQueue;
  }

  public void setGlobalImageQueue(String globalImageQueue) {
    this.globalImageQueue = globalImageQueue;
  }

  public Integer getGlobalImageinterval() {
    return globalImageinterval;
  }

  public void setGlobalImageinterval(Integer globalImageinterval) {
    this.globalImageinterval = globalImageinterval;
  }

  public Integer getGlobalThreads() {
    return globalThreads;
  }

  public void setGlobalThreads(Integer globalThread) {
    this.globalThreads = globalThread;
  }

  public String getFatherOfChannels() {
    return fatherOfChannels;
  }

  public void setFatherOfChannels(String fatherOfChannels) {
    this.fatherOfChannels = fatherOfChannels;
  }

  public String getScopeOfChannels() {
    return scopeOfChannels;
  }

  public void setScopeOfChannels(String scopeOfChannels) {
    this.scopeOfChannels = scopeOfChannels;
  }
}
