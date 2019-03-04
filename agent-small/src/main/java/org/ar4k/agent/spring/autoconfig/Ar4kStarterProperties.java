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

import static org.ar4k.agent.spring.autoconfig.Ar4kStarterProperties.AR4K_PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Proprità per bootstrap agente. Questa classe è utitilizzata da
 * Ar4kAutoConfiguration.
 * 
 * @author Andrea Ambrosini
 * 
 * 
 */
@ConfigurationProperties(prefix = AR4K_PREFIX)
public class Ar4kStarterProperties {

	public static final String AR4K_PREFIX = "ar4k";
	private String confPath = "~/.ar4k";
	private String fileConfig = "~/.ar4k/defaultBoot.config.base64.ar4k";
	private String webConfig = "https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k";
	private String dnsConfig = "subdomain.domain.com";
	private String base64Config = "";
	private String fileKeystore = "~/.ar4k/default.keystore";
	private String webKeystore = "https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k";
	private String dnsKeystore = "subdomain.domain.com";
	private String keystoreCaAlias = "ca";
	private String keystorePassword = "secA4.rk!8";
	private String otpRegistrationSeed = "a4c8ff551a";
	private String webRegistrationEndpoint = "https://xxxx.com";
	private String dnsRegistrationEndpoint = "subdomain.domain.com";
	private int fileConfigOrder = 1;
	private int webConfigOrder = 2;
	private int dnsConfigOrder = 0;
	private int base64ConfigOrder = 3;
	private long threadSleep = 500;
	private boolean consoleOnly = false;
	private boolean test = true;
	private boolean ethereum = true;
	private boolean camel = true;
	private boolean web = true;
	private boolean tribe = true;
	private boolean serial = true;
	private boolean cnc = true;
	private String logoUrl= "/static/img/ar4k.png";

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public boolean isEthereum() {
		return ethereum;
	}

	public void setEthereum(boolean ethereum) {
		this.ethereum = ethereum;
	}

	public boolean isCamel() {
		return camel;
	}

	public void setCamel(boolean camel) {
		this.camel = camel;
	}

	public boolean isWeb() {
		return web;
	}

	public void setWeb(boolean web) {
		this.web = web;
	}

	public boolean isTribe() {
		return tribe;
	}

	public void setTribe(boolean tribe) {
		this.tribe = tribe;
	}

	public boolean isSerial() {
		return serial;
	}

	public void setSerial(boolean serial) {
		this.serial = serial;
	}

	public boolean isCnc() {
		return cnc;
	}

	public void setCnc(boolean cnc) {
		this.cnc = cnc;
	}

	public boolean isConsoleOnly() {
		return consoleOnly;
	}

	public void setConsoleOnly(boolean consoleOnly) {
		this.consoleOnly = consoleOnly;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public String getFileConfig() {
		return fileConfig;
	}

	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}

	public String getWebConfig() {
		return webConfig;
	}

	public void setWebConfig(String webConfig) {
		this.webConfig = webConfig;
	}

	public String getDnsConfig() {
		return dnsConfig;
	}

	public void setDnsConfig(String dnsConfig) {
		this.dnsConfig = dnsConfig;
	}

	public String getBase64Config() {
		return base64Config;
	}

	public void setBase64Config(String base64Config) {
		this.base64Config = base64Config;
	}

	public String getFileKeystore() {
		return fileKeystore;
	}

	public void setFileKeystore(String fileKeystore) {
		this.fileKeystore = fileKeystore;
	}

	public String getWebKeystore() {
		return webKeystore;
	}

	public void setWebKeystore(String webKeystore) {
		this.webKeystore = webKeystore;
	}

	public String getDnsKeystore() {
		return dnsKeystore;
	}

	public void setDnsKeystore(String dnsKeystore) {
		this.dnsKeystore = dnsKeystore;
	}

	public String getKeystoreCaAlias() {
		return keystoreCaAlias;
	}

	public void setKeystoreCaAlias(String keystoreCaAlias) {
		this.keystoreCaAlias = keystoreCaAlias;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getOtpRegistrationSeed() {
		return otpRegistrationSeed;
	}

	public void setOtpRegistrationSeed(String otpRegistrationSeed) {
		this.otpRegistrationSeed = otpRegistrationSeed;
	}

	public String getWebRegistrationEndpoint() {
		return webRegistrationEndpoint;
	}

	public void setWebRegistrationEndpoint(String webRegistrationEndpoint) {
		this.webRegistrationEndpoint = webRegistrationEndpoint;
	}

	public String getDnsRegistrationEndpoint() {
		return dnsRegistrationEndpoint;
	}

	public void setDnsRegistrationEndpoint(String dnsRegistrationEndpoint) {
		this.dnsRegistrationEndpoint = dnsRegistrationEndpoint;
	}

	public int getFileConfigOrder() {
		return fileConfigOrder;
	}

	public void setFileConfigOrder(int fileConfigOrder) {
		this.fileConfigOrder = fileConfigOrder;
	}

	public int getWebConfigOrder() {
		return webConfigOrder;
	}

	public void setWebConfigOrder(int webConfigOrder) {
		this.webConfigOrder = webConfigOrder;
	}

	public int getDnsConfigOrder() {
		return dnsConfigOrder;
	}

	public void setDnsConfigOrder(int dnsConfigOrder) {
		this.dnsConfigOrder = dnsConfigOrder;
	}

	public int getBase64ConfigOrder() {
		return base64ConfigOrder;
	}

	public void setBase64ConfigOrder(int base64ConfigOrder) {
		this.base64ConfigOrder = base64ConfigOrder;
	}

	public long getThreadSleep() {
		return threadSleep;
	}

	public void setThreadSleep(long threadSleep) {
		this.threadSleep = threadSleep;
	}


}
