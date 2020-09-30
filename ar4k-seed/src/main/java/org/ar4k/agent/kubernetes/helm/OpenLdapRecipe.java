package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class OpenLdapRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/openldap" };
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
    return "Community developed LDAP software";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/openldap";
  }

}
