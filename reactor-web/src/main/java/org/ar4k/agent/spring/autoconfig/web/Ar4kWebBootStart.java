package org.ar4k.agent.spring.autoconfig.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.ar4k.agent")
public class Ar4kWebBootStart {

  public static void main(String[] args) {
    SpringApplication.run(Ar4kWebBootStart.class, args);
  }
}
