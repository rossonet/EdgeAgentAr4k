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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

/**
 * Classe main per avvio Agente Ar4k as is
 *
 * @author Andrea Ambrosini
 */
@SpringBootApplication
@ComponentScan("org.ar4k.agent")
public class Ar4kAgentWatson {
	static final boolean running = true;

	public static void main(String[] args) {
		final String[] disabledCommands = { "--spring.shell.command.quit.enabled=false" };
		final String[] fullArgs = StringUtils.concatenateStringArrays(args, disabledCommands);
		SpringApplication.run(Ar4kAgentWatson.class, fullArgs);
	}

}