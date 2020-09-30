package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class ExternalDnsRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "bitnami/external-dns" };
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
    return "ExternalDNS is a Kubernetes addon that configures public DNS servers with information about exposed Kubernetes services to make them discoverable";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/bitnami/external-dns";
  }

}
