package org.ar4k.agent.spring.autoconfig.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

@SpringBootApplication
@ComponentScan("org.ar4k.agent")
public class Ar4kWebBootStart {

  public static void main(String[] args) {
    String[] disabledCommands = { "--spring.shell.command.quit.enabled=false" };
    String[] fullArgs = StringUtils.concatenateStringArrays(args, disabledCommands);
    SpringApplication.run(Ar4kWebBootStart.class, fullArgs);
  }
}
