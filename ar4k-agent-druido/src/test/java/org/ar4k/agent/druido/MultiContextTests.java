package org.ar4k.agent.druido;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ar4k.agent.console.Ar4kAgent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class MultiContextTests {

  class CreateContext implements Runnable {

    private ConfigurableApplicationContext context;
    private final String[] disabledCommands = { "--spring.shell.command.quit.enabled=false" };

    CreateContext(ConfigurableApplicationContext ctx) {
      this.context = ctx;
    }

    @Override
    public void run() {
      context = SpringApplication.run(Ar4kAgent.class, disabledCommands);
    }

    public ConfigurableApplicationContext getContext() {
      return context;
    }
  };

  private ExecutorService executor = Executors.newCachedThreadPool();
  private ConfigurableApplicationContext context1;
  private ConfigurableApplicationContext context2;
  private ConfigurableApplicationContext context3;

  @Before
  public void setUp() throws Exception {
    executor.submit(new CreateContext(context1));
    executor.submit(new CreateContext(context2));
    executor.submit(new CreateContext(context3));
  }

  @After
  public void tearDown() throws Exception {
    ExitCodeGenerator exitCodeGenerators = new ExitCodeGenerator() {
      @Override
      public int getExitCode() {
        return 0;
      }
    };
    if (context1 != null)
      SpringApplication.exit(context1, exitCodeGenerators);
    if (context2 != null)
      SpringApplication.exit(context2, exitCodeGenerators);
    if (context3 != null)
      SpringApplication.exit(context3, exitCodeGenerators);
  }

  @Test
  public void baseTest() throws InterruptedException {
    Thread.sleep(60000);
  }

}
