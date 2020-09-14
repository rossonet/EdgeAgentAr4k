package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class ElasticSearchRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "elastic/elasticsearch", "--version", " 7.1.1" };
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
    return "Elasticsearch is a search engine based on the Lucene library. It provides a distributed, multitenant-capable full-text search engine with an HTTP web interface and schema-free JSON documents. Elasticsearch is developed in Java.";
  }

  @Override
  public String linkSite() {
    return "https://github.com/elastic/helm-charts/tree/master/elasticsearch";
  }

}
