/*
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
package org.ar4k.tests;

import java.io.IOException;
import java.text.ParseException;

import org.ar4k.agent.camera.CameraShellInterface;
import org.ar4k.agent.camera.usb.UsbCameraConfig;
import org.ar4k.agent.config.AnimaStateMachineConfig;
import org.ar4k.agent.console.DataShellInterface;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.jline.builtins.Commands;
import org.jline.reader.LineReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@Configuration
@Import({
    // Core runtime
    SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class,
    // Various Resolvers
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class,
    // Built-In Commands
    StandardCommandsAutoConfiguration.class,
    // Sample Commands
    Commands.class, FileValueProvider.class, CameraShellInterface.class, AnimaStateMachineConfig.class,
    AnimaHomunculus.class, Ar4kuserDetailsService.class, Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class,
    Anima.class, DataShellInterface.class })
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class VideoStreamTests {

  public BeanFactory context = null;

  Anima anima = null;

  Shell shell = null;

  @Bean
  @Autowired
  public InputProvider inputProvider(LineReader lineReader, PromptProvider promptProvider) {
    return new InteractiveShellApplicationRunner.JLineInputProvider(lineReader, promptProvider);
  }

  @Before
  public void setUp() throws Exception {
    anima = new Anima();
    System.out.println("\n\n" + anima.toString() + "\n\n");
    KeystoreConfig ks = new KeystoreConfig();
    ks.create(anima.getAgentUniqueName(), ConfigHelper.organization, ConfigHelper.unit, ConfigHelper.locality,
        ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri, ConfigHelper.dns, ConfigHelper.ip,
        anima.getMyAliasCertInKeystore(), true);
    anima.setMyIdentityKeystore(ks);
    context = new AnnotationConfigApplicationContext(this.getClass());
    shell = context.getBean(Shell.class);
  }

  @After
  public void tearDown() throws Exception {
    anima.close();
    anima = null;
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void checkVideoInterfaceLaboratorio() throws InterruptedException, IOException, ParseException {
    Thread.sleep(2000L);
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    config.setVlcjName("laboratorio");
    config.setVlcjDriverPath("rtsp://admin:westing85;@192.168.0.100:554/");
    CameraShellInterface interfaceVideo = context.getBean(CameraShellInterface.class);
    interfaceVideo.createUsbCameraService(config);
    Thread.sleep(6000L);
    DataShellInterface datashellInteface = context.getBean(DataShellInterface.class);
    System.out.println("channels: " + datashellInteface.listDataChannels());
    interfaceVideo.subscribeVideoChannel("image-global");
    Thread.sleep(2 * 60000L);
  }

  @Test
  public void checkVideoInterfaceEsterno() throws InterruptedException, IOException, ParseException {
    Thread.sleep(2000L);
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    CameraShellInterface interfaceVideo = context.getBean(CameraShellInterface.class);
    interfaceVideo.createUsbCameraService(config);
    Thread.sleep(6000L);
    DataShellInterface datashellInteface = context.getBean(DataShellInterface.class);
    System.out.println("channels: " + datashellInteface.listDataChannels());
    interfaceVideo.subscribeVideoChannel("image-global");
    Thread.sleep(2 * 60000L);
  }

  @Test
  public void checkVideoInterfaceLocale() throws InterruptedException, IOException, ParseException {
    Thread.sleep(2000L);
    UsbCameraConfig config = new UsbCameraConfig();
    config.setDriver("VlcjDriver");
    config.setVlcjDriverPath(null);
    CameraShellInterface interfaceVideo = context.getBean(CameraShellInterface.class);
    interfaceVideo.createUsbCameraService(config);
    Thread.sleep(6000L);
    DataShellInterface datashellInteface = context.getBean(DataShellInterface.class);
    System.out.println("channels: " + datashellInteface.listDataChannels());
    interfaceVideo.subscribeVideoChannel("image-global");
    Thread.sleep(2 * 60000L);
  }

}
