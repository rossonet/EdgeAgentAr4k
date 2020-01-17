package org.ar4k.agent.camera.usb;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.ar4k.agent.camera.messages.VideoMessage;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;
import com.github.sarxos.webcam.ds.javacv.JavaCvDriver;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.github.sarxos.webcam.ds.vlcj.VlcjDriver;

import uk.co.caprica.vlcj.medialist.MediaListItem;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione seriale.
 */
public class UsbCameraService implements Ar4kComponent, Runnable {

  public static final String[] drivers = { "JavaCvDriver", "VlcjDriver", "V4l4jDriver", "IpCamDriver" }; // "FFmpegCliDriver"

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(UsbCameraService.class.toString());

  private Anima anima = null;

  private UsbCameraConfig configuration = null;

  private ScheduledExecutorService executor = null;

  private Webcam webcam = null;

  private Ar4kChannel channelRoot = null;

  private IPublishSubscribeChannel globalImageQueue = null;

  private IPublishSubscribeChannel globalMotionDetectQueue = null;

  private final Set<VideoCapture> runningIstances = new HashSet<>();

  private class VideoCapture implements Runnable {
    @Override
    public void run() {
      if (webcam != null && globalImageQueue != null && webcam.isOpen()) {
        VideoMessage m = new VideoMessage();
        final Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("size-width", webcam.getViewSize().getWidth());
        headersMap.put("size-height", webcam.getViewSize().getHeight());
        headersMap.put("source", webcam.getName());
        headersMap.put("type", "video");
        final MessageHeaders headers = new MessageHeaders(headersMap);
        m.setHeaders(headers);
        m.setPayload(webcam.getImage());
        globalImageQueue.send(m);
      }
    }
  }

  private void popolateDataTopics() {
    channelRoot = anima.getDataAddress().createOrGetDataChannel(configuration.getFatherOfChannels(),
        INoDataChannel.class, "video " + configuration.description + " root node", (String) null, null);
    globalImageQueue = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        configuration.getGlobalImageQueue(), IPublishSubscribeChannel.class, "video " + configuration.description,
        channelRoot, configuration.getScopeOfChannels() != null ? configuration.getScopeOfChannels()
            : anima.getDataAddress().getDefaultScope());
    globalMotionDetectQueue = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        configuration.getGlobalMotionDetectQueue(), IPublishSubscribeChannel.class,
        "video " + configuration.description, channelRoot,
        configuration.getScopeOfChannels() != null ? configuration.getScopeOfChannels()
            : anima.getDataAddress().getDefaultScope());
    globalImageQueue.addTag("video");
    globalImageQueue.setDescription("video camera stream of BufferedImage");
    globalMotionDetectQueue.addTag("detect");
    globalMotionDetectQueue.addTag("video");
    globalMotionDetectQueue.setDescription("video camera stream of BufferedImage when motion is triggered");
  }

  @Override
  public ServiceConfig getConfiguration() {
    return configuration;
  }

  public Webcam getWebcam() {
    return webcam;
  }

  @Override
  public void kill() {
    if (executor != null) {
      executor.shutdownNow();
      executor = null;
    }
    if (!runningIstances.isEmpty()) {
      runningIstances.clear();
    }
    if (webcam != null) {
      webcam.close();
      webcam = null;
    }
  }

  @Override
  public void init() {
    if (configuration.getDriver().equals("IpCamDriver") && configuration.getIpCamMode() != null
        && configuration.getIpCamName() != null && configuration.getIpCamPath() != null) {
      IpCamMode mode = null;
      if (configuration.getIpCamMode().equalsIgnoreCase("PUSH")) {
        mode = IpCamMode.PUSH;
      } else {
        mode = IpCamMode.PULL;
      }
      try {
        IpCamDeviceRegistry.register(configuration.getIpCamName(), configuration.getIpCamPath(), mode);
      } catch (MalformedURLException e) {
        logger.logException(e);
      }
    }
    setDriver(configuration);
    webcam = Webcam.getWebcamByName(configuration.getCameraId());
    if (webcam == null) {
      webcam = Webcam.getDefault();
    }
    if (configuration.getWorkingSize() != null) {
      Dimension workingDimension = new Dimension(Integer.valueOf(configuration.getWorkingSize().split("x|X")[0]),
          Integer.valueOf(configuration.getWorkingSize().split("x|X")[1]));
      webcam.setViewSize(workingDimension);
    }
    logger.info("Opened Camera");
    webcam.open();
    popolateDataTopics();
    if (executor == null) {
      executor = Executors.newScheduledThreadPool(configuration.getGlobalThreads());
      for (int n = 0; n < configuration.getGlobalThreads(); n++) {
        runningIstances.add(new VideoCapture());
        executor.scheduleAtFixedRate(new VideoCapture(), configuration.getGlobalImageinterval(),
            configuration.getGlobalImageinterval(), TimeUnit.MILLISECONDS);
      }
    }
  }

  public String getName() {
    return webcam != null ? webcam.getName() : null;
  }

  public Double getFps() {
    return webcam != null ? webcam.getFPS() : null;
  }

  public String getDevice() {
    return webcam != null ? webcam.getDevice().getName() : null;
  }

  public Dimension getViewSize() {
    return webcam != null ? webcam.getViewSize() : null;
  }

  public BufferedImage getImage() {
    return webcam != null ? webcam.getImage() : null;
  }

  public Dimension[] getSupportedSizes() {
    return webcam != null ? webcam.getViewSizes() : null;
  }

  public void writeToFile(String fileType, String fileName) throws IOException {
    ImageIO.write(getImage(), fileType, new File(fileName));
  }

  public static String[] getSupportedTypes() {
    return ImageIO.getReaderFormatNames();
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    this.configuration = (UsbCameraConfig) configuration;
  }

  @Override
  public void close() throws Exception {
    kill();
  }

  public static Collection<String> getUsbCameras(String driver) {
    UsbCameraConfig c = new UsbCameraConfig();
    c.setDriver(driver);
    setDriver(c);
    List<String> result = new ArrayList<>();
    for (Webcam webcam : Webcam.getWebcams()) {
      result.add(webcam.getName() + " [" + webcam.getDevice() + "]");
    }
    return result;
  }

  public static void resetDriver() {
    try {
      Webcam.resetDriver();
    } catch (Exception a) {
      logger.info("reset video driver fault");
    }
  }

  private static void setDriver(UsbCameraConfig config) {
    WebcamDriver driver = null;
    switch (config.getDriver()) {
    case "JavaCvDriver":
      driver = new JavaCvDriver();
      break;
    /*
     * case "FFmpegCliDriver": driver = new FFmpegCliDriver(); break;
     */
    case "VlcjDriver":
      if (config.getVlcjDriverPath() != null && config.getVlcjName() != null) {
        driver = new VlcjDriver(Arrays.asList(
            new MediaListItem(config.getVlcjName(), config.getVlcjDriverPath(), new ArrayList<MediaListItem>())));
      } else {
        driver = new VlcjDriver();
      }
      break;
    case "V4l4jDriver":
      driver = new V4l4jDriver();
      break;
    case "IpCamDriver":
      driver = new IpCamDriver();
      break;
    default:
      throw new UnsupportedOperationException("the drive " + config.getDriver() + " not exists");
    }
    Webcam.setDriver(driver);
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub

  }

  @Override
  public ServiceStates updateAndGetStatus() throws ServiceWatchDogException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Anima getAnima() {
    return anima;
  }

  @Override
  public DataAddress getDataAddress() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDataAddress(DataAddress dataAddress) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setAnima(Anima anima) {
    this.anima = anima;
  }

  @Override
  public JSONObject getDescriptionJson() {
    // TODO Auto-generated method stub
    return null;
  }
}
