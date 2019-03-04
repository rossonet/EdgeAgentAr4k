package org.ar4k.agent.spring.autoconfig.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;

@Configuration
@EnableWebFluxSecurity
@ConditionalOnProperty(name = "ar4k.web", havingValue = "true")
public class Ar4kWebSecurity {

	@Bean
	public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		http.headers().frameOptions().mode(Mode.SAMEORIGIN);
		return http.csrf().disable().authorizeExchange().anyExchange().authenticated().and().httpBasic().and().logout()
				.logoutUrl("/logout").and().build();
	}

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		// System.out.println("MapReactiveUserDetailsService checked");
		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("h_5Ee!.")).roles("ADMIN")
				.build();
		UserDetails andrea = User.withUsername("andrea").password(passwordEncoder().encode("h_5Ee!."))
				.roles("ADMIN,ROLE_GUEST").build();
		UserDetails guest = User.withUsername("guest").password(passwordEncoder().encode("h_5Ee!."))
				.roles("ADMIN,ROLE_GUEST").build();
		return new MapReactiveUserDetailsService(andrea, admin, guest);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
