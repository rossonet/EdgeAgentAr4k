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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.validation.Valid;

import org.ar4k.agent.camera.usb.StreamCameraConfig;
import org.ar4k.agent.camera.usb.StreamCameraService;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.VideoMessage;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
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
@RestController
@RequestMapping("/cameraInterface")
public class CameraShellInterface extends AbstractShellHelper {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(StreamCameraService.class);

	private StreamCameraService camera = null;
	private VideoInterface videoInterface = null;

	protected Availability testUsbCameraServiceNull() {
		return camera == null ? Availability.available()
				: Availability.unavailable("a camera exists with status " + camera);
	}

	protected Availability testUsbCameraServiceRunning() {
		return camera != null ? Availability.available() : Availability.unavailable("no camera service are running");
	}

	@ShellMethod(value = "List camera attached to this host", group = "Camera Commands")
	@ManagedOperation
	public String listUsbCameras(
			@ShellOption(help = "driver type", valueProvider = CameraDriverValuesProvider.class) String driver) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(StreamCameraService.getUsbCameras(driver));
	}

	@ShellMethod(value = "Add a usb camera service to the selected configuration", group = "Camera Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addUsbCameraService(@ShellOption(optOut = true) @Valid StreamCameraConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Create a usb camera service", group = "Camera Commands")
	@ManagedOperation
	@ShellMethodAvailability("testUsbCameraServiceNull")
	public void createUsbCameraService(@ShellOption(optOut = true) @Valid StreamCameraConfig configuration) {
		logger.info("Camera driver " + configuration.getDriver());
		camera = new StreamCameraService();
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

	@ShellMethod(value = "Subscribe video and view the output in a X window", group = "Camera Commands")
	@ManagedOperation
	public void subscribeVideoChannel(@ShellOption(help = "channel id (nodeId)") String channelId) {
		if (videoInterface == null) {
			videoInterface = new VideoInterface();
		}
		if (homunculus.getDataAddress().getChannel(channelId).getChannelClass()
				.equals(IPublishSubscribeChannel.class)) {
			((IPublishSubscribeChannel) homunculus.getDataAddress().getChannel(channelId)).subscribe(videoInterface);
		} else
			logger.error(channelId + " is not subscribable");
	}

	private class VideoInterface implements MessageHandler {

		JFrame window = null;
		ImagePanel panel = null;
		AtomicBoolean started = new AtomicBoolean(false);

		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			if (started.get() == false) {
				started.set(true);
				if (panel == null) {
					panel = new ImagePanel();
				}
				if (window == null) {
					String videoQueueName = "Video queue " + message.getHeaders().get("source").toString();
					logger.info("open window " + videoQueueName);
					window = new JFrame(videoQueueName);
					final Double width = Double.valueOf(message.getHeaders().get("size-width").toString());
					final Double height = Double.valueOf(message.getHeaders().get("size-height").toString());
					int roundWidth = (int) Math.round(width);
					int roundHeight = (int) Math.round(height);
					logger.info("size: " + roundWidth + "x" + roundHeight);
					window.add(panel);
					window.setResizable(true);
					window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					window.pack();
					window.setSize(roundWidth, roundHeight);
					window.setVisible(true);
				}
			}
			panel.setImage(((VideoMessage) message).getPayload());
		}

		public class ImagePanel extends JPanel {

			private static final long serialVersionUID = 4289146356652598217L;
			private BufferedImage image = null;

			public void setImage(BufferedImage image) {
				this.image = image;
				super.repaint();
			}

			@Override
			protected void paintComponent(Graphics g) {
				if (image != null) {
					super.paintComponent(g);
					g.drawImage(image, 0, 0, this);
				}
			}
		}
	}
}
