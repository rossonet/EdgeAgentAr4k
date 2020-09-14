package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class StolonRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/stolon" };
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
    return "Stolon - PostgreSQL cloud native High Availability";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/stolon";
  }

}
