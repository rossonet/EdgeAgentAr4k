package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class TerracottaRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/terracotta" };
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
    return "Terracotta Ehcache is an improved version of Java's de facto caching API, Ehcache. It has a powerful, streamlined, modernized caching API taking advantage of newer Java features as well as the capability to be used via the JSR-107 \"JCache\" API";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/terracotta";
  }

}
