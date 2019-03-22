package org.ar4k.agent.tribe;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.tribe.TribeConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.helper.HardwareHelper;

import com.google.gson.JsonElement;

import io.atomix.cluster.Member;
import io.atomix.cluster.MemberConfig;
import io.atomix.core.Atomix;
import io.atomix.core.profile.Profile;
import io.atomix.utils.net.Address;

public class AtomixTribeComponent implements Ar4kComponent, Runnable {

  public static enum Governance {
    MONARCHY, DEMOCRACY, JUNGLE, NONE
  }

  private String uuid = UUID.randomUUID().toString();

  private Thread processo = null;

  private Anima anima;

  private TribeConfig configuration;

  private Atomix atomix;

  private List<Atomix> tempNodes;

  private boolean running = true;

  public AtomixTribeComponent(Anima anima, TribeConfig tribeConfig) {
    this.anima = anima;
    this.configuration = tribeConfig;
  }

  public AtomixTribeComponent(TribeConfig tribeConfig) {
    this.configuration = tribeConfig;
    this.anima = (Anima) Anima.getApplicationContext().getBean("anima");
  }

  @PostConstruct
  private synchronized void preparaAmbiente() {
    try {
      if (processo == null) {
        processo = new Thread(this);
        processo.setName("tribe_" + uuid);
        processo.start();
      }
    } catch (Exception aa) {
    }
  }

  private Map<String, String> produceMembershipMetadata() throws IOException, InterruptedException, ParseException {
    Map<String, String> metadata = new HashMap<String, String>();
    Map<String, Object> hal = HardwareHelper.getSystemInfo();
    for (String chiave : hal.keySet()) {
      metadata.put(chiave, hal.get(chiave).toString());
    }
    Map<String, String> envs = anima.getEnvironmentVariables();
    for (String chiave : envs.keySet()) {
      metadata.put(chiave, envs.get(chiave).toString());
    }
    metadata.put("anima-status", anima.getState().toString());
    metadata.put("timestamp", String.valueOf((new Date()).getTime()));
    return metadata;
  }

  public boolean connect() {
    try {
      Atomix.Builder builder = Atomix.builder();
      builder.withLocalMember(
          Member.builder().withId(uuid).withAddress("localhost:" + String.valueOf(configuration.port)).build());
      builder.withProfiles(Profile.CONSENSUS, Profile.DATA_GRID);
      for (String single : configuration.joinLinks) {
        MemberConfig mc = new MemberConfig();
        mc.setAddress(single);
        Member member = new Member(mc);
        builder.withMembers(member);
      }
      builder.withMulticastEnabled();
      builder.withMulticastAddress(Address.from(configuration.multicastIp + ":" + configuration.multicastPort));
      Atomix atomix = builder.build();
      atomix.start();
      return true;
    } catch (Exception ae) {
      return false;
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

  public List<Atomix> incubation(int nodes, int basePort) {
    detachIncubation();
    List<Atomix> temp = new ArrayList<Atomix>();
    for (int contatore = 0; contatore < nodes; contatore++) {
      Atomix.Builder aBuilder = Atomix.builder();
      aBuilder.withLocalMember(Member.builder().withId(uuid + "_" + String.valueOf(contatore))
          .withAddress("localhost:" + String.valueOf(configuration.port + contatore)).build());
      aBuilder.withProfiles(Profile.CONSENSUS, Profile.DATA_GRID);
      aBuilder.withMulticastEnabled();
      aBuilder.withMulticastAddress(Address.from(configuration.multicastIp + ":" + configuration.multicastPort));
      Atomix aAtomix = aBuilder.build();
      aAtomix.start();
    }
    tempNodes = temp;
    return temp;
  }

  public boolean detachIncubation() {
    if (tempNodes != null) {
      for (Atomix a : tempNodes) {
        a.stop();
      }
      tempNodes = null;
    }
    return true;
  }

  @Override
  public void run() {
    while (running) {
      if (configuration.active) {
        if (!isConnected()) {
          connect();
        }
        refreshMetadata();
      }
    }
    try {
      Thread.sleep(configuration.attention);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void refreshMetadata() {
    if (atomix != null && atomix.isRunning()) {
      try {
        Map<String, String> dati = produceMembershipMetadata();
        for (String chiave : dati.keySet()) {
          atomix.getMembershipService().getLocalMember().metadata().put(chiave, dati.get(chiave));
        }
      } catch (IOException | InterruptedException | ParseException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void kill() {
    // TODO Auto-generated method stub

  }

  @Override
  public ConfigSeed getConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonElement getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }
}
