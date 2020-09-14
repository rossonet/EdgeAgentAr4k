package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class WordPressRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/wordpress" };
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
    return "Web publishing platform for building blogs and websites";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/wordpress";
  }

}
