package org.ar4k.agent.hazelcast;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import io.atomix.cluster.Member;
import io.atomix.cluster.MemberId;
import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.cluster.discovery.NodeDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixBuilder;
import io.atomix.core.election.Leadership;
import io.atomix.core.map.AtomicMap;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;
import io.atomix.utils.net.Address;

public class HazelcastComponent implements Ar4kComponent {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private HazelcastConfig configuration = null;

  private Atomix atomix = null;

  private AtomicMap<String, Object> map = null;

  private boolean startStatus = false;

  Leadership<MemberId> leadership = null;

  private boolean discoveryOn = true;

  private Map<String, String> hostAddressNodes = new HashMap<String, String>();

  // (Anima) Anima.getApplicationContext().getBean("anima");

  public HazelcastComponent(Anima anima, HazelcastConfig tribeConfig) {
    this.configuration = tribeConfig;
  }

  public HazelcastComponent(HazelcastConfig tribeConfig) {
    this.configuration = tribeConfig;
  }

  private class RunConnection implements Runnable {

    public void connect() {
      try {
        AtomixBuilder builder = Atomix.builder();
        builder.withMemberId(configuration.name).withAddress(new Address(configuration.hostBind, configuration.port));
        if (discoveryOn) {
          builder.withMulticastEnabled();
          builder.withMulticastAddress(Address.from(configuration.multicastIp + ":" + configuration.multicastPort));
        } else {
          Node[] nodesToAdd = new Node[hostAddressNodes.size()];
          int counter = 0;
          for (Entry<String, String> singleData : hostAddressNodes.entrySet()) {
            nodesToAdd[counter] = Node.builder().withId(singleData.getKey())
                .withAddress(Address.from(singleData.getValue())).build();
            counter++;
          }
          NodeDiscoveryProvider bootstrapProvider = BootstrapDiscoveryProvider.builder().withNodes(nodesToAdd).build();
          builder.withMembershipProvider(bootstrapProvider);
        }
        builder.withRackId(configuration.rack);
        builder.withHost(configuration.hostBind);
        builder.withZoneId(configuration.zone);
        builder.withReachabilityTimeout(Duration.of(120, ChronoUnit.SECONDS));
        // builder.addProfile(Profile.dataGrid());
        builder.withManagementGroup(RaftPartitionGroup.builder("system").withMembers(configuration.joinLinks)
            .withDataDirectory(new File(
                configuration.storagePath.replaceFirst("^~", System.getProperty("user.home")) + "/system-raft"))
            .withNumPartitions(1).build());
        /*
         * builder.withPartitionGroups(PrimaryBackupPartitionGroup.builder("data")
         * .withMemberGroupStrategy(MemberGroupStrategy.NODE_AWARE).withNumPartitions(32
         * ).build());
         */
        builder.withPartitionGroups(RaftPartitionGroup.builder("raft")
            .withDataDirectory(
                new File(configuration.storagePath.replaceFirst("^~", System.getProperty("user.home")) + "/data-raft"))
            .withNumPartitions(3).withMembers(configuration.joinLinks).build()).build();
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
      // getAtomixMap();
    }
  }

  public List<String> listAtomixNodes() {
    List<String> ritorno = new ArrayList<String>();
    if (atomix != null && atomix.getMembershipService() != null) {
      Set<Member> ms = atomix.getMembershipService().getMembers();
      for (Member a : ms) {
        ritorno.add(a.address().host() + ":" + a.address().port());
      }
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
    this.configuration = (HazelcastConfig) configuration;
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
      map = atomix.<String, Object>atomicMapBuilder(configuration.mapName).withCacheEnabled().withCacheSize(1000)
          .withKeyType(String.class).build();
    }
    return map;
  }

  public boolean isDiscoveryOn() {
    return discoveryOn;
  }

  public void setDiscoveryOn(boolean discoveryOn) {
    this.discoveryOn = discoveryOn;
  }

  public Map<String, String> getHostAddressNodes() {
    return hostAddressNodes;
  }

  public void setHostAddressNodes(Map<String, String> hostAddressNodes) {
    this.hostAddressNodes = hostAddressNodes;
  }

}
