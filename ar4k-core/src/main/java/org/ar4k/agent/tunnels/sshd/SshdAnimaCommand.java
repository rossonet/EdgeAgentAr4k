package org.ar4k.agent.tunnels.sshd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.command.Command;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Shell;

/**
 *
 * @author andrea
 *
 */
public class SshdAnimaCommand implements Command {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AbstractShellHelper.class.toString());

	private static final CharSequence COMPLETION_CHAR = "?";
	private static final CharSequence EXIT_MESSAGE = "exit";

	private final Shell shell;
	private final Anima anima;
	private RpcExecutor rpcExecutor = null;
	private final String sessionId = UUID.randomUUID().toString();
	private ExitCallback exitCallback = null;
	private OutputStream errorStream = null;
	private OutputStream outputStream = null;
	private InputStream inputStream = null;

	private boolean running = true;
	Thread runner = null;

	public SshdAnimaCommand(Anima homunculus, Shell shell) {
		this.anima = homunculus;
		this.shell = shell;
	}

	private void sendMessage(String txt) throws IOException {
		outputStream.flush();
		outputStream.write(txt.getBytes(StandardCharsets.UTF_8));
		outputStream.flush();
	}

	@Override
	public void start(Environment env) throws IOException {
		logger.info("Run SSHD executer");
		running();
	}

	private void running() throws IOException {
		runner = new Thread(new LoopRunner(), "sshd_" + sessionId);
		runner.start();
	}

	private String completeMessage(String message) {
		final List<String> m = Arrays.asList(StringUtils.split(message));
		final List<String> clean = new ArrayList<>(m.size());
		int pos = 0;
		int word = 0;
		int counter = 0;
		for (final String p : m) {
			if (p.contains(COMPLETION_CHAR)) {
				word = counter;
				pos = p.indexOf(COMPLETION_CHAR.toString());
				if (!p.equals(COMPLETION_CHAR.toString())) {
					// System.out.println("add " + p.replace(COMPLETION_CHAR, ""));
					clean.add(p.replace(COMPLETION_CHAR, ""));
				} else {
					// System.out.println("add " + p);
					clean.add(p);
				}
			} else {
				clean.add(p);
			}
			counter++;
		}
		final CompletionContext context = new CompletionContext(clean, word, pos);
		final List<CompletionProposal> listCompletionProposal = rpcExecutor.complete(context);
		final StringBuffer response = new StringBuffer();
		for (final CompletionProposal prop : listCompletionProposal) {
			response.append(prop.toString() + (prop.description() != null ? " => " + prop.description() : "") + "\n");
		}
		return response.toString();
	}

	@Override
	public void destroy() throws Exception {
		running = false;
		inputStream.close();
		outputStream.flush();
		outputStream.close();
		errorStream.flush();
		errorStream.close();
		rpcExecutor = null;
	}

	@Override
	public void setInputStream(InputStream in) {
		this.inputStream = in;
	}

	@Override
	public void setOutputStream(OutputStream out) {
		this.outputStream = out;
	}

	@Override
	public void setErrorStream(OutputStream err) {
		this.errorStream = err;
	}

	@Override
	public void setExitCallback(ExitCallback callback) {
		this.exitCallback = callback;
	}

	private void readLineString(InputStream inputStream, StringBuilder messageBuilder, boolean echo)
			throws IOException {
		int c = 0;
		do {
			if (inputStream.available() > 0) {
				c = inputStream.read();
				// logger.warn("*** remote char -> " + (char) c);
				if (echo) {
					sendMessage(String.valueOf((char) c));
				} else {
					sendMessage("x");
				}
				if ((c) == 13)
					break;
				messageBuilder.append((char) c);
			}
		} while (c != -1);
	}

	public Shell getShell() {
		return shell;
	}

	private class LoopRunner implements Runnable {
		private String username = null;
		private String password = null;
		private boolean logged = false;

		@Override
		public void run() {
			logger.info("*** start remote sshd session");
			while (running) {
				try {
					// logger.debug("*** while");
					if (!logged) {
						if (username == null || username.isEmpty()) {
							final StringBuilder usernameBuilder = new StringBuilder();
							sendMessage("USERNAME: ");
							readLineString(inputStream, usernameBuilder, true);
							username = usernameBuilder.toString();
							sendMessage(
									"\r                                                                                                                   \r");
						}
						if (password == null || password.isEmpty()) {
							final StringBuilder passwordBuilder = new StringBuilder();
							sendMessage("PASSWORD: ");
							readLineString(inputStream, passwordBuilder, false);
							password = passwordBuilder.toString();
							sendMessage(
									"\r                                                                                                                   \r");
						}
						if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
							try {
								final String session = anima.loginAgent(username, password, sessionId);
								if (anima.isSessionValid(session) && session.equals(sessionId)) {
									rpcExecutor = anima.getRpc(sessionId);
									logged = true;
									printPrompt();
								} else {
									username = null;
									password = null;
									sendMessage("FAILED!\n\r");
									logger.info("failed login for " + username);
								}
							} catch (NullPointerException | AuthenticationException e) {
								username = null;
								password = null;
								sendMessage("FAILED!\n\r");
								logger.info("failed login for " + username);
							}
						}
					} else {
						final StringBuilder messageBuilder = new StringBuilder();
						readLineString(inputStream, messageBuilder, true);
						final String message = messageBuilder.toString();
						if (message != null && !message.isEmpty()) {
							if (!message.contains(COMPLETION_CHAR) && !message.equals(EXIT_MESSAGE)) {
								try {
									final String elaborateMessage = rpcExecutor.elaborateMessage(message);
									// logger.debug("*** remote command " + message + " -> " + elaborateMessage);
									sendMessage("\n\r" + elaborateMessage.replaceAll("\n", "\n\r"));
									printPrompt();
								} catch (final Exception re) {
									logger.logException(re);
								}
							} else {
								final String response = completeMessage(message);
								sendMessage("\n\r" + response.replaceAll("\n", "\n\r"));
								printPrompt();
							}
							if (message.equals(EXIT_MESSAGE)) {
								running = false;
							}
						} else {
							sendMessage("\n\r");
							printPrompt();
						}
					}
				} catch (final Exception e) {
					logger.logException(e);
				}
			}
			try {
				outputStream.flush();
			} catch (final Exception e) {
				logger.logException(e);
			}
			logger.warn("*** exit from remote sshd session");
			running = false;
			exitCallback.onExit(0, "bye...");
		}

		private void printPrompt() {
			try {
				sendMessage("\n\r" + username + "@" + anima.getAgentUniqueName() + " # ");
			} catch (final Exception e) {
				logger.logException(e);
			}

		}

	}

}
