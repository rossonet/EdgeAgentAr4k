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
package org.ar4k.agent.docker;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.Availability;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.PruneResponse;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia gestione docker.
 */

@ShellCommandGroup("Docker Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=dockerInterface", description = "Ar4k Agent Docker Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "dockerInterface")
@RestController
@RequestMapping("/dockerInterface")
public class DockerInterface extends AbstractShellHelper {

  private static final String PORTAINER_IMAGE = "portainer/portainer:latest";

  private static final int PORTAINER_PORT = 9000;

  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Autowired
  Shell shell;

  DockerClient dockerClient = null;

  protected Availability testDockerClientNull() {
    return (dockerClient == null) ? Availability.available()
        : Availability.unavailable(
            "the Docker client is already connected: " + dockerClient != null ? dockerClient.toString() : "NaN");
  }

  protected Availability testDockerClientRunning() {
    return (dockerClient != null) ? Availability.available()
        : Availability.unavailable("the Docker client is not connected");
  }

  @ShellMethod(value = "Connect to Docker local sock", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientNull")
  public void connectToDockerDaemon() {
    DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
  }

  @ShellMethod(value = "Connect to Docker local sock", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientNull")
  public void connectToDockerDaemonWithParameters(
      @ShellOption(help = "the dockerd host for the docker connection") String dockerHost,
      @ShellOption(help = "the username for the docker connection") String registryUser,
      @ShellOption(help = "the password for the docker connection") String registryPass,
      @ShellOption(help = "the path of the certificate file for the docker connection") String dockerCertPath,
      @ShellOption(help = "verify the tls certificate of the docker server", defaultValue = "false") boolean dockerTlsVerify,
      @ShellOption(help = "the mail for the docker connection") String registryMail,
      @ShellOption(help = "the url for the docker connection") String registryUrl) {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(dockerHost)
        .withDockerTlsVerify(dockerTlsVerify).withDockerCertPath(dockerCertPath).withRegistryUsername(registryUser)
        .withRegistryPassword(registryPass).withRegistryEmail(registryMail).withRegistryUrl(registryUrl).build();
    dockerClient = DockerClientBuilder.getInstance(config).build();
  }

  @ShellMethod(value = "Disconnect from Docker system.", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void disconnectFromDocker() {
    try {
      dockerClient.close();
    } catch (IOException e) {
      logger.logException(e);
    }
    dockerClient = null;
  }

  @ShellMethod(value = "Get info from a container in Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String getDockerContainerInspect(@ShellOption(help = "the containerId") String containerId) {
    return gson.toJson(dockerClient.inspectContainerCmd(containerId).exec());
  }

  @ShellMethod(value = "Get logs from a container in Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void getDockerContainerLogsFromContainer(@ShellOption(help = "the containerId") String containerId) {
    LogContainerResultCallback callback = new LogContainerResultCallback() {
      @Override
      public void onNext(Frame i) {
        System.out.println("L: " + i);
        super.onNext(i);
      };
    };
    try {
      dockerClient.logContainerCmd(containerId).withStdOut(true).withStdErr(true).exec(callback).awaitCompletion();
    } catch (InterruptedException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Get list of containers in Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String listDockerContainers() {
    ListContainersCmd req = dockerClient.listContainersCmd();
    req.withShowAll(true);
    return gson.toJson(req.exec());
  }

  @ShellMethod(value = "Get list of containers in Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String listDockerContainerImages() {
    return gson.toJson(dockerClient.listImagesCmd().withShowAll(true).exec());
  }

  @ShellMethod(value = "Get list of networks on Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String listDockerContainerNetworks() {
    return gson.toJson(dockerClient.listNetworksCmd().exec());
  }

  @ShellMethod(value = "Get list of services on Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String listDockerContainerServices() {
    return gson.toJson(dockerClient.listServicesCmd().exec());
  }

  @ShellMethod(value = "Get list of volumes on Docker installation", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String listDockerContainerVolumes() {
    return gson.toJson(dockerClient.listVolumesCmd().exec());
  }

  @ShellMethod(value = "Load container in Docker system from the hub", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void pullContainerOnDocker(@ShellOption(help = "the name of the image") String containerImage) {
    PullImageResultCallback callback = new PullImageResultCallback() {
      @Override
      public void onNext(PullResponseItem i) {
        System.out.println("P: " + i);
        super.onNext(i);
      };
    };
    try {
      dockerClient.pullImageCmd(containerImage).exec(callback).awaitCompletion();
    } catch (InterruptedException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Start container in Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void startContainerOnDocker(@ShellOption(help = "the id of the container") String containerId) {
    dockerClient.startContainerCmd(containerId).exec();
  }

  @ShellMethod(value = "Stop container in Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void stopContainerOnDocker(@ShellOption(help = "the id of the container") String containerId) {
    dockerClient.stopContainerCmd(containerId).exec();
  }

  @ShellMethod(value = "Remove container from Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void removeContainerFromDocker(@ShellOption(help = "the id of the container") String containerId) {
    dockerClient.removeContainerCmd(containerId).exec();
  }

  @ShellMethod(value = "Remove image from Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void removeImageFromDocker(@ShellOption(help = "the id of the image") String imageId) {
    dockerClient.removeImageCmd(imageId).exec();
  }

  @ShellMethod(value = "Kill container in Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void killContainerOnDocker(@ShellOption(help = "the id of the container to kill") String containerId) {
    dockerClient.killContainerCmd(containerId).exec();
  }

  @ShellMethod(value = "Create container in Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String createContainerOnDocker(@ShellOption(help = "the id of the image") String imageId) {
    return gson.toJson(dockerClient.createContainerCmd(imageId).exec());
  }

  @ShellMethod(value = "Create container in Docker system and exec a command", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String createContainerOnDockerWithCmd(@ShellOption(help = "the id of the image") String imageId,
      @ShellOption(help = "the command") String command) {
    return gson.toJson(dockerClient.createContainerCmd(imageId).withCmd(command.split(" ")).exec());
  }

  @ShellMethod(value = "Get Info Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String getInfoFromDocker() {
    return gson.toJson(dockerClient.infoCmd().exec());
  }

  @ShellMethod(value = "Prune Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String pruneDocker() {
    List<PruneResponse> result = new ArrayList<>();
    result.add(dockerClient.pruneCmd(PruneType.BUILD).exec());
    result.add(dockerClient.pruneCmd(PruneType.CONTAINERS).exec());
    result.add(dockerClient.pruneCmd(PruneType.IMAGES).withDangling(true).exec());
    result.add(dockerClient.pruneCmd(PruneType.NETWORKS).exec());
    // result.add(dockerClient.pruneCmd(PruneType.VOLUMES).withDangling(true).exec());
    return gson.toJson(result);
  }

  @ShellMethod(value = "Load container in Docker system from the hub", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String execCommandOnDockerContainer(@ShellOption(help = "the id of the container") String containerId,
      @ShellOption(help = "the command to execute") String command) {
    return execCommand(containerId, command);
  }

  @ShellMethod(value = "Initialize Swarm sub-system on Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public void initializeSwarm(@ShellOption(help = "the cluster name") String name) {
    SwarmSpec swarmSpec = new SwarmSpec();
    swarmSpec.withName(name);
    dockerClient.initializeSwarmCmd(swarmSpec).exec();
  }

  @ShellMethod(value = "Run Portainer on Docker system", group = "Docker Commands")
  @ManagedOperation
  @ShellMethodAvailability("testDockerClientRunning")
  public String startPortainerContainer(
      @ShellOption(help = "the name for the backend's volume storage", defaultValue = "portainer_data") String portainerDataVolumeName,
      @ShellOption(help = "password for admin user in salt", defaultValue = "$2y$05$oJqTnuwufskqiAGD93h1auvKlq9TP3a5EhdJDLpV.JU2Cj7yGr4mK") String saltPaasword) {
    CreateVolumeResponse volume = dockerClient.createVolumeCmd().withName(portainerDataVolumeName).exec();
    ExposedPort port = ExposedPort.tcp(PORTAINER_PORT);
    Ports portBindings = new Ports();
    portBindings.bind(port, Binding.bindPort(PORTAINER_PORT));
    pullContainerOnDocker(PORTAINER_IMAGE);
    Volume v1 = Volume.parse("/var/run/docker.sock");
    Volume v2 = Volume.parse("/data portainer/portainer");
    Bind bind1 = new Bind("/var/run/docker.sock", v1);
    Bind bind2 = new Bind(volume.getName(), v2);
    List<Volume> volumes = new ArrayList<>();
    volumes.add(v1);
    volumes.add(v2);
    List<Bind> binds = new ArrayList<>();
    binds.add(bind1);
    binds.add(bind2);
    RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);
    CreateContainerResponse node = dockerClient.createContainerCmd(PORTAINER_IMAGE).withHostName("ar4k-portainer")
        .withTty(false).withExposedPorts(port).withVolumes(volumes)
        .withHostConfig(
            newHostConfig().withBinds(binds).withPortBindings(portBindings).withRestartPolicy(restartPolicy))
        .withCmd("--admin-password=" + saltPaasword + " --logo https://avatars3.githubusercontent.com/u/10661699")
        .exec();
    startContainerOnDocker(node.getId());
    return "VOLUME:\n" + gson.toJson(volume) + "\nCONTAINER:\n" + gson.toJson(node);
  }

  private String execCommand(String containerId, String command) {
    ExecCreateCmdResponse exec = dockerClient.execCreateCmd(containerId).withCmd(command.split(" ")).withTty(false)
        .withAttachStdin(true).withAttachStdout(true).withAttachStderr(true).exec();
    OutputStream outputStream = new ByteArrayOutputStream();
    String output = null;
    try {
      dockerClient.execStartCmd(exec.getId()).withDetach(false).withTty(true)
          .exec(new ExecStartResultCallback(outputStream, System.err)).awaitCompletion();
      output = outputStream.toString();// IOUtils.toString(outputStream, Charset.defaultCharset());
    } catch (InterruptedException e) {
      logger.logException(e);
    }
    return output;
  }

}
