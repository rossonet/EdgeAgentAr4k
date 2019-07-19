package org.ar4k.agent.tribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.tribe.TribeConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import io.atomix.cluster.Member;
import io.atomix.cluster.MemberId;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixBuilder;
import io.atomix.core.election.Leadership;
import io.atomix.core.map.AtomicMap;
import io.atomix.primitive.partition.MemberGroupStrategy;
import io.atomix.protocols.backup.partition.PrimaryBackupPartitionGroup;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;
import io.atomix.utils.net.Address;

public class AtomixTribeComponent implements Ar4kComponent {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private TribeConfig configuration = null;

  private Atomix atomix = null;

  private AtomicMap<String, Object> map = null;

  private boolean startStatus = false;

  Leadership<MemberId> leadership = null;

  // (Anima) Anima.getApplicationContext().getBean("anima");

  public AtomixTribeComponent(Anima anima, TribeConfig tribeConfig) {
    this.configuration = tribeConfig;
  }

  public AtomixTribeComponent(TribeConfig tribeConfig) {
    this.configuration = tribeConfig;
  }

  private class RunConnection implements Runnable {

    public void connect() {
      try {
        AtomixBuilder builder = Atomix.builder();
        builder.withMemberId(configuration.name).withAddress(new Address(configuration.hostBind, configuration.port));
        builder.withMulticastEnabled();
        builder.withMulticastAddress(Address.from(configuration.multicastIp + ":" + configuration.multicastPort));
        builder.withRackId(configuration.rack);
        builder.withHost(configuration.hostBind);
        builder.withZoneId(configuration.zone);
        builder.withManagementGroup(RaftPartitionGroup.builder("system").withMembers(configuration.joinLinks)
            .withDataDirectory(new File(configuration.storagePath.replaceFirst("^~", System.getProperty("user.home"))))
            .withNumPartitions(1).build());
        builder.withPartitionGroups(PrimaryBackupPartitionGroup.builder("data")
            .withMemberGroupStrategy(MemberGroupStrategy.NODE_AWARE).withNumPartitions(32).build());
        atomix = builder.build();
        atomix.start().join();
        startStatus = true;
      } catch (Exception ae) {
        logger.logException(ae);
        startStatus = false;
      }
    }

    @Override
    public void run() {
      connect();
      getAtomixMap();
    }
  }

  public List<String> listAtomixNodes() {
    Set<Member> ms = atomix.getMembershipService().getMembers();
    List<String> ritorno = new ArrayList<String>();
    for (Member a : ms) {
      ritorno.add(a.address().host() + ":" + a.address().port());
    }
    return ritorno;
  }

  public boolean isConnected() {
    return (atomix != null && atomix.isRunning()) ? true : false;
  }

  @Override
  public void init() {
    Thread t = new Thread(new RunConnection());
    t.setName("start-atomix");
    t.start();
  }

  @Override
  public void kill() {
    if (atomix != null)
      atomix.stop();
    atomix = null;
    map = null;
  }

  @Override
  public ConfigSeed getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (TribeConfig) configuration;
  }

  @Override
  public String getStatusString() {
    return atomix != null ? ("[running: " + String.valueOf(atomix.isRunning()) + "]")
        : (startStatus ? "started" : "fault");
  }

  @Override
  public JsonElement getStatusJson() {
    return gson.toJsonTree(configuration);
  }

  @Override
  public void setBeanName(String name) {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() throws IOException {
    kill();
  }

  public Atomix getAtomix() {
    return atomix;
  }

  public void sendChatMessage(String message) {
    atomix.getCommunicationService().broadcast("chat", message);
  }

  public AtomicMap<String, Object> getAtomixMap() {
    if (map == null) {
      /*
       * MultiRaftProtocol protocol =
       * MultiRaftProtocol.builder().withReadConsistency(ReadConsistency.LINEARIZABLE)
       * .build();
       */
      map = atomix.<String, Object>atomicMapBuilder(configuration.mapName).withCacheEnabled().withCacheSize(1000)
          .withKeyType(String.class).build();
    }
    return map;
  }

}
