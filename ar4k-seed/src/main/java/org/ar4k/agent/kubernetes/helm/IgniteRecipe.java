package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class IgniteRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/ignite" };
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
    return "Apache Ignite is an open-source distributed database, caching and processing platform designed to store and compute on large volumes of data across a cluster of nodes";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/ignite";
  }

}
