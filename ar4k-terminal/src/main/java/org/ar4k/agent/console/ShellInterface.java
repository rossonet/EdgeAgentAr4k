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
package org.ar4k.agent.console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.ReflectionException;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.core.ServiceComponent;
import org.ar4k.agent.core.valueProvider.Ar4kEventsValuesProvider;
import org.ar4k.agent.core.valueProvider.LogLevelValuesProvider;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.helper.ReflectionUtils;
import org.ar4k.agent.helper.UserSpaceByteSystemCommandHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.rpc.process.AgentProcess;
import org.ar4k.agent.rpc.process.ScriptEngineManagerProcess;
import org.ar4k.agent.rpc.process.xpra.XpraSessionProcess;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.bouncycastle.cms.CMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;

/**
 * Interfaccia da linea di comando principale.
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("Configure Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=mainInterface", description = "Ar4k Agent Main Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "mainInterface")
@RestController
@RequestMapping("/anima")
public class ShellInterface extends AbstractShellHelper {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @ShellMethod(value = "Login in the agent", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionFalse")
  public boolean login(@ShellOption(help = "username", defaultValue = "admin") String username,
      @ShellOption(help = "password") String password) {
    boolean result = false;
    anima.loginAgent(username, password, null);
    if (getSessionId() != null)
      result = true;
    return result;
  }

  // TODO implementare la gestione dell'Address Space

  @ShellMethod(value = "List sessions attached to the user", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public Collection<String> listSessions() {
    List<String> sessions = new ArrayList<>();
    for (SessionInformation s : sessionRegistry.getAllSessions(SecurityContextHolder.getContext().getAuthentication(),
        false)) {
      sessions.add(((UsernamePasswordAuthenticationToken) s.getPrincipal()).getPrincipal() + ": " + s.getSessionId()
          + " [last used: " + s.getLastRequest() + "]");
    }
    return sessions;
  }

  @ShellMethod(value = "Create user", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public boolean createUserAccount(@ShellOption(help = "username") String username,
      @ShellOption(help = "password") String password,
      @ShellOption(help = "authorities for the account separated by comma, must starts with ROLE_ For example: ROLE_USER,ROLE_ADMIN", defaultValue = "ROLE_USER") String authorities) {
    return addUser(username, password, authorities, passwordEncoder);
  }

  @ShellMethod(value = "Get local users list", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public Collection<Ar4kUserDetails> getUsersList() {
    return anima.getLocalUsers();
  }

  @ShellMethod(value = "Drop user from local users list", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public boolean deleteUserAccount(@ShellOption(help = "username") String username) {
    return removeUser(username);
  }

  @ShellMethod(value = "Get all roles configured in the users list", group = "Authentication Commands")
  @ManagedOperation
  public Collection<GrantedAuthority> getRolesAuthority() {
    Set<GrantedAuthority> roles = new HashSet<>();
    for (Ar4kUserDetails u : anima.getLocalUsers()) {
      for (GrantedAuthority a : u.getAuthorities()) {
        roles.add(a);
      }
    }
    return roles;
  }

  @ShellMethod(value = "Get the unique name for the agent", group = "Monitoring Commands")
  @ManagedOperation
  public String getUniqueName() {
    return anima.getAgentUniqueName();
  }

  @ShellMethod(value = "Logout from the agent", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public boolean logout() {
    anima.logoutFromAgent();
    return true;
  }

  @ShellMethod(value = "Logout from the agent and delete the session", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public boolean closeSessionAndLogout() {
    anima.terminateSession(getSessionId());
    return true;
  }

  @ShellMethod(value = "Get your info", group = "Authentication Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public Authentication me() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @ShellMethod(value = "Test method. Just return the string parameter", group = "Testing Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOk")
  @GetMapping("test")
  public String test(@Size(min = 1, max = 40) @ShellOption(help = "example string for test method") String testString)
      throws InterruptedException {
    return runShellTest(testString);
  }

  @ShellMethod("View the selected configuration in base64 text")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigBase64() throws IOException {
    return ConfigHelper.toBase64(getWorkingConfig());
  }

  @ShellMethod("View base64 config text prepared for dns")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigForDns(@ShellOption(help = "the hostname for this configuration") String name)
      throws IOException {
    return ConfigHelper.toBase64ForDns(name, getWorkingConfig());
  }

  @ShellMethod("View base64 config text prepared for dns encrypted")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigForDnsEncrypted(@ShellOption(help = "the hostname for this configuration") String name,
      @ShellOption(help = "the alias in ca for the certificate") String certificateAlias)
      throws IOException, CertificateEncodingException, CMSException {
    return ConfigHelper.toBase64ForDnsCrypto(name, getWorkingConfig(), certificateAlias);
  }

  @ShellMethod("Save selected configuration in base64 text file")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String saveSelectedConfigBase64(
      @ShellOption(help = "file for saving the configuration. The system will add .conf.base64.ar4k to the string") String filename)
      throws IOException {
    Files.write(Paths.get(filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.base64.ar4k"),
        getSelectedConfigBase64().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return "saved";
  }

  @ShellMethod("Import configuration from base64 text to selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void importSelectedConfigBase64(
      @ShellOption(help = "configuration exported by export-selected-config-base64") String base64Config)
      throws IOException, ClassNotFoundException {
    setWorkingConfig((Ar4kConfig) ConfigHelper.fromBase64(base64Config));
  }

  @ShellMethod("Load selected configuration from a base64 text file")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void loadSelectedConfigBase64(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.base64.ar4k to the string") String filename)
      throws IOException, ClassNotFoundException {
    String config = readFromFile(filename, ".conf.base64.ar4k");
    importSelectedConfigBase64(config);
  }

  @ShellMethod("View the selected configuration in base64 text crypted")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigBase64Crypto(@ShellOption(help = "keystore alias for the key") String alias)
      throws CertificateEncodingException, UnsupportedEncodingException, CMSException, IOException {
    return ConfigHelper.toBase64Crypto(getWorkingConfig(), alias);
  }

  @ShellMethod("Save selected configuration in base64 text file crypted")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String saveSelectedConfigBase64Crypto(
      @ShellOption(help = "file for saving the configuration. The system will add .conf.base64.crypto.ar4k to the string") String filename,
      @ShellOption(help = "keystore alias for the key") String alias)
      throws CertificateEncodingException, UnsupportedEncodingException, IOException, CMSException {
    Files.write(Paths.get(filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.base64.crypto.ar4k"),
        getSelectedConfigBase64Crypto(alias).getBytes(), StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
    return "saved";
  }

  @ShellMethod("Import the selected configuration from base64 text crypted")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void importSelectedConfigBase64Crypted(
      @ShellOption(help = "configuration exported by export-selected-config-base64-crypted") String base64ConfigCrypto,
      @ShellOption(help = "alias key in Anima repository") String aliasKey)
      throws ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, CMSException {
    setWorkingConfig((Ar4kConfig) ConfigHelper.fromBase64Crypto(base64ConfigCrypto, aliasKey));
  }

  @ShellMethod("Load selected configuration from a base64 text file crypted")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void loadSelectedConfigBase64Crypted(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.base64.crypto.ar4k to the string") String filename,
      @ShellOption(help = "alias key in Anima repository") String aliasKey) throws FileNotFoundException, IOException,
      ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, CMSException {
    String config = readFromFile(filename, ".conf.base64.crypto.ar4k");
    importSelectedConfigBase64Crypted(config, aliasKey);
  }

  @ShellMethod("View the selected configuration in json text")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigJson() {
    return ConfigHelper.toJson(getWorkingConfig());
  }

  @ShellMethod("View the selected configuration in Yaml text")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigYaml() {
    return ConfigHelper.toYaml(getWorkingConfig());
  }

  @ShellMethod("View the runtime configuration in json text")
  @ManagedOperation
  @ShellMethodAvailability("testRuntimeConfigOk")
  public String getRuntimeConfigJson() {
    return ConfigHelper.toJson(anima.getRuntimeConfig());
  }

  @ShellMethod("View the runtime configuration in Yaml text")
  @ManagedOperation
  @ShellMethodAvailability("testRuntimeConfigOk")
  public String getRuntimeConfigYaml() {
    return ConfigHelper.toYaml(anima.getRuntimeConfig());
  }

  @ShellMethod("Save selected configuration in json text file")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String saveSelectedConfigJson(
      @ShellOption(help = "file for saving the configuration. The system will add .conf.json.ar4k to the string") String filename)
      throws IOException {
    Files.write(Paths.get(filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.json.ar4k"),
        getSelectedConfigJson().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return "saved";
  }

  @ShellMethod("Save selected configuration in Yaml text file")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String saveSelectedConfigYaml(
      @ShellOption(help = "file for saving the configuration. The system will add .conf.yaml.ar4k to the string") String filename)
      throws IOException {
    Files.write(Paths.get(filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.yaml.ar4k"),
        getSelectedConfigYaml().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return "saved";
  }

  @ShellMethod("Import the selected configuration from json text")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void importSelectedConfigJson(
      @ShellOption(help = "configuration exported by export-selected-config-json") String jsonConfig) {
    setWorkingConfig((Ar4kConfig) ConfigHelper.fromJson(jsonConfig, Ar4kConfig.class));
  }

  @ShellMethod("Import the selected configuration from yaml text")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void importSelectedConfigYaml(
      @ShellOption(help = "configuration exported by export-selected-config-yaml") String yamlConfig) {
    setWorkingConfig(ConfigHelper.fromYaml(yamlConfig));
  }

  @ShellMethod("Load selected configuration from a json text file")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void loadSelectedConfigJson(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.json.ar4k to the string") String filename)
      throws IOException, ClassNotFoundException {
    String config = readFromFile(filename, ".conf.json.ar4k");
    importSelectedConfigJson(config);
  }

  @ShellMethod("Load selected configuration from a yaml text file")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void loadSelectedConfigYaml(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.yaml.ar4k to the string") String filename)
      throws IOException, ClassNotFoundException {
    String config = readFromFile(filename, ".conf.yaml.ar4k");
    importSelectedConfigYaml(config);
  }

  @ShellMethod("Create new configuration as selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void createSelectedConfig(@ShellOption() @Valid Ar4kConfig confCreated) {
    setWorkingConfig(confCreated);
  }

  @ShellMethod("Modify selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void modifySelectedConfig() {
    // TODO: implementare
    throw new UnsupportedOperationException();
  }

  @ShellMethod("Remove a service from selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public void removeServiceSelectedConfig(@ShellOption(help = "service name to delete") String serviceName) {
    for (ConfigSeed a : getWorkingConfig().pots) {
      try {
        if (a.getName() != null && a.getName().equals(serviceName)) {
          getWorkingConfig().pots.remove(a);
        }
      } catch (Exception ee) {
        logger.logException(ee);
      }
    }
  }

  @ShellMethod("List configs in runtime session")
  @ManagedOperation
  @ShellMethodAvailability("testListConfigOk")
  public String listConfig() {
    String risposta = "";
    for (Entry<String, Ar4kConfig> configurazione : getConfigs().entrySet()) {
      risposta = risposta + AnsiOutput.toString(AnsiColor.GREEN, configurazione.getValue().uniqueId.toString(),
          AnsiColor.DEFAULT, " - ", configurazione.getValue().name, " [", configurazione.getValue().promptColor,
          configurazione.getValue().prompt, AnsiColor.DEFAULT, "]\n");
    }
    return risposta;
  }

  @ShellMethod(value = "List services in selected config")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String listServicesSelectedConfig() {
    String risposta = "";
    for (ConfigSeed a : getWorkingConfig().pots) {
      try {
        if (a.getName() != null) {
          risposta = risposta + AnsiOutput.toString(AnsiColor.GREEN, a.getName(), AnsiColor.DEFAULT, " - ",
              a.getClass().getName(), "\n");
        }
      } catch (Exception ee) {
        logger.logException(ee);
      }
    }
    return risposta;
  }

  @ShellMethod("Select a config in the runtime list")
  @ManagedOperation
  @ShellMethodAvailability("testListConfigOk")
  public void selectConfig(@ShellOption(help = "the id of the config to select") String idConfig) {
    Ar4kConfig target = null;
    for (Entry<String, Ar4kConfig> configurazione : getConfigs().entrySet()) {
      if (configurazione.getValue().uniqueId.toString().equals(idConfig)) {
        target = configurazione.getValue();
        break;
      }
    }
    setWorkingConfig(target);
  }

  @ShellMethod("Clone a config in the runtime list with a new id, name and prompt")
  @ManagedOperation
  @ShellMethodAvailability("testRuntimeConfigOk")
  public String cloneConfig(@ShellOption(help = "the id of the config to clone from") String idConfig,
      @ShellOption(help = "the name of the new config") String newName,
      @ShellOption(help = "the promp for the new config") String newPrompt) throws IOException, ClassNotFoundException {
    Ar4kConfig target = null;
    for (Entry<String, Ar4kConfig> configurazione : getConfigs().entrySet()) {
      if (configurazione.getValue().uniqueId.toString().equals(idConfig)) {
        target = configurazione.getValue();
        break;
      }
    }
    Ar4kConfig newTarget = cloneConfigHelper(newName, newPrompt, target);
    getConfigs().put(newTarget.getName(), newTarget);
    return "cloned";
  }

  @ShellMethod("Unset the selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String unsetSelectedConfig() {
    setWorkingConfig(null);
    return "unseted";
  }

  @ShellMethod(value = "Set selected config as target config for the agent", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String setSelectedConfigAsRuntime() {
    anima.setTargetConfig(getWorkingConfig());
    return "set";
  }

  @ShellMethod(value = "View the actual status", group = "Agent Life Cycle Commands")
  @ManagedOperation
  public String getAgentStatus() {
    return anima.getState().name();
  }

  @ShellMethod(value = "View the Anima Bean", group = "Agent Life Cycle Commands")
  @ManagedOperation
  public Anima getAnima() {
    return anima;
  }

  @ShellMethod(value = "Set a event to the agent", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public void setAgentStatus(
      @ShellOption(help = "target status", valueProvider = Ar4kEventsValuesProvider.class) AnimaEvents target) {
    anima.sendEvent(target);
  }

  @ShellMethod(value = "Shutdown agent", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public void goodbye() {
    setAgentStatus(AnimaEvents.STOP);
    System.exit(0);
  }

  @ShellMethod(value = "Pause agent", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public void pause() {
    setAgentStatus(AnimaEvents.PAUSE);
  }

  @ShellMethod(value = "Restart agent", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public void restart() {
    setAgentStatus(AnimaEvents.RESTART);
  }

  @ShellMethod(value = "Reload agent", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public void completeReload() {
    setAgentStatus(AnimaEvents.COMPLETE_RELOAD);
  }

  @ShellMethod(value = "List runtime services", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("testIsRunningOk")
  public String listService() {
    String risposta = "";
    for (ServiceComponent servizio : anima.getServicesOnly()) {
      risposta = risposta + AnsiOutput.toString(AnsiColor.GREEN, servizio.getConfiguration().getUniqueId().toString(),
          AnsiColor.DEFAULT, " - ", servizio.getConfiguration().getName(), " [", AnsiColor.RED,
          servizio.getStatusString(), AnsiColor.DEFAULT, "]\n");
    }
    return risposta;
  }

  @ShellMethod(value = "Clone runtime config in the runtime list with a new id, name and prompt", group = "Agent Life Cycle Commands")
  @ManagedOperation
  @ShellMethodAvailability("testRuntimeConfigOk")
  public void cloneRuntimeConfig(@ShellOption(help = "the name of the new config") String newName,
      @ShellOption(help = "the promp for the new config") String newPrompt) throws IOException, ClassNotFoundException {
    Ar4kConfig target = anima.getRuntimeConfig();
    Ar4kConfig newTarget = cloneConfigHelper(newName, newPrompt, target);
    addConfig(newTarget);
  }

  @ShellMethod(value = "Set the log filter for the console", group = "Monitoring Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOkOrStatusInit")
  public String setLogLevel(
      @ShellOption(help = "the new log level to set", defaultValue = "INFO", valueProvider = LogLevelValuesProvider.class) String newLogLevel) {
    Ar4kLogger.level = Ar4kLogger.LogLevel.valueOf(newLogLevel);
    changeLogLevel(Ar4kLogger.level.name());
    return Ar4kLogger.level.name();
  }

  @ShellMethod(value = "List JMX endpoints", group = "Monitoring Commands")
  @ManagedOperation
  public Collection<String> listJmxEndpoints()
      throws IntrospectionException, InstanceNotFoundException, ReflectionException {
    return listMbeans();
  }

  @ShellMethod(value = "Get the log filter for the console", group = "Monitoring Commands")
  @ManagedOperation
  public String getLogLevel() {
    return Ar4kLogger.level.name();
  }

  @ShellMethod(value = "Send message to log as a INFO", group = "Monitoring Commands")
  @ManagedOperation
  public void sendLogMessageInfo(@ShellOption(help = "message to write in the logger") String message) {
    logger.info(message);
  }

  @ShellMethod(value = "Send message to log as a ERROR", group = "Monitoring Commands")
  @ManagedOperation
  public void sendLogMessageError(@ShellOption(help = "message to write in the logger") String message) {
    logger.error(message);
  }

  private void changeLogLevel(String gwlog) {
    for (ch.qos.logback.classic.Logger l : findAllLogger()) {
      l.setLevel(ch.qos.logback.classic.Level.toLevel(gwlog, ch.qos.logback.classic.Level.INFO));
    }
  }

  private LoggerContext getLoggerContext() {
    return ContextSelectorStaticBinder.getSingleton().getContextSelector().getLoggerContext();
  }

  public Collection<ch.qos.logback.classic.Logger> findAllLogger() {
    return getLoggerContext().getLoggerList();
  }

  @ShellMethod(value = "Get info about the JVM and hardware", group = "Monitoring Commands")
  @ManagedOperation
  public String getHardwareInfo() throws IOException, InterruptedException, ParseException {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      return gson.toJson(HardwareHelper.getSystemInfo());
    } catch (Exception ae) {
      logger.logException(ae);
      return null;
    }
  }

  private String printBeans() {
    StringBuilder sb = new StringBuilder();
    for (String s : applicationContext.getBeanDefinitionNames()) {
      sb.append(s + "\n");
    }
    return sb.toString();
  }

  @ShellMethod(value = "Get info about Beans on the system", group = "Monitoring Commands")
  @ManagedOperation
  public String getBeansInfo() throws IOException, InterruptedException, ParseException {
    return printBeans();
  }

  @ShellMethod(value = "Get info about threads on the system", group = "Monitoring Commands")
  @ManagedOperation
  public String getThreadsInfo() {
    return ReflectionUtils.logThreadInfo();
  }

  @ShellMethod(value = "Get variable from Spring Framework", group = "Monitoring Commands")
  @ManagedOperation
  public void getEnvironmentVariables() {
    System.out.println(anima.getEnvironmentVariablesAsString());
  }

  @ShellMethod(value = "Run Xpra server on the enviroment in where the agent is running", group = "Remote Management Commands")
  @ManagedOperation
  @ShellMethodAvailability("isUnixAndSessionOk")
  public boolean runXpraServer(@ShellOption(help = "label to identify the xpra server") String executorLabel,
      @ShellOption(help = "the tcp port for the HTML5 console", defaultValue = "0") int port,
      @ShellOption(help = "the command to start in the X server", defaultValue = "xterm") String cmd) {
    XpraSessionProcess p = new XpraSessionProcess();
    p.setLabel(executorLabel);
    p.setTcpPort(port);
    p.setCommand(cmd);
    p.eval(cmd);
    ((RpcConversation) anima.getRpc(getSessionId())).getScriptSessions().put(executorLabel, p);
    return true;
  }

  @ShellMethod(value = "Run a text script in JSR 223 engine", group = "Remote Management Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public boolean runJsr223Script(@ShellOption(help = "label to identify the script") String executorLabel,
      @ShellOption(help = "script engine. you can find the list with list-jsr223-script-engines-in-runtime") String scriptEngine,
      @ShellOption(help = "the script for the engine language") String script) {
    ScriptEngineManagerProcess p = new ScriptEngineManagerProcess();
    p.setLabel(executorLabel);
    p.setEngine(scriptEngine);
    p.eval(script);
    ((RpcConversation) anima.getRpc(getSessionId())).getScriptSessions().put(executorLabel, p);
    return true;
  }

  @ShellMethod(value = "List runtime engine JSR 223 on the system", group = "Remote Management Commands")
  @ManagedOperation
  public List<Map<String, Object>> listJsr223ScriptEnginesInRuntime() {
    return ScriptEngineManagerProcess.listScriptEngines();
  }

  @ShellMethod(value = "List active processes", group = "Remote Management Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public Map<String, AgentProcess> listProcesses() {
    return ((RpcConversation) anima.getRpc(getSessionId())).getScriptSessions();
  }

  @ShellMethod(value = "List active Xpra endpoint ipv4", group = "Remote Management Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public List<String> listXpraServers() {
    List<String> result = new ArrayList<>();
    try {
      for (Entry<String, AgentProcess> d : listProcesses().entrySet()) {
        if (d.getValue().isAlive() && d.getValue() instanceof XpraSessionProcess) {
          XpraSessionProcess dataXpra = (XpraSessionProcess) d.getValue();
          if (dataXpra.getTcpPort() != 0) {
            for (NetworkInterface n : Collections.list(NetworkInterface.getNetworkInterfaces())) {
              for (InetAddress i : Collections.list(n.getInetAddresses())) {
                if (i instanceof Inet4Address) {
                  result.add(
                      d.getKey() + " => http://" + i.getHostAddress() + ":" + String.valueOf(dataXpra.getTcpPort()));
                }
              }
            }
          }
        }
      }
    } catch (SocketException e) {
      logger.logException(e);
    }
    return result;
  }

  @ShellMethod(value = "Kill running process", group = "Remote Management Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  public boolean killProcess(@ShellOption(help = "label to identify the process to kill") String label) {
    if (listProcesses().get(label) != null) {
      try {
        listProcesses().get(label).close();
      } catch (Exception e) {
        logger.logException(e);
      }
      listProcesses().remove(label);
    }
    return true;
  }

  @ShellMethod(value = "Run shell command on the enviroment in where the agent is running. The default code to terminate the session is CTRL-E exit, you can change it", group = "Remote Management Commands")
  @ManagedOperation
  @ShellMethodAvailability("sessionOk")
  // TODO Migliorare l'interazione della command line bash
  public String runCommandLine(
      @ShellOption(help = "the command to start in the shell", defaultValue = ConfigHelper.BASE_BASH_CMD) String shellCommand,
      @ShellOption(help = "the int number of the end character. 5 is Ctrl+E", defaultValue = "5") String endCharacter) {
    return UserSpaceByteSystemCommandHelper.runShellCommandLineByteToByte(shellCommand, endCharacter, logger, System.in,
        System.out);
  }

  public void setAnima(Anima anima) {
    this.anima = anima;
  }
}
