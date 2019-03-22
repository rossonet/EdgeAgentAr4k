package org.ar4k.agent.config.tribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.tribe.AtomixTribeComponent;
import org.ar4k.agent.tribe.TribeGovernanceValidator;
import org.ar4k.agent.tribe.AtomixTribeComponent.Governance;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;

public class TribeConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -2924869182396567535L;

  public Instant creationDate = new Instant();
  public Instant lastUpdate = new Instant();
  public UUID uniqueId = UUID.randomUUID();

  @Parameter(names = "--name", description = "label name", required = true)
  public String name;

  @Parameter(names = "--description", description = "tribe description")
  public String description;

  @Parameter(names = "--port", description = "tcp bind port")
  public Integer port;

  @Parameter(names = "--joinLinks", description = "list of Atomix machines as host:port (multi selection)", variableArity = true)
  public Collection<String> joinLinks = new ArrayList<String>();

  @Parameter(names = "--tribeGovernance", description = "tribe governance system", variableArity = true, validateWith = TribeGovernanceValidator.class)
  public Governance tribeGovernance = Governance.MONARCHY;

  @Parameter(names = "--multicastIp", description = "the multicast ip for the discovery server", required = true)
  public String multicastIp = "224.0.0.66";

  @Parameter(names = "--multicastPort", description = "the multicast port for the discovery server", required = true)
  public Integer multicastPort = 6566;

  @Parameter(names = "--attention", description = "the sleep time in milliseconds between each loop check", required = true)
  public Long attention = 10000L;

  @Parameter(names = "--active", description = "false if the server will not start at boot time; else true", required = true)
  public boolean active = true;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Ar4kComponent instantiate() {
    return new AtomixTribeComponent(this);
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
}
