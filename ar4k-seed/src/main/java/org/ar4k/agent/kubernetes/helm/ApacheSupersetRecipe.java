package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class ApacheSupersetRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/superset" };
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
    return "Apache Superset (incubating) is a modern, enterprise-ready business intelligence web application";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/superset";
  }

}
