package org.ar4k.agent.video;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.ar4k.agent.camera.usb.UsbCameraConfig;
import org.ar4k.agent.camera.usb.UsbCameraService;
import org.junit.After;
import org.junit.Test;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;

public class VideoTest implements WebcamMotionListener, WebcamPanel.Painter {

  public static final String[] drivers = { "JavaCvDriver", "VlcjDriver", "V4l4jDriver", "IpCamDriver" }; // "FFmpegCliDriver"

  private UsbCameraService service = null;
  WebcamMotionDetector detector = null;
  public static WebcamPanel.Painter painter = null;
  public static HashMap<Point, Integer> motionPoints = new HashMap<>();

  @After
  public void tearDown() throws Exception {
    UsbCameraService.resetDriver();
    if (service != null) {
      service.close();
      service = null;
    }
    if (detector != null) {
      detector.stop();
      detector = null;
    }
  }

  @Test(timeout = 600000)
  public void testDetectionLaboratorio() throws IOException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    config.setVlcjName("laboratorio");
    config.setVlcjDriverPath("rtsp://admin:westing85;@192.168.0.100:554/");
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    detectMotionExample(service.getWebcam());
    System.in.read(); // keep program open
  }

  @Test(timeout = 600000)
  public void testDetectionCiospo() throws IOException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    config.setVlcjDriverPath(null);
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    detectMotionExample(service.getWebcam());
    System.in.read(); // keep program open
  }

  @Test(timeout = 600000)
  public void testDetectionEsterno() throws IOException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    detectMotionExample(service.getWebcam());
    System.in.read(); // keep program open
  }

  @Test(timeout = 10000)
  public void checkCamerasIpCamDriver() {
    System.out.println("\n\n");
    System.out.println("IpCamDriver " + UsbCameraService.getUsbCameras("IpCamDriver"));
    System.out.println("\n\n");
  }

  @Test(timeout = 10000)
  public void checkCamerasV4l4jDriver() {
    System.out.println("\n\n");
    System.out.println("V4l4jDriver " + UsbCameraService.getUsbCameras("V4l4jDriver"));
    System.out.println("\n\n");
  }

  @Test(timeout = 10000)
  public void checkCamerasVlcjDriver() {
    System.out.println("\n\n");
    System.out.println("VlcjDriver " + UsbCameraService.getUsbCameras("VlcjDriver"));
    System.out.println("\n\n");
  }

  @Test(timeout = 10000)
  public void checkCamerasJavaCvDriver() {
    System.out.println("\n\n");
    System.out.println("JavaCvDriver " + UsbCameraService.getUsbCameras("JavaCvDriver"));
    System.out.println("\n\n");
  }

  @Test(timeout = 10000)
  public void checkFileTypes() {
    System.out.println("\n\n");
    for (String data : UsbCameraService.getSupportedTypes())
      System.out.println(data);
    System.out.println("\n\n");
  }

  @Test(timeout = 10000)
  public void logImage() throws IOException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    System.out.println("Name: " + service.getName());
    System.out.println("FPS: " + service.getFps());
    System.out.println("Device: " + service.getDevice());
    System.out.println("View Size: " + service.getViewSize());
    for (Dimension data : service.getSupportedSizes())
      System.out.println("possible size: " + data);
    service.writeToFile("PNG", "/home/andrea/Scrivania/prova.png");
  }

  @Test(timeout = 600000)
  public void getSwingPannelLaboratorioVlc() throws InterruptedException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    config.setVlcjName("laboratorio");
    config.setVlcjDriverPath("rtsp://admin:westing85;@192.168.0.100:554/");
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    WebcamPanel panel = new WebcamPanel(service.getWebcam());
    panel.setFPSDisplayed(true);
    panel.setDisplayDebugInfo(true);
    panel.setImageSizeDisplayed(true);
    panel.setMirrored(false);
    JFrame window = new JFrame("Laboratorio via rtsp");
    window.add(panel);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test(timeout = 600000)
  public void getSwingPannelCiospo() throws InterruptedException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    config.setVlcjDriverPath(null);
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    WebcamPanel panel = new WebcamPanel(service.getWebcam());
    panel.setFPSDisplayed(true);
    panel.setDisplayDebugInfo(true);
    panel.setImageSizeDisplayed(true);
    panel.setMirrored(false);
    JFrame window = new JFrame("Local camera");
    window.add(panel);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test(timeout = 600000)
  public void getSwingPannelEsterno() throws InterruptedException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    WebcamPanel panel = new WebcamPanel(service.getWebcam());
    panel.setFPSDisplayed(true);
    panel.setDisplayDebugInfo(true);
    panel.setImageSizeDisplayed(true);
    panel.setMirrored(false);
    JFrame window = new JFrame("Telecamera esterna");
    window.add(panel);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test(timeout = 600000)
  public void getSwingPannelLaboratorioIpCam() throws InterruptedException {
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("IpCamDriver");
    config.setWorkingSize("320x176");
    service = new UsbCameraService();
    service.setConfiguration(config);
    service.init();
    WebcamPanel panel = new WebcamPanel(service.getWebcam());
    panel.setFPSDisplayed(true);
    panel.setDisplayDebugInfo(true);
    panel.setImageSizeDisplayed(true);
    panel.setMirrored(false);
    JFrame window = new JFrame("Laboratorio via ipcam");
    window.add(panel);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void detectMotionExample(Webcam webcam) {
    detector = new WebcamMotionDetector(webcam);
    detector.setInterval(500); // one check per 500 ms
    detector.addMotionListener(this);
    detector.start();
    WebcamPanel panel = new WebcamPanel(service.getWebcam());
    panel.setFPSDisplayed(true);
    panel.setDisplayDebugInfo(true);
    panel.setImageSizeDisplayed(true);
    panel.setPainter(this);
    panel.setMirrored(false);
    painter = panel.getDefaultPainter();
    JFrame window = new JFrame("Motion Detection " + webcam.getName());
    window.add(panel);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
  }

  @Override
  public void motionDetected(WebcamMotionEvent wme) {
    for (Point p : wme.getPoints()) {
      motionPoints.put(p, 0);
    }
  }

  @Override
  public void paintPanel(WebcamPanel panel, Graphics2D g2) {
    if (painter != null) {
      painter.paintPanel(panel, g2);
    }
  }

  /**
   * Used to render the effect for the motion points.
   */
  private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
      new float[] { 1.0f }, 0.0f);

  /**
   * The amount of time each point should be rendered for before being removed.
   */
  public static final int renderTime = 12;

  /**
   * The actual size of the rendered effect for each point.
   */
  public static final int renderSize = 10;

  @Override
  public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {
    if (painter != null) {
      painter.paintImage(panel, image, g2);
    }
    // Gets all the points and updates the amount of time they have been rendered
    // for
    // And removes the ones that exceed the renderTime variable
    ArrayList<Point> rem = new ArrayList<>();
    for (Map.Entry<Point, Integer> ent : motionPoints.entrySet()) {
      Point p = ent.getKey();
      if (ent.getValue() != null) {
        int tt = ent.getValue();
        if (tt >= renderTime) {
          rem.add(ent.getKey());
        } else {
          int temp = ent.getValue() + 1;
          motionPoints.put(p, temp);
        }
      }
    }

    for (Point p : rem) {
      motionPoints.remove(p);
    }

    // Gets all the remaining points after removing the exceeded ones and then
    // renders the
    // current ones as a red square
    for (Map.Entry<Point, Integer> ent : motionPoints.entrySet()) {
      Point p = ent.getKey();
      int xx = p.x - (renderSize / 2), yy = p.y - (renderSize / 2);
      Rectangle bounds = new Rectangle(xx, yy, renderSize, renderSize);
      int dx = (int) (0.1 * bounds.width);
      int dy = (int) (0.2 * bounds.height);
      int x = bounds.x - dx;
      int y = bounds.y - dy;
      int w = bounds.width + 2 * dx;
      int h = bounds.height + dy;
      g2.setStroke(STROKE);
      if (ent.getValue() < 6)
        g2.setColor(Color.RED);
      else if (ent.getValue() < 10)
        g2.setColor(Color.ORANGE);
      else if (ent.getValue() < 12)
        g2.setColor(Color.YELLOW);
      g2.drawRect(x, y, w, h);
    }
  }
}
