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
package org.ar4k.agent.kubernetes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.json.JSONException;
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

import com.google.common.io.CharStreams;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.V1ComponentStatusList;
import io.kubernetes.client.models.V1ConfigMapList;
import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.models.V1EnvVar;
import io.kubernetes.client.models.V1LimitRangeList;
import io.kubernetes.client.models.V1NodeList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1PersistentVolumeList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1PodSpec;
import io.kubernetes.client.models.V1ResourceRequirements;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.Config;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia gestione kubernetes.
 */

@ShellCommandGroup("Kubernetes Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=kubernetesInterface", description = "Ar4k Agent Kubernetes Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "kubernetesInterface")
@RestController
@RequestMapping("/kubernetesInterface")
public class KubernetesInterface extends AbstractShellHelper {
  @Autowired
  Shell shell;

  ApiClient client = null;
  ProtoClient pc = null;
  CoreV1Api coreApi = null;

  protected Availability testApiClientNull() {
    return (pc == null && client == null) ? Availability.available()
        : Availability.unavailable(
            "the Kubernetes client is already connected: " + client != null ? client.getBasePath() : "NaN");
  }

  protected Availability testApiClientRunning() {
    return (pc != null && client != null) ? Availability.available()
        : Availability.unavailable("the Kubernetes client is not connected");
  }

  @ShellMethod(value = "Connect to Kubernetes cluster. You can put the configuration in ${KUBECONFIG} or ${HOME}/.config/kube", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public void connectToK8sApi() {
    try {
      client = Config.defaultClient();
      Configuration.setDefaultApiClient(client);
      pc = new ProtoClient(client);
      coreApi = new CoreV1Api(client);
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Connect to Kubernetes cluster with username and password", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public void connectToK8sApiFromPassword(@ShellOption(help = "the username for the login") String apiUsername,
      @ShellOption(help = "the password for the login") String apiPassword,
      @ShellOption(help = "the apiKey for the login") String url) {
    client = Config.fromUserPassword(url, apiUsername, apiPassword);
    Configuration.setDefaultApiClient(client);
    pc = new ProtoClient(client);
    coreApi = new CoreV1Api(client);
  }

  @ShellMethod(value = "Connect to Kubernetes with token", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public void connectToK8sApiFromToken(@ShellOption(help = "the access token for the login") String accessToken,
      @ShellOption(help = "the apiKey for the login") String url) {
    client = Config.fromToken(url, accessToken);
    Configuration.setDefaultApiClient(client);
    pc = new ProtoClient(client);
    coreApi = new CoreV1Api(client);
  }

  @ShellMethod(value = "Connect to Kubernetes cluster from inside", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public void connectToK8sApiInsideCluster() {
    try {
      client = Config.fromCluster();
      Configuration.setDefaultApiClient(client);
      pc = new ProtoClient(client);
      coreApi = new CoreV1Api(client);
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Connect to Kubernetes cluster using config file", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public void connectToK8sApiFromFile(@ShellOption(help = "path of the config file") String k8sConfigFile) {
    try {
      client = Config.fromConfig(k8sConfigFile);
      Configuration.setDefaultApiClient(client);
      pc = new ProtoClient(client);
      coreApi = new CoreV1Api(client);
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Disconnect from Kubernetes cluster", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public void disconnectFromK8sApi() {
    coreApi = null;
    pc = null;
    client = null;
  }

  @ShellMethod(value = "list K8s nodes connected to the cluster", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1NodeList listK8sNodes() {
    try {
      return coreApi.listNode(true, "true", null, null, null, null, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the components status from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ComponentStatusList listK8sComponentStatus() {
    try {
      return coreApi.listComponentStatus(null, null, true, null, null, "true", null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the config map for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ConfigMapList listK8sConfigMapForAllNamespaces() {
    try {
      return coreApi.listConfigMapForAllNamespaces(null, null, true, null, null, "true", null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the secret config map for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ConfigMapList listK8sSecretConfigMapForAllNamespaces() {
    try {
      return coreApi.listConfigMapForAllNamespaces(null, null, true, null, null, "true", null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the pods for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1PodList listK8sPodsForAllNamespaces() {
    try {
      return coreApi.listPodForAllNamespaces(null, null, true, null, null, "true", null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the services for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ServiceList listK8sServiceForAllNamespaces() {
    try {
      return coreApi.listServiceForAllNamespaces(null, null, true, null, null, "true", null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the limit range for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1LimitRangeList listK8sLimitRangeForAllNamespaces() {
    try {
      return coreApi.listLimitRangeForAllNamespaces(null, null, true, null, null, "true", null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the volumes on K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1PersistentVolumeList listK8sPersistentVolume() {
    try {
      return coreApi.listPersistentVolume(true, "true", null, null, null, null, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  // TODO register e list services

  @ShellMethod(value = "create a new pod in the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public String createK8sNamespacedPod(String namespace, String name, String addressName, String addressValue,
      String envName, String envValue, String cpuLimit, String memoryLimit, String containerName,
      String containerImage) {
    try {
      V1ObjectMeta meta = new V1ObjectMeta();
      meta.name(name);
      V1EnvVar addr = new V1EnvVar();
      addr.name(addressName);
      addr.value(addressValue);
      V1EnvVar port = new V1EnvVar();
      addr.name(envName);
      addr.value(envValue);
      V1ResourceRequirements res = new V1ResourceRequirements();
      Map<String, Quantity> limits = new HashMap<>();
      // Quantity cpuQLimit = Quantity.fromString("300m");
      Quantity cpuQLimit = Quantity.fromString(cpuLimit);
      limits.put("cpu", cpuQLimit);
      // Quantity memoryQLimit = Quantity.fromString("500Mi");
      Quantity memoryQLimit = Quantity.fromString(memoryLimit);
      limits.put("memory", memoryQLimit);
      res.limits(limits);
      V1Container container = new V1Container();
      container.name(containerName);
      container.image(containerImage);
      container.env(Arrays.asList(addr, port));
      container.resources(res);
      V1PodSpec spec = new V1PodSpec();
      spec.containers(Arrays.asList(container));
      V1Pod podBody = new V1Pod();
      podBody.apiVersion("v1");
      podBody.kind("Pod");
      podBody.metadata(meta);
      podBody.spec(spec);
      V1Pod pod = coreApi.createNamespacedPod(namespace, podBody, true, "true", "true");
      return pod.toString();
    } catch (ApiException e) {
      logger.logException(e);
      return e.getMessage();
    }
  }

  @ShellMethod(value = "Run Kops (K8s admin tool) (https://github.com/kubernetes/kops)", group = "Kubernetes Commands")
  @ManagedOperation
  public String runKopsCommandLine(
      @ShellOption(help = "command to execute in Kops", defaultValue = "help") String command) {
    StringBuilder result = new StringBuilder();
    try {
      getKopsBinary();
    } catch (JSONException | IOException e) {
      logger.logException(e);
    }
    String[] splitCmd = command.split(" |\t");
    String[] commandArgs = new String[splitCmd.length + 1];
    int counter = 0;
    commandArgs[counter] = Anima.KOPS_BINARY_PATH;
    for (counter = 1; counter < splitCmd.length + 1; counter++) {
      commandArgs[counter] = splitCmd[counter - 1];
    }
    try {
      Runtime rt = Runtime.getRuntime();
      Process p = rt.exec(commandArgs);
      p.waitFor();
      InputStream is = p.getInputStream();
      InputStream es = p.getErrorStream();
      Reader rin = new InputStreamReader(is, StandardCharsets.UTF_8);
      Reader rerr = new InputStreamReader(es, StandardCharsets.UTF_8);
      // result.append(" -- OUTPUT -- \n");
      result.append(CharStreams.toString(rin));
      if (p.exitValue() != 0) {
        result.append("\n -- ERROR -- \n");
        result.append(CharStreams.toString(rerr));
      }
    } catch (IOException | InterruptedException e) {
      logger.logException(e);
    }
    return result.toString();
  }

  @ShellMethod(value = "Create a K8s cluster with minikube command line and login to it (https://github.com/kubernetes/minikube)", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public String createK8sWithMiniKube(
      @ShellOption(help = "virtual machine name for MiniKube", defaultValue = "mini-k8s") String vmName) {
    StringBuilder result = new StringBuilder();
    result.append("Deleting previos cluster");
    System.out.println("Deleting previos cluster");
    result.append(runMiniKubeCommandLine(vmName, "delete"));
    result.append("Starting K8s Cluster");
    System.out.println("Starting K8s Cluster");
    result.append(runMiniKubeCommandLine(vmName, "start"));
    System.out.println("Starting K8s Cluster");
    result.append(runMiniKubeCommandLine(vmName, "status"));
    connectToK8sApi();
    return result.toString();
  }

  @ShellMethod(value = "Create a K8s cluster with minikube command line and login to it (https://github.com/kubernetes/minikube)", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public String removeK8sWithMiniKube(
      @ShellOption(help = "virtual machine name for MiniKube", defaultValue = "mini-k8s") String vmName) {
    StringBuilder result = new StringBuilder();
    result.append("Deleting cluster " + vmName);
    result.append(runMiniKubeCommandLine(vmName, "delete"));
    return result.toString();
  }

  @ShellMethod(value = "Run MiniKube (K8s admin tool) (https://github.com/kubernetes/minikube)", group = "Kubernetes Commands")
  @ManagedOperation
  public String runMiniKubeCommandLine(
      @ShellOption(help = "virtual machine name for MiniKube", defaultValue = "mini-k8s") String vmName,
      @ShellOption(help = "command to execute in MiniKube", defaultValue = "help") String command) {
    StringBuilder result = new StringBuilder();
    try {
      getMiniKubeBinary();
    } catch (JSONException | IOException e) {
      logger.logException(e);
    }
    String[] splitCmd = (command + " -p " + vmName).split(" |\t");
    String[] commandArgs = new String[splitCmd.length + 1];
    int counter = 0;
    commandArgs[counter] = Anima.MINIKUBE_BINARY_PATH;
    for (counter = 1; counter < splitCmd.length + 1; counter++) {
      commandArgs[counter] = splitCmd[counter - 1];
    }
    try {
      Runtime rt = Runtime.getRuntime();
      Process p = rt.exec(commandArgs);
      p.waitFor();
      InputStream is = p.getInputStream();
      InputStream es = p.getErrorStream();
      Reader rin = new InputStreamReader(is, StandardCharsets.UTF_8);
      Reader rerr = new InputStreamReader(es, StandardCharsets.UTF_8);
      // result.append(" -- OUTPUT -- \n");
      result.append(CharStreams.toString(rin));
      if (p.exitValue() != 0) {
        result.append("\n -- ERROR -- \n");
        result.append(CharStreams.toString(rerr));
      }
    } catch (IOException | InterruptedException e) {
      logger.logException(e);
    }
    return result.toString();
  }

  private void getMiniKubeBinary() throws JSONException, IOException {
    File miniBinary = new File(Anima.MINIKUBE_BINARY_PATH);
    if (!miniBinary.exists()) {
      logger.warn("Downloading " + Anima.MINIKUBE_URL + " PLEASE WAIT");
      HardwareHelper.downloadFileFromUrl(Anima.MINIKUBE_BINARY_PATH, Anima.MINIKUBE_URL);
      logger.warn("Download of " + Anima.MINIKUBE_URL + " completed");
    }
    if (!miniBinary.canExecute()) {
      miniBinary.setExecutable(true);
    }
  }

  private void getKopsBinary() throws JSONException, IOException {
    File kopsBinary = new File(Anima.KOPS_BINARY_PATH);
    if (!kopsBinary.exists()) {
      String latestKopsVersion = HardwareHelper.readJsonFromUrl(Anima.LATEST_KOPS_URL).optString("tag_name");
      String kopsUrl = Anima.KOPS_URL.replace("$version", latestKopsVersion);
      logger.warn("Downloading " + kopsUrl + " PLEASE WAIT");
      HardwareHelper.downloadFileFromUrl(Anima.KOPS_BINARY_PATH, kopsUrl);
      logger.warn("Download of " + kopsUrl + " completed");
    }
    if (!kopsBinary.canExecute()) {
      kopsBinary.setExecutable(true);
    }
  }

}
