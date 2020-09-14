package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class NatsRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/nats" };
      return cmd;
    } else
      return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return "NATS is an open-source, cloud-native messaging system. It provides a lightweight server that is written in the Go programming language";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/nats";
  }

}
