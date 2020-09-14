package org.ar4k.agent.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

public class ContextCreationHelper implements Callable<Homunculus>, ApplicationListener<ApplicationPreparedEvent> {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(ContextCreationHelper.class.toString());

	private class RunContext implements Runnable {
		private final String[] argsRunner;
		private final SpringApplication app;

		private RunContext(SpringApplication app, String[] argsRunner) {
			this.app = app;
			this.argsRunner = argsRunner;
		}

		@Override
		public void run() {
			try {
				app.run(argsRunner);
				logger.info("\n\tHomunculus Started...\n");
			} catch (Exception a) {
				logger.logException(a);
			}
		}
	}

	private final List<String> args = new ArrayList<>();
	private ApplicationListener<ApplicationPreparedEvent> listeners = this;
	private ConfigurableApplicationContext context = null;
	private final ExecutorService executor;
	private final Class<?> startClass;

	public ContextCreationHelper(Class<?> startClass, ExecutorService executor, String loggerString, String keyStore,
			int serverPort, List<String> additionalArgs) {
		this.startClass = startClass;
		this.executor = executor;
		if (loggerString != null && !loggerString.isEmpty())
			args.add("--logging.file=" + loggerString);
		args.add("--server.port=" + String.valueOf(serverPort));
		args.add("--spring.shell.interactive.enabled=false");
		args.add("--spring.jmx.enabled=false");
		if (keyStore != null && !keyStore.isEmpty())
			args.add("--ar4k.fileKeystore=" + keyStore);
		if (additionalArgs != null) {
			args.addAll(additionalArgs);
		}
	}

	public ContextCreationHelper(Class<?> startClass, ExecutorService executor, String loggerString, String keyStore,
			int serverPort, List<String> additionalArgs, EdgeConfig config, String mainAliasInKeystore,
			String keystoreBeaconAlias, String webRegistrationEndpoint) {
		this(startClass, executor, loggerString, keyStore, serverPort, additionalArgs);
		if (mainAliasInKeystore != null && !mainAliasInKeystore.isEmpty())
			args.add("--ar4k.keystoreMainAlias=" + mainAliasInKeystore);
		if (keystoreBeaconAlias != null && !keystoreBeaconAlias.isEmpty())
			args.add("--ar4k.keystoreBeaconAlias=" + keystoreBeaconAlias);
		if (webRegistrationEndpoint != null && !webRegistrationEndpoint.isEmpty())
			args.add("--ar4k.webRegistrationEndpoint=" + webRegistrationEndpoint);
		try {
			args.add("--ar4k.baseConfig=" + ConfigHelper.toBase64(config));
		} catch (Exception e) {
			logger.logException(e);
		}
	}

	@Override
	public Homunculus call() throws Exception {
		try {
			SpringApplication newCtx = new SpringApplication(startClass);
			newCtx.setBannerMode(Banner.Mode.LOG);
			newCtx.addListeners(listeners);
			String[] argsArray = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {
				argsArray[i] = args.get(i);
			}
			Future<?> r = executor.submit(new RunContext(newCtx, argsArray));
			r.get();
			while (context == null) {
				logger.info("waiting context");
				Thread.sleep(500L);
			}
			logger.info("found context");
			while (!context.containsBean("homunculus")) {
				logger.info("waiting Homunculus");
				Thread.sleep(500L);
			}
			logger.info("found Homunculus");
			Homunculus homunculus = context.getBean(Homunculus.class);
			/*
			 * while (anima.getState() != null) { logger.info("waiting first state");
			 * Thread.sleep(500L); }
			 */
			logger.info("Homunculus -> " + homunculus.toString() + " [" + homunculus.getState() + "]");
			logger.info("Enviroment -> " + homunculus.getEnvironmentVariablesAsString());
			logger.info("Configuration -> " + homunculus.getRuntimeConfig());
			return context.getBean(Homunculus.class);
		} catch (Exception e) {
			logger.logException(e);
			return null;
		}
	}

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		context = event.getApplicationContext();
	}
};