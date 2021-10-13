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
package org.ar4k.agent.farm.kubernetes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.ar4k.agent.farm.kubernetes.operator.KubernetesOperator;
import org.ar4k.agent.farm.kubernetes.operator.OperatorController;
import org.ar4k.agent.farm.kubernetes.operator.ResourceController;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per la gestione operatori k8s
 *
 */

//TODO completare gestione modulo da Operatori

@ShellCommandGroup("Kubernetes Commands")
@ShellComponent
@RestController
@RequestMapping("/kubernetesInterface")
public class KubernetesShellInterface extends AbstractShellHelper {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(KubernetesShellInterface.class);

	private Config config = null;
	private KubernetesClient client = null;
	private KubernetesOperator operator = null;

	protected Availability operatorActiveInConsole() {
		return (operator != null) ? Availability.available()
				: Availability.unavailable("please start operator interface with startK8sOperator command");
	}

	@ShellMethod(value = "Start Kubernetes operator interface")
	@ManagedOperation
	public String startK8sOperator() {
		config = new ConfigBuilder().withNamespace(null).build();
		client = new DefaultKubernetesClient(config);
		operator = new KubernetesOperator(client);
		return "Kubernetes Operator Started";
	}

	@ShellMethod(value = "List Kubernetes operator controller classes")
	@ManagedOperation
	public Set<String> listK8sOperator(
			@ShellOption(help = "package for searching", defaultValue = "org.ar4k.agent") String packageName) {
		Set<String> rit = new HashSet<>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(OperatorController.class));
		Set<BeanDefinition> classes = provider.findCandidateComponents(packageName);
		for (BeanDefinition c : classes) {
			rit.add(c.getBeanClassName());
		}
		return rit;
	}

	@ShellMethod(value = "Add controller to the active Kubernetes operator")
	@ManagedOperation
	@ShellMethodAvailability("operatorActiveInConsole")
	public void addOperatorControllerClass(@ShellOption(help = "controller class") String className) {
		try {
			operator.register(istantiateController(className));
		} catch (Exception e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "Remove controller to the active Kubernetes operator")
	@ManagedOperation
	@ShellMethodAvailability("operatorActiveInConsole")
	public void removeOperatorControllerClass(@ShellOption(help = "controller class") String className) {
		try {
			operator.remove(className);
		} catch (Exception e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "List active controllers in the Kubernetes operator")
	@ManagedOperation
	@ShellMethodAvailability("operatorActiveInConsole")
	public Collection<ResourceController> listOperatorController() {
		return operator.getControllers();
	}

	@ShellMethod(value = "Stop active Kubernetes operator")
	@ManagedOperation
	@ShellMethodAvailability("operatorActiveInConsole")
	public void stopOperator() throws Exception {
		operator.close();
		operator = null;
	}

	private ResourceController istantiateController(String className)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		final Class<?> targetClass = Class.forName(className);
		final Constructor<?> constructor = targetClass.getConstructor(KubernetesClient.class);
		final Object objectController = constructor.newInstance(client);
		if (objectController instanceof ResourceController) {
			return (ResourceController) objectController;
		} else {
			throw new ClassNotFoundException("no operator controller found");
		}
	}

	@ShellMethod(value = "Add Kubernetes operator configuration to the selected configuration")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addKubernetesOperatorToSelectedConfig(
			@ShellOption(optOut = true) @Valid KubernetesConfig kubernetesConfig) {
		getWorkingConfig().pots.add(kubernetesConfig);
	}

}
