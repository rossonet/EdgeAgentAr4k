package org.ar4k.agent.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.Availability;

public abstract class AbstractShellHelper {

  protected static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AbstractShellHelper.class.toString());

  private static final Long load = 1500L;

  @Value("${ar4k.test}")
  protected final String flagTestOk = "false";

  @Autowired
  protected ApplicationContext applicationContext;

  @Autowired
  protected Anima anima;

  @Autowired
  protected SessionRegistry sessionRegistry;

  protected String getSessionId() {
    List<SessionInformation> ss = sessionRegistry.getAllSessions(SecurityContextHolder.getContext().getAuthentication(),
        false);
    return ss.isEmpty() ? null : ss.get(0).getSessionId();
  }

  protected Availability testOk() {
    return Boolean.valueOf(flagTestOk) ? Availability.available()
        : Availability.unavailable("test command not available in this configuration");
  }

  protected Availability sessionOk() {
    return getSessionId() != null ? Availability.available()
        : Availability.unavailable("you must login in the system before");
  }

  protected Availability sessionOkOrStatusInit() {
    return (getSessionId() != null || anima.getState().equals(AnimaStates.INIT)
        || anima.getState().equals(AnimaStates.STAMINAL)) ? Availability.available()
            : Availability.unavailable("you must login in the system or to be in INIT status");
  }

  protected Availability sessionFalse() {
    return getSessionId() == null ? Availability.available()
        : Availability.unavailable("you have a valide session. Logout from the system before");
  }

  protected Availability isUnix() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("nix") || os.contains("nux") || os.contains("aix")) ? Availability.available()
        : Availability.unavailable("this command must run on unix system");
  }

  protected Availability isUnixAndSessionOk() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("nix") || os.contains("nux") || os.contains("aix")) && getSessionId() != null
        ? Availability.available()
        : Availability.unavailable("this command must run on unix system");
  }

  protected Availability testSelectedConfigOk() {
    Availability result = Availability.unavailable("you have to login in the agent before");
    if (getSessionId() != null) {
      result = getWorkingConfig() != null ? Availability.available()
          : Availability.unavailable("you have to select a config before");
    }
    return result;
  }

  protected Availability selectedConfigurationAndOneSslKey() {
    boolean ok = true;
    String message = "";
    if (getWorkingConfig() == null) {
      ok = false;
      message += "you have to select a config before";
    }
    if (anima.getMyIdentityKeystore() == null) {
      if (ok == false) {
        message += " and ";
      }
      ok = false;
      message += "you need a keystore configured on the gateway";
    }
    return ok ? Availability.available() : Availability.unavailable(message);
  }

  protected Ar4kConfig getWorkingConfig() {
    if (getSessionId() != null) {
      return ((RpcConversation) anima.getRpc(getSessionId())).getWorkingConfig();
    } else
      return null;
  }

  protected void setWorkingConfig(Ar4kConfig config) {
    Map<String, Ar4kConfig> actualConfig = ((RpcConversation) anima.getRpc(getSessionId())).getConfigurations();
    if (config != null && actualConfig != null && !actualConfig.containsValue(config))
      addConfig(config);
    if (config != null)
      ((RpcConversation) anima.getRpc(getSessionId())).setWorkingConfig(config.getName());
    else
      ((RpcConversation) anima.getRpc(getSessionId())).setWorkingConfig(null);
  }

  protected Availability testRuntimeConfigOk() {
    return (anima.getRuntimeConfig() != null && getSessionId() != null) ? Availability.available()
        : anima.getRuntimeConfig() != null ? Availability.unavailable("you have to configure a runtime config before")
            : Availability.unavailable("you must login in the system or to be in INIT status");
  }

  protected Availability testListConfigOk() {
    Availability result = Availability.unavailable("you have to login in the agent before");
    if (getSessionId() != null) {
      result = (getConfigs() != null && getConfigs().size() > 0) ? Availability.available()
          : Availability.unavailable("there are no configs in memory");
    }
    return result;
  }

  protected Map<String, Ar4kConfig> getConfigs() {
    return ((RpcConversation) anima.getRpc(getSessionId())).getConfigurations();
  }

  protected void addConfig(Ar4kConfig config) {
    ((RpcConversation) anima.getRpc(getSessionId())).getConfigurations().put(config.getName(), config);
  }

  protected Availability testIsRunningOk() {
    return anima.isRunning() ? Availability.available() : Availability.unavailable("there are no configs in memory");
  }

  protected boolean addUser(String username, String password, String authorities, PasswordEncoder passwordEncoder) {
    Ar4kUserDetails u = new Ar4kUserDetails();
    u.setUsername(username);
    u.setPassword(passwordEncoder.encode(password));
    List<SimpleGrantedAuthority> a = new ArrayList<>();
    for (String p : authorities.split(",")) {
      SimpleGrantedAuthority g = new SimpleGrantedAuthority(p);
      a.add(g);
    }
    u.setAuthorities(a);
    anima.getLocalUsers().add(u);
    return true;
  }

  protected boolean removeUser(String username) {
    boolean result = false;
    Ar4kUserDetails t = null;
    for (Ar4kUserDetails u : anima.getLocalUsers()) {
      if (u.getUsername().equals(username)) {
        t = u;
        break;
      }
    }
    if (t != null) {
      anima.getLocalUsers().remove(t);
      t = null;
      result = true;
    }
    return result;
  }

  protected static Ar4kConfig cloneConfigHelper(String newName, String newPrompt, Ar4kConfig target)
      throws IOException, ClassNotFoundException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(target);
    oos.close();
    byte[] data = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(baos.toByteArray()));
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    Ar4kConfig newTarget = (Ar4kConfig) ois.readObject();
    ois.close();
    newTarget.uniqueId = UUID.randomUUID().toString();
    newTarget.name = newName;
    newTarget.prompt = newPrompt;
    return newTarget;
  }

  protected static Collection<String> listMbeans()
      throws ReflectionException, IntrospectionException, InstanceNotFoundException {
    List<String> ritorno = new ArrayList<>();
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

  protected static String readFromFile(String filename, String endStringFilter)
      throws FileNotFoundException, IOException {
    String config = "";
    FileReader fileReader = new FileReader(
        filename.replaceFirst("^~", System.getProperty("user.home")) + endStringFilter);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      config = config + line;
    }
    bufferedReader.close();
    return config;
  }

  protected static String runShellTest(String testString) {
    int totali = 1000;
    Instant inizio = new Instant();
    waitingConsole(testString, totali, inizio);
    System.out.println("\n\n");
    return "Processing: Done!";
  }

  protected static void waitingConsole(String text, int totali, Instant inizio) {
    char[] animationChars = new char[] { '|', '/', '-', '\\' };
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
      System.out.print("Running:  " + text + " " + circle + "\r");
      fib(load);
    }
  }

}
