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

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.spring.EdgeUserDetails;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.Availability;

public abstract class AbstractShellHelper {

	protected static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AbstractShellHelper.class.toString());

	private static final Long load = 1500L;

	@Value("${ar4k.test}")
	protected final String flagTestOk = "false";

	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected Homunculus homunculus;

	@Autowired
	protected SessionRegistry sessionRegistry;

	@Autowired
	protected ResourceLoader resourceLoader;

	protected void addConfig(EdgeConfig config) {
		((RpcConversation) homunculus.getRpc(getSessionId())).getConfigurations().put(config.getName(), config);
	}

	protected boolean addUser(String username, String password, String authorities, PasswordEncoder passwordEncoder) {
		EdgeUserDetails u = new EdgeUserDetails();
		u.setUsername(username);
		u.setPassword(passwordEncoder.encode(password));
		List<SimpleGrantedAuthority> a = new ArrayList<>();
		for (String p : authorities.split(",")) {
			SimpleGrantedAuthority g = new SimpleGrantedAuthority(p);
			a.add(g);
		}
		u.setAuthorities(a);
		homunculus.getLocalUsers().add(u);
		return true;
	}

	protected Map<String, EdgeConfig> getConfigs() {
		return ((RpcConversation) homunculus.getRpc(getSessionId())).getConfigurations();
	}

	protected String getSessionId() {
		List<SessionInformation> ss = sessionRegistry
				.getAllSessions(SecurityContextHolder.getContext().getAuthentication(), false);
		return ss.isEmpty() ? null : ss.get(0).getSessionId();
	}

	protected EdgeConfig getWorkingConfig() {
		if (getSessionId() != null) {
			return ((RpcConversation) homunculus.getRpc(getSessionId())).getWorkingConfig();
		} else
			return null;
	}

	protected ServiceConfig getWorkingService() {
		if (getWorkingConfig() != null) {
			return ((RpcConversation) homunculus.getRpc(getSessionId())).getWorkingService();
		} else
			return null;
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
				: Availability.unavailable("this command must run on unix system in a valid session (after login)");
	}

	protected boolean removeUser(String username) {
		boolean result = false;
		EdgeUserDetails t = null;
		for (EdgeUserDetails u : homunculus.getLocalUsers()) {
			if (u.getUsername().equals(username)) {
				t = u;
				break;
			}
		}
		if (t != null) {
			homunculus.getLocalUsers().remove(t);
			t = null;
			result = true;
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
		if (homunculus.getMyIdentityKeystore() == null) {
			if (ok == false) {
				message += " and ";
			}
			ok = false;
			message += "you need a keystore configured on the gateway";
		}
		return ok ? Availability.available() : Availability.unavailable(message);
	}

	protected Availability sessionFalse() {
		return getSessionId() == null ? Availability.available()
				: Availability.unavailable("you have a valide session. Logout from the system before");
	}

	protected Availability sessionOk() {
		return getSessionId() != null ? Availability.available()
				: Availability.unavailable("you must login in the system before");
	}

	protected Availability sessionOkOrStatusInit() {
		return (getSessionId() != null || homunculus.getState().equals(HomunculusStates.INIT)
				|| homunculus.getState().equals(HomunculusStates.STAMINAL)) ? Availability.available()
						: Availability.unavailable("you must login in the system or to be in INIT status");
	}

	protected void setWorkingConfig(EdgeConfig config) {
		Map<String, EdgeConfig> actualConfig = ((RpcConversation) homunculus.getRpc(getSessionId()))
				.getConfigurations();
		if (config != null && actualConfig != null && !actualConfig.containsValue(config))
			addConfig(config);
		if (config != null)
			((RpcConversation) homunculus.getRpc(getSessionId())).setWorkingConfig(config.getName());
		else
			((RpcConversation) homunculus.getRpc(getSessionId())).setWorkingConfig(null);
	}

	protected void modifyService(String serviceName) {
		final RpcConversation rpcConversation = (RpcConversation) homunculus.getRpc(getSessionId());
		if (serviceName != null && rpcConversation != null && rpcConversation.getWorkingConfig() != null) {
			rpcConversation.setModifyService(serviceName);
		} else if (serviceName == null && rpcConversation != null) {
			rpcConversation.setModifyService(null);
		}
	}

	protected Availability testIsRunningOk() {
		return homunculus.isRunning() ? Availability.available() : Availability.unavailable("anima is not running");
	}

	protected Availability testListConfigOk() {
		Availability result = Availability.unavailable("you have to login in the agent before");
		if (getSessionId() != null) {
			result = (getConfigs() != null && getConfigs().size() > 0) ? Availability.available()
					: Availability.unavailable("there are no configs in memory");
		}
		return result;
	}

	protected Availability testOk() {
		return Boolean.valueOf(flagTestOk) ? Availability.available()
				: Availability.unavailable("test command not available in this configuration");
	}

	protected Availability testRuntimeConfigOk() {
		return (homunculus.getRuntimeConfig() != null && getSessionId() != null) ? Availability.available()
				: homunculus.getRuntimeConfig() != null
						? Availability.unavailable("you have to configure a runtime config before")
						: Availability.unavailable("you must login in the system or to be in INIT status");
	}

	protected Availability testSelectedConfigOk() {
		Availability result = Availability.unavailable("you have to login in the agent before");
		if (getSessionId() != null) {
			result = getWorkingConfig() != null ? Availability.available()
					: Availability.unavailable("you have to select a config before");
		}
		return result;
	}

	protected Availability testSelectedServiceOk() {
		Availability result = Availability.unavailable("you have to login in the agent before");
		if (getSessionId() != null) {
			result = getWorkingService() != null ? Availability.available()
					: Availability.unavailable("you have to select a service before");
		}
		return result;
	}

	public static void printOnTerminal(AnsiColor ansiColor, String message) {
		try {
			Terminal terminal = TerminalBuilder.builder().build();
			terminal.writer().println(AnsiOutput.toString(ansiColor, message, AnsiColor.DEFAULT));
			terminal.flush();
		} catch (Exception e) {
			logger.logException(e);
		}
	}

	public static String readStringParameterOnTerminal(String request) {
		try {
			Terminal terminal = TerminalBuilder.builder().build();
			terminal.writer().print(AnsiOutput.toString(AnsiColor.RED, request + ": ", AnsiColor.DEFAULT));
			terminal.flush();
			LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
			String result = null;
			do {
				result = lineReader.readLine();
			} while (result == null);
			return result;
		} catch (Exception e) {
			logger.logException(e);
			return null;
		}
	}

	protected static EdgeConfig cloneConfigHelper(String newName, String newPrompt, EdgeConfig target)
			throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(target);
		oos.close();
		byte[] data = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(baos.toByteArray()));
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		EdgeConfig newTarget = (EdgeConfig) ois.readObject();
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

}
