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

import java.lang.reflect.InvocationTargetException;

import javax.validation.Valid;

import org.ar4k.agent.bootstrap.recipes.BootstrapViaAws;
import org.ar4k.agent.bootstrap.recipes.BootstrapViaAzure;
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
import org.ar4k.agent.exception.DriverClassNotFoundException;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
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

	private static final String DEFAULT_CONFIGURATION_APTH = "./seed-config.bin";
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BootstrapShellInterface.class.toString());

	public enum BootstrapSupport {
		LOCAL_CONSOLE(new BootstrapViaLocalConsole()), LOCAL_DOCKER(new BootstrapViaLocalDocker()),
		LOCAL_VIRTUALBOX(new BootstrapViaLocalVirtualBox()), SSH_CONSOLE(new BootstrapViaSshConsole()),
		SSH_DOCKER(new BootstrapViaSshDocker()), SSH_VIRTUALBOX(new BootstrapViaSshVirtualBox()),
		AWS(new BootstrapViaAws()), AZURE(new BootstrapViaAzure()), ROSSONET(new BootstrapViaRossonet());

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

	private GitArchive templateProject = null;

	private ManagedArchives runningProject = null;

	protected Availability mayStart() {
		return (bootstrapSupport != null && !bootstrapSupport.getBootstrapRecipe().isSetupRequired()
				&& !bootstrapSupport.getBootstrapRecipe().isStarted()) ? Availability.available()
						: Availability.unavailable("setup the enviroment before");
	}

	protected Availability mayStop() {
		return (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe().isStarted())
				? Availability.available()
				: Availability.unavailable("the server is not started");
	}

	protected Availability mayClean() {
		return (bootstrapSupport != null) ? Availability.available() : Availability.unavailable("no objects to clean");
	}

	protected Availability completedConfigurationRequired() {
		if (bootstrapSupport != null && !bootstrapSupport.getBootstrapRecipe().isProviderEndpointAndAuthRequired()
				&& !bootstrapSupport.getBootstrapRecipe().isRunningArchiveRequired()
				&& !bootstrapSupport.getBootstrapRecipe().isMasterKeystoreRequired())
			return Availability.available();
		else {
			StringBuilder sb = new StringBuilder();
			if (bootstrapSupport == null) {
				sb.append("set bootstrap method before");
			} else {
				if (bootstrapSupport.getBootstrapRecipe().isProviderEndpointAndAuthRequired()) {
					sb.append(" " + bootstrapSupport.getBootstrapRecipe().descriptionProviderEndpointAndAuthRequired());
				}
				if (bootstrapSupport.getBootstrapRecipe().isMasterKeystoreRequired()) {
					sb.append(" " + bootstrapSupport.getBootstrapRecipe().descriptionMasterKeystoreRequired());
				}
				if (bootstrapSupport.getBootstrapRecipe().isRunningArchiveRequired()) {
					sb.append(" " + bootstrapSupport.getBootstrapRecipe().descriptionRunningArchiveRequired());
				}
			}
			return Availability.unavailable(sb.toString());
		}
	}

	@ShellMethod(value = "Setup enviroment")
	@ManagedOperation
	@ShellMethodAvailability("completedConfigurationRequired")
	public void setupBootstrapEnviroment(
			@ShellOption(help = "web server port for Beacon agent", defaultValue = "8442") String serverPort,
			@ShellOption(help = "keystore path", defaultValue = "keys/master.ks") String keystoreFile,
			@ShellOption(help = "master alias in keystore", defaultValue = "master") String keystoreCa,
			@ShellOption(help = "Beacon server alias in keystore", defaultValue = "master") String keystoreBeacon,
			@ShellOption(help = "admin password of the agent", defaultValue = "password") String adminPassword,
			@ShellOption(help = "Beacon discovery port", defaultValue = "8444") String discoveryPort,
			@ShellOption(help = "port of Beacon service", defaultValue = "8443") String beaconserverPort) {
		bootstrapSupport.getBootstrapRecipe().setUp(serverPort, keystoreFile, keystoreCa, keystoreBeacon, adminPassword,
				discoveryPort, beaconserverPort);
	}

	@ShellMethod(value = "Configure provider auth and endpoint")
	@ManagedOperation
	@ShellMethodAvailability("mayClean")
	public void configureBootstrapProvider() {
		bootstrapSupport.getBootstrapRecipe().shellProviderEndpointAndAuth();
	}

	@ShellMethod(value = "Save this configuration")
	@ManagedOperation
	@ShellMethodAvailability("mayClean")
	public void saveBootstrapConfiguration(
			@ShellOption(help = "configuration file path", defaultValue = DEFAULT_CONFIGURATION_APTH) String seedConfigFile) {
		bootstrapSupport.getBootstrapRecipe().saveConfiguration(seedConfigFile);
	}

	@ShellMethod(value = "Load this configuration")
	@ManagedOperation
	@ShellMethodAvailability("mayClean")
	public void loadBootstrapConfiguration(
			@ShellOption(help = "configuration file path", defaultValue = DEFAULT_CONFIGURATION_APTH) String seedConfigFile) {
		bootstrapSupport.getBootstrapRecipe().loadConfiguration(seedConfigFile);
	}

	@ShellMethod(value = "Start Beacon server")
	@ManagedOperation
	@ShellMethodAvailability("mayStart")
	public void startBootstrapEnviroment() {
		bootstrapSupport.getBootstrapRecipe().start();
	}

	@ShellMethod(value = "Stop Beacon server")
	@ManagedOperation
	@ShellMethodAvailability("mayStop")
	public void stopBootstrapEnviroment() {
		bootstrapSupport.getBootstrapRecipe().stop();
	}

	@ShellMethod(value = "Clean enviroment")
	@ManagedOperation
	@ShellMethodAvailability("mayClean")
	public void cleanBootstrapEnviroment() {
		bootstrapSupport.getBootstrapRecipe().destroy();
		bootstrapSupport = null;
		masterKeystore = null;
		templateProject = null;
		runningProject = null;
	}

	@ShellMethod(value = "Set bootstrap method")
	@ManagedOperation
	public void setBootstrapMethod(@ShellOption(help = "the bootstrap method") BootstrapSupport method) {
		bootstrapSupport = method;
	}

	@ShellMethod(value = "Get bootstrap status")
	@ManagedOperation
	public String getBootstrapStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append("BOOTSTRAP WORKFLOW: ");
		sb.append(bootstrapSupport + "\n");
		sb.append("PROJECT TEMPLATE: ");
		sb.append(templateProject + "\n");
		sb.append("PROJECT STORAGE: ");
		sb.append(runningProject + "\n");
		sb.append("PROJECT MASTER KEYSTORE: ");
		sb.append(masterKeystore + "\n");
		if (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe() != null) {
			sb.append("SETUP REQUIRED: ");
			sb.append(bootstrapSupport.getBootstrapRecipe().isSetupRequired() + "\n");
		}
		if (bootstrapSupport != null && bootstrapSupport.getBootstrapRecipe() != null) {
			sb.append("ENDPOINT AND AUTH REQUIRED: ");
			sb.append(bootstrapSupport.getBootstrapRecipe().isProviderEndpointAndAuthRequired() + "\n");
		}
		return sb.toString();
	}

	@ShellMethod(value = "Set template repository")
	@ManagedOperation
	public void setBootstrapTemplateRepository(
			@ShellOption(help = "the template repository url", defaultValue = "https://github.com/rossonet/TemplateEdgeAgentAr4k.git") String gitUrl) {
		templateProject = GitArchive.fromHttpUrl(gitUrl);
	}

	@ShellMethod(value = "List Gradle build tasks in runtime repository")
	@ManagedOperation
	@ShellMethodAvailability("mayStart")
	public String listGradleBuildTasksInRuntimeRepository() {
		return bootstrapSupport.getBootstrapRecipe().listGradleTasks();
	}

	@ShellMethod(value = "Set runtime repository")
	@ManagedOperation
	public void setBootstrapRuntimeRepository(@ShellOption(help = "the local bootstrap directory") String url,
			@ShellOption(help = "storage driver", defaultValue = "org.ar4k.agent.core.archives.LocalFileSystemArchive", valueProvider = org.ar4k.agent.spring.valueProvider.StorageTypeValuesProvider.class) String driver) {
		boolean foundDriver = false;
		try {
			runningProject = (ManagedArchives) Class.forName(driver).getConstructor().newInstance();
			runningProject.setUrl(url);
			foundDriver = true;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			logger.logException(e);
		}
		if (!foundDriver) {
			throw new DriverClassNotFoundException("Driver class " + driver + " for url " + url + " not works");
		}
	}

	@ShellMethod(value = "Set master keystore")
	@ManagedOperation
	public void setBootstrapKeystore(@ShellOption(optOut = true) @Valid KeystoreConfig keyStore,
			@ShellOption(help = "common name for the CA certificate", defaultValue = "ca.agents.ar4k.org") String commonName,
			@ShellOption(help = "company for the CA certificate", defaultValue = "Rossonet s.c.a r.l.") String organization,
			@ShellOption(help = "organization unit for the CA certificate", defaultValue = "AR4K") String unit,
			@ShellOption(help = "city for the CA certificate", defaultValue = "Imola") String locality,
			@ShellOption(help = "province for the CA certificate", defaultValue = "Bologna") String state,
			@ShellOption(help = "country for the CA certificate", defaultValue = "IT") String country,
			@ShellOption(help = "URI for the CA certificate", defaultValue = "urn:org.ar4k.agent:ca-agents") String uri,
			@ShellOption(help = "host name for the CA certificate", defaultValue = "localhost") String dns,
			@ShellOption(help = "id address for the CA certificate", defaultValue = "127.0.0.1") String ip,
			@ShellOption(help = "alias for new cert in the keystore", defaultValue = "master") String alias,
			@ShellOption(help = "validity in days", defaultValue = "365") int validityDays) {
		keyStore.create(commonName, organization, unit, locality, state, country, uri, dns, ip, alias, true,
				validityDays);
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
		// TODO valutare chiusure oggetti in shell bootstrap
	}

	public Homunculus getHomunculus() {
		return homunculus;
	}

}
