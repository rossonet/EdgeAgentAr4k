package org.ar4k.agent.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.rpc.Homunculus;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.RpcMessage;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

public class RpcConversation implements RpcExecutor {

  private Map<String, Ar4kConfig> configurations = new HashMap<>();
  private Map<String, KeystoreConfig> keyStores = new HashMap<String, KeystoreConfig>();
  private Map<String, PotConfig> components = new HashMap<String, PotConfig>();
  Homunculus homunculus = null;

  @Override
  public String elaborateMessage(String message) {
    return message;
  }

  @Override
  public RpcMessage<? extends String> elaborateMessage(RpcMessage<? extends String> message) {
    return message;
  }

  @Override
  public Map<String, MethodTarget> listCommands() {
    return null;
  }

  @Override
  public List<CompletionProposal> complete(CompletionContext context) {
    return null;
  }

  @Override
  public void setHomunculus(Homunculus homunculus) {
    this.homunculus = homunculus;
  }

  public Map<String, Ar4kConfig> getConfigurations() {
    return configurations;
  }

  public void setConfigurations(Map<String, Ar4kConfig> configurations) {
    this.configurations = configurations;
  }

  public Map<String, KeystoreConfig> getKeyStores() {
    return keyStores;
  }

  public void setKeyStores(Map<String, KeystoreConfig> keyStores) {
    this.keyStores = keyStores;
  }

  public Map<String, PotConfig> getComponents() {
    return components;
  }

  public void setComponents(Map<String, PotConfig> components) {
    this.components = components;
  }

}
