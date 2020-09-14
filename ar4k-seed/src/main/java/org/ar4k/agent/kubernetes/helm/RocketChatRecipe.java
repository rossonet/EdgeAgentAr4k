package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class RocketChatRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/rocketchat" };
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
    return "Rocket.Chat is the leading open source team chat software solution. Free, unlimited and completely customizable with on-premises and SaaS cloud hosting";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/rocketchat";
  }

}
