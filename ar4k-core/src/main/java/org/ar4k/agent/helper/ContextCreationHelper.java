package org.ar4k.agent.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Anima;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

public class ContextCreationHelper implements Callable<Anima>, ApplicationListener<ApplicationPreparedEvent> {

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
        System.out.println("\n\tAnima Started...\n");
      } catch (Exception a) {
        a.printStackTrace();
      }
    }
  }

  private final List<String> args = new ArrayList<>();
  private ApplicationListener<ApplicationPreparedEvent> listeners = this;
  private ConfigurableApplicationContext context = null;
  private final ExecutorService executor;
  private final Class<?> startClass;

  public ContextCreationHelper(Class<?> startClass, ExecutorService executor, String logger, String keyStore,
      int serverPort, List<String> additionalArgs) {
    this.startClass = startClass;
    this.executor = executor;
    if (logger != null && !logger.isEmpty())
      args.add("--logging.file=" + logger);
    args.add("--server.port=" + String.valueOf(serverPort));
    args.add("--spring.shell.interactive.enabled=false");
    args.add("--spring.jmx.enabled=false");
    if (keyStore != null && !keyStore.isEmpty())
      args.add("--ar4k.fileKeystore=" + keyStore);
    if (additionalArgs != null) {
      args.addAll(additionalArgs);
    }
  }

  public ContextCreationHelper(Class<?> startClass, ExecutorService executor, String logger, String keyStore,
      int serverPort, List<String> additionalArgs, EdgeConfig config, String mainAliasInKeystore,
      String keystoreBeaconAlias, String webRegistrationEndpoint) {
    this(startClass, executor, logger, keyStore, serverPort, additionalArgs);
    if (mainAliasInKeystore != null && !mainAliasInKeystore.isEmpty())
      args.add("--ar4k.keystoreMainAlias=" + mainAliasInKeystore);
    if (keystoreBeaconAlias != null && !keystoreBeaconAlias.isEmpty())
      args.add("--ar4k.keystoreBeaconAlias=" + keystoreBeaconAlias);
    if (webRegistrationEndpoint != null && !webRegistrationEndpoint.isEmpty())
      args.add("--ar4k.webRegistrationEndpoint=" + webRegistrationEndpoint);
    try {
      args.add("--ar4k.baseConfig=" + ConfigHelper.toBase64(config));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Anima call() throws Exception {
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
        System.out.println("waiting context");
        Thread.sleep(500L);
      }
      System.out.println("found context");
      while (!context.containsBean("anima")) {
        System.out.println("waiting anima");
        Thread.sleep(500L);
      }
      System.out.println("found anima");
      Anima anima = context.getBean(Anima.class);
      /*
       * while (anima.getState() != null) { System.out.println("waiting first state");
       * Thread.sleep(500L); }
       */
      System.out.println("Anima -> " + anima.toString() + " [" + anima.getState() + "]");
      System.out.println("Enviroment -> " + anima.getEnvironmentVariablesAsString());
      System.out.println("Configuration -> " + anima.getRuntimeConfig());
      return context.getBean(Anima.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void onApplicationEvent(ApplicationPreparedEvent event) {
    context = event.getApplicationContext();
  }
};