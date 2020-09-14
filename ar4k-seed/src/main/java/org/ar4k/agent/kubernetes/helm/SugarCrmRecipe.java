package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class SugarCrmRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/sugarcrm" };
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
    return "SugarCRM offers the most innovative, flexible and affordable CRM in the market and delivers the best all-around value of any CRM";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/bitnami/sugarcrm";
  }

}
