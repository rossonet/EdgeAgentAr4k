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
package org.ar4k.agent.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.config.validator.AnsiColorValidator;
import org.ar4k.agent.config.validator.EdgeStatusValidator;
import org.ar4k.agent.config.validator.RouterTypeValidator;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusRouterType;
import org.ar4k.agent.core.interfaces.ConfigSeed;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.joda.time.Instant;
import org.springframework.boot.ansi.AnsiColor;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class EdgeConfig implements ConfigSeed {

	private static final long serialVersionUID = 7447810727276010241L;

	public Instant creationDate = new Instant();
	public Instant lastUpdate = new Instant();
	public String uniqueId = UUID.randomUUID().toString();

	@Parameter(names = "--name", description = "name")
	public String name = UUID.randomUUID().toString();

	@Parameter(names = "--nextConfigDns", description = "if set the configuration is monitored and replaced in runtine when change")
	public String nextConfigDns = null;

	@Parameter(names = "--nextConfigWeb", description = "if set the configuration is monitored and replaced in runtine when change")
	public String nextConfigWeb = null;

	@Parameter(names = "--nextConfigFile", description = "if set the configuration is monitored and replaced in runtine when change")
	public String nextConfigFile = null;

	@Parameter(names = "--nextConfigReload", description = "if set to true the agent will be reload after the configuration change else, if false, it will be just restarted")
	public Boolean nextConfigReload = false;

	@Parameter(names = "--configCheckPeriod", description = "the between every check for the config changes (ms)")
	public Integer configCheckPeriod = 10000;

	@Parameter(names = "--prompt", description = "prompt when configuration is selected")
	public String prompt = "studio";

	@Parameter(names = "--promptColor", description = "prompt color when configuration is selected", validateWith = AnsiColorValidator.class)
	public AnsiColor promptColor = AnsiColor.BRIGHT_RED;

	@Parameter(names = "--description", description = "description")
	public String description = "Template configurazione Agente Ar4k";

	@Parameter(names = "--beaconServer", description = "Beacon server for remote management (multi values are split by comma)")
	public String beaconServer = null;

	@Parameter(names = "--beaconServerCertChain", description = "Beacon client cert chain")
	public String beaconServerCertChain = null;

	@Parameter(names = "--beaconDiscoveryPort", description = "Beacon auto discovery port. 0 => disabled")
	public int beaconDiscoveryPort = 0;

	@Parameter(names = "--beaconDiscoveryFilterString", description = "Beacon discovery filter string")
	public String beaconDiscoveryFilterString = "AR4K";

	@Parameter(names = "--dataCenter", description = "datacenter in where the agent run")
	public String dataCenter = "ALONE";

	@Parameter(names = "--version", description = "version")
	public int version = 0;

	@Parameter(names = "--subVersion", description = "sub version")
	public int subVersion = 1;

	@Parameter(names = "--tagVersion", description = "tag version -String-")
	public String tagVersion = "demo";

	@Parameter(names = "--author", description = "author")
	public String author = "Ar4k demo user";

	@Parameter(names = "--project", description = "project")
	public String project = "Ar4k Edge Agent Test Project 1";

	@Parameter(names = "--license", description = "license")
	public String license = "GNU AFFERO GENERAL PUBLIC LICENSE";

	@Parameter(names = "--tags", description = "tags", variableArity = true)
	public List<String> tags = new ArrayList<>();

	@Parameter(names = "--contexts", description = "contexts", variableArity = true)
	public List<String> contexts = new ArrayList<>();

	@Parameter(names = "--groups", description = "groups", variableArity = true)
	public List<String> groups = new ArrayList<>();

	@Parameter(names = "--targetRunLevel", description = "target run level at boot of the configuration", validateWith = EdgeStatusValidator.class)
	public Homunculus.HomunculusStates targetRunLevel = Homunculus.HomunculusStates.RUNNING;

	@Parameter(names = "--preScript", description = "pre script")
	public String preScript = null;

	@Parameter(names = "--postScript", description = "post script")
	public String postScript = null;

	@Parameter(names = "--preScriptLanguage", description = "pre script language")
	public String preScriptLanguage = "groovy";

	@Parameter(names = "--postScriptLanguage", description = "post script language")
	public String postScriptLanguage = "groovy";

	@Parameter(names = "--initializeKeystore", description = "initialize the keystore if need", arity = 0)
	public boolean initializeKeystore = true;

	@Parameter(names = "--sshdAuthorizedKeysPath", description = "sshd authorizedkeys file path")
	public String sshdAuthorizedKeysPath = "~/.ssh/authorized_keys";

	@Parameter(names = "--routerType", description = "routerType", validateWith = RouterTypeValidator.class)
	public HomunculusRouterType routerType = HomunculusRouterType.NONE;

	@Parameter(names = "--updateFileConfig", description = "if true, this configuration will update the original one on file", validateWith = RouterTypeValidator.class)
	public boolean updateFileConfig = false;

	@Parameter(names = "--logoUrl", description = "default log url")
	public String logoUrl = "/static/img/ar4k.png";

	public Collection<ServiceConfig> pots = new HashSet<>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Instant getCreationDate() {
		return creationDate;
	}

	@Override
	public Instant getLastUpdateDate() {
		return lastUpdate;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public List<String> getTags() {
		return tags;
	}

	public boolean isMoreUpToDateThan(EdgeConfig runtimeConfig) {
		return lastUpdate.isAfter(runtimeConfig.lastUpdate);
	}

	@Override
	public String toString() {
		return "EdgeConfig [creationDate=" + creationDate + ", lastUpdate=" + lastUpdate + ", uniqueId=" + uniqueId
				+ ", name=" + name + ", nextConfigDns=" + nextConfigDns + ", nextConfigWeb=" + nextConfigWeb
				+ ", nextConfigFile=" + nextConfigFile + ", nextConfigReload=" + nextConfigReload
				+ ", configCheckPeriod=" + configCheckPeriod + ", prompt=" + prompt + ", promptColor=" + promptColor
				+ ", description=" + description + ", beaconServer=" + beaconServer + ", beaconServerCertChain="
				+ beaconServerCertChain + ", beaconDiscoveryPort=" + beaconDiscoveryPort + ", dataCenter=" + dataCenter
				+ ", version=" + version + ", subVersion=" + subVersion + ", tagVersion=" + tagVersion + ", author="
				+ author + ", project=" + project + ", license=" + license + ", tags=" + tags + ", contexts=" + contexts
				+ ", groups=" + groups + ", targetRunLevel=" + targetRunLevel + ", preScript=" + preScript
				+ ", postScript=" + postScript + ", preScriptLanguage=" + preScriptLanguage + ", postScriptLanguage="
				+ postScriptLanguage + ", initializeKeystore=" + initializeKeystore + ", sshdAuthorizedKeysPath="
				+ sshdAuthorizedKeysPath + ", routerType=" + routerType + ", logoUrl=" + logoUrl + ", pots=" + pots
				+ "]";
	}
}
