/**
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
package org.ar4k.agent.bootstrap;

import javax.validation.Valid;

import org.ar4k.agent.bootstrap.recipes.BootstrapViaAws;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaLocalConsole;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaLocalDocker;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaLocalVirtualBox;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaRossonet;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaSshConsole;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaSshDocker;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaSshVirtualBox;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.archives.GitArchive;
import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia di bootstrap sistema
 */

@ShellCommandGroup("_Bootstrap Commands")
@ShellComponent
@RestController
@RequestMapping("/bootStrapInterface")
public class BootstrapShellInterface extends AbstractShellHelper implements AutoCloseable {

	public enum BootstrapSupport {
		LOCAL_CONSOLE(new BootstrapViaLocalConsole()), LOCAL_DOCKER(new BootstrapViaLocalDocker()),
		LOCAL_VIRTUALBOX(new BootstrapViaLocalVirtualBox()), SSH_CONSOLE(new BootstrapViaSshConsole()),
		SSH_DOCKER(new BootstrapViaSshDocker()), SSH_VIRTUALBOX(new BootstrapViaSshVirtualBox()),
		AWS(new BootstrapViaAws()), ROSSONET(new BootstrapViaRossonet());

		private final BootstrapRecipe bootstrapRecipe;

		BootstrapSupport(BootstrapRecipe bootExecutor) {
			bootExecutor.setShellInterface(Homunculus.getApplicationContext().getBean(BootstrapShellInterface.class));
			bootstrapRecipe = bootExecutor;
		}

		public BootstrapRecipe getBootstrapRecipe() {
			return bootstrapRecipe;
		}
	}

	private BootstrapSupport bootstrapSupport = null;

	@Autowired
	private Homunculus homunculus;

	private KeystoreConfig masterKeystore = null;

	private GitArchive templateProject = GitArchive
			.fromHttpUrl("https://github.com/rossonet/TemplateEdgeAgentAr4k.git");

	private ManagedArchives runningProject = null;

	protected Availability authRequired() {
		return (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe().isAuthRequired())
				? Availability.available()
				: Availability.unavailable(bootstrapSupport.getBootstrapRecipe().descriptionAuthenticationRequired());
	}

	protected Availability endPointRequired() {
		return (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe().isEndPointRequired())
				? Availability.available()
				: Availability.unavailable(bootstrapSupport.getBootstrapRecipe().descriptionEndPointRequired());
	}

	protected Availability masterKeystoreRequired() {
		return (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe().isMasterKeystoreRequired())
				? Availability.available()
				: Availability.unavailable(bootstrapSupport.getBootstrapRecipe().descriptionMasterKeystoreRequired());
	}

	protected Availability runningArchiveRequired() {
		return (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe().isRunningArchiveRequired())
				? Availability.available()
				: Availability.unavailable(bootstrapSupport.getBootstrapRecipe().descriptionRunningArchiveRequired());
	}

	@ShellMethod(value = "Set bootstrap method")
	@ManagedOperation
	public void setBootstrapMethod(@ShellOption(help = "the bootstrap method") BootstrapSupport method) {
		bootstrapSupport = method;
	}

	@ShellMethod(value = "Set template repository")
	@ManagedOperation
	public void setTemplateRepository(@ShellOption(help = "the template repository url") String gitUrl) {
		templateProject = GitArchive.fromHttpUrl("https://github.com/rossonet/TemplateEdgeAgentAr4k.git");
	}

	@ShellMethod(value = "Set runtime repository")
	@ManagedOperation
	public void setRuntimeRepository(@ShellOption(help = "the bootstrap method") String url) {
		runningProject = method;
	}

	@ShellMethod(value = "Generate master keystore")
	@ManagedOperation
	public void generateBootstrapKeystore(@ShellOption(optOut = true) @Valid KeystoreConfig keyStore) {
		masterKeystore = keyStore;
	}

	public KeystoreConfig getMasterKeystore() {
		return masterKeystore;
	}

	public GitArchive getTemplateProject() {
		return templateProject;
	}

	public ManagedArchives getRunningProject() {
		return runningProject;
	}

	@Override
	public void close() throws Exception {
		if (bootstrapSupport != null) {
			bootstrapSupport.getBootstrapRecipe().cleanAll();
		}
	}

	public Homunculus getHomunculus() {
		return homunculus;
	}

}
