package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class KafkaRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "bitnami/kafka" };
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
    return "Kafka is a distributed streaming platform used for building real-time data pipelines and streaming apps. It is horizontally scalable, fault-tolerant, wicked fast, and runs in production in thousands of companies";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/bitnami/kafka";
  }

}
