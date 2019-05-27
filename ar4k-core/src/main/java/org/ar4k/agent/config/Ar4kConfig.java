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

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import javax.validation.constraints.Size;

import org.ar4k.agent.config.json.Ar4kConfigJsonAdapter;
import org.ar4k.agent.config.validator.AnsiColorValidator;
import org.ar4k.agent.config.validator.Ar4kStatusValidator;
import org.ar4k.agent.config.validator.RouterTypeValidator;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaRouterType;
import org.ar4k.agent.core.Ar4kComponent;
import org.joda.time.Instant;
import org.springframework.boot.ansi.AnsiColor;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class Ar4kConfig implements ConfigSeed {

  private static final long serialVersionUID = 7447810727276010241L;

  public Instant creationDate = new Instant();
  public Instant lastUpdate = new Instant();
  public UUID uniqueId = UUID.randomUUID();

  @Parameter(names = "--name", description = "name")
  public String name = UUID.randomUUID().toString();

  @Parameter(names = "--prompt", description = "prompt when configuration is selected")
  public String prompt = "studio";

  @Parameter(names = "--promptColor", description = "prompt color when configuration is selected", validateWith = AnsiColorValidator.class)
  public AnsiColor promptColor = AnsiColor.BRIGHT_RED;

  @Parameter(names = "--description", description = "description")
  public String description = "Template configurazione Agente Ar4k";

  @Parameter(names = "--beaconServer", description = "Beacon server for remote management")
  public String beaconServer = null;

  @Parameter(names = "--autoRegisterBeaconServer", description = "if the Beacon server is not present or is non reacheable, use the Beacon discovery protocol to find a end point")
  public boolean autoRegisterBeaconServer = true;

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
  public Collection<String> tags = new HashSet<String>();

  @Parameter(names = "--contexts", description = "contexts", variableArity = true)
  public Collection<String> contexts = new HashSet<String>();

  @Parameter(names = "--groups", description = "groups", variableArity = true)
  public Collection<String> groups = new HashSet<String>();

  @Parameter(names = "--targetRunLevel", description = "target run level at boot of the configuration", validateWith = Ar4kStatusValidator.class)
  public Anima.AnimaStates targetRunLevel = Anima.AnimaStates.RUNNING;

  @Parameter(names = "--preScript", description = "pre script in format <LANGUAGE>:<SCRIPT>")
  public String preScript = null;

  @Parameter(names = "--postScript", description = "post script in format <LANGUAGE>:<SCRIPT>")
  public String postScript = null;

  @Parameter(names = "--clockAfterFinishCallLambda", description = "time before exit after all operations in lambda mode")
  public long clockAfterFinishCallLambda = 1000 * 60 * 5;

  @Parameter(names = "--keyStorePassword", description = "keyStore password", password = true)
  @Size(min = 8)
  public String keyStorePassword = "secA4.rk!8";

  @Parameter(names = "--keyStorePath", description = "keyStore file path")
  public String keyStorePath = "~/.ar4k/key-store";

  @Parameter(names = "--initializeKeystore", description = "initialize the keystore if need", arity = 0)
  public boolean initializeKeystore = true;

  @Parameter(names = "--sshdAuthorizedKeysPath", description = "sshd authorizedkeys file path")
  public String sshdAuthorizedKeysPath = "~/.ssh/authorized_keys";

  @Parameter(names = "--routerType", description = "routerType", validateWith = RouterTypeValidator.class)
  public AnimaRouterType routerType = AnimaRouterType.NONE;

  @Parameter(names = "--logoUrl", description = "default log url")
  public String logoUrl = "/static/img/ar4k.png";

  // public Collection<ServiceConfig> services = new HashSet<ServiceConfig>();

  public Collection<PotConfig> pots = new HashSet<PotConfig>();

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Ar4kComponent instantiate() {
    Anima a = (Anima) Anima.getApplicationContext().getBean("anima");
    a.setTargetConfig(this);
    return (Ar4kComponent) a;
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
  public UUID getUniqueId() {
    return uniqueId;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Collection<String> getTags() {
    return tags;
  }

  @Override
  public int getPriority() {
    return 0;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new Ar4kConfigJsonAdapter();
  }
}
