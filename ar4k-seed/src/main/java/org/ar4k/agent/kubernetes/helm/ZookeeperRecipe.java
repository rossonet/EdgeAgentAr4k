package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class ZookeeperRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "bitnami/zookeeper" };
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
    return "A centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services for distributed applications";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/bitnami/zookeeper";
  }

}
