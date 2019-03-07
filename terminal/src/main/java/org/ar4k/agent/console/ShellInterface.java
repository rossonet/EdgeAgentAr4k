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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.tunnel.TunnelConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.Ar4kException;
import org.ar4k.agent.core.Ar4kLogger;
import org.ar4k.agent.core.Ar4kService;
import org.ar4k.agent.core.TunnelComponent;
import org.ar4k.agent.core.valueProvider.Ar4kEventsValuesProvider;
import org.ar4k.agent.core.valueProvider.LogLevelValuesProvider;
import org.ar4k.agent.helper.Utils;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.Availability;
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
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=mainInterface", description = "Ar4k Agent Main Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "mainInterface")
@RestController
@RequestMapping("/anima")
public class ShellInterface {

  private static final Logger logger = LoggerFactory.getLogger(ShellInterface.class);

  @Value("${ar4k.test}")
  boolean flagTestOk;

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  Anima anima;

  private static final Long load = 1500L;

  @Override
  protected void finalize() {
  }

  @SuppressWarnings("unused")
  private Availability testOk() {
    return flagTestOk ? Availability.available()
        : Availability.unavailable("test command not available in this version");
  }

  @SuppressWarnings("unused")
  private Availability testSelectedConfigOk() {
    return anima.getWorkingConfig() != null ? Availability.available()
        : Availability.unavailable("you have to select a config before");
  }

  @SuppressWarnings("unused")
  private Availability testRuntimeConfigOk() {
    return anima.getRuntimeConfig() != null ? Availability.available()
        : Availability.unavailable("you have to configure a runtime config before");
  }

  @SuppressWarnings("unused")
  private Availability testListConfigOk() {
    return anima.getConfigs().size() > 0 ? Availability.available()
        : Availability.unavailable("there are no configs in memory");
  }

  @SuppressWarnings("unused")
  private Availability testIsRunningOk() {
    return anima.isRunning() ? Availability.available() : Availability.unavailable("there are no configs in memory");
  }

  @ShellMethod(value = "Test method. Just return the string parameter", group = "Testing Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOk")
  @GetMapping("test")
  public String test(@Size(min = 1, max = 40) @ShellOption(help = "example string for test method") String testString)
      throws InterruptedException {
    char[] animationChars = new char[] { '|', '/', '-', '\\' };
    int totali = 1000;
    Instant inizio = new Instant();
    for (int conto = 0; conto <= totali; conto++) {
      Duration trascorso = new Duration(inizio, new Instant());
      String circle = "";
      final String spazii = "                            ";
      if (conto > 0 && trascorso.getMillis() > 0 && trascorso.getMillis() / 1000 > 0) {
        circle = AnsiOutput.toString("  ", AnsiColor.GREEN, animationChars[conto % 4], AnsiColor.DEFAULT, " [ ",
            AnsiColor.GREEN, String.valueOf(conto), AnsiColor.DEFAULT, "/", AnsiColor.RED,
            String.valueOf(totali - conto), AnsiColor.DEFAULT, " - ", AnsiColor.YELLOW,
            String.valueOf(trascorso.getStandardSeconds()), AnsiColor.DEFAULT, " sec - ", AnsiColor.YELLOW,
            new DecimalFormat("#.###").format(Double.valueOf(conto) / trascorso.getStandardSeconds()),
            AnsiColor.DEFAULT, " x sec ]", spazii);
      } else {
        circle = "  starting..." + spazii;
      }
      System.out.print("Running:  " + testString + " " + String.valueOf(conto % testString.length()) + circle + "\r");
      fib(load);
    }
    System.out.println("\n\n");
    return "Processing: Done!";
  }

  private static BigInteger fib(long nth) {
    nth = nth - 1;
    long count = 0;
    BigInteger first = BigInteger.ZERO;
    BigInteger second = BigInteger.ONE;

    BigInteger third = null;
    while (count < nth) {
      third = new BigInteger(first.add(second).toString());
      first = new BigInteger(second.toString());
      second = new BigInteger(third.toString());
      count++;
    }
    return third;
  }

  @ShellMethod("View the selected configuration in base64 text")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigBase64() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(anima.getWorkingConfig());
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
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
  public void importSelectedConfigBase64(
      @ShellOption(help = "configuration exported by export-selected-config-base64") String base64Config)
      throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(base64Config);
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    anima.setWorkingConfig((Ar4kConfig) ois.readObject());
    anima.addConfig(anima.getWorkingConfig());
    ois.close();
  }

  @ShellMethod("Load selected configuration from a base64 text file")
  @ManagedOperation
  public void loadSelectedConfigBase64(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.base64.ar4k to the string") String filename)
      throws IOException, ClassNotFoundException {
    String config = "";
    FileReader fileReader = new FileReader(
        filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.base64.ar4k");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      config = config + line;
    }
    bufferedReader.close();
    importSelectedConfigBase64(config);
  }

  @ShellMethod("View the selected configuration in base64 text crypted in RSA")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigBase64Rsa(@ShellOption(help = "keystore alias for the key") String alias)
      throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, NoSuchPaddingException,
      InvalidKeyException, UnrecoverableEntryException {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(null);
    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
    Cipher inputCipher = Cipher.getInstance("SHA256withRSA");
    inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(baos, inputCipher);
    ObjectOutputStream oos = new ObjectOutputStream(cipherOutputStream);
    oos.writeObject(anima.getWorkingConfig());
    cipherOutputStream.close();
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  // TODO: da completare con la crittografia e provare.
  @ShellMethod("Save selected configuration in base64 text file crypted in RSA")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String saveSelectedConfigBase64Rsa(
      @ShellOption(help = "file for saving the configuration. The system will add .conf.base64.rsa.ar4k to the string") String filename,
      @ShellOption(help = "keystore alias for the key") String alias) throws InvalidKeyException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, UnrecoverableEntryException, IOException {
    Files.write(Paths.get(filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.base64.rsa.ar4k"),
        getSelectedConfigBase64Rsa(alias).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return "saved";
  }

  // TODO: da completare con la crittografia e provare
  @ShellMethod("Import the selected configuration from base64 text crypted in RSA")
  @ManagedOperation
  public void importSelectedConfigBase64Rsa(
      @ShellOption(help = "configuration exported by export-selected-config-base64-rsa") String base64Config)
      throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(base64Config);
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    anima.setWorkingConfig((Ar4kConfig) ois.readObject());
    anima.addConfig(anima.getWorkingConfig());
    ois.close();
  }

  // TODO: aggiungere il salvataggio dell configurazione su DNS con key (provare
  // su sottodominio ar4k.net) e il salvataggio della configurazione su web con
  // http push autenticato.

  @ShellMethod("Load selected configuration from a base64 text file crypted in RSA")
  @ManagedOperation
  public void loadSelectedConfigBase64Rsa(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.base64.rsa.ar4k to the string") String filename)
      throws IOException, ClassNotFoundException {
    String config = "";
    FileReader fileReader = new FileReader(
        filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.base64.rsa.ar4k");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      config = config + line;
    }
    bufferedReader.close();
    importSelectedConfigBase64Rsa(config);
  }

  @ShellMethod("View the selected configuration in json text")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String getSelectedConfigJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(anima.getWorkingConfig());
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

  // TODO: risolvere gli oggetti annidati
  @ShellMethod("Import the selected configuration from json text")
  @ManagedOperation
  public void importSelectedConfigJson(
      @ShellOption(help = "configuration exported by export-selected-config-json") String jsonConfig) {
    Gson gson = new Gson();
    anima.setWorkingConfig((Ar4kConfig) gson.fromJson(jsonConfig, ConfigSeed.class));
    anima.addConfig(anima.getWorkingConfig());
  }

  // TODO: risolvere gli oggetti annidati
  @ShellMethod("Load selected configuration from a json text file")
  @ManagedOperation
  public void loadSelectedConfigJson(
      @ShellOption(help = "file in where the configuration is saved. The system will add .conf.json.ar4k to the string") String filename)
      throws IOException, ClassNotFoundException {
    String config = "";
    FileReader fileReader = new FileReader(
        filename.replaceFirst("^~", System.getProperty("user.home")) + ".conf.json.ar4k");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      config = config + line;
    }
    bufferedReader.close();
    importSelectedConfigJson(config);
  }

  @ShellMethod("Create new configuration as selected configuration")
  @ManagedOperation
  public void createSelectedConfig(@ShellOption() @Valid Ar4kConfig confCreated) {
    anima.setWorkingConfig(confCreated);
    anima.addConfig(anima.getWorkingConfig());
  }

  @ShellMethod("List configs in runtime")
  @ManagedOperation
  @ShellMethodAvailability("testListConfigOk")
  public String listConfig() {
    String risposta = "";
    for (Ar4kConfig configurazione : anima.getConfigs()) {
      risposta = risposta
          + AnsiOutput.toString(AnsiColor.GREEN, configurazione.uniqueId.toString(), AnsiColor.DEFAULT, " - ",
              configurazione.name, " [", configurazione.promptColor, configurazione.prompt, AnsiColor.DEFAULT, "]\n");
    }
    return risposta;
  }

  @ShellMethod(value = "List tunnel in runtime", group = "Tunnel Commands")
  @ManagedOperation
  public String listTunnelsRuntime() {
    String risposta = "";
    for (Ar4kComponent configurazione : anima.getComponentBeans()) {
      try {
        TunnelComponent t = (TunnelComponent) configurazione;
        TunnelConfig c = ((TunnelConfig) t.getConfiguration());
        risposta = risposta + AnsiOutput.toString(AnsiColor.GREEN, c.name, AnsiColor.DEFAULT, " - ",
            t.getClass().getName(), " [", AnsiColor.RED,
            t.socket != null ? (t.socket.isConnected() ? "connected" : "disconnected") : "not implemented",
            AnsiColor.DEFAULT, "]\n");
      } catch (Exception aa) {
      }
    }
    return risposta;
  }

  @ShellMethod(value = "List tunnel in selected config", group = "Tunnel Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String listTunnelsSelectedConfig() {
    String risposta = "";
    for (ConfigSeed a : anima.getWorkingConfig().beans) {
      try {
        TunnelConfig b = (TunnelConfig) a;
        if (b.name != null) {
          risposta = risposta
              + AnsiOutput.toString(AnsiColor.GREEN, b.name, AnsiColor.DEFAULT, " - ", b.getClass().getName(), "\n");
        }
      } catch (Exception ee) {
      }
    }
    return risposta;
  }

  @ShellMethod("Select a config in the list")
  @ManagedOperation
  @ShellMethodAvailability("testListConfigOk")
  public void selectConfig(@ShellOption(help = "the id of the config to select") String idConfig) {
    Ar4kConfig target = null;
    for (Ar4kConfig configurazione : anima.getConfigs()) {
      if (configurazione.uniqueId.toString().equals(idConfig)) {
        target = configurazione;
        break;
      }
    }
    anima.setWorkingConfig(target);
  }

  @ShellMethod("Clone a config in the list with a new id, name and prompt")
  @ManagedOperation
  @ShellMethodAvailability("testListConfigOk")
  public String cloneConfig(@ShellOption(help = "the id of the config to clone from") String idConfig,
      @ShellOption(help = "the name of the new config") String newName,
      @ShellOption(help = "the promp for the new config") String newPrompt) throws IOException, ClassNotFoundException {
    Ar4kConfig target = null;
    for (Ar4kConfig configurazione : anima.getConfigs()) {
      if (configurazione.uniqueId.toString().equals(idConfig)) {
        target = configurazione;
        break;
      }
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(target);
    oos.close();
    byte[] data = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(baos.toByteArray()));
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    Ar4kConfig newTarget = (Ar4kConfig) ois.readObject();
    ois.close();
    newTarget.uniqueId = UUID.randomUUID();
    newTarget.name = newName;
    newTarget.prompt = newPrompt;
    anima.getConfigs().add(newTarget);
    return "cloned";
  }

  @ShellMethod("Unset the selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String unsetSelectedConfig() {
    anima.setWorkingConfig(null);
    return "unseted";
  }

  @ShellMethod(value = "Set selected config as target config for the agent", group = "Run Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public String setSelectedConfigAsRuntime() {
    anima.setTargetConfig(anima.getWorkingConfig());
    return "set";
  }

  @ShellMethod(value = "View the actual status", group = "Run Commands")
  @ManagedOperation
  public String getAr4kAgentStatus() {
    return anima.getState().name();
  }

  @ShellMethod(value = "Set a event to the agent", group = "Run Commands")
  @ManagedOperation
  public void setAr4kAgentStatus(
      @ShellOption(help = "target status", valueProvider = Ar4kEventsValuesProvider.class) AnimaEvents target) {
    anima.sendEvent(target);
  }

  @ShellMethod(value = "Shutdown agent", group = "Run Commands")
  @ManagedOperation
  public void goodbye() {
    setAr4kAgentStatus(AnimaEvents.FINALIZE);
    System.exit(0);
  }

  @ShellMethod(value = "Pause agent", group = "Run Commands")
  @ManagedOperation
  @ShellMethodAvailability("testIsRunningOk")
  public void pause() {
    setAr4kAgentStatus(AnimaEvents.PAUSE);
  }

  @ShellMethod(value = "Restart agent", group = "Run Commands")
  @ManagedOperation
  @ShellMethodAvailability("testIsRunningOk")
  public void restart() {
    setAr4kAgentStatus(AnimaEvents.STOP);
    try {
      Thread.sleep(1500L);
    } catch (InterruptedException e) {
    }
  }

  @ShellMethod(value = "List runtime services", group = "Run Commands")
  @ManagedOperation
  @ShellMethodAvailability("testIsRunningOk")
  public String listService() {
    String risposta = "";
    for (Ar4kService servizio : anima.getServices()) {
      risposta = risposta + AnsiOutput.toString(AnsiColor.GREEN, servizio.getConfiguration().getUniqueId().toString(),
          AnsiColor.DEFAULT, " - ", servizio.getConfiguration().name, " [", AnsiColor.RED, servizio.status(),
          AnsiColor.DEFAULT, "]\n");
    }
    return risposta;
  }

  @ShellMethod(value = "Clone runtime config in the list with a new id, name and prompt", group = "Run Commands")
  @ManagedOperation
  @ShellMethodAvailability("testRuntimeConfigOk")
  public void cloneRuntimeConfig(@ShellOption(help = "the name of the new config") String newName,
      @ShellOption(help = "the promp for the new config") String newPrompt) throws IOException, ClassNotFoundException {
    Ar4kConfig target = anima.getRuntimeConfig();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(target);
    oos.close();
    byte[] data = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(baos.toByteArray()));
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    Ar4kConfig newTarget = (Ar4kConfig) ois.readObject();
    ois.close();
    newTarget.uniqueId = UUID.randomUUID();
    newTarget.name = newName;
    newTarget.prompt = newPrompt;
    anima.addConfig(newTarget);
  }

  @ShellMethod(value = "Set the log filter for the console", group = "Monitoring Commands")
  @ManagedOperation
  public String setLogLevel(
      @ShellOption(help = "the new log level to set", defaultValue = "INFO", valueProvider = LogLevelValuesProvider.class) String newLogLevel) {
    Ar4kLogger.level = Ar4kLogger.LogLevel.valueOf(newLogLevel);
    changeLogLevel(Ar4kLogger.level.name());
    return Ar4kLogger.level.name();
  }

  @ShellMethod(value = "List jmx endpoints", group = "Monitoring Commands")
  @ManagedOperation
  public List<String> listJmxEndpoints() throws IntrospectionException, InstanceNotFoundException, ReflectionException {
    List<String> ritorno = new ArrayList<String>();
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    Set<ObjectInstance> instances = mbs.queryMBeans(null, null);
    Iterator<ObjectInstance> iterator = instances.iterator();
    while (iterator.hasNext()) {
      ObjectInstance instance = iterator.next();
      MBeanInfo mbi = mbs.getMBeanInfo(instance.getObjectName());
      for (MBeanOperationInfo op : mbi.getOperations()) {
        ritorno.add(instance.getClassName() + "." + op.getName() + " [" + op.getDescription() + "]");
      }
    }
    return ritorno;
  }

  @ShellMethod(value = "Get the log filter for the console", group = "Monitoring Commands")
  @ManagedOperation
  public String getLogLevel() {
    return Ar4kLogger.level.name();
  }

  private void changeLogLevel(String gwlog) {
    for (ch.qos.logback.classic.Logger l : findAllLogger()) {
      l.setLevel(ch.qos.logback.classic.Level.toLevel(gwlog, ch.qos.logback.classic.Level.INFO));
    }
  }

  private LoggerContext getLoggerContext() {
    return ContextSelectorStaticBinder.getSingleton().getContextSelector().getLoggerContext();
  }

  public List<ch.qos.logback.classic.Logger> findAllLogger() {
    return getLoggerContext().getLoggerList();
  }

  @ShellMethod(value = "Get info about the JVM and hardware", group = "Monitoring Commands")
  @ManagedOperation
  public String getHardwareInfo() throws IOException, InterruptedException, ParseException {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      return gson.toJson(Utils.getSystemInfo());
    } catch (Exception ae) {
      ae.printStackTrace();
      return null;
    }
  }

  @ShellMethod(value = "Get ar4k variable from Spring Framework", group = "Monitoring Commands")
  @ManagedOperation
  public void printEnvironmentVariables() {
    System.out.println(anima.getEnvironmentVariablesAsString());
  }

  // TODO: visualizzazione contatori camel con possibilità di impostare un
  // servizio di notifica regolare con camel dei valori.
  // TODO: runCommandLine in package command line interface

  @ShellMethod(value = "Run shell command on the system. CTRL-D exit", group = "Run Commands")
  @ManagedOperation
  public String runCommandLine(
      @ShellOption(help = "the command to start in the shell", defaultValue = "/bin/bash -login") String shellCommand,
      @ShellOption(help = "the int number of the end character. 5 is Ctrl+E", defaultValue = "5") String endCharacter) {
    Integer errori = 0;
    boolean running = true;
    Process pr = null;
    Runtime rt = Runtime.getRuntime();
    try {
      pr = rt.exec(shellCommand);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    InputStream is = pr.getInputStream();
    OutputStream os = pr.getOutputStream();
    InputStream es = pr.getErrorStream();
    Reader rin = new InputStreamReader(is, StandardCharsets.UTF_8);
    Reader rerr = new InputStreamReader(es, StandardCharsets.UTF_8);
    Writer rout = new OutputStreamWriter(os, StandardCharsets.UTF_8);
    Reader si = new InputStreamReader(System.in, StandardCharsets.UTF_8);
    Writer so = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
    while (running && errori < 10) {
      try {
        if (rin != null && rin.ready()) {
          char[] c = new char[1];
          rin.read(c, 0, 1);
          so.write(c);
          so.flush();
        }
      } catch (IOException e) {
        errori++;
        // logger.logException(e);
      }
      try {
        if (rerr != null && rerr.ready()) {
          char[] c = new char[1];
          rerr.read(c, 0, 1);
          so.write(c);
          so.flush();
        }
      } catch (IOException e) {
        errori++;
        // logger.logException(e);
      }
      try {
        if (si.ready()) {
          char c[] = new char[1];// System.in.read();
          // if (c > -1) {
          si.read(c, 0, 1);
          // System.out.println(String.valueOf((int) Character.valueOf(c[0]).charValue())
          // + " "
          // + (Integer.parseInt(endCharacter) == Character.valueOf(c[0]).charValue()));
          // System.out.println(c);
          if (Integer.parseInt(endCharacter) == Character.valueOf(c[0]).charValue()) {
            running = false;
            Thread.sleep(500L);
          }
          if (rout != null) {
            rout.write(c);
            rout.flush();
          }
        }
      } catch (IOException | InterruptedException e) {
        errori++;
        // logger.logException(e);
      }
    }
    System.out.println("close the readers after " + String.valueOf(errori) + " errors. wait...");
    if (pr != null) {
      pr.destroyForcibly();
      pr = null;
    }
    if (rin != null) {
      try {
        rin.close();
        rin = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (rerr != null) {
      try {
        rerr.close();
        rerr = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (rout != null) {
      try {
        rout.close();
        rout = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (es != null) {
      try {
        es.close();
        es = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (is != null) {
      try {
        is.close();
        is = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (os != null) {
      try {
        os.close();
        os = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    try {
      System.in.reset();
      throw new Ar4kException("running shell terminated");
    } catch (Ar4kException | IOException e) {
      errori++;
      logger.warn(e.getMessage());
      if (errori > 0) {
        throw new Ar4kException("running shell terminated");
      }
    }

    if (si != null) {
      try {
        si.close();
        si = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (so != null) {
      try {
        so.close();
        so = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    return "session terminated with " + String.valueOf(errori) + " errors";
  }

  @SuppressWarnings("unused")
  private boolean processIsTerminated(Process process) {
    try {
      process.exitValue();
    } catch (IllegalThreadStateException itse) {
      return false;
    }
    return true;
  }
}
