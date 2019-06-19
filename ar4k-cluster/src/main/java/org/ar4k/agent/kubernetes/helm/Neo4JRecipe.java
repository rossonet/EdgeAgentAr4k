package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class Neo4JRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/neo4j" };
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
    return "Neo4j is a highly scalable native graph database that leverages data relationships as first-class entities, helping enterprises build intelligent applications to meet today’s evolving data challenges";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/neo4j";
  }

}
