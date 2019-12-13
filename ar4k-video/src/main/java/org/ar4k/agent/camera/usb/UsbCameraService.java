package org.ar4k.agent.camera.usb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.javacv.JavaCvDriver;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.github.sarxos.webcam.ds.vlcj.VlcjDriver;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione seriale.
 */
public class UsbCameraService extends AbstractAr4kService {

  public static final String[] drivers = { "JavaCvDriver", "VlcjDriver", "V4l4jDriver", "IpCamDriver" }; // "FFmpegCliDriver"

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(UsbCameraService.class.toString());

  private UsbCameraConfig configuration = null;

  private Webcam webcam = null;

  @Override
  public void stop() {
    if (webcam != null) {
      webcam.close();
      webcam = null;
    }
  }

  @Override
  public void init() {
    setDriver(configuration.getDriver());
    webcam = Webcam.getWebcamByName(configuration.getCameraId());
    if (webcam == null) {
      webcam = Webcam.getDefault();
    }
    webcam.open();
    try {
      ImageIO.write(webcam.getImage(), "PNG", new File(configuration.getCameraId().replace("/", "-") + ".png"));
      logger.info("Open Camera");
      logger.info("Name: " + webcam.getName());
      logger.info("FPS: " + webcam.getFPS());
      logger.info("Device: " + webcam.getDevice());
      logger.info("View Size: " + webcam.getViewSize());
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @Override
  public void setConfiguration(AbstractServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = (UsbCameraConfig) configuration;
  }

  @Override
  public JSONObject getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void close() throws Exception {
    stop();
  }

  @Override
  public void loop() {
    // TODO Auto-generated method stub

  }

  public static Collection<String> getUsbCameras(String driver) {
    setDriver(driver);
    List<String> result = new ArrayList<>();
    for (Webcam webcam : Webcam.getWebcams()) {
      result.add(webcam.getName() + " [" + webcam.getDevice() + "]");
    }
    return result;
  }

  private static void setDriver(String driverLabel) {
    WebcamDriver driver = null;
    switch (driverLabel) {
    case "JavaCvDriver":
      driver = new JavaCvDriver();
      break;
    /*
     * case "FFmpegCliDriver": driver = new FFmpegCliDriver(); break;
     */
    case "VlcjDriver":
      driver = new VlcjDriver();
      break;
    case "V4l4jDriver":
      driver = new V4l4jDriver();
      break;
    case "IpCamDriver":
      driver = new IpCamDriver();
      break;
    default:
      throw new UnsupportedOperationException("the drive " + driverLabel + " not exists");
    }

    Webcam.setDriver(driver);
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (UsbCameraConfig) configuration;
  }

}
