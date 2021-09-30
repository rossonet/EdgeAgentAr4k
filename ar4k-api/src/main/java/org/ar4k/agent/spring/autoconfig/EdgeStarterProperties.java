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

import static org.ar4k.agent.spring.autoconfig.EdgeStarterProperties.AR4K_PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Proprità per bootstrap agente. Questa classe è utilizzata da
 * EdgeAutoConfiguration.
 *
 * @author Andrea Ambrosini
 *
 *
 */

@ConfigurationProperties(prefix = AR4K_PREFIX)
public class EdgeStarterProperties {

	public static final String AR4K_PREFIX = "ar4k";
	private String adminPassword = "admin123";
	private String animaDatastoreFileName = "data_map";
	private String baseConfig = "";
	private String baseConfigOrder = "3";
	private String beaconCaChainPem = "xxxxx";
	private String beaconClearText = "true";
	private String beaconDiscoveryFilterString = "AR4K";
	private String beaconDiscoveryPort = "33666";
	private String confPath = System.getProperty("user.home") + "/.ar4k";
	private String consoleOnly = "false";
	private String defaultWebPage = "/app/ar4k-console";
	private String dnsConfig = "";
	private String dnsConfigOrder = "1";
	private String dnsKeystore = "";
	private String dnsRegistrationEndpoint = "";
	private String fileConfig = System.getProperty("user.home") + "/.ar4k/default.config.base64.ar4k";
	private String fileConfigOrder = "0";
	private String fileKeystore = System.getProperty("user.home") + "/.ar4k/default.keystore";
	private String keystoreBeaconAlias = "";
	private String keystoreConfigAlias = "";
	private String keystoreMainAlias = "cert-agent";
	private String keystorePassword = "1234";
	private String logoUrl = "/static/img/ar4k.png";
	private String rossonetChatPassword = null;
	private String rossonetChatServer = null;
	private String rossonetChatToken = null;
	private String rossonetChatUser = null;
	private String showRegistrationCode = "true";
	private String test = "true";
	private String threadSleep = "800";
	private String uniqueName = null;

	private String uniqueNameFile = null;

	private String webConfig = "";

	private String webConfigOrder = "2";

	private String webKeystore = "";

	private String webRegistrationEndpoint = "";

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getAnimaDatastoreFileName() {
		return animaDatastoreFileName;
	}

	public String getBaseConfig() {
		return baseConfig;
	}

	public String getBaseConfigOrder() {
		return baseConfigOrder;
	}

	public String getBeaconCaChainPem() {
		return beaconCaChainPem;
	}

	public String getBeaconClearText() {
		return beaconClearText;
	}

	public String getBeaconDiscoveryFilterString() {
		return beaconDiscoveryFilterString;
	}

	public String getBeaconDiscoveryPort() {
		return beaconDiscoveryPort;
	}

	public String getConfPath() {
		return confPath;
	}

	public String getConsoleOnly() {
		return consoleOnly;
	}

	public String getDefaultWebPage() {
		return defaultWebPage;
	}

	public String getDnsConfig() {
		return dnsConfig;
	}

	public String getDnsConfigOrder() {
		return dnsConfigOrder;
	}

	public String getDnsKeystore() {
		return dnsKeystore;
	}

	public String getDnsRegistrationEndpoint() {
		return dnsRegistrationEndpoint;
	}

	public String getFileConfig() {
		return fileConfig;
	}

	public String getFileConfigOrder() {
		return fileConfigOrder;
	}

	public String getFileKeystore() {
		return fileKeystore;
	}

	public String getHomunculusDatastoreFileName() {
		return animaDatastoreFileName;
	}

	public String getKeystoreBeaconAlias() {
		return keystoreBeaconAlias;
	}

	public String getKeystoreConfigAlias() {
		return keystoreConfigAlias;
	}

	public String getKeystoreMainAlias() {
		return keystoreMainAlias;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public String getRossonetChatPassword() {
		return rossonetChatPassword;
	}

	public String getRossonetChatServer() {
		return rossonetChatServer;
	}

	public String getRossonetChatToken() {
		return rossonetChatToken;
	}

	public String getRossonetChatUser() {
		return rossonetChatUser;
	}

	public String getShowRegistrationCode() {
		return showRegistrationCode;
	}

	public String getTest() {
		return test;
	}

	public String getThreadSleep() {
		return threadSleep;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public String getUniqueNameFile() {
		return uniqueNameFile;
	}

	public String getWebConfig() {
		return webConfig;
	}

	public String getWebConfigOrder() {
		return webConfigOrder;
	}

	public String getWebKeystore() {
		return webKeystore;
	}

	public String getWebRegistrationEndpoint() {
		return webRegistrationEndpoint;
	}

	public String isConsoleOnly() {
		return consoleOnly;
	}

	public String isShowRegistrationCode() {
		return showRegistrationCode;
	}

	public String isTest() {
		return test;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public void setAnimaDatastoreFileName(String animaDatastore) {
		this.animaDatastoreFileName = animaDatastore;
	}

	public void setBaseConfig(String baseConfig) {
		this.baseConfig = baseConfig;
	}

	public void setBaseConfigOrder(String baseConfigOrder) {
		this.baseConfigOrder = baseConfigOrder;
	}

	public void setBeaconCaChainPem(String beaconCaChainPem) {
		this.beaconCaChainPem = beaconCaChainPem;
	}

	public void setBeaconClearText(String beaconClearText) {
		this.beaconClearText = beaconClearText;
	}

	public void setBeaconDiscoveryFilterString(String beaconDiscoveryFilterString) {
		this.beaconDiscoveryFilterString = beaconDiscoveryFilterString;
	}

	public void setBeaconDiscoveryPort(String beaconDiscoveryPort) {
		this.beaconDiscoveryPort = beaconDiscoveryPort;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public void setConsoleOnly(String consoleOnly) {
		this.consoleOnly = consoleOnly;
	}

	public void setDefaultWebPage(String defaultWebPage) {
		this.defaultWebPage = defaultWebPage;
	}

	public void setDnsConfig(String dnsConfig) {
		this.dnsConfig = dnsConfig;
	}

	public void setDnsConfigOrder(String dnsConfigOrder) {
		this.dnsConfigOrder = dnsConfigOrder;
	}

	public void setDnsKeystore(String dnsKeystore) {
		this.dnsKeystore = dnsKeystore;
	}

	public void setDnsRegistrationEndpoint(String dnsRegistrationEndpoint) {
		this.dnsRegistrationEndpoint = dnsRegistrationEndpoint;
	}

	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}

	public void setFileConfigOrder(String fileConfigOrder) {
		this.fileConfigOrder = fileConfigOrder;
	}

	public void setFileKeystore(String fileKeystore) {
		this.fileKeystore = fileKeystore;
	}

	public void setKeystoreBeaconAlias(String keystoreBeaconAlias) {
		this.keystoreBeaconAlias = keystoreBeaconAlias;
	}

	public void setKeystoreConfigAlias(String keystoreConfigAlias) {
		this.keystoreConfigAlias = keystoreConfigAlias;
	}

	public void setKeystoreMainAlias(String keystoreMainAlias) {
		this.keystoreMainAlias = keystoreMainAlias;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public void setRossonetChatPassword(String rossonetChatPassword) {
		this.rossonetChatPassword = rossonetChatPassword;
	}

	public void setRossonetChatServer(String rossonetChatServer) {
		this.rossonetChatServer = rossonetChatServer;
	}

	public void setRossonetChatToken(String rossonetChatToken) {
		this.rossonetChatToken = rossonetChatToken;
	}

	public void setRossonetChatUser(String rossonetChatUser) {
		this.rossonetChatUser = rossonetChatUser;
	}

	public synchronized void setShowRegistrationCode(String showRegistrationCode) {
		this.showRegistrationCode = showRegistrationCode;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void setThreadSleep(String threadSleep) {
		this.threadSleep = threadSleep;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public void setUniqueNameFile(String uniqueNameFile) {
		this.uniqueNameFile = uniqueNameFile;
	}

	public void setWebConfig(String webConfig) {
		this.webConfig = webConfig;
	}

	public void setWebConfigOrder(String webConfigOrder) {
		this.webConfigOrder = webConfigOrder;
	}

	public void setWebKeystore(String webKeystore) {
		this.webKeystore = webKeystore;
	}

	public void setWebRegistrationEndpoint(String webRegistrationEndpoint) {
		this.webRegistrationEndpoint = webRegistrationEndpoint;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EdgeStarterProperties [");
		if (animaDatastoreFileName != null) {
			builder.append("animaDatastoreFileName=");
			builder.append(animaDatastoreFileName);
			builder.append(", ");
		}
		if (baseConfig != null) {
			builder.append("baseConfig=");
			builder.append(baseConfig);
			builder.append(", ");
		}
		if (baseConfigOrder != null) {
			builder.append("baseConfigOrder=");
			builder.append(baseConfigOrder);
			builder.append(", ");
		}
		if (beaconCaChainPem != null) {
			builder.append("beaconCaChainPem=");
			builder.append(beaconCaChainPem);
			builder.append(", ");
		}
		if (beaconClearText != null) {
			builder.append("beaconClearText=");
			builder.append(beaconClearText);
			builder.append(", ");
		}
		if (beaconDiscoveryFilterString != null) {
			builder.append("beaconDiscoveryFilterString=");
			builder.append(beaconDiscoveryFilterString);
			builder.append(", ");
		}
		if (beaconDiscoveryPort != null) {
			builder.append("beaconDiscoveryPort=");
			builder.append(beaconDiscoveryPort);
			builder.append(", ");
		}
		if (confPath != null) {
			builder.append("confPath=");
			builder.append(confPath);
			builder.append(", ");
		}
		if (consoleOnly != null) {
			builder.append("consoleOnly=");
			builder.append(consoleOnly);
			builder.append(", ");
		}
		if (defaultWebPage != null) {
			builder.append("defaultWebPage=");
			builder.append(defaultWebPage);
			builder.append(", ");
		}
		if (dnsConfig != null) {
			builder.append("dnsConfig=");
			builder.append(dnsConfig);
			builder.append(", ");
		}
		if (dnsConfigOrder != null) {
			builder.append("dnsConfigOrder=");
			builder.append(dnsConfigOrder);
			builder.append(", ");
		}
		if (dnsKeystore != null) {
			builder.append("dnsKeystore=");
			builder.append(dnsKeystore);
			builder.append(", ");
		}
		if (dnsRegistrationEndpoint != null) {
			builder.append("dnsRegistrationEndpoint=");
			builder.append(dnsRegistrationEndpoint);
			builder.append(", ");
		}
		if (fileConfig != null) {
			builder.append("fileConfig=");
			builder.append(fileConfig);
			builder.append(", ");
		}
		if (fileConfigOrder != null) {
			builder.append("fileConfigOrder=");
			builder.append(fileConfigOrder);
			builder.append(", ");
		}
		if (fileKeystore != null) {
			builder.append("fileKeystore=");
			builder.append(fileKeystore);
			builder.append(", ");
		}
		if (keystoreBeaconAlias != null) {
			builder.append("keystoreBeaconAlias=");
			builder.append(keystoreBeaconAlias);
			builder.append(", ");
		}
		if (keystoreConfigAlias != null) {
			builder.append("keystoreConfigAlias=");
			builder.append(keystoreConfigAlias);
			builder.append(", ");
		}
		if (keystoreMainAlias != null) {
			builder.append("keystoreMainAlias=");
			builder.append(keystoreMainAlias);
			builder.append(", ");
		}
		if (logoUrl != null) {
			builder.append("logoUrl=");
			builder.append(logoUrl);
			builder.append(", ");
		}
		if (rossonetChatServer != null) {
			builder.append("rossonetChatServer=");
			builder.append(rossonetChatServer);
			builder.append(", ");
		}
		if (rossonetChatToken != null) {
			builder.append("rossonetChatToken=");
			builder.append(rossonetChatToken);
			builder.append(", ");
		}
		if (rossonetChatUser != null) {
			builder.append("rossonetChatUser=");
			builder.append(rossonetChatUser);
			builder.append(", ");
		}
		if (showRegistrationCode != null) {
			builder.append("showRegistrationCode=");
			builder.append(showRegistrationCode);
			builder.append(", ");
		}
		if (test != null) {
			builder.append("test=");
			builder.append(test);
			builder.append(", ");
		}
		if (threadSleep != null) {
			builder.append("threadSleep=");
			builder.append(threadSleep);
			builder.append(", ");
		}
		if (uniqueName != null) {
			builder.append("uniqueName=");
			builder.append(uniqueName);
			builder.append(", ");
		}
		if (uniqueNameFile != null) {
			builder.append("uniqueNameFile=");
			builder.append(uniqueNameFile);
			builder.append(", ");
		}
		if (webConfig != null) {
			builder.append("webConfig=");
			builder.append(webConfig);
			builder.append(", ");
		}
		if (webConfigOrder != null) {
			builder.append("webConfigOrder=");
			builder.append(webConfigOrder);
			builder.append(", ");
		}
		if (webKeystore != null) {
			builder.append("webKeystore=");
			builder.append(webKeystore);
			builder.append(", ");
		}
		if (webRegistrationEndpoint != null) {
			builder.append("webRegistrationEndpoint=");
			builder.append(webRegistrationEndpoint);
		}
		builder.append("]");
		return builder.toString();
	}

}
