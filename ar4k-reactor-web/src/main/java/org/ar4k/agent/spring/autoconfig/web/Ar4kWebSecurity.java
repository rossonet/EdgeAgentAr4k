package org.ar4k.agent.spring.autoconfig.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFluxSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
public class Ar4kWebSecurity {

  // @Autowired
  // PasswordEncoder passwordEncoder;

  //@Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
    http.headers().frameOptions().mode(Mode.SAMEORIGIN);
    return http.authorizeExchange().anyExchange().authenticated().and().httpBasic().and().logout()
        .logoutUrl("/logout").and().build();
  }
  
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf().disable().authorizeExchange().anyExchange().permitAll();
    return http.build();
  }

}
