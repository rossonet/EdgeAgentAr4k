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

import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.helper.AbstractShellHelper;
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
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.org
 */

@ShellCommandGroup("OpenShift Commands")
@ShellComponent
@RestController
@RequestMapping("/openShiftInterface")
public class OpenShiftShellInterface extends AbstractShellHelper {

	private final Map<String, OpenShiftClusterTwin> openShiftClusterTwins = new HashMap<>();

	private OpenShiftClusterTwin selectedOpenShiftClusterTwin = null;

	protected Availability atLeastOneOpenShiftTwin() {
		return (!openShiftClusterTwins.isEmpty()) ? Availability.available()
				: Availability.unavailable("please create a OpenShift cluster twin");
	}

	protected Availability clusterTwinSelected() {
		return (selectedOpenShiftClusterTwin != null) ? Availability.available()
				: Availability.unavailable("please select a OpenShift cluster twin");
	}

	protected Availability clusterTwinSelectedAndConfigured() {
		return (selectedOpenShiftClusterTwin != null && selectedOpenShiftClusterTwin.isConfigured())
				? Availability.available()
				: Availability.unavailable(
						selectedOpenShiftClusterTwin == null ? "please select a OpenShift cluster twin and configure it"
								: selectedOpenShiftClusterTwin.getConfigureWarning());
	}

	@ShellMethod(value = "List OpenShift Cluster Twins")
	@ManagedOperation
	@ShellMethodAvailability("atLeastOneOpenShiftTwin")
	public Map<String, OpenShiftClusterTwin> listOpenshiftTwins() {
		return openShiftClusterTwins;
	}

	@ShellMethod(value = "Add new OpenShift Cluster Twin for bare metal installation")
	@ManagedOperation
	public void addOpenshiftTwin(@ShellOption(help = "unique name for OpenShift cluster") String uniqueName) {
		openShiftClusterTwins.put(uniqueName,
				new OpenShiftClusterTwin(OpenShiftClusterTwin.PhisicalType.BARE_METAL, uniqueName));
	}

	@ShellMethod(value = "Delete OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("atLeastOneOpenShiftTwin")
	public void deleteOpenshiftTwin(@ShellOption(help = "unique name for OpenShift cluster") String uniqueName) {
		openShiftClusterTwins.remove(uniqueName);
	}

	@ShellMethod(value = "Select a OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("atLeastOneOpenShiftTwin")
	public String selectOpenshiftTwin(@ShellOption(help = "unique name for OpenShift cluster") String uniqueName) {
		selectedOpenShiftClusterTwin = openShiftClusterTwins.get(uniqueName);
		return selectedOpenShiftClusterTwin.getHumanDescription();
	}

	@ShellMethod(value = "Describe selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public String describeSelectedOpenshiftTwin() {
		return selectedOpenShiftClusterTwin.getHumanDescription();
	}

	@ShellMethod(value = "Prepare enviroment to install selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelectedAndConfigured")
	public String prepareEnviromentForSelectedOpenshiftTwin() {
		selectedOpenShiftClusterTwin.prepareEnviroment();
		return selectedOpenShiftClusterTwin.getHumanDescription();
	}

	@ShellMethod(value = "Get public dns data of OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelectedAndConfigured")
	public String getPublicDnsZoneSelectedOpenshiftTwin() {
		return selectedOpenShiftClusterTwin.getPublicDnsZone();
	}

	@ShellMethod(value = "Set SSH public key for selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void setPublicSshKeyForSelectedOpenshiftTwin(@ShellOption(help = "ssh public key") String publicKey) {
		selectedOpenShiftClusterTwin.setSshPublicKey(publicKey);
	}

	@ShellMethod(value = "Set baseDomain for selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void setBaseDomainForSelectedOpenshiftTwin(@ShellOption(help = "OpenShift baseDomain") String baseDomain) {
		selectedOpenShiftClusterTwin.setBaseDomain(baseDomain);
	}

	@ShellMethod(value = "Set domain for selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void setDomainForSelectedOpenshiftTwin(@ShellOption(help = "OpenShift domain") String domain) {
		selectedOpenShiftClusterTwin.setDomain(domain);
	}

	@ShellMethod(value = "Set cluster network for selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void setNetworkForSelectedOpenshiftTwin(
			@ShellOption(help = "console node internal ip") String consoleInternalIp,
			@ShellOption(help = "cluster network") String network, @ShellOption(help = "cluster mask") String mask,
			@ShellOption(help = "first external dns") String dns1,
			@ShellOption(help = "second external dns") String dns2,
			@ShellOption(help = "console node public ip") String consolePublicIp) {
		selectedOpenShiftClusterTwin.setClusterNetworkDetails(consoleInternalIp, network, mask, dns1, dns2,
				consolePublicIp);
	}

	@ShellMethod(value = "add control node mac address and ip to selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void addControlNodeToSelectedOpenshiftTwin(@ShellOption(help = "node mac address") String mac,
			@ShellOption(help = "node ip") String ip) {
		selectedOpenShiftClusterTwin.addMacAddressAndIp(OpenShiftClusterTwin.NodeType.CONTROL_NODE, mac, ip);
	}

	@ShellMethod(value = "add bootstrap node mac address and ip to selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void addBootstrapNodeToSelectedOpenshiftTwin(@ShellOption(help = "node mac address") String mac,
			@ShellOption(help = "node ip") String ip) {
		selectedOpenShiftClusterTwin.addMacAddressAndIp(OpenShiftClusterTwin.NodeType.BOOTSTRAP_NODE, mac, ip);
	}

	@ShellMethod(value = "add worker node mac address and ip to selected OpenShift Cluster Twin")
	@ManagedOperation
	@ShellMethodAvailability("clusterTwinSelected")
	public void addWorkerNodeToSelectedOpenshiftTwin(@ShellOption(help = "node mac address") String mac,
			@ShellOption(help = "node ip") String ip) {
		selectedOpenShiftClusterTwin.addMacAddressAndIp(OpenShiftClusterTwin.NodeType.WORKER_NODE, mac, ip);
	}

}
