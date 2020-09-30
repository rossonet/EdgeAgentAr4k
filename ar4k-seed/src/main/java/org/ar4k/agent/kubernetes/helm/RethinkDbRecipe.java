package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class RethinkDbRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/rethinkdb" };
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
    return "RethinkDB is a free and open-source, distributed document-oriented database originally created by the company of the same name. The database stores JSON documents with dynamic schemas, and is designed to facilitate pushing real-time updates for query results to applications";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/rethinkdb";
  }

}
