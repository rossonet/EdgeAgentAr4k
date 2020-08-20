/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.console;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * Classe main per avvio Agente Ar4k as is
 *
 * @author Andrea Ambrosini
 */
@SpringBootApplication
@ComponentScan("org.ar4k.agent")
public class Ar4kAgent {

	static final boolean running = true;

	public static void main(String[] args) {
		final String[] disabledCommands = { "--spring.shell.command.quit.enabled=false" };
		final String[] fullArgs = StringUtils.concatenateStringArrays(args, disabledCommands);
		final SpringApplication app = new SpringApplication(Ar4kAgent.class);
		// app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(fullArgs);
		final Ar4kUserDetails u = new Ar4kUserDetails();
		u.setUsername("admin");
		u.setPassword(Anima.getApplicationContext().getBean(PasswordEncoder.class).encode("admin"));
		final List<SimpleGrantedAuthority> a = new ArrayList<>();
		for (final String p : "ROLE_ADMIN,ROLE_USER".split(",")) {
			final SimpleGrantedAuthority g = new SimpleGrantedAuthority(p);
			a.add(g);
		}
		u.setAuthorities(a);
		Anima.getApplicationContext().getBean(Anima.class).getLocalUsers().add(u);
	}

}
