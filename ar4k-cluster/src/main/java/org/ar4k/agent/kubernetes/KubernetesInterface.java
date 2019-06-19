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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.kubernetes.helm.Ar4kHelmRecipe;
import org.ar4k.agent.kubernetes.helm.HelmRecipe;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.type.filter.AnnotationTypeFilter;
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
import io.kubernetes.client.models.V1ComponentStatusList;
import io.kubernetes.client.models.V1ConfigMapList;
import io.kubernetes.client.models.V1LimitRangeList;
import io.kubernetes.client.models.V1NamespaceBuilder;
import io.kubernetes.client.models.V1NodeList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1PersistentVolumeList;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;

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

  private ApiClient client = null;
  private ProtoClient pc = null;
  private CoreV1Api coreApi = null;
  private Process miniKubeDashboard = null;

  protected Availability testApiClientNull() {
    return (pc == null && client == null) ? Availability.available()
        : Availability.unavailable(
            "the Kubernetes client is already connected to " + client != null ? client.getBasePath() : "NaN");
  }

  protected Availability testApiClientRunning() {
    return (pc != null && client != null) ? Availability.available()
        : Availability.unavailable("the Kubernetes client is not connected");
  }

  protected Availability testMiniKubeDashboardNull() {
    return (miniKubeDashboard == null || !miniKubeDashboard.isAlive()) ? Availability.available()
        : Availability
            .unavailable("the Kubernetes dashboard is already running with status " + miniKubeDashboard.isAlive());
  }

  protected Availability testMiniKubeDashboardRunning() {
    return (miniKubeDashboard != null && miniKubeDashboard.isAlive()) ? Availability.available()
        : Availability.unavailable("the Kubernetes dashboard not running");
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
  public V1NodeList listK8sNodes(@ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listNode(true, prettyPrint, null, null, null, null, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "List the components status from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ComponentStatusList listK8sComponentStatus(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listComponentStatus(null, null, true, null, null, prettyPrint, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "List the config map for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ConfigMapList listK8sConfigMapForAllNamespaces(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listConfigMapForAllNamespaces(null, null, true, null, null, prettyPrint, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "List the secret config map for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ConfigMapList listK8sSecretConfigMapForAllNamespaces(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listConfigMapForAllNamespaces(null, null, true, null, null, prettyPrint, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "List the pods for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1PodList listK8sPodsForAllNamespaces(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listPodForAllNamespaces(null, null, true, null, null, prettyPrint, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "List the services for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1ServiceList listK8sServiceForAllNamespaces(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listServiceForAllNamespaces(null, null, true, null, null, prettyPrint, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "List the limit range for all namespaces from the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1LimitRangeList listK8sLimitRangeForAllNamespaces(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listLimitRangeForAllNamespaces(null, null, true, null, null, prettyPrint, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "list the volumes on K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public V1PersistentVolumeList listK8sPersistentVolume(
      @ShellOption(help = "true/false", defaultValue = "true") String prettyPrint) {
    try {
      return coreApi.listPersistentVolume(true, prettyPrint, null, null, null, null, null, null, null);
    } catch (ApiException e) {
      logger.logException(e);
      return null;
    }
  }

  @ShellMethod(value = "Add namespace to the K8s cluster connected", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public void addK8sNameSpace(@ShellOption(help = "namespace") String namespace) {
    try {
      V1NamespaceBuilder nb = new V1NamespaceBuilder();
      V1ObjectMeta metadata = new V1ObjectMeta();
      metadata.setName(namespace);
      coreApi.createNamespace(nb.withMetadata(metadata).build(), null, "true", null);
    } catch (ApiException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Apply configuration to the cluster from a remote file", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public String applyK8sConfigFromUrl(@ShellOption(help = "url for the configuration") String urlConfig,
      @ShellOption(help = "file name for the configuration", defaultValue = "k8s_tmp_conf.yaml") String fileName) {
    try {
      HardwareHelper.downloadFileFromUrl(fileName, urlConfig);
    } catch (IOException | SecurityException | IllegalArgumentException e) {
      logger.logException(e);
    }
    return runK8sApply(fileName);
  }

  @ShellMethod(value = "Apply configuration to the cluster from a local file", group = "Kubernetes Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public String applyK8sConfigFromFile(
      @ShellOption(help = "file name for the configuration with the path") String fileName) {
    return runK8sApply(fileName);
  }

  private String runK8sApply(String fileName) {
    StringBuilder result = new StringBuilder();
    try {
      File confFile = new File(fileName);
      List<Object> targetList = Yaml.loadAll(confFile);
      for (Object obj : targetList) {
        Method m = obj.getClass().getMethod("getMetadata");
        V1ObjectMeta metadata = (V1ObjectMeta) m.invoke(obj);
        result.append(metadata.toString());
      }
    } catch (IOException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      logger.logException(e);
      result.append("ERROR: " + e.getMessage());
    }
    return result.toString();
  }

  // TODO register e list services k8s

  @ShellMethod(value = "Run Kops (K8s admin tool) (https://github.com/kubernetes/kops)", group = "Kubernetes Admin Commands")
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

  @ShellMethod(value = "Create a K8s cluster with minikube command line and login to it (https://github.com/kubernetes/minikube)", group = "Kubernetes Admin Commands")
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

  @ShellMethod(value = "Create a K8s cluster with MiniKube command line and login to it (https://github.com/kubernetes/minikube)", group = "Kubernetes Admin Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientNull")
  public String removeK8sWithMiniKube(
      @ShellOption(help = "virtual machine name for MiniKube", defaultValue = "mini-k8s") String vmName) {
    StringBuilder result = new StringBuilder();
    result.append("Deleting cluster " + vmName);
    result.append(runMiniKubeCommandLine(vmName, "delete"));
    return result.toString();
  }

  @ShellMethod(value = "Get the status of the MiniKube cluster", group = "Kubernetes Admin Commands")
  @ManagedOperation
  public String getMiniKubeStatus(
      @ShellOption(help = "virtual machine name for MiniKube", defaultValue = "mini-k8s") String vmName) {
    StringBuilder result = new StringBuilder();
    result.append(runMiniKubeCommandLine(vmName, "status"));
    return result.toString();
  }

  @ShellMethod(value = "Start the MiniKube cluster dashboard", group = "Kubernetes Admin Commands")
  @ManagedOperation
  @ShellMethodAvailability("testMiniKubeDashboardNull")
  public void getMiniKubeDashboard(
      @ShellOption(help = "virtual machine name for MiniKube", defaultValue = "mini-k8s") String vmName) {
    try {
      Runtime rt = Runtime.getRuntime();
      String[] command = { Anima.MINIKUBE_BINARY_PATH, "dashboard", "-p", vmName };
      miniKubeDashboard = rt.exec(command);
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @ShellMethod(value = "Stop the MiniKube cluster dashboard", group = "Kubernetes Admin Commands")
  @ManagedOperation
  @ShellMethodAvailability("testMiniKubeDashboardRunning")
  public void stopMiniKubeDashboard() {
    miniKubeDashboard.destroyForcibly();
    miniKubeDashboard = null;
  }

  @ShellMethod(value = "Run MiniKube (K8s admin tool) (https://github.com/kubernetes/minikube)", group = "Kubernetes Admin Commands")
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
      InputStream is = p.getInputStream();
      InputStream es = p.getErrorStream();
      Reader rin = new InputStreamReader(is, StandardCharsets.UTF_8);
      Reader rerr = new InputStreamReader(es, StandardCharsets.UTF_8);
      while (p.isAlive()) {
        if (rin.ready()) {
          char ic = (char) rin.read();
          System.out.print(ic);
        }
        if (rerr.ready()) {
          char ec = (char) rerr.read();
          System.err.print(ec);
          result.append(ec);
        }
      }
    } catch (IOException e) {
      logger.logException(e);
    }
    return result.toString();
  }

  @ShellMethod(value = "List recipe of Helm managed by the platform", group = "K8s Helm Commands")
  @ManagedOperation
  public Set<String> listK8sHelmRecipe(
      @ShellOption(help = "package for searching", defaultValue = "org.ar4k.agent.kubernetes.helm") String packageName) {
    Set<String> rit = new HashSet<>();
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(Ar4kHelmRecipe.class));
    Set<BeanDefinition> helmRecipeClasses = provider.findCandidateComponents(packageName);
    for (BeanDefinition c : helmRecipeClasses) {
      try {
        HelmRecipe o = (HelmRecipe) Class.forName(c.getBeanClassName()).newInstance();
        rit.add("\nClass: " + c.getBeanClassName() + "\n" + o.getDescription() + "\n[" + o.linkSite() + "]");
      } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
        logger.logException(e);
      }
    }
    return rit;
  }

  @ShellMethod(value = "Run Helm (K8s package manager) (https://helm.sh/)", group = "K8s Helm Commands")
  @ManagedOperation
  @ShellMethodAvailability("testApiClientRunning")
  public String runHelmCommandLine(
      @ShellOption(help = "command to execute in MiniKube", defaultValue = "help") String command) {
    StringBuilder result = new StringBuilder();
    try {
      getHelmBinary();
    } catch (JSONException | IOException e) {
      logger.logException(e);
    }
    String[] splitCmd = command.split(" |\t");
    String[] commandArgs = new String[splitCmd.length + 1];
    int counter = 0;
    commandArgs[counter] = Anima.HELM_DIRECTORY_PATH + "/helm";
    for (counter = 1; counter < splitCmd.length + 1; counter++) {
      commandArgs[counter] = splitCmd[counter - 1];
    }
    try {
      Runtime rt = Runtime.getRuntime();
      Process p = rt.exec(commandArgs);
      InputStream is = p.getInputStream();
      InputStream es = p.getErrorStream();
      Reader rin = new InputStreamReader(is, StandardCharsets.UTF_8);
      Reader rerr = new InputStreamReader(es, StandardCharsets.UTF_8);
      while (p.isAlive()) {
        if (rin.ready()) {
          char ic = (char) rin.read();
          System.out.print(ic);
        }
        if (rerr.ready()) {
          char ec = (char) rerr.read();
          System.err.print(ec);
          result.append(ec);
        }
      }
    } catch (IOException e) {
      logger.logException(e);
    }
    return result.toString();
  }

  private void getMiniKubeBinary() throws IOException {
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

  private void getHelmBinary() throws IOException {
    File helmBinaryTgz = new File(Anima.HELM_TGZ_PATH);
    if (!helmBinaryTgz.exists()) {
      logger.warn("Downloading " + Anima.HELM_COMPRESSED_URL + " PLEASE WAIT");
      HardwareHelper.downloadFileFromUrl(Anima.HELM_TGZ_PATH, Anima.HELM_COMPRESSED_URL);
      logger.warn("Download of " + Anima.HELM_COMPRESSED_URL + " completed");
    }
    File helmDirectory = new File(Anima.HELM_DIRECTORY_PATH);
    if (!helmDirectory.exists()) {
      logger.warn("Decompress " + Anima.HELM_TGZ_PATH + " to " + Anima.HELM_DIRECTORY_PATH + " PLEASE WAIT");
      HardwareHelper.extractTarGz(new FileInputStream(helmBinaryTgz));
      logger.warn("Decompress of " + Anima.HELM_TGZ_PATH + " to " + Anima.HELM_DIRECTORY_PATH + " completed");
    }
    File helmBinary = new File(Anima.HELM_DIRECTORY_PATH + "/helm");
    if (!helmBinary.canExecute()) {
      helmBinary.setExecutable(true);
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
