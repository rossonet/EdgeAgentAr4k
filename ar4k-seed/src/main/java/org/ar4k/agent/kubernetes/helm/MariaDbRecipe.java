package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class MariaDbRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/mariadb" };
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
    return "MariaDB is one of the most popular database servers in the world. It’s made by the original developers of MySQL and guaranteed to stay open source";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/mariadb";
  }

}
