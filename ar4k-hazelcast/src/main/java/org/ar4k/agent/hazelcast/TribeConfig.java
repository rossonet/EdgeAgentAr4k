package org.ar4k.agent.hazelcast;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Ar4kComponent;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

public class TribeConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -2924869182396567535L;

  public Instant creationDate = new Instant();
  public Instant lastUpdate = new Instant();
  public UUID uniqueId = UUID.randomUUID();

  @Parameter(names = "--port", description = "tcp bind port")
  public Integer port = 12600;

  @Parameter(names = "--host", description = "tcp bind host")
  public String hostBind = "127.0.0.1";

  @Parameter(names = "--joinLinks", description = "list of Atomix machines for system group", variableArity = true)
  public Set<String> joinLinks = new HashSet<String>();

  @Parameter(names = "--multicastIp", description = "the multicast ip for the discovery server")
  public String multicastIp = "224.0.0.66";

  @Parameter(names = "--multicastPort", description = "the multicast port for the discovery server")
  public Integer multicastPort = 6566;

  @Parameter(names = "--active", description = "false if the server will not start at boot time; else true")
  public boolean active = true;

  @Parameter(names = "--rack", description = "the rack parameter for Atomix. See: https://atomix.io/docs/latest/user-manual/cluster-management/member-groups/")
  public String rack = "rack-tag";

  @Parameter(names = "--zone", description = "the zone parameter for Atomix. See: https://atomix.io/docs/latest/user-manual/cluster-management/member-groups/")
  public String zone = uniqueId.toString();

  @Parameter(names = "--mapName", description = "the name for the map in where store the data")
  public String mapName = "base-map";

  @Parameter(names = "--storage", description = "the path for the raft storage")
  public String storagePath = "/tmp/" + UUID.randomUUID().toString();

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

  @Override
  public int getPriority() {
    return 2;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new TribeConfigJsonAdapter();
  }

  @Override
  public boolean isSpringBean() {
    // TODO implementare come spring bean
    return true;
  }
}
