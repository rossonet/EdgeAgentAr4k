package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class RabbitMqRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/rabbitmq" };
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
    return "Open source message broker software that implements the Advanced Message Queuing Protocol (AMQP)";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/bitnami/rabbitmq";
  }

}
