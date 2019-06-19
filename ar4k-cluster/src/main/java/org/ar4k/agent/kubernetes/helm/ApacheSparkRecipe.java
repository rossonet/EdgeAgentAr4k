package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class ApacheSparkRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/spark" };
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
    return "Apache Spark is a fast and general-purpose cluster computing system including Apache Zeppelin";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/spark";
  }

}
