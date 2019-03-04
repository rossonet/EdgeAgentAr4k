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
package org.ar4k.agent.spring.autoconfig;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.spring.Ar4kHealthIndicator;
import org.ar4k.agent.spring.autoconfig.web.Ar4kWebFluxConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(Anima.class)
@EnableConfigurationProperties(Ar4kStarterProperties.class)
@Import(Ar4kWebFluxConfiguration.class)

/**
 * Classe starter per inclusione come libreria in ambiente Spring Boot.
 * 
 * @author Andrea Ambrosini
 */
public class Ar4kAutoConfiguration {

	@SuppressWarnings("unused")
	@Autowired
	private Ar4kStarterProperties properties;

	@Bean
	@ConditionalOnBean(Anima.class)
	Ar4kHealthIndicator ar4kHealthIndicator() {
		return new Ar4kHealthIndicator();
	}
}
