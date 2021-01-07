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
package org.ar4k.agent.farm.openshift;

import java.util.List;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openshift.client.ConnectionBuilder;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.IQuickstart;
import com.openshift.client.cartridge.ICartridge;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia gestione openshift.
 */

@ShellCommandGroup("OpenShift Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=openshiftInterface", description = "Ar4k Agent OpenShift Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "openshiftInterface")
@RestController
@RequestMapping("/openshiftInterface")
//TODO implementare OpenShiftShellInterface e servizi come per docker e k8s
public class OpenShiftShellInterface extends AbstractShellHelper {

	@Autowired
	Shell shell;
	IOpenShiftConnection connection = null;

	protected Availability testOpenshiftApiClientNull() {
		return (connection == null) ? Availability.available()
				: Availability.unavailable(
						"the OpenShift client is already connected to " + connection != null ? connection.toString()
								: "NaN");
	}

	protected Availability testOpenshiftApiClientRunning() {
		return (connection != null) ? Availability.available()
				: Availability.unavailable("the OpenShift client is not connected");
	}

	@ShellMethod(value = "Connect to OpenShift cluster with username and password", group = "OpenShift Commands")
	@ManagedOperation
	@ShellMethodAvailability("testOpenshiftApiClientNull")
	public void connectToOpenshift(@ShellOption(help = "server URL") String serverUrl,
			@ShellOption(help = "the username for the login") String apiUsername,
			@ShellOption(help = "the password for the login") String apiPassword) {
		try {
			connection = new ConnectionBuilder(serverUrl).credentials(apiUsername, apiPassword).create();
		} catch (final Exception e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "Connect to OpenShift cluster with username and password", group = "OpenShift Commands")
	@ManagedOperation
	@ShellMethodAvailability("testOpenshiftApiClientNull")
	public void connectToOpenshiftWithToken(@ShellOption(help = "server URL") String serverUrl,
			@ShellOption(help = "the token for the login") String token) {
		try {
			connection = new ConnectionBuilder(serverUrl).token(token).create();
		} catch (final Exception e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "Connect to OpenShift cluster with username and password", group = "OpenShift Commands")
	@ManagedOperation
	@ShellMethodAvailability("testOpenshiftApiClientNull")
	public void connectToOpenshiftWithKey(@ShellOption(help = "server URL") String serverUrl,
			@ShellOption(help = "the auth for the login") String auth,
			@ShellOption(help = "the authKey for the login") String authKey) {
		try {
			connection = new ConnectionBuilder(serverUrl).key(auth, authKey).create();
		} catch (final Exception e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "List OpenShift cartridges", group = "OpenShift Commands")
	@ManagedOperation
	@ShellMethodAvailability("testOpenshiftApiClientRunning")
	public List<ICartridge> listCartridges() {
		try {
			return connection.getCartridges();
		} catch (final Exception e) {
			logger.logException(e);
			return null;
		}
	}

	@ShellMethod(value = "List OpenShift quick starts", group = "OpenShift Commands")
	@ManagedOperation
	@ShellMethodAvailability("testOpenshiftApiClientRunning")
	public List<IQuickstart> listQuickstarts() {
		try {
			return connection.getQuickstarts();
		} catch (final Exception e) {
			logger.logException(e);
			return null;
		}
	}

}
