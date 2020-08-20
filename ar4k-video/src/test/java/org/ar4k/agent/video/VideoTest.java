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

import org.ar4k.agent.camera.CameraShellInterface;
import org.ar4k.agent.camera.usb.StreamCameraConfig;
import org.ar4k.agent.camera.usb.StreamCameraService;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
		Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class, CameraShellInterface.class })
@TestPropertySource(locations = "classpath:application-base.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//@Ignore
public class VideoTest implements WebcamMotionListener, WebcamPanel.Painter {

	@Autowired
	Anima anima;

	public static final String[] drivers = { "JavaCvDriver", "VlcjDriver", "V4l4jDriver", "IpCamDriver" }; // "FFmpegCliDriver"

	private StreamCameraService service = null;
	WebcamMotionDetector detector = null;
	public static WebcamPanel.Painter painter = null;
	public static HashMap<Point, Integer> motionPoints = new HashMap<>();

	@After
	public void tearDown() throws Exception {
		StreamCameraService.resetDriver();
		if (service != null) {
			service.close();
			service = null;
		}
		if (detector != null) {
			detector.stop();
			detector = null;
		}
	}

	@Test(timeout = 60000)
	public void testDetectionLaboratorio() throws IOException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		config.setVlcjName("laboratorio");
		config.setVlcjDriverPath("rtsp://admin:westing85;@192.168.0.100:554/");
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		detectMotionExample(service.getWebcam());
		System.in.read(); // keep program open
	}

	@Test(timeout = 60000)
	public void testDetectionCiospo() throws IOException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		config.setVlcjDriverPath(null);
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		detectMotionExample(service.getWebcam());
		System.in.read(); // keep program open
	}

	@Test(timeout = 60000)
	public void testDetectionEsterno() throws IOException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		detectMotionExample(service.getWebcam());
		System.in.read(); // keep program open
	}

	@Test(timeout = 10000)
	public void checkCamerasIpCamDriver() {
		System.out.println("\n\n");
		System.out.println("IpCamDriver " + StreamCameraService.getUsbCameras("IpCamDriver"));
		System.out.println("\n\n");
	}

	@Test(timeout = 10000)
	public void checkCamerasV4l4jDriver() {
		System.out.println("\n\n");
		System.out.println("V4l4jDriver " + StreamCameraService.getUsbCameras("V4l4jDriver"));
		System.out.println("\n\n");
	}

	@Test(timeout = 10000)
	public void checkCamerasVlcjDriver() {
		System.out.println("\n\n");
		System.out.println("VlcjDriver " + StreamCameraService.getUsbCameras("VlcjDriver"));
		System.out.println("\n\n");
	}

	@Test(timeout = 10000)
	public void checkCamerasJavaCvDriver() {
		System.out.println("\n\n");
		System.out.println("JavaCvDriver " + StreamCameraService.getUsbCameras("JavaCvDriver"));
		System.out.println("\n\n");
	}

	@Test(timeout = 10000)
	public void checkFileTypes() {
		System.out.println("\n\n");
		for (final String data : StreamCameraService.getSupportedTypes())
			System.out.println(data);
		System.out.println("\n\n");
	}

	@Test(timeout = 10000)
	public void logImage() throws IOException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		System.out.println("Name: " + service.getName());
		System.out.println("FPS: " + service.getFps());
		System.out.println("Device: " + service.getDevice());
		System.out.println("View Size: " + service.getViewSize());
		for (final Dimension data : service.getSupportedSizes())
			System.out.println("possible size: " + data);
		service.writeToFile("PNG", "/home/andrea/Scrivania/prova.png");
	}

	@Test(timeout = 60000)
	public void getSwingPannelLaboratorioVlc() throws InterruptedException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		config.setVlcjName("laboratorio");
		config.setVlcjDriverPath("rtsp://admin:westing85;@192.168.0.100:554/");
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		final WebcamPanel panel = new WebcamPanel(service.getWebcam());
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(false);
		final JFrame window = new JFrame("Laboratorio via rtsp");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 60000)
	public void getSwingPannelCiospo() throws InterruptedException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		config.setVlcjDriverPath(null);
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		final WebcamPanel panel = new WebcamPanel(service.getWebcam());
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(false);
		final JFrame window = new JFrame("Local camera");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 60000)
	public void getSwingPannelEsterno() throws InterruptedException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("VlcjDriver");
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		final WebcamPanel panel = new WebcamPanel(service.getWebcam());
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(false);
		final JFrame window = new JFrame("Telecamera esterna");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 60000)
	public void getSwingPannelLaboratorioIpCam() throws InterruptedException {
		final StreamCameraConfig config = new StreamCameraConfig();
		config.setDriver("IpCamDriver");
		config.setWorkingSize("320x176");
		service = new StreamCameraService();
		service.setConfiguration(config);
		service.setAnima(anima);
		service.init();
		final WebcamPanel panel = new WebcamPanel(service.getWebcam());
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(false);
		final JFrame window = new JFrame("Laboratorio via ipcam");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void detectMotionExample(Webcam webcam) {
		detector = new WebcamMotionDetector(webcam);
		detector.setInterval(500); // one check per 500 ms
		detector.addMotionListener(this);
		detector.start();
		final WebcamPanel panel = new WebcamPanel(service.getWebcam());
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setPainter(this);
		panel.setMirrored(false);
		painter = panel.getDefaultPainter();
		final JFrame window = new JFrame("Motion Detection " + webcam.getName());
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	@Override
	public void motionDetected(WebcamMotionEvent wme) {
		for (final Point p : wme.getPoints()) {
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
		final ArrayList<Point> rem = new ArrayList<>();
		for (final Map.Entry<Point, Integer> ent : motionPoints.entrySet()) {
			final Point p = ent.getKey();
			if (ent.getValue() != null) {
				final int tt = ent.getValue();
				if (tt >= renderTime) {
					rem.add(ent.getKey());
				} else {
					final int temp = ent.getValue() + 1;
					motionPoints.put(p, temp);
				}
			}
		}

		for (final Point p : rem) {
			motionPoints.remove(p);
		}

		// Gets all the remaining points after removing the exceeded ones and then
		// renders the
		// current ones as a red square
		for (final Map.Entry<Point, Integer> ent : motionPoints.entrySet()) {
			final Point p = ent.getKey();
			final int xx = p.x - (renderSize / 2), yy = p.y - (renderSize / 2);
			final Rectangle bounds = new Rectangle(xx, yy, renderSize, renderSize);
			final int dx = (int) (0.1 * bounds.width);
			final int dy = (int) (0.2 * bounds.height);
			final int x = bounds.x - dx;
			final int y = bounds.y - dy;
			final int w = bounds.width + 2 * dx;
			final int h = bounds.height + dy;
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
