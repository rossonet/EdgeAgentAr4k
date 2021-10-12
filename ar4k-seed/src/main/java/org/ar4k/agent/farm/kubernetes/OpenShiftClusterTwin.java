package org.ar4k.agent.farm.kubernetes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.ar4k.agent.helper.IOUtils;
import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class OpenShiftClusterTwin {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(OpenShiftClusterTwin.class);

	private static final String BASE_DOMAIN_TAG = "<!base-domain!>";
	private static final String DOMAIN_TAG = "<!domain!>";
	private static final String SSH_KEY_TAG = "<!ssh-key!>";
	private static final String SERVER_WEB_TAG = "<!server-web!>";

	private static final String NETWORK_TAG = "<!network!>";

	private static final String NETWORK_MASK_TAG = "<!netmask!>";

	private static final String NETWORK_BROADCAST_TAG = "<!broadcast!>";

	private static final String NETWORK_INTERNAL_DNS_TAG = "<!dns!>";

	private static final String NETWORK_ROUTER_TAG = "<!router!>";

	private static final String DHCP_HOSTS_DATA_PLACEHOLDER = "<!dhcp-hosts!>";

	private static final String DHCP_LEFT_IP_RANGE_TAG = "<!ip-range-left!>";

	private static final String HOST_SINGLE_NAME_TAG = "<!single-host-name!>";

	private static final String HOST_SINGLE_MAC_ADDRESS_TAG = "<!single-host-mac-address!>";

	private static final String HOST_SINGLE_TYPE_TAG = "<!single-host-type!>";

	private static final String DHCP_RIGHT_IP_RANGE_TAG = "<!ip-range-right!>";

	private static final int IP_RANGE_IN_DHCP = 10;

	public enum NodeType {
		CONTROL_NODE("master"), WORKER_NODE("worker"), BOOTSTRAP_NODE("boot");

		private final String ipxeFileName;

		NodeType(String ipxeFileName) {
			this.ipxeFileName = ipxeFileName;
		}

		String getIpxeFileName() {
			return ipxeFileName;
		}
	}

	public enum PhisicalType {
		BARE_METAL, LOCAL_SINGLE_HOST, AWS, AZURE, JUST_CONNECTION
	}

	private final PhisicalType phisicalType;
	private final String uniqueName;
	private final Map<String, ClusterNode> nodes = new HashMap<>();
	private String publicKey = null;
	private String baseDomain = null;
	private String domain = null;
	private String consoleIp = null;
	private String network = null;
	private String mask = null;
	private String dns1 = null;
	private String dns2 = null;

	private File workingDirectory = null;

	private StringBuilder completeInstallLog = null;

	public OpenShiftClusterTwin(PhisicalType phisicalType, String uniqueName) {
		this.phisicalType = phisicalType;
		this.uniqueName = uniqueName;
	}

	public String getHumanDescription() {
		return uniqueName + " -> " + phisicalType.toString()
				+ (completeInstallLog != null ? "\n" + completeInstallLog.toString() : "");
	}

	public String prepareEnviroment() {
		completeInstallLog = new StringBuilder();
		try {
			installSystemPackages();
			createWorkingDirectory();
			downloadDependencies();
			createOpenShiftConfigFile();
			createIpxeFile("boot.ipxe");
			createIpxeFile("master.ipxe");
			createIpxeFile("worker.ipxe");
			createDhcpConfig();
			createNamedConf();
			createNamedReverseZone();
			createNamedLocalZone();
			createHaProxyConfig();
			runConfigurationScript();
		} catch (IOException | InterruptedException exception) {
			logger.error("installing openshift enviroment", exception);
			completeInstallLog.append("EXCEPTION\n" + EdgeLogger.stackTraceToString(exception));
		}
		return completeInstallLog.toString();
	}

	private void createDhcpConfig() throws FileNotFoundException {
		String dhcpdConfigTemplate = IOUtils.readResourceFileToString("classpath:openshift/dhcpd.conf");
		dhcpdConfigTemplate.replace(NETWORK_TAG, getNetwork());
		dhcpdConfigTemplate.replace(NETWORK_MASK_TAG, getMask());
		dhcpdConfigTemplate.replace(DHCP_LEFT_IP_RANGE_TAG, getLeftDhcpLimit());
		dhcpdConfigTemplate.replace(DHCP_RIGHT_IP_RANGE_TAG, getRightDhcpLimit());
		dhcpdConfigTemplate.replace(NETWORK_BROADCAST_TAG, getBrodcastForNetwork());
		dhcpdConfigTemplate.replace(NETWORK_ROUTER_TAG, getConsoleIp());
		dhcpdConfigTemplate.replace(NETWORK_INTERNAL_DNS_TAG, getConsoleIp());
		final String hostsPart = generateDhcpHostsPart();
		dhcpdConfigTemplate.replace(DHCP_HOSTS_DATA_PLACEHOLDER, hostsPart);
		File dhcpdConfigFile = new File(workingDirectory.getAbsolutePath() + "/dhcpd.conf");
		IOUtils.writeStringToFile(dhcpdConfigTemplate, dhcpdConfigFile);
	}

	private String generateDhcpHostsPart() {
		StringBuilder sb = new StringBuilder();
		final String dhcpdSingleHostConfigTemplate = IOUtils
				.readResourceFileToString("classpath:openshift/dhcpd-host.conf");
		for (ClusterNode singleNode : nodes.values()) {
			String nodePart = dhcpdSingleHostConfigTemplate;
			nodePart.replace(HOST_SINGLE_NAME_TAG, singleNode.getName());
			nodePart.replace(HOST_SINGLE_MAC_ADDRESS_TAG, singleNode.getMac());
			nodePart.replace(BASE_DOMAIN_TAG, getBaseDomain());
			nodePart.replace(DOMAIN_TAG, getDomain());
			nodePart.replace(SERVER_WEB_TAG, getConsoleIp());
			nodePart.replace(HOST_SINGLE_TYPE_TAG, singleNode.getType());
			sb.append(nodePart);
		}
		return sb.toString();
	}

	private String getBrodcastForNetwork() {
		return NetworkHelper.getBrodcastForNetwork(getNetwork(), getMask());
	}

	private String getRightDhcpLimit() {
		int counter = 0;
		for (String singleIp : NetworkHelper.getAllIpsInNetworkSorted(getNetwork(), getMask())) {
			if (!singleIp.equals(getConsoleIp())) {
				if (counter > IP_RANGE_IN_DHCP) {
					return singleIp;
				} else {
					counter++;
				}
			}
		}
		throw new InvalidParameterException(
				"for nework " + getNetwork() + " with mask " + getMask() + " the last ip of dhcp doesn't exist");
	}

	private String getLeftDhcpLimit() {
		for (String singleIp : NetworkHelper.getAllIpsInNetworkSorted(getNetwork(), getMask())) {
			if (!singleIp.equals(getConsoleIp())) {
				return singleIp;
			}
		}
		throw new InvalidParameterException(
				"for nework " + getNetwork() + " with mask " + getMask() + " the first ip of dhcp doesn't exist");
	}

	private void createHaProxyConfig() {
		// TODO Auto-generated method stub

	}

	private void createNamedLocalZone() {
		// TODO Auto-generated method stub

	}

	private void createNamedReverseZone() {
		// TODO Auto-generated method stub

	}

	private void createNamedConf() {
		// TODO Auto-generated method stub

	}

	private void createIpxeFile(String ipxeFile) throws FileNotFoundException {
		String ipxeConfigTemplate = IOUtils.readResourceFileToString("classpath:openshift/" + ipxeFile);
		ipxeConfigTemplate.replace(SERVER_WEB_TAG, getConsoleIp());
		File ipxeConfigFile = new File(workingDirectory.getAbsolutePath() + "/install-config.yaml");
		IOUtils.writeStringToFile(ipxeConfigTemplate, ipxeConfigFile);
	}

	private void createOpenShiftConfigFile() throws FileNotFoundException {
		String openShiftConfigTemplate = IOUtils.readResourceFileToString("classpath:openshift/install-config.yaml");
		openShiftConfigTemplate.replace(BASE_DOMAIN_TAG, getBaseDomain());
		openShiftConfigTemplate.replace(DOMAIN_TAG, getDomain());
		openShiftConfigTemplate.replace(SSH_KEY_TAG, getPublicKey());
		File openShiftConfigFile = new File(workingDirectory.getAbsolutePath() + "/install-config.yaml");
		IOUtils.writeStringToFile(openShiftConfigTemplate, openShiftConfigFile);
	}

	private void runConfigurationScript() throws IOException, InterruptedException {
		String configurationScript = IOUtils.readResourceFileToString("classpath:openshift/configure_all_template.sh");
		File configurationScriptFile = new File(workingDirectory.getAbsolutePath() + "/configure.sh");
		IOUtils.writeStringToFile(configurationScript, configurationScriptFile);
		configurationScriptFile.setExecutable(true);
		String[] downloadCommand = new String[] { configurationScriptFile.getAbsolutePath() };
		Process process = IOUtils.runCommand(downloadCommand, workingDirectory.getAbsolutePath(), completeInstallLog);
		process.waitFor();
	}

	private void downloadDependencies() throws IOException, InterruptedException {
		String downloadScript = IOUtils.readResourceFileToString("classpath:openshift/download_all_template.sh");
		File downloadScriptFile = new File(workingDirectory.getAbsolutePath() + "/download.sh");
		IOUtils.writeStringToFile(downloadScript, downloadScriptFile);
		downloadScriptFile.setExecutable(true);
		String[] downloadCommand = new String[] { downloadScriptFile.getAbsolutePath() };
		Process process = IOUtils.runCommand(downloadCommand, workingDirectory.getAbsolutePath(), completeInstallLog);
		process.waitFor();
	}

	private void createWorkingDirectory() throws IOException {
		String openShiftDirectoryName = System.getProperty("user.home") + "/openshift-" + UUID.randomUUID().toString();
		workingDirectory = new File(openShiftDirectoryName);
		if (workingDirectory.exists()) {
			IOUtils.deleteDirectory(workingDirectory);
		}
		Files.createDirectories(workingDirectory.toPath());
	}

	private void installSystemPackages() throws IOException, InterruptedException {
		String[] installCommand = new String[] { "sudo", "dnf", "install", "bind", "bind-utils", "haproxy",
				"dhcp-server", "tar", "wget", "httpd", "iptables", "-y" };
		Process process = IOUtils.runCommand(installCommand, "~", completeInstallLog);
		process.waitFor();
	}

	public void setSshPublicKey(String publicKey) {
		if (publicKey == null) {
			throw new InvalidParameterException("publicKey is null");
		}
		this.publicKey = publicKey;
	}

	public void setBaseDomain(String baseDomain) {
		if (baseDomain == null) {
			throw new InvalidParameterException("baseDomain is null");
		}
		this.baseDomain = baseDomain;

	}

	public void setDomain(String domain) {
		if (domain == null) {
			throw new InvalidParameterException("domain is null");
		}
		this.domain = domain;

	}

	public void setClusterNetworkDetails(String consoleIp, String network, String mask, String dns1, String dns2,
			String consolePublicIp) {
		if (!NetworkHelper.isValidIPAddress(consoleIp)) {
			throw new InvalidParameterException(consoleIp + " is not a valid ip");
		}
		this.consoleIp = consoleIp;
		if (!NetworkHelper.isValidIPAddress(network)) {
			throw new InvalidParameterException(network + " is not a valid ip");
		}
		this.network = network;
		if (!NetworkHelper.isValidSubnetMask(mask)) {
			throw new InvalidParameterException(mask + " is not a valid network mask");
		}
		this.mask = mask;
		if (!NetworkHelper.isValidIPAddress(dns1)) {
			throw new InvalidParameterException(dns1 + " is not a valid ip");
		}
		this.dns1 = dns1;
		if (!NetworkHelper.isValidIPAddress(dns2)) {
			throw new InvalidParameterException(dns2 + " is not a valid ip");
		}
		this.dns2 = dns2;
	}

	public void addMacAddressAndIp(NodeType nodeType, String macAddress, String ip, String name) {
		if (!NetworkHelper.isValidIPAddress(ip)) {
			throw new InvalidParameterException(ip + " is not a valid ip");
		}
		if (!NetworkHelper.isValidMacAddress(macAddress)) {
			throw new InvalidParameterException(macAddress + " is not a valid mac address");
		}
		if (domain == null) {
			throw new InvalidParameterException("name is null");
		}
		nodes.put(macAddress, new ClusterNode(nodeType, macAddress, ip, name));
	}

	public boolean isConfigured() {
		return (checkBaseParameters() && checkOneBootstrapNode() && checkControllerNodes());
	}

	private boolean checkControllerNodes() {
		int total = 0;
		for (ClusterNode n : nodes.values()) {
			if (n.getNodeType().equals(NodeType.CONTROL_NODE)) {
				total++;
			}
		}
		return total >= 3;
	}

	private boolean checkOneBootstrapNode() {
		for (ClusterNode n : nodes.values()) {
			if (n.getNodeType().equals(NodeType.BOOTSTRAP_NODE)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkBaseParameters() {
		return publicKey != null && baseDomain != null && domain != null && consoleIp != null && network != null
				&& mask != null && dns1 != null && dns2 != null;
	}

	public String getConfigureWarning() {
		// TODO Auto-generated method stub
		return "complete the class first";
	}

	public String getPublicDnsZone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpenShiftClusterTwin [");
		if (phisicalType != null) {
			builder.append("phisicalType=");
			builder.append(phisicalType);
			builder.append(", ");
		}
		if (uniqueName != null) {
			builder.append("uniqueName=");
			builder.append(uniqueName);
		}
		builder.append("configured=");
		builder.append(isConfigured());
		builder.append("]");
		return builder.toString();
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getConsoleIp() {
		return consoleIp;
	}

	public void setConsoleIp(String consoleIp) {
		this.consoleIp = consoleIp;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns2() {
		return dns2;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public PhisicalType getPhisicalType() {
		return phisicalType;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public Map<String, ClusterNode> getNodes() {
		return nodes;
	}

	public String getBaseDomain() {
		return baseDomain;
	}

	public String getDomain() {
		return domain;
	}

}
